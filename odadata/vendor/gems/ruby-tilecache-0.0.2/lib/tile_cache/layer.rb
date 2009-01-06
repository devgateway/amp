require 'tile_cache/tile'
require 'tile_cache/meta_tile'

module TileCache
  class Layer
    DEFAULT_CONFIGURATION = {
      :bbox => [-180, -90, 180, 90],
      :srs => "EPSG:4326",
      :description => "",
      :size => [256, 256],
      :levels => 20,
      :extension => "png",
      :metatile => false,
      :metasize => [5, 5],
      :metabuffer => [10, 10]
    }
    
    attr_reader :name, :description, :layers, :bbox, :size, :units, :srs, :extension, :resolutions
    
    def initialize(name, config)
      @name = name
      @description = config[:description]
      @layers = config[:layers] || name
      @bbox = config[:bbox].is_a?(String) ? TileCache::Bounds.parse_string(config[:bbox]) : TileCache::Bounds.new(*config[:bbox])
      @size = config[:size].is_a?(String) ? config[:size].split(",").map { |s| Integer(s.strip) } : config[:size].map { |s| s.to_i }
      @units = config[:units]
      @srs = config[:srs]
      
      @extension = config[:extension].downcase
      @extension = 'jpeg' if @extension == 'jpg'
      
      @resolutions = parse_resolutions(config[:resolutions], config[:minresolution], config[:maxresolution], config[:levels])
    end
    
    # Create a new tile for the given bounds
    def get_tile(bbox)
      coords = get_cell(bbox)
      TileCache::Tile.new(self, *coords)        
    end
    
    # Fetch or render tile 
    def render(tile)
      cache = ConfigParser.instance.cache

      unless cache.get(tile)
        data = render_tile(tile)
        cache.store(tile, data)
      end
      
      return tile
    end  
    
    def render_bbox(bbox)
      case bbox
      when String
        bounds = Bounds.parse_string(bbox)
      when Bounds
        bounds = bbox
      else
        raise ArgumentError, "Invalid argument for bbox: #{bbox.inspect}"
      end
      
      tile = get_tile(bounds)
      render(tile)
    end
    
    
    # This returns the total number of columns and rows of the grid, 
    # based on the given z-indexes resolution
    def grid_limits(z)
      maxcols = (@bbox.maxx - @bbox.minx) / (@resolutions[z] * @size[0])
      maxrows = (@bbox.maxy - @bbox.miny) / (@resolutions[z] * @size[1])
      
      return [maxcols.to_i, maxrows.to_i]
    end
    
    def format
      return "image/" + @extension
    end
    
  protected
    # Returns the z-index for the bboxes resolution
    def level_for_bbox(bbox)
      max_diff = bbox.resolution(*@size) / @size.max
      
      if match = @resolutions.detect { |res| (res - bbox.resolution(*@size)).abs < max_diff }
        @resolutions.index(match)
      else
        raise TileCache::InvalidResolution, "Can't find resolution index for #{bbox.resolution}. Available resolutions are #{@resolutions.join(', ')}."
      end
    end
    
    # Returns x, y and z coordinates for a given bbox
    def get_cell(bbox)
      # Get exact resolution as specified
      z = level_for_bbox(bbox)
      res = @resolutions[z]
            
      x = ((bbox.minx - @bbox.minx) / (res * @size[0])).round
      y = ((bbox.miny - @bbox.miny) / (res * @size[1])).round
      
      return [x, y, z]
    end
    
  private
    # Calculate resolutions unless given via configuration
    def parse_resolutions(res_list, min_res, max_res, levels)
      case res_list
      when String
        res_list.split(",").map { |r| Float(r.strip) }
      when Array
        res_list.map { |r| Float(r) }
      when NilClass
        max_res ||= @bbox.max_resolution(*@size)
        (0..levels).map { |i| max_res.to_f / 2 ** i }
      else
        raise TileCache::Layers::InvalidConfiguration, "Invalid format of resolutions for layer #{@name}"
      end
    end
    
  end
end