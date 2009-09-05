module Reports
  class SectorDisaggregator < Ruport::Middleware
    def initialize(sector_id)
      @sector_id = sector_id
    end
    
    def process_record(object, preprocessed_record)
      sector_relevances = object.sector_relevances.find_all_by_dac_sector_id(@sector_id)
      percentage = sector_relevances.sum(&:amount) / 100.0
      
      [:total_commitments, :total_disbursements, :commitments_forecast, :disbursements_forecast].each do |f|
        preprocessed_record[f] = preprocessed_record[f] * percentage if preprocessed_record[f]
      end
    end
  end
  
  class LocationDisaggregator < Ruport::Middleware
    def initialize(disaggregate_by, location_id)
      @disaggregate_by = disaggregate_by
      @location_id = location_id
    end
    
    def process_record(object, preprocessed_record)
      if @disaggregate_by == :province
        geo_relevances = object.geo_relevances.find_all_by_province_id(@location_id)
      elsif @disaggregate_by == :district
        geo_relevances = object.geo_relevances.find_all_by_district_id(@location_id)
      end
      
      percentage = geo_relevances.sum(&:amount) / 100.0
      [:total_commitments, :total_disbursements, :commitments_forecast, :disbursements_forecast].each do |f|
        preprocessed_record[f] = preprocessed_record[f] * percentage if preprocessed_record[f]
      end
    end
  end
  
  class ProjectReport < Ruport::Controller::Table
    stage :table_header, :table_body, :table_footer
    
    def setup
      self.data ||= ProjectAggregator.new(options).data
    end
    
    formatter :html do 
      build :table_header do
        output << "<table><tr>"
        data.column_names.each do |c|
          output << "<th>" + Project.human_attribute_name(c.to_s) + "</th>"
        end
        output << "</tr>"
      end
  
      build :table_body do
        data.each do |record|
          output << "<tr>"
          record.each do |c|
            if c.is_a?(MultiCurrency::ConvertibleCurrency)
              output << %{<td class="currency right">#{c || I18n.t('reports.na')}</td>}
            else
              output << %{<td>#{c}</td>}
            end
          end
          output << "</tr>"
        end
      end
  
      build :table_footer do
        output << "</table>"
      end
    end
  end
end

__END__
module Reports::Formatters
  class HTML < Ruport::Formatter::HTML
    renders :html, :for => [DonorReport, SectorReport, ProvinceReport, MdgReport]
    
    build :table_header do
      output << "<table><tr>"
      data.column_names.each do |c|
        output << "<th>" + Project.human_attribute_name(c.to_s) + "</th>"
      end
      output << "</tr>"
    end
    
    build :table_body do
      data.each do |record|
        output << "<tr>"
        record.each do |c|
          output << "<td>" + c.to_s + "</td>"
        end
        output << "</tr>"
      end
    end
    
    build :table_footer do
      output << "</table>"
    end
  end

  class Excel < Ruport::Formatter
    renders :xls, :for => [DonorReport, SectorReport, ProvinceReport, MdgReport]
    OFFSET_TOP = 0
    OFFSET_LEFT = 0
    
    def setup_excel
      @workbook = Spreadsheet::Workbook.new
      @worksheet = @workbook.add_worksheet(Spreadsheet::Worksheet.new(:name => "ODAnic Custom Report"))
    end
    
    build :table_header do   
      headings = data.column_names
      @worksheet.write_row(OFFSET_TOP, OFFSET_LEFT, encode_row_for_excel(headings), @workbook.add_format(:color => "blue", :bold => 1))
    end
    
    build :table_body do
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
