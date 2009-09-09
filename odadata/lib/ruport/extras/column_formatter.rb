module Ruport
  module Extras
    module ColumnFormatter
      def self.included(base)
        base.extend ClassMethods
        base.instance_eval do 
          class_inheritable_accessor :column_formatters
          self.column_formatters = {}
          
          build :format_columns do
            columns_to_format = data.column_names.reject { |c| self.column_formatters[c.to_sym].nil? }
            columns_to_format.each do |column|
              formatter = self.column_formatters[column.to_sym]
              data.replace_column(column) { |r| formatter.call(r[column]) }
            end
          end
        end
      end
      
      module ClassMethods
        def format_column(field, &block)
          self.column_formatters[field.to_sym] = block
        end
      end
    end
  end
end