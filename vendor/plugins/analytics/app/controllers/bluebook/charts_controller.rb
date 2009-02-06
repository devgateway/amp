class Bluebook::ChartsController < BluebookController
  
  # Renders chart_name.xml.erb template from settings/ directory
  def settings
    render :action => "settings/#{params[:id]}", :layout => false
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
    @sector_payments = @donor.sector_payments.all.published.find_all_by_year(year)
    
    render :layout => false
  end
  
  def sectors_column_data
    @donors = Donor.main.ordered
    @donor_payments = OrderedHash.new
    @donors.each { |d| @donor_payments[d.name] = d.annual_payments[year].andand.in("EUR") }
    @donor_commitments = OrderedHash.new
    @donors.each { |d| @donor_commitments[d.name] = d.annual_commitments[year].andand.in("EUR") }

    render :layout => false
  end
 
  def forecasts_column_data
    @donors = Donor.main.ordered
    @donor_payments_forecast = OrderedHash.new
    @donors.each { |d| @donor_payments_forecast[d.name] = d.annual_payments_forecasts[year + 1].andand.in("EUR") }
    @donor_commitments_forecast = OrderedHash.new
    @donors.each { |d| @donor_commitments_forecast[d.name] = d.annual_commitments_forecasts[year + 1].andand.in("EUR") }
 
    render :layout => false
  end
 
  def eu_cooperation_trends_column_data
    @donors = Donor.main.ordered
    @donor_payments_forecast = OrderedHash.new
    @donors.each { |d| @donor_payments_forecast[d.name] = d.annual_payments_forecasts[year + 1].andand.in("EUR") }
    @donor_payments = OrderedHash.new
    @donors.each { |d| @donor_payments[d.name] = d.annual_payments[year].andand.in("EUR") }
     
    render :layout => false
  end
 
 
  # TODO: Whoaaa, this definitely needs refactoring.
  # Best way would probably be to create another table for the type of aid options and handle it like
  # the sectors. Using a view for aggregates.
  def eu_cooperation_aid_modality_data
    @aid_payments, @aid_forecasts = [], []
    
    Donor.main.each do |d|
      res = Funding.find(:all, 
        :select => "SUM((payments_q1 + payments_q2 + payments_q3 + payments_q4)) AS total_payments, year, projects.aid_modality_id AS aid_modality",
        :joins => "LEFT OUTER JOIN projects ON fundings.project_id = projects.id",
        :conditions => ["fundings.year = ? AND projects.data_status = ? AND projects.donor_id = ?", year, Project::PUBLISHED, d.id],
        :group => "projects.aid_modality_id, year")
      
      res.each do |r|
        @aid_payments[r.aid_modality.to_i] ||= 0.to_currency(Prefs.default_currency)
        @aid_payments[r.aid_modality.to_i] += r.total_payments.to_currency(d.currency, r.year)
      end
    end
    
    Donor.main.each do |d|
      res = FundingForecast.find(:all, 
        :select => "SUM(payments) AS forecasts, year, projects.aid_modality_id AS aid_modality",
        :joins => "LEFT OUTER JOIN projects ON funding_forecasts.project_id = projects.id",
        :conditions => ["funding_forecasts.year = ? AND projects.data_status = ? AND projects.donor_id = ?", year+1, Project::PUBLISHED, d.id],
        :group => "projects.aid_modality_id, year")
      
      res.each do |r|
        @aid_forecasts[r.aid_modality.to_i] ||= 0.to_currency(Prefs.default_currency)
        @aid_forecasts[r.aid_modality.to_i] += r.forecasts.to_currency(d.currency, r.year)
      end
    end
    
    render :layout => false
  end
  
  def eu_cooperation_aid_modality_percentages_data
    @aid_payments = []
    @donors = Donor.main.ordered
    
    @donors.each do |d|
      @aid_payments[d.id] ||= []
      
      res = Funding.find(:all, 
        :select => "SUM((payments_q1 + payments_q2 + payments_q3 + payments_q4)) AS payments, year, currency, projects.aid_modality_id AS aid_modality",
        :joins => "LEFT OUTER JOIN projects ON fundings.project_id = projects.id",
        :conditions => ["fundings.year = ? AND projects.data_status = ? AND projects.donor_id = ?", year, Project::PUBLISHED, d.id],
        :group => "projects.aid_modality_id, year, currency")
      
      res.each do |r|
        @aid_payments[d.id][r.aid_modality.to_i] ||= 0.to_currency(Prefs.default_currency)
        @aid_payments[d.id][r.aid_modality.to_i] += r.payments.to_currency(d.currency, r.year)
      end
    end
    
    render :layout => false
  end
  
  def dac_sectors_column_data
    @sector_payments = SectorPayment.published.all.find_all_by_year(year)
    @sector_totals = SectorPayment.published.totals.all.find_all_by_year(year)
    
    render :layout => false
  end
  
  def regions_column_data
    @province_payments = ProvincePayment.published.all.find_all_by_year(year)
    @province_totals = ProvincePayment.published.totals.all.find_all_by_year(year)
    
    render :layout => false
  end
end
