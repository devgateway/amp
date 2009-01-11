# This is for doing all kinds of translation tasks,
# including translation of constant options and automatic
# selection of localized table columns
module Translator
  DEFAULT_LANGUAGE = :en
  AVAILABLE_LANGUAGES = [:en, :es]
  def self.extended(base)
    base.class_eval <<-END_SRC
      class << #{base}; alias_method_chain :find, :localization end
      
      @@localized_attributes = []
      cattr_accessor :localized_attributes
    END_SRC
  end
  
   
  def translates(*attributes)
    attributes.each do |attrib|
      class_eval <<-END_SRC
        def #{attrib}
          lang = I18n.locale
          localized_attribute = "#{attrib}_\#{lang}"
          
          if lang != Translator::DEFAULT_LANGUAGE && self.class.content_columns.map(&:name).include?(localized_attribute)
            read_attribute(localized_attribute)
          else
            read_attribute(:#{attrib})
          end
        end
        
        def non_localized_#{attrib}
          read_attribute(:#{attrib})
        end
        
        localized_attributes << "#{attrib}"
      END_SRC
    end
  end
  
  def find_with_localization(*args)
    lang = I18n.locale
    
    if lang != DEFAULT_LANGUAGE
      options = args.extract_options!

      %w( order group select ).each do |key|
        if options[key.to_sym]
          self.localized_attributes.each do |attrib|
            localized_attribute = "#{attrib}_#{lang}"
            options[key.to_sym].to_s.gsub!(attrib, localized_attribute)
          end
        end
      end      
      
      args << options
    end
    
    find_without_localization(*args)
  end
end
