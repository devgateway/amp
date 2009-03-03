module Report
  module Proxy
    class Excel < Proxy::Base
      # Return total Co-Funding with currency in column heading
      # to allow easy handling in Excel
      def factsheet_link
        nil
      end
      
      def total_cofunding
        cofunding = @target.total_cofunding
        ["#{I18n.t('reports.total_cofunding')} (#{cofunding.currency})", cofunding.in(MultiCurrency.output_currency)]
      end
      
      def impl_agencies
        [I18n.t('reports.impl_agencies'), @target.implementing_agencies.map(&:name).join(', ')]      
      end
      
      def contr_agencies
        [I18n.t('reports.contr_agencies'), @target.contracted_agencies.map(&:name).join(', ')]   
      end
      
      def mdg_goals
        # TODO: Put each goal in it's own column
        [I18n.t('reports.mdg_goals'), @target.mdgs.map(&:name).join(', ')]
      end
      
      def strategy_link
        id = @target.country_strategy_id
        
        if id.blank?
          # TODO: Translation
          [I18n.t('reports.cs_link'), "Not linked to any strategy"]
        else
          [I18n.t('reports.cs_link'), "http://nic.odadata.eu/country_strategies/#{id}"]
        end
      end
      
      def total_payments
        ["#{I18n.t('reports.disbursements_td')} (#{MultiCurrency.output_currency})", @target.total_payments.in(MultiCurrency.output_currency)]
      end
      
      def total_commitments
        ["#{I18n.t('reports.commitments_td')} (#{MultiCurrency.output_currency})", @target.total_commitments.in(MultiCurrency.output_currency)]
      end
      
      # Adds one column per focal region
      def focal_regions
        columns = []        
        columns << [I18n.t('reports.locations.national_project'), @target.geo_relevances.empty? ? I18n.t('reports.locations.national_project') : ""]

        @target.provinces.each do |prov|
          if (districts = @target.districts.find_all_by_province_id(prov.id)).any?
            columns << [prov.name, districts.map(&:name).join(", ")]
          else
            columns << [prov.name, I18n.t('reports.locations.all_districts')]
          end
        end
        
        columns
      end
      
      def markers
        markers = Markers.content_columns.map(&:name)
        
        markers.map do |marker|
          [marker.titleize, option_text_by_id(Markers::OPTIONS, @target.markers.send(marker))]
        end
      end
      
      def website
        [I18n.t('terms.internet_link'), @target.website]
      end
      
      def funds(year)  
        finances = @target.fundings.find_by_year(year)
        forecasts = @target.funding_forecasts.find_by_year(year)
        columns = []

        if year <= Time.now.year
          columns << ["#{I18n.t('reports.commitments_td')} #{year} (#{MultiCurrency.output_currency})", finances.andand.commitments.andand.in(MultiCurrency.output_currency)]
          columns << ["#{I18n.t('reports.disbursements_td')} #{year} (#{MultiCurrency.output_currency})", finances.andand.payments.andand.in(MultiCurrency.output_currency)]
                    
          columns += (1..4).map do |quarter|
           ["#{I18n.t('terms.disbursements')} Q#{quarter}/#{year} (#{MultiCurrency.output_currency})", finances.andand.send("payments_q#{quarter}").andand.in(MultiCurrency.output_currency)]
          end
        end
        
        if year >= Time.now.year
          columns << ["#{I18n.t('reports.commitments_forecast', :year => year)} (#{MultiCurrency.output_currency})", forecasts.andand.commitments.andand.in(MultiCurrency.output_currency)]
          columns << ["#{I18n.t('reports.payments_forecast', :year => year)} (#{MultiCurrency.output_currency})", forecasts.andand.payments.andand.in(MultiCurrency.output_currency)]
        end
        
        columns
      end
      
    end # class
  end # module
end # module