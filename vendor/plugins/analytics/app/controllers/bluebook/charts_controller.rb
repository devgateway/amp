class Bluebook::ChartsController < BluebookController
  
  # Renders chart_name.xml.erb template from settings/ directory
  def settings
    render :action => File.join("settings", params[:id]), :layout => false
  end
  
  # Charts
  def sectors_chart
    # Show large version of sectors chart
    @donor = Donor.find(params[:id])
   
    render :layout => "report_window"
  end
  
  ##
  # Chart data
    
  def sectors_chart_data
    @donor = Donor.find(params[:id])
    @sector_payments = @donor.sector_payments.all.published.find_all_by_year(Time.now.year-1)
    
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
 
 
  # TODO: Whoaaa, this definitely needs refactoring.
  # Best way would probably be to create another table for the type of aid options and handle it like
  # the sectors. Using a view for aggregates.
  def eu_cooperation_type_of_aid_data
    @aid_payments, @aid_forecasts = [], []
    
    Donor.main.each do |d|
      res = Funding.find(:all, 
        :select => "SUM((payments_q1 + payments_q2 + payments_q3 + payments_q4)) AS total_payments, year, projects.type_of_aid AS type_of_aid",
        :joins => "LEFT OUTER JOIN projects ON fundings.project_id = projects.id",
        :conditions => ["fundings.year = ? AND projects.data_status = ? AND projects.donor_id = ?", Time.now.year-1, Project::PUBLISHED, d.id],
        :group => "projects.type_of_aid, year")
      
      res.each do |r|
        @aid_payments[r.type_of_aid.to_i] ||= 0.to_currency(Prefs.default_currency)
        @aid_payments[r.type_of_aid.to_i] += r.total_payments.to_currency(d.currency, r.year)
      end
    end
    
    Donor.main.each do |d|
      res = FundingForecast.find(:all, 
        :select => "SUM(payments) AS forecasts, year, projects.type_of_aid AS type_of_aid",
        :joins => "LEFT OUTER JOIN projects ON funding_forecasts.project_id = projects.id",
        :conditions => ["funding_forecasts.year = ? AND projects.data_status = ? AND projects.donor_id = ?", Time.now.year, Project::PUBLISHED, d.id],
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
    @sector_payments = SectorPayment.published.all.find_all_by_year(Time.now.year-1)
    @sector_totals = SectorPayment.published.totals.all.find_all_by_year(Time.now.year-1)
    
    render :layout => false
  end
  
  def regions_column_data
    @province_payments = ProvincePayment.published.all.find_all_by_year(Time.now.year-1)
    @province_totals = ProvincePayment.published.totals.all.find_all_by_year(Time.now.year-1)
    
    render :layout => false
  end
end
