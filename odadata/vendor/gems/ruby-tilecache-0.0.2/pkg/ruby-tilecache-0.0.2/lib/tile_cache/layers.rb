Dir[File.dirname(__FILE__) + '/layers/*.rb'].each { |c| require c }

module TileCache
  module Layers
    class << self
      def get_handler_class(cache_type)
        class_name = cache_type.sub(/Cache$/, '')
        
        if Layers.const_defined?(class_name)
          Layers.const_get(class_name)
        else
          raise InvalidConfiguration, "Invalid cache type attribute: #{type}"
        end
      end
    end
  end
end