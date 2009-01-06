$:.unshift(File.dirname(__FILE__)) unless
  $:.include?(File.dirname(__FILE__)) || $:.include?(File.expand_path(File.dirname(__FILE__)))

require 'active_support/core_ext/hash/reverse_merge.rb'
require 'yaml'
  
require 'tile_cache/bounds'
require 'tile_cache/services'
require 'tile_cache/caches'
require 'tile_cache/layers'
  
module TileCache  
  CONFIG_PATHS = [
    Pathname.new("/etc/tilecache.yml"), 
    Pathname.new("#{ENV['HOME']}/.tilecache.yml"),
    Pathname.new("#{Dir.pwd}/config/tilecache.yml")
  ]
  
  # Exception classes
  class InvalidBounds < StandardError; end
  class InvalidResolution < StandardError; end
  class InvalidConfiguration < StandardError; end
  class CacheError < StandardError; end
  class LayerNotFound < StandardError; end
  
  class << self
    attr_accessor :cache, :layers
  
    # Initialize TileCache framework, read out configuration and setup cache and layer classes
    def initialize!
      read_configuration
      
      initialize_cache
      initialize_layers_pool
    end
    
  private
    def read_configuration
      # Select first existing path from hardcoded list
      path = CONFIG_PATHS.reverse.find(&:exist?)
  
      @config = YAML.load(File.read(path))
    rescue => e
      raise InvalidConfiguration, "Can't read configuration from #{CONFIG_PATHS.join(', ')}: #{e.message}"
    end
  
    def initialize_cache
      config = @config.delete('cache')
      raise InvalidConfiguration, "Missing cache section in your configuration file" unless config
      config.symbolize_keys!
  
      klass = Caches.get_handler_class(config.delete(:type))
      validate_attributes!(klass, config)
      merge_with_defaults!(klass, config)
  
      TileCache.cache = klass.new(config)
    end    
  
    def initialize_layers_pool
      layers = @config.delete('layers')
      raise InvalidConfiguration, "Please configure at least one layer" unless layers
      
      TileCache.layers = {}
      layers.each do |layer, settings|
        settings.symbolize_keys!
        
        klass = Layers.get_handler_class(settings.delete(:type))
        validate_attributes!(klass, settings)
        merge_with_defaults!(klass, settings)
  
        TileCache.layers.store(layer, klass.new(layer, settings))
      end
    end
    
    # Reads DEFAULT_CONFIGURATION hash from the class specified in settings[:class]
    # and merges settings hash with these values. If no DEFAULT_CONFIGURATION has
    # been specified, it doesn't change settings.
    def merge_with_defaults!(klass, config)
      defaults = klass.const_get("DEFAULT_CONFIGURATION").symbolize_keys rescue {}
      config.reverse_merge!(defaults)    
    end

    # Check for the validity of configuration attributes
    # If any attributes that are not specified in the classes VALID_ATTRIBUTES
    # constant appear in the configuration file or if there are attributes from REQUIRED_ATTRIBUTES missing
    # this raises TileCache::InvalidConfiguration
    def validate_attributes!(klass, config)
      valid_attributes = klass.const_get("VALID_ATTRIBUTES") rescue []
      required_attributes = klass.const_get("REQUIRED_ATTRIBUTES") rescue []
      # Merge with required attributes so that we don't have to specify them twice
      valid_attributes |= required_attributes

      unless valid_attributes.empty?
        config.except(:name).each_key do |a|
          unless valid_attributes.include?(a.to_s)
            raise InvalidConfiguration, "Unrecognized attribute in #{config[:name]}: #{a}" 
          end
        end
      end

      unless required_attributes.empty?
        required_attributes.each do |a|
          unless config.include?(a.to_sym)
            raise InvalidConfiguration, "Missing attribute in #{config[:name]}: #{a}" 
          end
        end
      end
    end
  end
end

TileCache.initialize!
