module ProjectsHelper
  def factsheet_link(project)
    img = image_tag('/images/details.gif', :size => "14x15", :class => 'no_print', 
      :title => I18n.t('project.factsheet_link'))
      
    link_to(img, project, :popup => ['_blank', 'height=800,width=800,scrollbars=1'])
  end
end