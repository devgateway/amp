class BluebookController < ApplicationController
	# All amounts in bluebooks are in EUR
  before_filter { |c| MultiCurrency.output_currency = "EUR" unless c.params[:currency] }

  def sectors_chart_data
    @donor = Donor.find_by_name(params[:dname])
    @sectors = @donor.total_payments_in_sectors(Time.now.year-1).sort { |e, f| f[1] <=> e[1] }
       
    render :layout => false
  end
 
  def sectors_chart
  # Show large version of sectors chart
    @donor = Donor.find_by_name(params[:id])
   
    render :layout => "report_window"
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
  
  def eu_cooperation_dac_sector_column_chart
    render :layout => "graph_window"
  end
 
  def eu_cooperation_trend
    render :layout => "graph_window"
  end

  def eu_cooperation_forecasts
    render :layout => "graph_window"
  end

  def eu_cooperation_achievements
    render :layout => "graph_window"
  end
  
  def eu_cooperation_type_of_aid
    render :layout => "graph_window"
  end
  
  def eu_cooperation_region_column_chart
    render :layout => "graph_window"
  end
  

  def donor_profile
    @donors = Donor.main
    
    # Find by either id or name
    @donor = (params[:id] =~ /\d+/) ? 
      Donor.main.find(params[:id]) : Donor.main.find_by_name(params[:id])
      
    @eu_total_payments = 0.to_currency(@donor.currency)
    # TODO: Is this working correctly with multiple strategies per donor?!
    @country_strategy = CountryStrategy.find_by_donor_id(@donor.id, :order => "id DESC")
   
    @projects = @donor.projects.published.all(:include => [:fundings, :funding_forecasts, :historic_funding])
    @projects_bilaterals = @projects.select { |p| p.type_of_implementation == 1 }
    @projects_multilaterals = @projects.select { |p| p.type_of_implementation == 2 }
               
    # Total amount of money spent on all projects by all donors in the country
    @eu_total_payments = @donors.inject(0) do |total, donor|
      (donor.annual_payments[Time.now.year-1] || 0.to_currency("EUR")).in("EUR") + total
    end
   
    # Total amount of money spent on all projects of a specific donor
    @total_payments = @donor.annual_payments[Time.now.year-1]
       
    # Total payments by grant/loan
    @total_payments_grants = @donor.total_grant_payments(Time.now.year-1)
    @total_payments_loans = @donor.total_loan_payments(Time.now.year-1)
   
    @total_forecasts_grants = @donor.total_grant_forecasts(Time.now.year)
    @total_forecasts_loans = @donor.total_loan_forecasts(Time.now.year)
       
    # Disbursment percentage of the donors projects relative to the total spent in the country
    @disbursement = @total_payments.in("EUR").to_f * 100 / @eu_total_payments.to_f
   
    # Specific donor's grant projects disbursements
    @disbursement_grants = @total_payments_grants.to_f * 100 / @total_payments.to_f
    # Specific donor's loan projects disbursements
    @disbursement_loans = 100 - @disbursement_grants
   
    # Total amount of money spent on 'bilateral' projects of a specific donor
    @total_payments_bilaterals = @projects_bilaterals.inject(0.to_currency(@donor.currency)) do |total, prj|
      total + prj.fundings.find_by_year(Time.now.year-1).andand.payments.to_currency
    end
   
    # Total amount of money spent on 'multilateral' projects of a specific donor
    @total_payments_multilaterals = @projects_multilaterals.inject(0.to_currency(@donor.currency)) do |total, prj|
      total + prj.fundings.find_by_year(Time.now.year-1).andand.payments.to_currency
    end
   
    # Specific donor's bilaterals projects disbursements (percentage)
    @disbursement_bilaterals = @total_payments_bilaterals.to_f * 100 / @total_payments.to_f 

    # Specific donor's multilaterals projects disbursements (percentage)
    @disbursement_multilaterals = @total_payments_multilaterals.to_f * 100 / @total_payments.to_f 

    # Specific donor's NGO Implemented projects disbursements (percentage)
    @disbursement_ngos = 100 - @disbursement_bilaterals.round - @disbursement_multilaterals.round # Ensure that we get 100% as total ;-)

    # Top three regions
    @top_three = @donor.provinces_by_amount(Time.now.year-1).sort { |a, b| b[1] <=> a[1] }.map { |r, a| "-&nbsp;#{r}" }[0..2]
   
    @pid = case @donor.non_localized_name 
      when "EUROPEAN COMMISSION"
        "EC"
      when "UNITED KINGDOM"
        "UK"
      else
        @donor.non_localized_name
    end
  end
 
end