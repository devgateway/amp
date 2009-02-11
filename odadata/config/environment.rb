$KCODE = 'u'
require 'jcode'

# Be sure to restart your server when you modify this file

# Uncomment below to force Rails into production mode when
# you don't control web/app server and can't set it the proper way
# ENV['RAILS_ENV'] ||= 'production'

# Specifies gem version of Rails to use when vendor/rails is not present
RAILS_GEM_VERSION = '2.2.2' unless defined? RAILS_GEM_VERSION

# Bootstrap the Rails environment, frameworks, and default configuration
require File.join(File.dirname(__FILE__), 'boot')

Rails::Initializer.run do |config|
  # Specify gems that this application depends on. 
  # They can then be installed with "rake gems:install" on new installations.
  # You have to specify the :lib option for libraries, where the Gem name (sqlite3-ruby) differs from the file itself (sqlite3)
  config.gem "paperclip"
  config.gem "rubyist-aasm",          :lib => "aasm", :source => 'http://gems.github.com'
  config.gem "andand"
  config.gem "RedCloth"
  config.gem "ruby-tilecache",        :lib => 'tile_cache'
  config.gem 'mislav-will_paginate',  :lib => 'will_paginate', :source => 'http://gems.github.com'
  

  # Add additional load paths for your own custom dirs
  config.load_paths += Dir["#{RAILS_ROOT}/app/models/*[^.rb]"]
  config.load_paths << "#{RAILS_ROOT}/app/reports"
  config.load_paths << "#{RAILS_ROOT}/app/builders"
  
  # Make Time.zone default to the specified zone, and make Active Record store time values
  # in the database in UTC, and return them converted to the specified local zone.
  # Run "rake -D time" for a list of tasks for finding time zone names. Comment line to use default local time.
  config.time_zone = 'UTC'

  # The internationalization framework can be changed to have another default locale (standard is :en) or more load paths.
  # All files from config/locales/*.rb,yml are added automatically.
  config.i18n.load_path += Dir["#{RAILS_ROOT}/config/locale/**/*.{rb,yml}"]
  config.i18n.default_locale = :en

  # Your secret key for verifying cookie session data integrity.
  # If you change this key, all old sessions will become invalid!
  # Make sure the secret is at least 30 characters and all random, 
  # no regular words or you'll be exposed to dictionary attacks.
  config.action_controller.session = {
    :session_key => '_odanic_session',
    :secret      => 'f10a3fe93e37a5f408382a00be1cba5a7b9fce00389a17b23a8831fccfcfc30c746833bb6be5f32586a2d2481ba99ee66e82164bc8e027bc7a96074ac3ca2132'
  }
  
  # Store sessions in the database because the default cookie store cannot handle sessions >4K
  # config.action_controller.session_store = :active_record_store

  # Use SQL instead of Active Record's schema dumper when creating the test database.
  # This is necessary if your schema can't be completely dumped by the schema dumper,
  # like if you have constraints or database-specific column types
  config.active_record.schema_format = :sql

  # Activate observers that should always be running
  # Please note that observers generated using script/generate observer need to have an _observer suffix
  # config.active_record.observers = :cacher, :garbage_collector, :forum_observer
end
