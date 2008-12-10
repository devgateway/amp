module ActiveRecord
  module Validations
    module Partial
      def valid_for_attributes?(*attr_names)
        return validate_for_attributes(*attr_names)
      end
      
      def validate_for_attributes(*attr_names)
        attr_names.flatten!
        attr_names.map!(&:to_s)
        
        unless valid?
          stage_errors = errors.select { |attr, error| attr_names.include?(attr) }
          errors.clear
          stage_errors.each { |attr, error| errors.add(attr, error) }
        end
        
        errors.empty?
      end
        
    end
  end
  
  class Errors
    # Returns a new instance of ActiveRecord:Errors with errors only for those fields given in attr_names
    def select(*attr_names)
      relevant_errors = self.dup
      relevant_errors.instance_eval do 
        @errors.delete_if { |attr, messages| !attr_names.map(&:to_s).include?(attr) } 
      end
      
      relevant_errors
    end
  end
end

module PartialValidationHelper
  # This allows to show only a specific subset of error messages
  def error_messages_for(*params)
    options = params.extract_options!.symbolize_keys
  
    if object = options.delete(:object)
      objects = [object].flatten
    else
      objects = params.collect {|object_name| instance_variable_get("@#{object_name}") }.compact
    end
  
    count  = objects.inject(0) { |sum, object| sum + filter_attributes(object, options[:attributes]).count }
  
    unless count.zero?
      html = {}
      [:id, :class].each do |key|
        if options.include?(key)
          value = options[key]
          html[key] = value unless value.blank?
        else
          html[key] = 'errorExplanation'
        end
      end
      options[:object_name] ||= params.first
  
      I18n.with_options :locale => options[:locale], :scope => [:activerecord, :errors, :template] do |locale|
        header_message = if options.include?(:header_message)
          options[:header_message]
        else
          object_name = options[:object_name].to_s.gsub('_', ' ')
          object_name = I18n.t(object_name, :default => object_name, :scope => [:activerecord, :models], :count => 1)
          locale.t :header, :count => count, :model => object_name
        end
        message = options.include?(:message) ? options[:message] : locale.t(:body)
        error_messages = generate_error_messages_for_attributes(objects, options[:attributes])
  
        contents = ''
        contents << content_tag(options[:header_tag] || :h2, header_message) unless header_message.blank?
        contents << content_tag(:p, message) unless message.blank?
        contents << content_tag(:ul, error_messages)
  
        content_tag(:div, contents, html)
      end
    else
      ''
    end
  end
  
private
  def generate_error_messages_for_attributes(objects, attributes)
    objects.sum { |object| 
      filter_attributes(object, attributes).full_messages.map {|msg| content_tag(:li, msg) } 
    }.join
  end
  
  def filter_attributes(object, attributes)
    return object.errors unless attributes

    object.errors.select(*attributes)
  end
end        
