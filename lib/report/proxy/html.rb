module Report
  module Proxy
    class Html < Proxy::Base      
      def factsheet_link
        [" ", %{<a href="/projects/#{@target.to_param}?report=with_currency_selector" onclick="report_window(this.href); return false;">
          <img width="14" height="15" border="0" src="/images/details.gif" alt="Factsheet" class="no_print"/></a>}]
      end
      
      def description
        [I18n.t('reports.project_desc'), short_expander_tag(@target.description)]
      end
      
      def comments
        [I18n.t('reports.comments'), short_expander_tag(@target.comments)]
      end
          
      def markers
        [I18n.t('terms.markers'), markers_list(@target)]
      end
      
      def impl_agencies
        [I18n.t('reports.impl_agencies'), format_as_html_list(@target.implementing_agencies.map(&:name))]      
      end
      
      def contr_agencies
        [I18n.t('reports.contr_agencies'), format_as_html_list(@target.contracted_agencies.map(&:name))]    
      end
      
      def mdg_goals
        [I18n.t('reports.mdg_goals'), format_as_html_list(@target.mdgs.map(&:name), :class => "fatlist")]
      end
      
      def website
        [I18n.t('terms.internet_link'), @target.website.to_link]
      end
      
      def strategy_link
        id = @target.country_strategy_id
        
        if id.blank?
          [I18n.t('reports.cs_link'), "n/a"]
        else
          [I18n.t('reports.cs_link'), reports_link_to('<img alt="Details" src="/images/details.gif" />', "http://nic.odadata.eu/country_strategies/#{id}")]
        end
      end
      
      def funds(year)
        cols = []
        
        if year <= Time.now.year
          cols << ["#{I18n.t('reports.commitments_td')} #{year}", @target.total_commitments(year)]
          cols << ["#{I18n.t('reports.disbursements_td')} #{year}", @target.total_payments(year)]
          cols << ["#{I18n.t('reports.quarterly_disbursements')} #{year}", quarterly_payments(@target, year)]
        end
        
        if year >= Time.now.year
          cols << [I18n.t('reports.commitments_forecast', :year => year), @target.funding_forecasts.find_by_year(year).andand.commitments]
          cols << [I18n.t('reports.payments_forecast', :year => year), @target.funding_forecasts.find_by_year(year).andand.payments]
        end
        
        cols
      end
    end
  end
end