class Bluebook::DonorProfilesController < BluebookController
  def show
    @donors = Donor.main
    @donor = Donor.main.find(params[:id])
      
    # Total amount of money spent on all projects of a specific donor
    # Also checks whether funding information for the last year is available at all. If it is not, an error message will be shown.
    unless @total_payments = @donor.annual_payments[year]
      render :action => 'data_missing'
      return
    end
    
    # TODO: Replace by country_strategies.current (i.e. current named scope) that returns
    # the currently applicable strategy, based on the start and end dates.
    @country_strategy = @donor.country_strategies.first(:order => "id DESC")
   
    @projects = @donor.projects.published.all(:include => [:fundings, :funding_forecasts, :historic_funding])
    @projects_bilaterals = @projects.select { |p| p.type_of_implementation == 1 }
    @projects_multilaterals = @projects.select { |p| p.type_of_implementation == 2 }
               
    # Total amount of money spent on all projects by all donors in the country
    @eu_total_payments = @donors.inject(0.to_currency("EUR")) do |total, donor|
      total + donor.annual_payments[year]
    end
          
    # Total payments by grant/loan
    @total_payments_grants = @donor.total_grant_payments(year)
    @total_payments_loans = @donor.total_loan_payments(year)
   
    @total_forecasts_grants = @donor.total_grant_forecasts(year + 1)
    @total_forecasts_loans = @donor.total_loan_forecasts(year + 1)
       
    # Disbursment percentage of the donors projects relative to the total spent in the country
    @disbursement = @total_payments.in("EUR").to_f * 100 / @eu_total_payments.to_f
   
    # Specific donor's grant projects disbursements
    @disbursement_grants = @total_payments_grants.to_f * 100 / @total_payments.to_f
    #TODO: Check why sometimes @total_payments is zero, I had to add this not NaN validations because there were errors in the page.
    if(@disbursement_grants.nan?)
      @disbursement_grants = 0
    end
    # Specific donor's loan projects disbursements
    @disbursement_loans = 100 - @disbursement_grants
   
    # Total amount of money spent on 'bilateral' projects of a specific donor
    @total_payments_bilaterals = @projects_bilaterals.inject(0.to_currency(@donor.currency)) do |total, prj|
      total + prj.fundings.find_by_year(year).andand.payments.to_currency
    end
   
    # Total amount of money spent on 'multilateral' projects of a specific donor
    @total_payments_multilaterals = @projects_multilaterals.inject(0.to_currency(@donor.currency)) do |total, prj|
      total + prj.fundings.find_by_year(year).andand.payments.to_currency
    end
   
    # Specific donor's bilaterals projects disbursements (percentage)
    @disbursement_bilaterals = @total_payments_bilaterals.to_f * 100 / @total_payments.to_f 

    # Specific donor's multilaterals projects disbursements (percentage)
    @disbursement_multilaterals = @total_payments_multilaterals.to_f * 100 / @total_payments.to_f 

    # Specific donor's NGO Implemented projects disbursements (percentage)
    #TODO: Check why sometimes @total_payments is zero, I had to add this not NaN validations because there were errors in the page.
    if (@disbursement_bilaterals.nan?)
      @disbursement_bilaterals = 0
    end
    if (@disbursement_multilaterals.nan?)
      @disbursement_multilaterals = 0
    end
    @disbursement_ngos = 100 - @disbursement_bilaterals.round - @disbursement_multilaterals.round # Ensure that we get 100% as total ;-)

    # Top three regions
    @top_three = @donor.province_payments.published.all.find(:all,
      :limit => 3, :order => "payments DESC", :conditions => ["year = ?", year])
  end
end