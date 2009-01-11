module Report
  module Proxy
    class Html < Proxy::Base      
      def factsheet_link
        [" ", %{<a href="/projects/#{@target.id}?report=with_currency_selector" onclick="report_window(this.href); return false;">
          <img width="14" height="15" border="0" src="/images/details.gif" alt="Factsheet" class="no_print"/></a>}]
      end
      
      def description
        ["Description", short_expander_tag(@target.description)]
      end
      
      def comments
        ["Comments", short_expander_tag(@target.comments)]
      end
          
      def markers
        ["Rio &amp; Policy Markers", markers_list(@target)]
      end
      
      def impl_agencies
        ["Implementing Agencies", format_as_html_list(@target.implementing_agencies.map(&:name))]      
      end
      
      def contr_agencies
        ["Contracted Agencies", format_as_html_list(@target.contracted_agencies.map(&:name))]    
      end
      
      def mdg_goals
        ["MDG Goals", format_as_html_list(@target.mdgs.map(&:name), :class => "fatlist")]
      end
      
      def website
        ["Internet Link", @target.website.to_link]
      end
      
      def strategy_link
        id = @target.country_strategy_id
        
        if id.blank?
          ["Strategy Link", "n/a"]
        else
          ["Strategy Link", reports_link_to('<img alt="Details" src="/images/details.gif" />', "http://nic.odadata.eu/country_strategy/show/#{id}")]
        end
      end
      
      def funds(year)
        cols = []
        
        if year <= Time.now.year
          cols << ["Total Commitments #{year}", @target.total_commitments(year)]
          cols << ["Total Disbursements #{year}", @target.total_payments(year)]
          cols << ["Quarterly Disbursements #{year}", quarterly_payments(@target, year)]
        end
        
        if year >= Time.now.year
          cols << ["Commitments Forecast #{year}", @target.funding_forecasts.find_by_year(year).andand.commitments]
          cols << ["Disbursements Forecast #{year}", @target.funding_forecasts.find_by_year(year).andand.payments]
        end
        
        cols
      end
    end
  end
end