module TileCache
  class Tile
    attr_reader :layer, :x, :y, :z, :size
    attr_accessor :data
    
    def initialize(layer, x, y, z)
      @layer = layer
      @x = x
      @y = y
      @z = z
      @data = nil
    end
    
    def bounds
      res = @layer.resolutions[@z]
      minx = @layer.bbox.minx + (res * @x * @layer.size[0])
      miny = @layer.bbox.miny + (res * @y * @layer.size[1])
      maxx = @layer.bbox.minx + (res * (@x + 1) * @layer.size[0])
      maxy = @layer.bbox.miny + (res * (@y + 1) * @layer.size[1])
      
      return TileCache::Bounds.new(minx, miny, maxx, maxy)
    end
    
    def size
      @layer.size
    end
  end
end