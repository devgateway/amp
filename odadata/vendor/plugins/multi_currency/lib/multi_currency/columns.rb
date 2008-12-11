# This module is designed to provide methods for easy conversion of 
# Integer fields to the Currency class in your models.
module MultiCurrency
  module Columns
    def self.included(base)
      base.extend ClassMethods
    end
    
    module ClassMethods
      def currency_columns(*args)
        options = args.extract_options!
        args.flatten!
        
        args.each do |col|
          # Attribute getter with currency formatting
          define_method(col) do
            currency = options[:currency].respond_to?(:call) ? options[:currency].call(self) : options[:currency]
            year = options[:year].respond_to?(:call) ? options[:year].call(self) : options[:year]
            
            ConvertibleCurrency.new(read_attribute(col), (currency || Prefs.default_currency), year)
          end
          
          # Attribute setter with currency parsing
          define_method("#{col}=") do |value|
            write_attribute(col, ConvertibleCurrency.new(value).to_i)
          end
        end
  
        if options[:validations]
          self.class_eval <<-END_SRC
            validates_each :#{args.join(", :")}, :allow_blank => true, :allow_nil => true do |record, attr, value|
              if value.base_value.to_s =~ /\./
                record.errors.add attr, 'should not be a decimal.'
              end
            end
          END_SRC
        end
      end
    end
  end
end
