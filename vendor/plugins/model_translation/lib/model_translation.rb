module ModelTranslation
  DEFAULT_LANGUAGE = :en
  AVAILABLE_LANGUAGES = [:en, :es]
  
  def self.included(base)
    base.extend ClassMethods
    class << base; alias_method_chain :find, :localization; end
  end
  
  module ClassMethods
    attr_accessor :localized_attributes
    
    def translates(*attributes)
      attributes.each do |attrib|
        class_eval <<-END_SRC
          def #{attrib}
            lang = I18n.locale.to_sym
            localized_attribute = "#{attrib}_\#{lang}"
            
            if lang != :#{DEFAULT_LANGUAGE} && self.class.content_columns.map(&:name).include?(localized_attribute)
              read_attribute(localized_attribute)
            else
              read_attribute(:#{attrib})
            end
          end
          
          def non_localized_#{attrib}
            read_attribute(:#{attrib})
          end          
        END_SRC
        
        (self.localized_attributes ||= []) << attrib.to_sym
      end
    end 
            
    def find_with_localization(*args)
      return find_without_localization(*args) unless self.localized_attributes 
      
      lang = I18n.locale.to_sym
      if lang != DEFAULT_LANGUAGE
        options = args.extract_options!
    
        %w( order group select ).each do |key|
          if options[key.to_sym]
            self.localized_attributes.each do |attrib|
              localized_attribute = "#{attrib}_#{lang}"
              options[key.to_sym].to_s.gsub!(attrib.to_s, localized_attribute)
            end
          end
        end      
        
        args << options
      end
      
      find_without_localization(*args)
    end
  end
end
