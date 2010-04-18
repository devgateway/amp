module OdaMap
  class Report < OdaMap::Base
    STYLE1 = returning(StyleObj.new) do |style|
      style.color = ColorObj.new(153, 153, 153)
      style.outlinecolor = ColorObj.new(0, 0, 0)
    end
    
    STYLE2 = returning(StyleObj.new) do |style|
      style.symbol = 1
      style.color = ColorObj.new(153, 51, 153)
      style.outlinecolor = ColorObj.new(0, 0, 0)
      style.angle = 90
      style.size = 3
      style.width = 1
    end
    
    STYLE3 = returning(StyleObj.new) do |style|
      style.symbol = 1
      style.color = ColorObj.new(255, 51, 51)
      style.outlinecolor = ColorObj.new(0, 0, 0)
      style.angle = 0
      style.size = 3
      style.width = 1
    end
    
    STYLE4 = returning(StyleObj.new) do |style|
      style.symbol = 1
      style.color = ColorObj.new(51, 204, 51)
      style.outlinecolor = ColorObj.new(0, 0, 0)
      style.angle = 45
      style.size = 3
      style.width = 1
    end
    
    STYLE5 = returning(StyleObj.new) do |style|
      style.symbol = 1
      style.color = ColorObj.new(51, 102, 153)
      style.outlinecolor = ColorObj.new(0, 0, 0)
      style.angle = 135
      style.size = 3
      style.width = 1
    end
    
    STYLES = [STYLE1, STYLE2, STYLE3, STYLE4, STYLE5]
    
    def initialize(projects)
      super()
      @projects = projects
      
      municipalities_layer = @map.getLayerByName("NIC2")
      municipalities_layer.labelitem = nil
      municipalities_layer.getClass(0).getStyle(0).color = ColorObj.new(255, 255, 255)
            
      @projects.each_with_index do |prj, idx|
        new_layer = municipalities_layer.clone
        cls = ClassObj.new()
              
        cls.insertStyle(STYLES[idx])
        new_layer.setFilter(build_expression(prj))
        
        new_layer.insertClass(cls, 0)
        @map.insertLayer(new_layer)
      end
    end
    
    def save
      file = File.join(RAILS_ROOT, "public", "maps", "report_#{Time.now.hash}.png")
      img = @map.draw
      img.save(file)
      
      file
    end
    
private
    def build_expression(prj)
      # This highlights all regions for national projects
      return nil if prj.districts.empty?
      
      #criteria = returning([]) do |crit|
      #  prj.districts.each do |dist|
      #    crit << "([id] == #{dist.id})"
      #  end
      #end
      #
      #"(" + criteria.join(" OR ") + ")"     
      "id IN (#{prj.districts.map(&:id).join(', ')})"    
    end
  end
end
    
# EXAMPLES:
# boaco = municipalities_layer.queryByAttributes(@map, "NAME_2", "Boaco", MS_SINGLE)
# municipalities_layer.open
# oRes = municipalities_layer.getResults
# shapeObj = municipalities_layer.getFeature(oRes.getResult(0).shapeindex)
# municipalities_layer.close