class Bluebook::DonorProfilesController < BluebookController
def show
    @donors = Donor.bluebook
    @donor = Donor.bluebook.find(params[:id])
      
    # Total amount of money spent on all projects of a specific donor
    # Also checks whether funding information for the last year is available at all. If it is not, an error message will be shown.
    #
    # FFerreyra: Changed the way the total payments are calculated, because there was a loss of precision when the exchange rates
    # were higher than expected i.e. with JPY there's a difference of 300 mil
    # Old formula:
    # @total_payments = @donor.annual_payments[year]
    # Maybe there's a better workaround for this

    @currency = params[:currency].nil? ? Prefs.default_currency : params[:currency]

    unless @total_payments = @donor.total_grant_payments(year).in(@currency) + @donor.total_loan_payments(year).in(@currency)
      render :action => 'data_missing'
      return
    end

    
    # The country strategy is selected from all available strategies from the donor
    # that apply to the bluebook, based on the start/end dates
    # This clause wasn't working: OR (start < :startYear AND 'end' > :endYear)
    @country_strategy = @donor.country_strategies.find(:first, :conditions => [
        "applies_to_bluebook = true AND (( 'end' >= :startYear AND 'end' <= :endYear) OR (start >= :startYear AND start <= :endYear) )",
        {:startYear => "#{year}-01-01", :endYear => "#{year}-12-31" }
       ])

    @projects = @donor.projects.published.all(:include => [:fundings, :funding_forecasts, :historic_funding])
    @projects_bilaterals = @projects.select { |p| p.type_of_implementation == 1 }
    @projects_multilaterals = @projects.select { |p| p.type_of_implementation == 2 }
               
    # Total amount of money spent on all projects by all donors in the country
    @eu_total_payments = @donors.inject(0.to_currency(@currency)) do |total, donor|
      total + donor.annual_payments[year]
    end
    
    # Total payments by grant/loan (in donor's currency)
    @total_payments_grants = Hash.new
    (Project::FIRST_YEAR_OF_RELEVANCE..year).to_a.map do |y|
      @total_payments_grants[y] = @donor.total_grant_payments(y).in(@currency)
    end

    @total_payments_loans = Hash.new
    (Project::FIRST_YEAR_OF_RELEVANCE..year).to_a.map do |y|
      @total_payments_loans[y] = @donor.total_loan_payments(y).in(@currency)
    end
   
    @total_forecasts_grants = @donor.total_grant_forecasts(year + 1).in(@currency)
    @total_forecasts_loans = @donor.total_loan_forecasts(year + 1).in(@currency)
       
    # Disbursment percentage of the donors projects relative to the total spent in the country
    @disbursement =  @donor.annual_payments[year].in(@currency).to_f * 100 / @eu_total_payments.to_f
   
    # Specific donor's grant projects disbursements
    @disbursement_grants = @total_payments_grants[year].to_f * 100 / @total_payments.in(@currency).to_f
    #TODO: Check why sometimes @total_payments is zero, I had to add this not NaN validations because there were errors in the page.
    if(@disbursement_grants.nan?)
      @disbursement_grants = 0
    end
    # Specific donor's loan projects disbursements
    @disbursement_loans = 100 - @disbursement_grants
   
    # Total amount of money spent on 'bilateral' projects of a specific donor
    @total_payments_bilaterals = @projects_bilaterals.inject(0.to_currency(@currency)) do |total, prj|
      total + prj.fundings.find_by_year(year).andand.payments.to_currency(@currency)
    end
   
    # Total amount of money spent on 'multilateral' projects of a specific donor
    @total_payments_multilaterals = @projects_multilaterals.inject(0.to_currency(@currency)) do |total, prj|
      total + prj.fundings.find_by_year(year).andand.payments.to_currency(@currency)
    end
   
    # Specific donor's bilaterals projects disbursements (percentage)
    @disbursement_bilaterals = @total_payments_bilaterals.in(@currency).to_f * 100 / @total_payments.in(@currency).to_f

    # Specific donor's multilaterals projects disbursements (percentage)
    @disbursement_multilaterals = @total_payments_multilaterals.in(@currency).to_f * 100 / @total_payments.in(@currency).to_f

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