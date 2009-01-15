module OdaMap
  class ProjectInfo < OdaMap::Base
    def initialize(project)
      super()
      @project = project
      
      municipalities_layer = @map.getLayerByName("NIC2")
      
      
      cls = ClassObj.new()
      style = StyleObj.new()
      color = ColorObj.new(237, 236, 120)
      outlinecolor = ColorObj.new(0, 0, 0)
      
      style.color = color
      style.outlinecolor = outlinecolor
      
      cls.insertStyle(style)
      cls.setExpression(build_expression)
      
      municipalities_layer.insertClass(cls, 0)
    end
    
    def save
      file = File.join(RAILS_ROOT, "public", "maps", "project_#{@project.id}.png")
      img = @map.draw
      img.save(file)
      
      file
    end
    
private
    def build_expression
      # This highlights all regions for national projects
      return nil if @project.geo_list.blank?
      
      criteria = []
      @project.geo_list.each do |l1, l2s|
        # TODO: We probably shouldn't check for the exact string here
        if l2s.include?("All Municipalities")
          criteria << "('[NAME_1]' eq '#{l1}' AND '[HASC_2]' ne 'lake')"
        else
          l2s.each do |l2|
            criteria << "('[NAME_1]' eq '#{l1}' AND '[NAME_2]' eq '#{l2}')"
          end
        end
      end
      
      "(" + criteria.join(" OR ") + ")"         
    end
  end
end
    
# EXAMPLES:
# boaco = municipalities_layer.queryByAttributes(@map, "NAME_2", "Boaco", MS_SINGLE)
# municipalities_layer.open
# oRes = municipalities_layer.getResults
# shapeObj = municipalities_layer.getFeature(oRes.getResult(0).shapeindex)
# municipalities_layer.close