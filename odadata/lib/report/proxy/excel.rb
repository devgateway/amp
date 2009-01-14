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
        ["Total Co-Funding in #{cofunding.currency}", cofunding]
      end
      
      def impl_agencies
        ["Implementing Agencies", @target.implementing_agencies.map(&:name).join(', ')]      
      end
      
      def contr_agencies
        ["Contracted Agencies", @target.contracted_agencies.map(&:name).join(', ')]   
      end
      
      def mdg_goals
        # TODO: Put each goal in it's own column
        ["MDG Goals", @target.mdgs.map(&:name).join(', ')]
      end
      
      def strategy_link
        id = @target.country_strategy_id
        
        if id.blank?
          ["Strategy Link", "Not linked to any strategy"]
        else
          ["Strategy Link", "http://nic.odadata.eu/country_strategy/show/#{id}"]
        end
      end
      
      def total_payments
        ["Total Disbursements to date (#{MultiCurrency.output_currency})", @target.fundings.total_payments]
      end
      
      def total_commitments
        ["Total Commitments to date (#{MultiCurrency.output_currency})", @target.fundings.total_commitments]
      end
      
      # Adds one column per focal region
      def focal_regions
        columns = []
        available_regions = GeoLevel1.find(:all, :order => "name asc")
        
        columns << ["National", @target.geo_level1_ids.empty? ? "National" : ""]
    
        available_regions.each do |loc|
          columns << if @target.geo_level1_ids.include?(loc.id)
            [loc.name, @target.geo_level2s.find_all_by_geo_level1_id(loc.id).map(&:name).join(", ")]
          else
            [loc.name, ""]
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
        ["Internet Link", @target.website]
      end
      
      def funds(year)  
        finances = @target.fundings.find_by_year(year)
        forecasts = @target.funding_forecasts.find_by_year(year)
        columns = []

        if year <= Time.now.year
          columns << ["Commitments #{year} in #{MultiCurrency.output_currency}", finances.andand.commitments]
          columns << ["Disbursements #{year} in #{MultiCurrency.output_currency}", finances.andand.payments]
                    
          columns += (1..4).map do |quarter|
           ["Disbursements Q#{quarter}/#{year} in #{MultiCurrency.output_currency}", finances.andand.send("payments_q#{quarter}")]
          end
        end
        
        if year >= Time.now.year
          columns << ["Commitments Forecast #{year} in #{MultiCurrency.output_currency}", forecasts.andand.commitments]
          columns << ["Disbursements Forecast #{year} in #{MultiCurrency.output_currency}", forecasts.andand.payments]
        end
        
        columns
      end
      
    end # class
  end # module
end # module