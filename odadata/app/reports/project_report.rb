module Reports  
  class ProjectReport < Ruport::Controller::Table
    AGGREGATEABLE_COLS = %w(total_commitments total_disbursements commitments_forecast disbursements_forecast total_cofunding)
    
    prepare :columns
    stage :format_columns, :table_structure, :table_header, :table_body, :table_footer, :output
    
    def setup
      self.data ||= ProjectAggregator.new(options.to_hash).data
    end
    
    class ProjectFormatter < Ruport::Formatter
      include Ruport::Extras::ColumnFormatter
      extend ActionView::Helpers::TagHelper
      extend ActionView::Helpers::UrlHelper
      extend ActionView::Helpers::TextHelper
      extend ActionView::Helpers::NumberHelper
      extend ApplicationHelper
      extend OptionsHelper
      extend ReportsHelper
      extend I18nHelper
      
      format_column(:grant_loan) { |r| option_text_by_id(Project::GRANT_LOAN_OPTIONS, r) }
      format_column(:prj_status) { |r| option_text_by_id(Project::STATUS_OPTIONS, r) }
      format_column(:national_regional) { |r| option_text_by_id(Project::NATIONAL_REGIONAL_OPTIONS, r) }
      format_column(:type_of_implementation) { |r| option_text_by_id(Project::IMPLEMENTATION_TYPES, r) }
      format_column(:geo_relevances) do |r| 
        if r.empty?
          I18n.t('options.national')
        else
          provinces_with_amounts = r.group_by(&:province).map { |p, a| [p.name, a.sum(&:amount)] }
          provinces_with_amounts.sort! { |a, b| b[1] <=> a[1] }
          provinces_with_amounts.map { |(p, a)| "#{p} (#{number_to_percentage(a, :precision => 1)})" }.join('<br />')
        end
      end
      
    protected
      def localized_heading_for(column)
        if column.to_s =~ /(total_commitments|total_disbursements|commitments_forecast|disbursements_forecast)_([0-9]{4})/
          search_paths = [
            :"activerecord.attributes.project.#{$1}_in.report_heading",
            :"activerecord.attributes.project.#{$1}_in.label",
            $1.humanize + "(miss)"]
            
          I18n.t(search_paths.shift, :default => search_paths, :year => $2)
        else
          search_paths = [
            :"activerecord.attributes.project.#{column}.report_heading",
            :"activerecord.attributes.project.#{column}.label",
            column.to_s.humanize + "(miss)"]
            
          I18n.t(search_paths.shift, :default => search_paths)
        end
      end
      
      def build_totals
        fields = AGGREGATEABLE_COLS.select { |f| data.column_names.include?(f) }
        fields += data.column_names.select { |f| f =~ /(total_commitments|total_disbursements|commitments_forecast|disbursements_forecast)_([0-9]{4})/ }

        totals_rec = Ruport::Data::Record.new([], :attributes => data.column_names)
        # TODO: Translation
        totals_rec[data.column_names.first] = "TOTAL"
        fields.each do |f|
          totals_rec[f] = data.column(f).reject(&:nil?).sum
        end
        
        totals_rec
      end
    end
      
    class HTML < ProjectFormatter
      renders :html, :for => [ProjectReport]
      
      format_column(:factsheet_link) { |r| %{<a href="/projects/#{r}"><img src="/images/details.gif" width="10" height="14" alt="Factsheet" /></a>} }
      format_column(:description) { |r| short_expander_tag(r) }
      format_column(:comment) { |r| short_expander_tag(r) }
      format_column(:markers) { |r| markers_list(r) }
      format_column(:implementing_agencies) { |r| format_as_html_list(r.map(&:name)) rescue nil }
      format_column(:contracted_agencies) { |r| format_as_html_list(r.map(&:name)) rescue nil }
      format_column(:mdgs) { |r| format_as_html_list(r.map(&:name), :class => 'fatlist') rescue nil }
      format_column(:website) { |r| auto_link(r) }
      format_column(:country_strategy) do |r| 
        if r.blank?
          I18n.t('reports.na')
        else 
          reports_link_to(r.strategy_number, "http://nic.odadata.eu/country_strategies/#{r.id}")
        end
      end
      format_column(:sector_relevances) do |r| 
        r.map { |sr| "#{sr.sector.name_with_code} (#{number_to_percentage(sr.amount, :precision => 1)})" }.join('<br />')
      end
      format_column(:cofundings) do |r|
        r.map { |sr| "#{sr.donor.name}: #{number_to_percentage(sr.amount, :precision => 1)}" }.join('<br />')
      end
      
      build :table_header do
        output << '<table class="sortable"><thead><tr>'
        data.column_names.each do |c|
          output << "<th>" + localized_heading_for(c) + "</th>"
        end
        output << "</thead></tr>"
      end

      build :table_body do
        output << "<tbody>"
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
        output << "</tbody>"
      end
       
      build :table_footer do
        output << '<tr class="totals">' 
        output << build_totals.map { |t| %{<td class="currency right">#{t}</td>} }.join
        output << '</tr>'
        output << "</table>"
      end      
    end

    class Excel < ProjectFormatter
      renders :xls, :for => [ProjectReport]
      OFFSET_TOP = 0
      OFFSET_LEFT = 0
      
      format_column(:implementing_agencies) { |r| r.map(&:name).join(', ') }
      format_column(:contracted_agencies) { |r| r.map(&:name).join(', ') }
      format_column(:mdgs) { |r| r.map(&:name).join(', ') }
      format_column(:country_strategy) { |r| r.strategy_number }
      format_column(:sector_relevances) do |r| 
        r.map { |sr| "#{sr.sector.name_with_code} (#{number_to_percentage(sr.amount, :precision => 1)}%)" }.join(', ')
      end
      format_column(:cofundings) do |r|
        r.map { |sr| "#{sr.donor.name}: #{number_to_percentage(sr.amount, :precision => 1)}" }.join("\n")
      end
      format_column(:geo_relevances) do |r| 
        if r.empty?
          I18n.t('options.national')
        else
          provinces_with_amounts = r.group_by(&:province).map { |p, a| [p.name, a.sum(&:amount)] }
          provinces_with_amounts.sort! { |a, b| b[1] <=> a[1] }
          provinces_with_amounts.map { |(p, a)| "#{p} (#{number_to_percentage(a, :precision => 1)})" }.join("\n")
        end
      end
      
      def prepare_columns
        # Remove factsheet link
        data.remove_column(:factsheet_link)
      end
      
      build :table_structure do 
        @workbook = Spreadsheet::Workbook.new
        @worksheet = @workbook.add_worksheet(Spreadsheet::Worksheet.new(:name => "ODAnic Custom Report"))
      end
      
      build :table_header do   
        headings = data.column_names.map { |h| localized_heading_for(h) }
        @worksheet.insert_row(OFFSET_TOP, encode_row_for_excel(headings))
      end

      build :table_body do
        data.each_with_index do |row, offset|
          @worksheet.insert_row(OFFSET_TOP + 1 + offset, encode_row_for_excel(row))
        end
      end
      
      build :table_footer do
        @worksheet.insert_row(OFFSET_TOP + 1 + data.length, encode_row_for_excel(build_totals))
      end
      
      build :output do 
        buffer = StringIO.new
        @workbook.write(buffer)
        
        output << buffer.string
      end
      
    private
      # Changes encoding to ISO-8859-1, the only compatible encoding for our spreadsheet generator
      def encode_row_for_excel(row)
        # Convert
        row.map do |cell| 
          # If this is a currency object convert to string without unit identifier
          if cell.is_a?(MultiCurrency::ConvertibleCurrency)
            str_repr = cell.to_i
          else
            str_repr = cell.to_s
            #str_repr = cell.is_a?(MultiCurrency::ConvertibleCurrency) ? cell.to_s(false) : cell.to_s
          end
        end
      end
    end

  end
end
