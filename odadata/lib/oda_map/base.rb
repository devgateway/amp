require "mapscript"

module OdaMap
  class Base
    include Mapscript
    
    def initialize
      @map = MapObj.new(OdaMap::MAPFILE)
    end
  end
end