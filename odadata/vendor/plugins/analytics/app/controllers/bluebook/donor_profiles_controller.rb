class Bluebook::DonorProfilesController < BluebookController
  def show
    @donors = Donor.main
    
    # Find by either id or name
    @donor = (params[:id] =~ /\d+/) ? 
      Donor.main.find(params[:id]) : Donor.main.find_by_name(params[:id])
  
    # TODO: Replace by country_strategies.current (i.e. current named scope) that returns
    # the currently applicable strategy, based on the start and end dates.
    @country_strategy = @donor.country_strategies.first(:order => "id DESC")
   
    @projects = @donor.projects.published.all(:include => [:fundings, :funding_forecasts, :historic_funding])
    @projects_bilaterals = @projects.select { |p| p.type_of_implementation == 1 }
    @projects_multilaterals = @projects.select { |p| p.type_of_implementation == 2 }
               
    # Total amount of money spent on all projects by all donors in the country
    @eu_total_payments = @donors.inject(0.to_currency("EUR")) do |total, donor|
      total + donor.annual_payments[Time.now.year-1]
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
    @top_three = @donor.province_payments.published.all.find(:all,
      :limit => 3, :order => "payments DESC", :conditions => ["year = ?", Time.now.year - 1])
  end
end