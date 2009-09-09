module Reports  
  class ProjectReport < Ruport::Controller::Table
    prepare :columns
    stage :format_columns, :table_header, :table_body, :table_footer
    
    def setup
      self.data ||= ProjectAggregator.new(options).data
    end
    
    class HTML < Ruport::Formatter::HTML
      include Ruport::Extras::ColumnFormatter
      extend ActionView::Helpers::TagHelper
      extend ActionView::Helpers::UrlHelper
      extend ApplicationHelper
      extend OptionsHelper
      extend ReportsHelper
      extend I18nHelper
      
      renders :html, :for => [ProjectReport]
      
      format_column(:factsheet_link) { |r| %{<a href="/projects/#{r}"><img src="/images/details.gif" width="10" height="14" alt="Factsheet" /></a>} }
      format_column(:description) { |r| short_expander_tag(r) }
      format_column(:comment) { |r| short_expander_tag(r) }
      format_column(:markers) { |r| markers_list(r) }
      format_column(:implementing_agencies) { |r| format_as_html_list(r.map(&:name)) }
      format_column(:contracted_agencies) { |r| format_as_html_list(r.map(&:name)) }
      format_column(:mdgs) { |r| format_as_html_list(r.map(&:name), :class => 'fatlist') }
      format_column(:website) { |r| r.to_link }
      format_column(:grant_loan) { |r| option_text_by_id(Project::GRANT_LOAN_OPTIONS, r) }
      format_column(:prj_status) { |r| option_text_by_id(Project::STATUS_OPTIONS, r) }
      format_column(:national_regional) { |r| option_text_by_id(Project::NATIONAL_REGIONAL_OPTIONS, r) }
      format_column(:type_of_implementation) { |r| option_text_by_id(Project::IMPLEMENTATION_TYPES, r) }
      # TODO: Translation
      format_column(:focal_regions) { |r| r.empty? ? I18n.t('options.national') :  r.map(&:name).sort.join("<br />") }
      format_column(:country_strategy) do |r| 
        if r.blank?
          I18n.t('reports.na')
        else 
          reports_link_to(r.strategy_number, "http://nic.odadata.eu/country_strategies/#{r.id}")
        end
      end
      format_column(:sectors) do |r| 
        r.map { |sr| "#{sr.sector.name_with_code} (#{sr.amount}%)" }.join('<br />')
      end
      
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

    class Excel < Ruport::Formatter
      include Ruport::Extras::ColumnFormatter
      
      renders :xls, :for => [ProjectReport]
      OFFSET_TOP = 0
      OFFSET_LEFT = 0
      
      format_column(:implementing_agencies) { |r| r.map(&:name).join(', ') }
      format_column(:contracted_agencies) { |r| r.map(&:name).join(', ') }
      format_column(:mdgs) { |r| r.map(&:name).join(', ') }
      format_column(:grant_loan) { |r| option_text_by_id(Project::GRANT_LOAN_OPTIONS, r) }
      format_column(:prj_status) { |r| option_text_by_id(Project::STATUS_OPTIONS, r) }
      format_column(:national_regional) { |r| option_text_by_id(Project::NATIONAL_REGIONAL_OPTIONS, r) }
      format_column(:type_of_implementation) { |r| option_text_by_id(Project::IMPLEMENTATION_TYPES, r) }
      format_column(:country_strategy) { |r| r.strategy_number }
      format_column(:focal_regions) { |r| r.empty? ? I18n.t('options.national') :  r.map(&:name).sort.join("<br />") }
      format_column(:sectors) do |r| 
        r.map { |sr| "#{sr.sector.name_with_code} (#{sr.amount}%)" }.join('\n')
      end
      
      def setup_excel
        @workbook = Spreadsheet::Workbook.new
        @worksheet = @workbook.add_worksheet(Spreadsheet::Worksheet.new(:name => "ODAnic Custom Report"))
      end
      
      def prepare_columns
        # Remove factsheet link
        data.remove_column(:factsheet_link)
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
end
