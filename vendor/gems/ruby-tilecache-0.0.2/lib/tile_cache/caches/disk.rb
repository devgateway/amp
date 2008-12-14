module TileCache
  module Caches
    class Disk
      REQUIRED_ATTRIBUTES = %w{ root }
      
      def initialize(settings)
        @root = settings[:root]
      end
      
      def get(tile)
        file = key_for_tile(tile)
        
        if File.exist?(file)
          File.open(file, File::RDONLY) { |f| tile.data = f.read }          
          return true
        else
          return false
        end
      end
      
      def store(tile, data = nil)
        tile.data = data if data
        raise CacheError, "Called #store! with no data argument and no data associated with the tile." unless tile.data
        
        # Create the full path to the cache file
        file = key_for_tile(tile)
        FileUtils.mkdir_p(File.dirname(file))
          
        File.open(file, (File::WRONLY | File::CREAT)) do |f|
          f.sync = true
          f.write(tile.data)
        end    
      end
      
    protected
      def key_for_tile(tile)
        components = [
          @root,
          tile.layer.name,
          sprintf("%02d", tile.z),
          sprintf("%03d", Integer(tile.x / 1000000)),
          sprintf("%03d", Integer((tile.x / 1000)) % 1000),
          sprintf("%03d", Integer(tile.x) % 1000),
          sprintf("%03d", Integer(tile.y / 1000000)),
          sprintf("%03d", Integer((tile.y / 1000)) % 1000),
          sprintf("%03d.%s", Integer(tile.y) % 1000, tile.layer.extension)
        ]      
        
        File.join(*components)
      end
    end
  end
end