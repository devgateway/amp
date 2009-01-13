require 'mapscript'

module TileCache
  module Layers    
    class MapServer < TileCache::MetaLayer
      include Mapscript
      
      VALID_ATTRIBUTES = %w{ maxresolution levels extension metatile metabuffer bbox resolutions width height }
      REQUIRED_ATTRIBUTES = %w{ mapfile layers }
      
      attr_reader :mapfile
      
      def initialize(name, config)
        @mapfile = config[:mapfile]
        super
      end
      
      def render_tile(tile)
        return nil # testing!
        set_metabuffer if @metabuffer
        req = build_request(tile)
        
        msIO_installStdoutToBuffer
        map.OWSDispatch(req)
        msIO_stripStdoutBufferContentType
        msIO_getStdoutBufferBytes
      end
      
    protected
      def map
        @map ||= MapObj.new(File.join(RAILS_ROOT, @mapfile))
      end
      
      def set_metabuffer
        # Don't override the mapfile settings!
        begin
          map.getMetaData("labelcache_map_edge_buffer")
        rescue
          buffer = -@metabuffer.max - 5
          map.setMetaData("labelcache_map_edge_buffer", buffer.to_s)
        end
      end
      
      def build_request(tile)
        req = OWSRequest.new
                
        req.setParameter("bbox", tile.bounds.to_s)
        req.setParameter("width", tile.size[0].to_s)
        req.setParameter("height", tile.size[1].to_s)
        req.setParameter("srs", @srs)
        req.setParameter("format", format)
        req.setParameter("layers", @layers)
        req.setParameter("request", "GetMap")
        req.setParameter("service", "WMS")
        req.setParameter("version", "1.1.1")
        
        return req        
      end
    end
  end
end