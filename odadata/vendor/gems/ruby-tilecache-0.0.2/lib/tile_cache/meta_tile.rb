module TileCache
  class MetaTile < TileCache::Tile
    # Returns the tile's size without meta-buffers
    def size_without_buffers
      (meta_cols, meta_rows) = @layer.meta_limits(@z)
      return [@layer.size[0] * meta_cols, @layer.size[1] * meta_rows]
    end
    
    # Returns this tile's size including buffers
    def size
      actual = size_without_buffers
      return [actual[0] + @layer.metabuffer[0] * 2,
              actual[1] + @layer.metabuffer[1] * 2]
    end
    
    def bounds
      tilesize    = size_without_buffers
      res         = @layer.resolutions[@z]
      buffer      = [res * @layer.metabuffer[0], res * @layer.metabuffer[1]]
      meta_width  = res * tilesize[0]
      meta_height = res * tilesize[1]
      
      minx = @layer.bbox.minx + @x * meta_width  - buffer[0]
      miny = @layer.bbox.miny + @y * meta_height - buffer[1]
      maxx = minx + meta_width  + 2 * buffer[0]
      maxy = miny + meta_height + 2 * buffer[1]
      
      return TileCache::Bounds.new(minx, miny, maxx, maxy)   
    end
  end
end