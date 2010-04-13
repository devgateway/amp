module Ruport
  module Extras
    module ColumnFormatter
      def self.included(base)
        base.extend ClassMethods
        base.instance_eval do 
          class_inheritable_accessor :column_formatters
          self.column_formatters = {}
          
          build :format_columns do
            self.column_formatters.each do |expr, formatter|
              expr = expr.to_s if expr.is_a?(Symbol)
              data.column_names.select { |c| expr === c }.each do |column|
                data.replace_column(column) { |r| formatter.call(r[column]) }
              end
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