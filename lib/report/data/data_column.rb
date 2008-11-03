module Report
  module Data
    class DataColumn
      def initialize(elements = [])
        @elements = elements
      end
      
      # Returns true when this column has only nil or ConvertibleCurrency objects,
      # meaning it can be summed up
      def has_total?
        @elements.all? { |e| e.nil? || e.is_a?(MultiCurrency::ConvertibleCurrency) }
      end
      
      def total
        return nil unless has_total?
        @elements.inject(0.to_currency(Prefs.default_currency)) { |sum, e| sum + e }  
      end
    end
  end
end