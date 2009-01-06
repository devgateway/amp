require 'rubygems'
require 'RMagick'

module TileCache
  class MetaLayer < TileCache::Layer
    include Magick
    
    attr_reader :metabuffer
    
    def initialize(name, config)
      super
      
      @metatile = [true, 1, "true", "yes", "1"].include?(config[:metatile])
      @metasize = config[:metasize].is_a?(String) ? config[:metasize].split(",").map { |s| Integer(s.strip) } : config[:metasize].map { |s| Integer(s) }
      @metabuffer = config[:metabuffer].is_a?(String) ? config[:metabuffer].split(",").map { |s| Integer(s.strip) } : config[:metabuffer].map { |s| Integer(s) }
    end
  
    # Columns and rows for a given z-indexes resolution.
    # Returns 1,1 if meta-tiling is disabled, otherwise the desired metasize or grid limits.
    def meta_limits(z)
      return [1, 1] unless @metatile
      
      limits = grid_limits(z)
      maxcols = [@metasize[0], limits[0] + 1].min
      maxrows = [@metasize[1], limits[1] + 1].min
      
      [maxcols, maxrows]
    end
    
    def get_meta_tile(tile)
      x = (tile.x / @metasize[0]).to_i
      y = (tile.y / @metasize[1]).to_i
      
      MetaTile.new(self, x, y, tile.z)
    end
    
    def render_meta_tile(metatile, tile)
      data = render_tile(metatile)
      image = Image.from_blob(data).first
      
      (meta_cols, meta_rows) = meta_limits(metatile.z)
      meta_height = meta_rows * @size[1] + 2 * @metabuffer[1]
      
      0.upto(meta_cols-1) do |c|
        0.upto(meta_rows-1) do |r|
          minx = c * @size[0] + @metabuffer[0]
          miny = r * @size[1] + @metabuffer[1]

          subimage = image.crop(SouthWestGravity, minx, miny, @size[0], @size[1])
          x = metatile.x * @metasize[0] + c
          y = metatile.y * @metasize[1] + r
          subtile = TileCache::Tile.new(self, x, y, metatile.z)
          TileCache.cache.store(subtile, subimage.to_blob)
          
          if x == tile.x && y == tile.y
            tile.data = subimage.to_blob
          end
        end
      end
      
      return tile.data
    end
    
    def render(tile)
      if @metatile
        metatile = get_meta_tile(tile)        
        
        unless TileCache.cache.get(tile)
          render_meta_tile(metatile, tile)
        end
            
        return tile
      else
        super
      end      
    end

  end
end