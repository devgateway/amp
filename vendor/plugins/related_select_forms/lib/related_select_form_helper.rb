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
    prompt = options.has_key?(:prompt) ? "<select>#{options[:prompt]}</select>" : ""
    choices = option_groups_from_collection_for_select(collection, group_method, group_label_method, 
									option_key_method, option_value_method, selected_value)
									
    content_tag("select", prompt + choices, html_options)
    
  end 
end







