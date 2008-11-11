class Bluebook::ChartsController < BluebookController
  
  # Renders chart_name.xml.erb template from settings/ directory
  def settings
    render :action => File.join("settings", params[:id]), :layout => false
  end
  
  # Charts
  def sectors_chart
    # Show large version of sectors chart
    @donor = Donor.find_by_name(params[:id])
   
    render :layout => "report_window"
  end
  
  ##
  # Chart data
    
  def sectors_chart_data
    @donor = Donor.find_by_name(params[:dname])
    @sectors = @donor.total_payments_in_sectors(Time.now.year-1).sort { |e, f| f[1] <=> e[1] }
       
    render :layout => false
  end
  
  def sectors_column_data
    @donors = Donor.main.all(:order => "name ASC")
    @donor_payments = OrderedHash.new
    @donors.each { |d| @donor_payments[d.name] = d.annual_payments[Time.now.year-1].in("EUR") }
    @donor_commitments = OrderedHash.new
    @donors.each { |d| @donor_commitments[d.name] = d.annual_commitments[Time.now.year-1].in("EUR") }
   
    render :layout => false
  end
 
  def forecasts_column_data
    @donors = Donor.main.all(:order => "name ASC")
    @donor_payments_forecast = OrderedHash.new
    @donors.each { |d| @donor_payments_forecast[d.name] = d.annual_payments_forecasts[Time.now.year].in("EUR") }
    @donor_commitments_forecast = OrderedHash.new
    @donors.each { |d| @donor_commitments_forecast[d.name] = d.annual_commitments_forecasts[Time.now.year].in("EUR") }
 
    render :layout => false
  end
 
  def eu_cooperation_trends_column_data
    @donors = Donor.main.all(:order => "name ASC")
    @donor_payments_forecast = OrderedHash.new
    @donors.each { |d| @donor_payments_forecast[d.name] = d.annual_payments_forecasts[Time.now.year].in("EUR") }
    @donor_payments = OrderedHash.new
    @donors.each { |d| @donor_payments[d.name] = d.annual_payments[Time.now.year-1].in("EUR") } 
     
    render :layout => false
  end
 
  def eu_cooperation_type_of_aid_data
    @aid_payments, @aid_forecasts = [], []
    
    Donor.main.each do |d|
      res = Finances.find(:all, 
        :select => "SUM((payments_q1 + payments_q2 + payments_q3 + payments_q4)) AS payments, year, projects.type_of_aid AS type_of_aid",
        :joins => "LEFT OUTER JOIN projects ON finances.project_id = projects.id",
        :conditions => ["finances.year = ? AND projects.data_status = ? AND projects.donor_id = ?", Time.now.year-1, Project::PUBLISHED, d.id],
        :group => "projects.type_of_aid, year")
      
      res.each do |r|
        @aid_payments[r.type_of_aid.to_i] ||= 0.to_currency(Prefs.default_currency)
        @aid_payments[r.type_of_aid.to_i] += r.payments.to_currency(d.currency, r.year)
      end
    end
    
    Donor.main.each do |d|
      res = Finances.find(:all, 
        :select => "SUM(payments_forecast) AS forecasts, year, projects.type_of_aid AS type_of_aid",
        :joins => "LEFT OUTER JOIN projects ON finances.project_id = projects.id",
        :conditions => ["finances.year = ? AND projects.data_status = ? AND projects.donor_id = ?", Time.now.year, Project::PUBLISHED, d.id],
        :group => "projects.type_of_aid, year")
      
      res.each do |r|
        @aid_forecasts[r.type_of_aid.to_i] ||= 0.to_currency(Prefs.default_currency)
        @aid_forecasts[r.type_of_aid.to_i] += r.forecasts.to_currency(d.currency, r.year)
      end
    end
    
    render :layout => false
  end
  
  def eu_cooperation_type_of_aid_percentages_data
    @aid_payments = []
    @donors = Donor.main.all(:order => "name ASC")
    
    @donors.each do |d|
      @aid_payments[d.id] ||= []
      
      res = Finances.find(:all, 
        :select => "SUM((payments_q1 + payments_q2 + payments_q3 + payments_q4)) AS payments, year, projects.type_of_aid AS type_of_aid",
        :joins => "LEFT OUTER JOIN projects ON finances.project_id = projects.id",
        :conditions => ["finances.year = ? AND projects.data_status = ? AND projects.donor_id = ?", Time.now.year-1, Project::PUBLISHED, d.id],
        :group => "projects.type_of_aid, year")
      
      res.each do |r|
        @aid_payments[d.id][r.type_of_aid.to_i] ||= 0.to_currency(Prefs.default_currency)
        @aid_payments[d.id][r.type_of_aid.to_i] += r.payments.to_currency(d.currency, r.year)
      end
    end
    
    render :layout => false
  end
  
  def dac_sectors_column_data
    @donors = Donor.main.all(:order => "name ASC")
    @sector_payments = OrderedHash.new

    @donors.each do |d|
      @sector_payments[d.name] = d.total_payments_in_sectors(Time.now.year-1) 
    end
    
    @totals = @sector_payments.inject(OrderedHash.new) do |totals, d|
      d[1].each do |sector, amount|
        totals[sector] ||= 0.to_currency(Prefs.default_currency)
        totals[sector] += amount
      end
      
      totals
    end
    
    @totals = @totals.sort_by { |k, v| v }
  end
  
  def regions_column_data
    @donors = Donor.main.all(:order => "name ASC")
    @region_payments = OrderedHash.new
    @donors.each do |d|
      @region_payments[d.name] = d.provinces_by_amount(Time.now.year-1) 
      #@region_payments[d.name]["National"] = d.payments_to_national_projects(Time.now.year-1)
    end
    
    @totals = @region_payments.inject(OrderedHash.new) do |totals, d|
      d[1].each do |region, amount|
        totals[region] ||= 0.to_currency(Prefs.default_currency)
        totals[region] += amount
      end
      
      totals
    end
    
    @totals = @totals.sort_by { |k, v| v }    
  end
end
