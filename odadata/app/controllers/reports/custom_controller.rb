class Reports::CustomController < ReportsController
  RELATIONS = {
    :donors => lambda { |m, v| ["donors.id IN (?) and donors.donor_type = 'country' ", v] },
    :un_agencies => lambda { |m, v| ["donors.id IN (?) and donors.donor_type = 'un_agency' ", v] },
    :targets => lambda { |m, v| ["mdg_relevances.mdg_id IN (?)", v] },
    :sectors => lambda { |m, v| ["sector_relevances.dac_sector_id IN (?)", v] },
    :provinces => lambda { |m, v| 
      conditions = returning([]) do |c|
        c << "geo_relevances.province_id IS NULL" if v.delete("national")
        c << "geo_relevances.province_id IN (?)" unless v.empty?
      end
      
      ary = [conditions.join(" OR ")]
      ary << v unless v.empty?
      
      ary 
    },
    :districts => lambda { |m, v| ["geo_relevances.district_id IN (?)", v] },
    :markers => lambda { |m, v| 
      v.map { |name| "projects.#{name}_marker >= 1"}.join(" OR ") 
    },
    [:prj_status, :grant_loan, :aid_modality_id, :type_of_implementation, :on_off_budget, :on_off_treasury] =>
      lambda { |m, v| ["#{m} IN (?)", v] }}
      
    # FIXME: This makes no sense for a filter, waiting for Vanessas response to ODANIC-82
    # Commented out features to allow project level On Budget and Single Treasury Account (this replaces On/Off treasury) ODAMOZ-66
#    :on_off_budget => lambda { |m, v|
       # both on and off are selected if length is 2
#       ["fundings.on_budget = ?", v] unless v.length == 2
#    },
#    :on_off_treasury => lambda { |m, v|
#       # both on and off are selected if length is 2
#       ["fundings.on_treasury = ?", v] unless v.length == 2
#    }
#  }
  
  def new
    
  end
  
  def create    
    fields = params[:report][:fields]
    format = params[:report][:format].keys.first
    funding_details = extract_detail_years_and_add_placeholder!(fields)
    disaggregators = disaggregators_from_params
    
    projects = Project.published.ordered.all(
      :include => [:mdg_relevances, :donor, :sector_relevances, :geo_relevances, :fundings], 
      :conditions => sql_conditions_from_params)
    # this may be interesting in later refactoring:
    # Project.send(:preload_associations, projects, [:fundings, :funding_forecasts])
    
    data = Reports::ProjectAggregator.new(
      :fields => fields, 
      :funding_details => funding_details, 
      :projects => projects, 
      :middleware => disaggregators_from_params).data

    if format == "html"
      # Remove quarterly information for the HTML report
      funding_details.each do |y|
        data.remove_column("disbursements_q1_#{y}")
        data.remove_column("disbursements_q2_#{y}")
        data.remove_column("disbursements_q3_#{y}")
        data.remove_column("disbursements_q4_#{y}")
      end
    end
      
    report = ComplexReport.create!({
      :data => data,
      :currency => params[:currency],
      :historic_rates_source => (params[:report][:exchange_rates][:historic] rescue nil),
      :current_rates_source => (params[:report][:exchange_rates][:current] rescue nil),
      :forecasts_rates_source => (params[:report][:exchange_rates][:forecasts] rescue nil)
    })
    
    redirect_to reports_custom_path(report, :format => format)
  end
  
  def show
    @report = ComplexReport.find(params[:id])
    @data = @report.data
    # Set exchange rate sources for currency conversion
    MultiCurrency.historic_rates_source = @report.historic_rates_source
    MultiCurrency.current_rates_source = @report.current_rates_source
    MultiCurrency.forecasts_rates_source = @report.forecasts_rates_source
    MultiCurrency.output_currency = @report.currency
    
    respond_to do |wants|
      wants.html { render :layout => 'report_window' }
      wants.xls do
        buffer = Reports::ProjectReport.render_xls(:data => @data)
        send_data buffer, :filename => "custom-report-#{params[:id]}.xls"
      end
    end
  end
  
protected
  def extract_detail_years_and_add_placeholder!(fields)
    fd_regex = /funding_details\[([0-9]+)\]/
    ff = fields.select { |f| f =~ fd_regex }
    if ff.any?
      idx = fields.index(ff.first)
      fields[idx] = 'funding_details'
      fields.delete_if { |f| f =~ fd_regex }
      ff.map { |f| f.sub(fd_regex) { $1 }.to_i }
    end
  end
  
  # Parse required disaggregation middleware
  def disaggregators_from_params
    dis = params[:disaggregators] || []
    returning([]) do |disaggregators|
      if dis.include?("sector")
        sectors = params[:sectors].include?("all") ? DacSector.all : DacSector.find(params[:sectors])
        disaggregators << Reports::SectorDisaggregator.new(sectors)
      end
      
      if dis.include?("location")
        locations = params[:provinces].include?("all") ? Province.all : Province.find(params[:provinces])
        disaggregators << Reports::LocationDisaggregator.new(:province, locations)
      end
    end
  end

  def parsed_params # :nodoc:
    p = []

    RELATIONS.each do |keys, handler|
      [*keys].each do |key| 
        # Get values from checkboxes.. If there is no input for the relation, ignore.
        values = params[key].blank? || params[key].include?("all") ? :ignore : params[key]
        p << [key, values, handler]
      end
    end 

    return p
  end
  
  # Build conditions for SQL query
  def sql_conditions_from_params
    conditions = []
    
    parsed_params.each do |key, values, handler|
      next if values == :ignore
      conditions << handler.call(key, values)       
    end
  
    # Merge arrays to find-compatible conditions array
    Project.merge_conditions(*conditions)
  end
end
