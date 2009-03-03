module Report
  class Excel < Base
    OFFSET_TOP = 0
    OFFSET_LEFT = 0
      
    def setup_excel
      @file = "/reports/odanic_report_#{Time.now.year}_#{Time.now.month}_#{Time.now.day}_#{Time.now.day+Time.now.hour+Time.now.sec}.xls"
      @workbook = Spreadsheet::Excel.new(File.join(RAILS_ROOT, 'public', @file))
      @worksheet = @workbook.add_worksheet("ODAnic Custom Report")
    end
    
    
    # ====================================================
    # = Override abstract output methods from base class =
    # ====================================================
    
    def output_head
      headings = data.columns
      @worksheet.write_row(OFFSET_TOP, OFFSET_LEFT, encode_row_for_excel(headings), @workbook.add_format(:color => "blue", :bold => 1))
    end
    
    def output_body
      data.each_row_with_offset do |row, offset|
        @worksheet.write_row(OFFSET_TOP + 1 + offset, OFFSET_LEFT, encode_row_for_excel(row))
      end
    end
    
    def output_totals
      totals = []
      data.each_column do |col|
        totals << (col.has_total? ? col.total.to_s(false) : "")
      end
      
      @worksheet.write_row(OFFSET_TOP  + 1 + data.length, OFFSET_LEFT, encode_row_for_excel(totals), @workbook.add_format(:bold => 1))
    end
    
    def output    
      setup_excel
      output_head
      output_body
      output_totals
      
      @workbook.close
      return @file
    end
    
  protected
    # Changes encoding to ISO-8859-1, the only compatible encoding for our spreadsheet generator
    def encode_row_for_excel(row)
      # Strip characters that are not encodeable with ISO-8859-1 first
      allowed =/[^#{Regexp.escape(Iconv.iconv('utf-8', 'iso-8859-1', [*0..255].pack("C*")).first)}]/u
            
      # Convert
      row.map do |cell| 
        # If this is a currency object convert to string without unit identifier
        if cell.is_a?(MultiCurrency::ConvertibleCurrency)
          str_repr = cell.to_i
        else
          str_repr = cell.to_s
          #str_repr = cell.is_a?(MultiCurrency::ConvertibleCurrency) ? cell.to_s(false) : cell.to_s
          Iconv.iconv('ISO-8859-1', 'utf-8', str_repr.gsub(allowed, "")).first
        end
      end
    end
      
  end
end