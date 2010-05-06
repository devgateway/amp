class DonorInfo::DonorReportController < DonorInfoController
unloadable

  def show
    @currency = params[:currency].nil? ? DEFAULT_CURRENCY : params[:currency]
    @donor = Donor.find(params[:id]) unless params[:id].nil?

    @commitments_by_aid_modality = []
    @payments_by_aid_modality = []
    @payments_on_cut_by_aid_modality = []
    @payments_off_cut_by_aid_modality = []
    @payments_on_budget_by_aid_modality = []
    @payments_off_budget_by_aid_modality = []

    @annual_commitments_on_budget = []
    @annual_payments_on_budget = []
    @annual_commitments_off_budget = []
    @annual_payments_off_budget = []
    @annual_commitments = []
    @annual_payments = []

    if params[:id].nil?

      (Project::FIRST_YEAR_OF_RELEVANCE_DONOR_REPORT..Time.now.year+4).to_a.map do |y|
        commitments_sum = Hash.new
        payments_sum = Hash.new
        AidModality.all.each do |a|
          commitments_sum[a.id] = 0.to_currency(@currency)
          payments_sum[a.id] = 0.to_currency(@currency)
        end

        Donor.send(params[:donor_type]).each do |d|
           AidModality.all.each do |a|
             commitments_sum[a.id] = commitments_sum[a.id] + d.commitments_by_aid_modality(y)[a.id] unless d.commitments_by_aid_modality(y)[a.id].nil?
             payments_sum[a.id] = payments_sum[a.id] + d.payments_by_aid_modality(y)[a.id] unless d.payments_by_aid_modality(y)[a.id].nil?
           end
        end
        @commitments_by_aid_modality[y] = commitments_sum
        @payments_by_aid_modality[y] = payments_sum

      end
      @payments_on_cut_by_aid_modality = payments_on_cut_by_aid_modality_all(params[:donor_type])
      @payments_off_cut_by_aid_modality = payments_off_cut_by_aid_modality_all(params[:donor_type])
      @payments_on_budget_by_aid_modality = payments_on_budget_by_aid_modality_all(params[:donor_type])
      @payments_off_budget_by_aid_modality = payments_off_budget_by_aid_modality_all(params[:donor_type])
      @annual_commitments_on_budget = annual_commitments_on_budget_all(params[:donor_type])
      @annual_payments_on_budget = annual_payments_on_budget_all(params[:donor_type])
      @annual_commitments_off_budget = annual_commitments_off_budget_all(params[:donor_type])
      @annual_payments_off_budget = annual_payments_off_budget_all(params[:donor_type])
      @annual_commitments = annual_commitments_all(params[:donor_type])
      @annual_payments = annual_payments_all(params[:donor_type])

    else
      (Project::FIRST_YEAR_OF_RELEVANCE_DONOR_REPORT..Time.now.year+4).to_a.map do |y|
        @commitments_by_aid_modality[y] = @donor.commitments_by_aid_modality(y)
        @payments_by_aid_modality[y] = @donor.payments_by_aid_modality(y)
      end

      @payments_on_cut_by_aid_modality = @donor.payments_on_cut_by_aid_modality
      @payments_off_cut_by_aid_modality = @donor.payments_off_cut_by_aid_modality
      @payments_on_budget_by_aid_modality = @donor.payments_on_budget_by_aid_modality
      @payments_off_budget_by_aid_modality = @donor.payments_off_budget_by_aid_modality
      @annual_commitments_on_budget = @donor.annual_commitments_on_budget
      @annual_payments_on_budget = @donor.annual_payments_on_budget
      @annual_commitments_off_budget = @donor.annual_commitments_off_budget
      @annual_payments_off_budget = @donor.annual_payments_off_budget
      @annual_commitments = @donor.annual_commitments
      @annual_payments = @donor.annual_payments
    end

    render :layout => 'donor_report_window'
  end
  
  def payments_on_cut_by_aid_modality_all(donor_type)
     @payments_on_cut_by_aid_modality_all = Funding.find(:all,
      :select=>'SUM(fundings.payments_q1 + fundings.payments_q2 + fundings.payments_q3 + fundings.payments_q4) AS pay, projects.aid_modality_id AS aid_modality, fundings.year ',
      :joins => 'JOIN projects ON fundings.project_id = projects.id JOIN donors ON donors.id = projects.donor_id ',
      :conditions => ['projects.data_status = ? AND projects.on_off_treasury = true AND donors.donor_type = ? ', Project::PUBLISHED, donor_type],
      :group => 'aid_modality, year'
    ).inject([]) {|totals, rec| totals[rec.aid_modality.to_i] = rec.pay.to_currency(@currency, rec.year); totals}
  end

  def payments_off_cut_by_aid_modality_all(donor_type)
     @payments_off_cut_by_aid_modality_all = Funding.find(:all,
      :select=>'SUM(fundings.payments_q1 + fundings.payments_q2 + fundings.payments_q3 + fundings.payments_q4) AS pay, projects.aid_modality_id AS aid_modality, fundings.year ',
      :joins => 'JOIN projects ON fundings.project_id = projects.id JOIN donors ON donors.id = projects.donor_id ',
      :conditions => ['projects.data_status = ? AND projects.on_off_treasury = false AND donors.donor_type = ? ', Project::PUBLISHED, donor_type],
      :group => 'aid_modality, year'
    ).inject([]) {|totals, rec| totals[rec.aid_modality.to_i] = rec.pay.to_currency(@currency, rec.year); totals}
  end

  def payments_on_budget_by_aid_modality_all(donor_type)
     @payments_on_budget_by_aid_modality_all = Funding.find(:all,
      :select=>'SUM(fundings.payments_q1 + fundings.payments_q2 + fundings.payments_q3 + fundings.payments_q4) AS pay, projects.aid_modality_id AS aid_modality, fundings.year ',
      :joins => 'JOIN projects ON fundings.project_id = projects.id JOIN donors ON donors.id = projects.donor_id ',
      :conditions => ['projects.data_status = ? AND projects.on_off_budget = true AND donors.donor_type = ? ', Project::PUBLISHED, donor_type],
      :group => 'aid_modality, year'
    ).inject([]) {|totals, rec| totals[rec.aid_modality.to_i] = rec.pay.to_currency(@currency, rec.year); totals}
  end

  def payments_off_budget_by_aid_modality_all(donor_type)
     @payments_off_budget_by_aid_modality_all = Funding.find(:all,
      :select=>'SUM(fundings.payments_q1 + fundings.payments_q2 + fundings.payments_q3 + fundings.payments_q4) AS pay, projects.aid_modality_id AS aid_modality, fundings.year ',
      :joins => 'JOIN projects ON fundings.project_id = projects.id JOIN donors ON donors.id = projects.donor_id ',
      :conditions => ['projects.data_status = ? AND projects.on_off_budget = false AND donors.donor_type = ? ', Project::PUBLISHED, donor_type],
      :group => 'aid_modality, year'
    ).inject([]) {|totals, rec| totals[rec.aid_modality.to_i] = rec.pay.to_currency(@currency, rec.year); totals}
  end

  def annual_commitments_on_budget_all(donor_type)
    @annual_commitments_on_budget_all = Funding.find(:all,
      :select=>'SUM(fundings.commitments) AS total_commitments, fundings.year as year',
      :joins => 'JOIN projects ON fundings.project_id = projects.id JOIN donors ON donors.id = projects.donor_id ',
      :conditions => ['projects.data_status = ? AND projects.on_off_budget = true AND donors.donor_type = ? ', Project::PUBLISHED, donor_type],
      :group => 'fundings.year'
    ).inject({}) {|totals, rec| totals[rec.year] = rec.total_commitments.to_currency(@currency, rec.year); totals}
  end

  def annual_payments_on_budget_all(donor_type)
    @annual_payments_on_budget_all = Funding.find(:all,
      :select=>'SUM(fundings.payments_q1 + fundings.payments_q2 + fundings.payments_q3 + fundings.payments_q4) AS pay, fundings.year as year',
      :joins => 'JOIN projects ON fundings.project_id = projects.id JOIN donors ON donors.id = projects.donor_id ',
      :conditions => ['projects.data_status = ? AND projects.on_off_budget = true AND donors.donor_type = ? ', Project::PUBLISHED, donor_type],
      :group => 'fundings.year'
    ).inject({}) {|totals, rec| totals[rec.year] = rec.pay.to_currency(@currency, rec.year); totals}
  end

  def annual_commitments_off_budget_all(donor_type)
    @annual_commitments_off_budget_all = Funding.find(:all,
      :select=>'SUM(fundings.commitments) AS total_commitments, fundings.year as year',
      :joins => 'JOIN projects ON fundings.project_id = projects.id JOIN donors ON donors.id = projects.donor_id ',
      :conditions => ['projects.data_status = ? AND projects.on_off_budget = false AND donors.donor_type = ? ', Project::PUBLISHED, donor_type],
      :group => 'fundings.year'
    ).inject({}) {|totals, rec| totals[rec.year] = rec.total_commitments.to_currency(@currency, rec.year); totals}
  end

  def annual_commitments_on_budget_all(donor_type)
    @annual_commitments_on_budget_all = Funding.find(:all,
      :select=>'SUM(fundings.commitments) AS total_commitments, fundings.year as year',
      :joins => 'JOIN projects ON fundings.project_id = projects.id JOIN donors ON donors.id = projects.donor_id ',
      :conditions => ['projects.data_status = ? AND projects.on_off_budget = true AND donors.donor_type = ? ', Project::PUBLISHED, donor_type],
      :group => 'fundings.year'
    ).inject({}) {|totals, rec| totals[rec.year] = rec.total_commitments.to_currency(@currency, rec.year); totals}
  end

  def annual_payments_off_budget_all(donor_type)
    @annual_commitments_off_budget_all = Funding.find(:all,
      :select=>'SUM(fundings.payments_q1 + fundings.payments_q2 + fundings.payments_q3 + fundings.payments_q4) AS pay, fundings.year as year',
      :joins => 'JOIN projects ON fundings.project_id = projects.id JOIN donors ON donors.id = projects.donor_id ',
      :conditions => ['projects.data_status = ? AND projects.on_off_budget = false AND donors.donor_type = ? ', Project::PUBLISHED, donor_type],
      :group => 'fundings.year'
    ).inject({}) {|totals, rec| totals[rec.year] = rec.pay.to_currency(@currency, rec.year); totals}
  end

  def annual_commitments_all(donor_type)
    @annual_commitments_all = Funding.find(:all,
      :select=>'SUM(fundings.commitments) AS total_commitments, fundings.year as year',
      :joins => 'JOIN projects ON fundings.project_id = projects.id JOIN donors ON donors.id = projects.donor_id ',
      :conditions => ['projects.data_status = ? AND donors.donor_type = ? ', Project::PUBLISHED, donor_type],
      :group => 'fundings.year'
    ).inject({}) {|totals, rec| totals[rec.year] = rec.total_commitments.to_currency(@currency, rec.year); totals}
  end

  def annual_payments_all(donor_type)
    @annual_payments_all = Funding.find(:all,
      :select=>'SUM(fundings.payments_q1 + fundings.payments_q2 + fundings.payments_q3 + fundings.payments_q4) AS pay, fundings.year as year',
      :joins => 'JOIN projects ON fundings.project_id = projects.id JOIN donors ON donors.id = projects.donor_id ',
      :conditions => ['projects.data_status = ? AND donors.donor_type = ? ', Project::PUBLISHED, donor_type],
      :group => 'fundings.year'
    ).inject({}) {|totals, rec| totals[rec.year] = rec.pay.to_currency(@currency, rec.year); totals}
  end


#  @annual_commitments = annual_commitments_all
#  @annual_payments = annual_payments_all

end