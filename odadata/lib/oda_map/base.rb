require "mapscript"

module OdaMap
  class Base
    include Mapscript
    
    def initialize
      @map = MapObj.new(OdaMap::MAPFILE)
    end
    
    ## Query
    def query_by_lon_lat(lon, lat)
      coords = PointObj.new(lon, lat)   

      # Region, Municipality
      [query_point(coords, "NIC1"), query_point(coords, "NIC2")]
    end
    
  private
    def query_point(point, layer)
      layer = @map.getLayerByName(layer)
      layer.queryByPoint(@map, point, MS_SINGLE, 4)
            
      layer.open
        qres = layer.getResults
        feature = layer.getFeature(qres.getResult(0).shapeindex)
      layer.close
      
      feature
    end
  end
end