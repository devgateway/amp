module ActionView::Helpers::RelatedSelectFormHelper
  def related_collection_select(object_name, method, parent_element, collection, group_method, group_label_method, option_key_method, option_value_method, options = {}, html_options = {})                  
    parent_tag_id = parent_element.is_a?(Array) ? parent_element.collect{|t| t.to_s}.join('_') : parent_element.to_s

    ActionView::Helpers::InstanceTag.new(object_name, method, self, options.delete(:object)).
      to_related_collection_select_tag(parent_tag_id, collection, group_method, group_label_method, option_key_method, option_value_method, options, html_options)
  end

end    
ActionView::Base.send :include, ActionView::Helpers::RelatedSelectFormHelper



class ActionView::Helpers::InstanceTag #:nodoc:  
  include ActionView::Helpers::JavaScriptHelper

  def to_related_collection_select_tag(parent_tag_id, collection, group_method, group_label_method, option_key_method, option_value_method, options = {}, html_options = {})  #:nodoc: 
    html_options.stringify_keys!
    add_default_name_and_id(html_options)

    selected_value = options.has_key?(:selected) ? options[:selected] : value(object)
    prompt = options.has_key?(:prompt) ? "<option>#{options[:prompt]}</option>" : ""
    choices = option_groups_from_collection_for_select_with_prompt(collection, group_method, group_label_method,
      option_key_method, option_value_method, selected_value,prompt)
									
    content_tag("select", prompt + choices, html_options)
   end
  
  def option_groups_from_collection_for_select_with_prompt(collection, group_method, group_label_method, option_key_method, option_value_method, selected_key = nil,prompt=nil)
    collection.inject("") do |options_for_select, group|
      group_label_string = group.instance_eval(group_label_method.to_s)
      
      options_for_select += %{<optgroup label="#{html_escape(group_label_string)}">}
      options_for_select += prompt
      options_for_select += options_from_collection_for_select(group.instance_eval(group_method.to_s), option_key_method, option_value_method, selected_key)
      options_for_select += "</optgroup>"
    end
  end
end
