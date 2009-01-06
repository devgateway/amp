require 'singleton'
require 'yaml'

require 'tile_cache/caches'
require 'tile_cache/layers'

module TileCache
  class ConfigParser
    include Singleton
    attr_reader :cache, :layers
    
    def initialize
      config = read_config_file
      @cache = instanciate_cache(config.delete(:cache))
      @layers = instanciate_layers(config)
    end
  
  private
      
    def instanciate_cache(config)
      settings = parse_cache_settings(config)
      settings[:class].new(settings.except(:class))
    end
    
    def instanciate_layers(config)
      layers = parse_layer_settings(config)
      
      instances = {}
      layers.each do |settings|
        name = settings[:name]
        instances[name] = settings[:class].new(settings.except(:class))
      end
      instances      
    end
    
    def parse_cache_settings(cache)
      cache.symbolize_keys! 
      
      raise InvalidConfiguration, "Missing cache section in your configuration file" unless cache
      raise InvalidConfiguration, "Missing type argument to determine which cache handler to use" unless cache[:type]
           
      cache[:class] = get_cache_handler(cache.delete(:type))
      cache[:name] = cache[:class].to_s
      validate_attributes!(cache[:class], cache)
      merge_with_defaults!(cache)
      
      cache
    end
    
    def parse_layer_settings(layers)
      layers.map do |layer, settings|
        settings.symbolize_keys!
        
        raise InvalidConfiguration, "Missing type argument for layer: #{layer}" unless settings[:type]
        settings[:name] = layer.to_s
        settings[:class] = get_layer_handler(settings.delete(:type))
        validate_attributes!(settings[:class], settings)
        merge_with_defaults!(settings)
        
        settings
      end
    end
    
    
    # Reads DEFAULT_CONFIGURATION hash from the class specified in settings[:class]
    # and merges settings hash with these values. If no DEFAULT_CONFIGURATION has
    # been specified, it doesn't change settings.
    def merge_with_defaults!(settings)
      defaults = settings[:class].const_get("DEFAULT_CONFIGURATION").symbolize_keys rescue {}
      settings.reverse_merge!(defaults)    
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
        config.except(:class, :name).each_key do |a|
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
    
    # Get layer class from TileCache::Layers for the specified type,
    # raises TileCache::InvalidConfiguration if no class has been found
    def get_layer_handler(type)
      class_name = type.sub(/Layer$/, '')
      raise InvalidConfiguration, "Invalid layer type attribute: #{type}" unless Layers.const_defined?(class_name)
      Layers.const_get(class_name)
    end
    
    # Get cache class from TileCache::Caches
    def get_cache_handler(type)
      class_name = type.sub(/Cache$/, '')
      raise InvalidConfiguration, "Invalid cache type attribute: #{type}" unless Caches.const_defined?(class_name)
      Caches.const_get(class_name)
    end
  end
end