module Report
  module Data
    class Table     
      # Total number of rows without headings
      attr_reader :length
      
      def initialize(records = [])
        @store = OrderedHash.new
        @length = 0
        
        add_records(records)
      end
      
      # Adds a new record to the table
      def add_record(record)  
        merge_headings_from_record(record)
        add_with_empty_fields(record)
        @length += 1
      end
      alias :<< :add_record 
      
      # Adds multiple new records to the table
      def add_records(records)
        records.each { |r| add_record(r) }
      end
      
      # Returns the existing column headings
      def columns
        @store.keys
      end
      
      # Iterate over all rows (without headings)
      def each_row
        0.upto(@length-1) do |i|
          yield @store.map { |k, v| v[i] }
        end
      end
      
      def each_row_with_offset
        0.upto(@length-1) do |i|
          # data, offset
          yield @store.map { |k, v| v[i] }, i 
        end
      end
      
      # Iterate over all columns (without headings)
      def each_column
        @store.each_value { |c| yield DataColumn.new(c) }
      end
     
    protected
      # Merges headings of each new record with existing ones
      def merge_headings_from_record(record)
        @store.keys.merge(record.keys)
      end
      
      # Adds new data to our tabular hash so that non-existing fields
      # will be added as nil:
      # { :a => [1, 2], :b => [3, 4], :c => [5, 6] }, merged with
      # { :a => 3, :c => 7} that way would become:
      # { :a => [1, 2, 3], :b => [3, 4, nil], :c => [5, 6, 7] }
      def add_with_empty_fields(record)
        @store.keys.each { |k| (@store[k] ||= []) << record[k] }
      end
    end
  end
end