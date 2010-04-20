module ProjectsHelper
  def factsheet_link(project)
    img = image_tag('/images/details.gif', :size => "14x15", :class => 'no_print', 
      :title => I18n.t('project.factsheet_link'))
      
    link_to(img, project, :popup => ['_blank', 'height=800,width=800,scrollbars=1'])
  end
  
  def aid_modality_select(form)
    choices = AidModality.all.group_by(&:group_name)
    ungrouped = options_from_collection_for_select(choices.delete(nil), :id, :name, form.object.aid_modality_id)
    groups = choices.inject("") do |s, (k, values)|
      s << content_tag(:option, k, :value => k.gsub(/ +/,'_'), :group => "true")
    end
    blank_option = content_tag(:option,  ll(:options, :prompt))
    grouped = choices.inject("") do |s, (k, values)|
      s << select_tag(k, blank_option + options_from_collection_for_select(values, :id, :name, form.object.aid_modality_id), :style => "display:none;")
    end

    form.label(:aid_modality_id) + hidden_field_tag("#{form.object_name}[aid_modality_id]", form.object.aid_modality_id) + select_tag("#{form.object_name}[dummy_aid_modality_id]", blank_option + ungrouped + groups) + grouped
  end
end