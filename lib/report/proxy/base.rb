module Report
  module Proxy
    class Base
      include ActionView::Helpers::TagHelper
      include ActionView::Helpers::UrlHelper
      include ApplicationHelper
      include OptionsHelper
      include ReportsHelper
      include I18nHelper
      
      def initialize(target)
        @target = target
      end
      
      def build_record(fields)
        record = fields.map { |f| self.send f }
        record.delete_if { |r| r.blank? }
        Report::Data::Record.new(record)
      end
      
      def method_missing(method, *args)
        # Are funding details requested? Match for methods with a year at the end (e.g. "total_payments_2007").
        # If match was positive, call appropriate method with year as first argument (total_payments(2007))
        if method.to_s =~ /^(.*)_(\d{4})$/
          receiver = self.respond_to?($1) ? self : @target
          receiver.send($1, $2.to_i)
        else  
          if @target.respond_to?(method) 
            [heading_from_query_options(method), @target.send(method, *args)]
          else
            raise Report::QueryError, "Invalid Request, don't know how to handle: #{method}"
          end
        end
      end
      
      def donor
        [ll(:terms, :donor), @target.donor.name]
      end
      
      def agency
        [ll(:reports, :donor_agency), @target.donor_agency.andand.name]
      end
            
      def total_payments(year = nil)
        ["#{ll(:reports, :disbursements_td)} #{year}", @target.total_payments(year).andand.in(Prefs.default_currency)]
      end
      
      def total_commitments(year = nil)
        ["#{ll(:reports, :commitments_td)} #{year}", @target.total_commitments(year).andand.in(Prefs.default_currency)]
      end
      
      def payments_forecast(year)
        ["Payments Forecast #{year}", 
          (@target.funding_forecasts.find_by_year(year).andand.payments.andand.in(Prefs.default_currency))]
      end
      
      def commitments_forecast(year)
        ["Commitments Forecast #{year}", 
          (@target.funding_forecasts.find_by_year(year).andand.commitments.andand.in(Prefs.default_currency))]
      end
      
      def dac_sector
        [ll(:terms, :dac_sectors),  @target.dac_sectors.andand.map(&:name_with_code).join(', ')]
      end
      
      def crs_sector
        [ll(:terms, :dac_crs_sector), @target.crs_sectors.andand.map(&:name_with_code).join(', ')]
      end
      
      def oecd_number
        [ll(:reports, :oecd_id), @target.oecd_number]
      end
      
      def grant_loan
        [ll(:reports, :grant_loan), option_text_by_id(Project::GRANT_LOAN_OPTIONS, @target.grant_loan)]
      end
      
      def prj_status
        [ll(:reports, :project_status), option_text_by_id(Project::STATUS_OPTIONS, @target.prj_status)]
      end
      
      def last_update
        [ll(:reports, :last_update), @target.updated_at]
      end
      
      # Comma separated list of co-funding donors
      def cofunding_donors
        ["Co-Funding Donor(s)", @target.cofundings.map(&:donor).map(&:name).join(', ')]
      end
      
      def national_regional
        [ll(:reports, :national_regional), option_text_by_id(Project::NATIONAL_REGIONAL_OPTIONS, @target.national_regional)]
      end
      
      def type_of_implementation
        [ll(:reports, :toi), option_text_by_id(Project::IMPLEMENTATION_TYPES, @target.type_of_implementation)]
      end
      
      def type_of_aid
        [ll(:reports, :toa), @target.type_of_aid.andand.name]
      end
      
      def gov_counterpart
        [ll(:reports, :gov_counterpart), @target.government_counterpart.andand.name]
      end
      
      def officer_responsible
        [ll(:reports, :officer_responsible), @target.officer_responsible_name]
      end
      
      def focal_regions
        [ll(:reports, :focal_regions), @target.provinces.empty? ? "National" :  @target.provinces.map(&:name).sort.join(", ")]
      end
            
      
    protected
      def heading_from_query_options(method)
        Report.query_options.find { |c| c[0] == method.to_s }.at(1)
      end    
    end
  end
end