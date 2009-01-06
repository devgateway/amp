module TileCache
  class Bounds
    attr_reader :minx, :miny, :maxx, :maxy
    
    def self.parse_string(str)
      new(*str.split(",").map { |s| Float(s.strip) })
    end
    
    def initialize(minx = -180, miny = -90, maxx = 180, maxy = 90)
      @minx = Float(minx)
      @miny = Float(miny)
      @maxx = Float(maxx)
      @maxy = Float(maxy)
        
      if (minx > maxx) || (miny > maxy)
        raise TileCache::InvalidBounds, "Invalid Bounds: #{self}"
      end
    end
    
    def to_s
      [@minx, @miny, @maxx, @maxy].join(", ")
    end
    
    # Returns this Bboxes resolution
    def resolution(width = 256, height = 256)
      return [(maxx - minx) / width,
              (maxy - miny) / height].max
    end
    
    # Returns the maximum resolution possible for a given tile width and height
    def max_resolution(width = 256, height = 256)
      b_width  = (maxx - minx).to_f
      b_height = (maxy - miny).to_f
      
      if b_width >= b_height
        aspect = (b_width / b_height).ceil
        b_width / (width * aspect)
      else
        aspect = (b_height / b_width).ceil
        b_height / (height * aspect)
      end      
    end
  end
end