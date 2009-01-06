# Preload all cache handler classes
Dir[File.dirname(__FILE__) + '/caches/*.rb'].each { |c| require c }

module TileCache
  module Caches
    class << self
      def get_handler_class(cache_type)
        class_name = cache_type.sub(/Cache$/, '')
        
        if Caches.const_defined?(class_name)
          Caches.const_get(class_name)
        else
          raise InvalidConfiguration, "Invalid cache type attribute: #{type}"
        end
      end
    end
  end
end