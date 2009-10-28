class ActiveRecord::Base
  class << self    
    def human_attribute_name(attribute_or_association_name, options = {})
      attribute_key_name = attribute_or_association_name.to_s.sub(/_id$/, '')
      defaults = self_and_descendants_from_active_record.map do |klass|
        "#{klass.name.underscore}.#{attribute_key_name}.label"
      end
      defaults << options[:default] if options[:default]
      defaults.flatten!
      defaults << attribute_key_name.humanize + "(miss)"
      options[:count] ||= 1
      I18n.translate(defaults.shift, options.merge(:default => defaults, :scope => [:activerecord, :attributes]))
    end
  end
end