module OdaMap
  module Cache
    class DiskCache < OdaMap::Cache::Base
      def hit?
        File.exists?(cache_file)
      end      
      
      def get
        unless hit?
          raise OdaMap::Cache::CacheMiss, "Cannot retrieve data as tile has not been found"
        else
          File.read(cache_file)
        end
      end
      
      def store!(data)
        File.open(cache_file, "w") do |f|
          f.write(data)
        end       
        
        data
      end
      
      def content_type
        "image/jpeg"
      end
    protected
      def cache_file
        @cache_file ||= File.join(OdaMap::CACHE_ROOT, @key + ".jpeg")
      end
    end
  end
end