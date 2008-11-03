module Report
  module Data
    class Record < OrderedHash
      def initialize(elements = [])
        if elements.is_a?(Array)
          # Flatten to two-level array if neccessary
          elements = elements.inject([]) {|res, it| it.first.is_a?(Array) ? res + it : res << it }
        end
        
        super(elements)
      end
    end
  end
end