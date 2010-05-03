module OdaMap
  class ProjectInfo < OdaMap::Base
    def initialize(project)
      super()
      @project = project
      
      municipalities_layer = @map.getLayerByName("NIC2")
      municipalities_layer.labelitem = nil
      
      cls = ClassObj.new()
      style = StyleObj.new()
      color = ColorObj.new(237, 236, 120)
      outlinecolor = ColorObj.new(0, 0, 0)
      
      style.color = color
      style.outlinecolor = outlinecolor
      
      cls.insertStyle(style)
      cls.setExpression(build_expression(@project))
      
      municipalities_layer.insertClass(cls, 0)
    end
    
    def save
      file = File.join(RAILS_ROOT, "public", "maps", "project_#{@project.id}.png")
      img = @map.draw
      img.save(file)
      
      file
    end
    
private
    def build_expression(prj)
      # This highlights all regions for national projects
      return nil if prj.districts.empty? && prj.provinces_with_total_coverage.empty?
      
      district_ids = prj.district_ids
      
      if (provinces = prj.provinces_with_total_coverage).any?
        district_ids << provinces.map(&:district_ids)
      end
      
      %{( [id] IN "#{district_ids.join(', ')}" )}
    end
  end
end
    
# EXAMPLES:
# boaco = municipalities_layer.queryByAttributes(@map, "NAME_2", "Boaco", MS_SINGLE)
# municipalities_layer.open
# oRes = municipalities_layer.getResults
# shapeObj = municipalities_layer.getFeature(oRes.getResult(0).shapeindex)
# municipalities_layer.close