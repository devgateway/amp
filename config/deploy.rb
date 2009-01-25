set :application, "odanic"

set :scm, :cvs
set :repository, ":ext:pehlert@cvs.digijava.org/home/source/amp-cvs"
set :scm_module, "odadata"
set :deploy_via, :copy

set :deploy_to, "/var/www/#{application}"
set :user, 'deploy'
set :ssh_options, { :forward_agent => true }

set :db_host, "od3.hacksrus.net"
set :db_user, "development"
set :db_password, "ggjtea6d"
role :db, "odanic.odadata.eu", :primary => true





namespace :deploy do
  desc "Restarting mod_rails with restart.txt"
  task :restart, :roles => :app, :except => { :no_release => true } do
    run "touch #{deploy_to}/#{shared_dir}/tmp/restart.txt"
  end

  [:start, :stop].each do |t|
    desc "#{t} task is a no-op with mod_rails"
    task t, :roles => :app do ; end
  end
  
  desc "Seed database with data from db/fixtures"
  task :seed, :roles => :app do
    run "rake -f #{deploy_to}/#{current_dir}/Rakefile db:seed"
  end
end


## Create and link database.yml

desc "Create database.yml in shared/config" 
task :create_database_config do
  database_configuration = <<-EOF
login: &login
  adapter: postgresql
  host: #{db_host}
  username: #{db_user}
  password: #{db_password}
  encoding: utf8

development:
  database: #{application}_development
  <<: *login

test:
  database: #{application}_test
  <<: *login

production:
  database: #{application}_development
  <<: *login
EOF

  run "mkdir -p #{deploy_to}/#{shared_dir}/config" 
  put database_configuration, "#{deploy_to}/#{shared_dir}/config/database.yml" 
end
after "deploy:setup", :create_database_config

desc "Link in the database.yml" 
task :link_database_config do
  run "ln -nfs #{deploy_to}/#{shared_dir}/config/database.yml #{release_path}/config/database.yml" 
end

desc "Link shared assets"
task :link_assets do
  %w{ flags profile_pictures maps reports }.each do |asset_dir|
    run "mkdir -p #{deploy_to}/#{shared_dir}/assets/#{asset_dir}"
    run "ln -nfs #{deploy_to}/#{shared_dir}/assets/#{asset_dir} #{release_path}/public/"
  end
end

after "deploy:update", :link_database_config
after "deploy:update", :link_assets