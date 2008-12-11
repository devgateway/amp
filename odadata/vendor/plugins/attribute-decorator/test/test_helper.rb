module AttributeDecorator
  module Initializer
    VENDOR_RAILS = File.expand_path('../../../../rails', __FILE__)
    OTHER_RAILS = File.expand_path('../../../rails', __FILE__)
    PLUGIN_ROOT = File.expand_path('../../', __FILE__)
    
    def self.rails_directory
      if File.basename(File.dirname(PLUGIN_ROOT)) == 'plugins'
        if File.exist?(VENDOR_RAILS)
          VENDOR_RAILS
        elsif File.exist?(OTHER_RAILS)
          OTHER_RAILS
        end
      end
    end
    
    def self.load_dependencies
      if rails_directory
        $:.unshift(File.join(rails_directory, 'activesupport', 'lib'))
        $:.unshift(File.join(rails_directory, 'activerecord', 'lib'))
      else
        require 'rubygems' rescue LoadError
      end
      
      require 'activesupport'
      require 'activerecord'
      
      require 'rubygems' rescue LoadError
      
      require 'test/spec'
      require 'mocha'
      require File.join(PLUGIN_ROOT, 'lib', 'attribute_decorator')
    end
    
    def self.configure_database
      ActiveRecord::Base.establish_connection(:adapter => "sqlite3", :dbfile => ":memory:")
      ActiveRecord::Migration.verbose = false
    end
    
    def self.setup_database
      ActiveRecord::Schema.define(:version => 1) do
        create_table :artists do |t|
          t.integer :day
          t.integer :month
          t.integer :year
          t.integer :start_year
          t.string  :location
        end
      end
    end
    
    def self.teardown_database
      ActiveRecord::Base.connection.tables.each do |table|
        ActiveRecord::Base.connection.drop_table(table)
      end
    end
    
    def self.start
      load_dependencies
      configure_database
    end
  end
end

AttributeDecorator::Initializer.start

class Artist < ActiveRecord::Base
  extend AttributeDecorator
  
  # Defines a non existing attribute decorting multiple existing attributes
  attribute_decorator :date_of_birth, :class_name => 'CompositeDate', :decorates => [:day, :month, :year]
  
  # Defines a decorates for one attribute.
  attribute_decorator :gps_location, :class_name => 'GPSCoordinator', :decorates => :location
  
  # Defines a decorator for an existing attribute.
  attribute_decorator :start_year, :class_name => 'Year'
end

# The decorator classes used in the test cases
class CompositeDate
  attr_reader :day, :month, :year
  
  def self.parse(value)
    new *value.scan(/(\d\d)-(\d\d)-(\d{4})/).flatten.map { |x| x.to_i }
  end
  
  def initialize(day, month, year)
    @day, @month, @year = day, month, year
  end
  
  def valid?
    true
  end
  
  def to_a
    [@day, @month, @year]
  end
  
  def to_s
    "#{@day}-#{@month}-#{@year}"
  end
end

class Year
  attr_reader :start_year
  
  def self.parse(value)
    new(value == '40 bc' ? -41 : value.to_i)
  end
  
  def initialize(start_year)
    @start_year = start_year
  end
  
  def valid?
    @start_year != 0
  end
  
  def to_a
    [@start_year]
  end
end

class GPSCoordinator
  attr_reader :location
  
  def self.parse(value)
    new(value == 'amsterdam' ? '+1, +1' : '-1, -1')
  end
  
  def initialize(location)
    @location = location
  end
  
  def to_a
    [@location]
  end
  
  def to_s
    @location
  end
end