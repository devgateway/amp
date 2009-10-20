class Reports::CustomController < ReportsController
  RELATIONS = {
    :donors => lambda { |m, v| ["donors.id IN (?)", v] },
    :targets => lambda { |m, v| ["mdg_relevances.mdg_id IN (?)", v] },
    :sectors => lambda { |m, v| ["sector_relevances.dac_sector_id IN (?)", v] },
    :provinces => lambda { |m, v| ["geo_relevances.province_id IN (?)", v] },
    :markers => lambda { |m, v| 
      v.map { |name| "projects.#{name}_marker >= 1"}.join(" OR ") 
    },
    [:prj_status, :grant_loan, :aid_modality_id, :type_of_implementation] => 
      lambda { |m, v| ["#{m} IN (?)", v] },
      
    # FIXME: This makes no sense for a filter, waiting for Vanessas response to ODANIC-82
    :on_off_budget => lambda { |m, v|
       # both on and off are selected if length is 2
       ["fundings.on_budget = ?", v] unless v.length == 2
    },
    :on_off_treasury => lambda { |m, v|
       # both on and off are selected if length is 2
       ["fundings.on_treasury = ?", v] unless v.length == 2
    }
  }
  
  def new
    
  end
  
  def create
    fields = params[:report][:fields]
    funding_details = extract_detail_years_and_add_placeholder!(fields)
    disaggregators = disaggregators_from_params
    
    projects = Project.published.ordered.all(
      :include => [:mdg_relevances, :donor, :sector_relevances, :geo_relevances, :fundings], 
      :conditions => sql_conditions_from_params)
    
    data = Reports::ProjectAggregator.new(
      :fields => fields, 
      :funding_details => funding_details, 
      :projects => projects, 
      :middleware => disaggregators_from_params).data
    
    report = ComplexReport.create!(:data => data)
    redirect_to reports_custom_path(report, :format => params[:format], :currency => "EUR")
  end
  
  def show
    @data = ComplexReport.find(params[:id]).data
    
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
    disaggregators = []
    disaggregators << Reports::SectorDisaggregator if params[:disaggregation][:sector]
    disaggregators << Reports::LocationDisaggregator if params[:disaggregation][:location]
    
    disaggregators
  end
  
  # Parse checkbox results
  def parse_checkboxes(res)
    if res[:all].to_i == 1
      :ignore
    else
      # Delete unchecked fields
      res.delete_if {|k,v| v.to_i != 1}

      # Return array of checked fields, if none was set we ignore these options
      res.keys.empty? ? :ignore : res.keys
    end
  end  

  def parsed_params # :nodoc:
    p = []

    RELATIONS.each do |keys, handler|
      keys.to_a.each do |key| 
        # Get values from checkboxes.. If there is no input for the relation, ignore.
        values = 
        if params[key].blank? 
          RAILS_DEFAULT_LOGGER.debug("Report Builder: No input for relation: '#{key}'")
          :ignore 
        else
          parse_checkboxes(params[key])
        end
        
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