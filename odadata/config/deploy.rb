default_run_options[:pty] = true

set :application, "odanic"

set :scm, :git
set :repository, "gitosis@repos.hacksrus.net:#{application}.git"
set :branch, "master"
set :deploy_via, :remote_cache
set :git_enable_submodules, 1

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

desc "Push current HEAD to git repository"
task :push_to_repo do
  `git push origin #{branch}:refs/heads/#{branch}`
end

task :ask_for_branch do
  print "Branch to deploy (empty for #{branch}): "
  branch = STDIN.gets.strip
  set :branch, branch unless branch.empty?
end

before "deploy", :ask_for_branch
after "ask_for_branch", :push_to_repo

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
after "deploy:update", :link_database_config