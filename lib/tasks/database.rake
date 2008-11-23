require 'ya2yaml'

namespace :db do
  desc "Flushes your entire database"
  task :flush => :environment do
    print "Are you sure you want to flush your entire database? [y/N] "
    exit unless STDIN.gets.strip.upcase == "Y"
        
    ActiveRecord::Base.connection.tables.each do |t|
      ActiveRecord::Base.connection.execute("DROP TABLE #{t} CASCADE")
      puts "Removed table \"#{t}\""
    end
  end 
  
  desc "Shows all tables existing in current database"
  task :show_tables => :environment do
    ActiveRecord::Base.connection.tables.each { |p| puts p }
  end
  
  desc "Load seed fixtures (from db/fixtures) into the current environment's database." 
  task :seed => :environment do
    require 'active_record/fixtures'
    Dir.glob(RAILS_ROOT + "/db/fixtures/#{ENV['SOURCE']}/*.yml").each do |file|
      Fixtures.create_fixtures("db/fixtures/#{ENV['SOURCE']}", File.basename(file, '.*'))
    end
  end
  
  desc 'Create YAML test fixtures from data in an existing database.
  Defaults to development database.'
  task :extract_fixtures => :environment do
    sql = "SELECT * FROM %s"
    skip_tables = ["schema_migrations", "sessions"]
    ActiveRecord::Base.establish_connection
    (ActiveRecord::Base.connection.tables - skip_tables).each do |table_name|
      i = "000"
      File.open("#{RAILS_ROOT}/db/exported_fixtures/#{table_name}.yml", 'w' ) do |file|
      data = ActiveRecord::Base.connection.select_all(sql % table_name)
      file.write data.inject({}) { |hash, record|
      hash["#{table_name}_#{i.succ!}"] = record
      hash
      }.ya2yaml
      end
    end
  end
end