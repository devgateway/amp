module TileCache
  module Services
    class WMS
      FIELDS = %{ bbox srs width height format layers styles }
      
      def initialize(params)
        @params = parse_request(params)
      end
      
      def get_map
        bbox = TileCache::Bounds.from_string(@params[:bbox])        
        layer = ConfigParser.instance.layers[@params[:layers]]
        tile = layer.get_tile(bbox)
        
        layer.render(tile)
      end
      
    private
      def parse_request(params)
        parsed = {}
        params.each do |k, v|
          key = k.downcase
          parsed[key.to_sym] = v if FIELDS.include?(key)
        end  
        parsed
      end
    end
  end
end