# This class can be used to build tabular HTML-Reports
# for online viewing, based on the ODAdata Report System
module Report
  class Html < Base
    include I18nHelper
        
    def output_start
      %{<h2 style="text-align: center">ODAnic #{ll(:reports, :custom, :title)}</h2>
      <p class="disclaimer">
        #{I18n.t('reports.disclaimer')}
      </p>
      <table class="admin" style="border: 1px solid #bbb; margin-top: 10px">
      }
    end
    
    def output_head
      "<tr>" +
        data.columns.map { |h| "<th>#{h}</th>" }.join('') +
        "</tr>"
    end
        
    def output_body
      rows = []
      data.each_row do |r|
        rows << 
          "<tr>" + 
          r.map { |f| "<td#{' class="currency right"' if f.is_a?(MultiCurrency::ConvertibleCurrency)}>#{f || ll(:reports, :na)}</td>" }.join('') +
          "</tr>"
      end
      
      "#{rows.join('')}"
    end
    
    def output_totals
      totals = []
      data.each_column do |col|
        totals << %{<td class="currency right">#{col.total}</td>}
      end

      totals[0] = "<td><strong>TOTAL</strong></td>"
      %{<tr class="totals">#{totals.join}</tr>}
    end
    
    def output_end
      %{</table>}
    end
    
    def output
      output_start +
      output_head +
      output_body +
      output_totals +
      output_end
    end
  end
end