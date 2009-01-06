require 'tile_cache/layer'
require 'tile_cache/meta_layer'

# Preload layer classes
Dir[File.dirname(__FILE__) + '/layers/*.rb'].each { |c| require c }

module TileCache
  module Layers
    class << self
      def get_handler_class(layer_type)
        class_name = layer_type.sub(/Layer$/, '')
        
        if Layers.const_defined?(class_name)
          Layers.const_get(class_name)
        else
          raise InvalidConfiguration, "Invalid layer type attribute: #{type}"
        end
      end
    end
  end
end