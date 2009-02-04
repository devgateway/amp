class Donor < ActiveRecord::Base
  has_many :province_payments
  has_many :sector_payments
  
  # Total commitments of donor
  def total_commitments
    annual_commitments.values.sum
  end
  
  # Fetches the donor's commitments per year from the database
  # They are returned as Currency objects in the donor's currency and 
  # can be accessed as an array (e.g. Donor.annual_commitments[2007])
  # Forecasts are *not* included!
  def annual_commitments
    # Thanks to new solutions, this can be written as:
    # accessible_fundings.in_currency("SEK").find(:all, :select => 'SUM(commitments) AS commitments, year', :group => 'year')
    # Use lazy loading to minimize database queries
    @annual_commitments ||= Funding.find(:all, 
      :select=>'SUM(fundings.commitments) AS total_commitments, fundings.year as year',
      :joins => 'JOIN projects ON fundings.project_id = projects.id',
      :conditions => ['projects.donor_id = ? AND projects.data_status = ?', self.id, Project::PUBLISHED],
      :group => 'fundings.year'
    ).inject({}) {|totals, rec| totals[rec.year] = rec.total_commitments.to_currency(currency, rec.year); totals} 
  end
  
  # Fetches the donor's commitment forecasts per year from the database
  # They are returned as Currency objects in the donor's currency and 
  # can be accessed as an array (e.g. Donor.annual_commitments_forecasts[2011])
  def annual_commitments_forecasts
    # Use lazy loading to minimize database queries
    @annual_commitments_forecasts ||= FundingForecast.find(:all, 
      :select=>'SUM(funding_forecasts.commitments) AS forecast, funding_forecasts.year as year',
      :joins => 'JOIN projects ON funding_forecasts.project_id = projects.id',
      :conditions => ['projects.donor_id = ? AND projects.data_status = ?', self.id, Project::PUBLISHED],
      :group => 'funding_forecasts.year'
    ).inject({}) {|totals, rec| totals[rec.year] = rec.forecast.to_currency(currency, rec.year); totals} 
  end
  
  
  
  # Total payments of donor
  def total_payments
    annual_payments.values.sum
  end
  
  # Fetches the donor's payments per year from the database
  # They are returned as Currency objects in the donor's currency and 
  # can be accessed as an array (e.g. Donor.annual_payments[2007])
  # Forecasts are *not* included!
  def annual_payments
    # Use lazy loading to minimize database queries
    @annual_payments ||= Funding.find(:all, 
      :select=>'SUM(fundings.payments_q1 + fundings.payments_q2 + fundings.payments_q3 + fundings.payments_q4) AS pay, fundings.year as year',
      :joins => 'JOIN projects ON fundings.project_id = projects.id',
      :conditions => ['projects.donor_id = ? AND projects.data_status = ?', self.id, Project::PUBLISHED],
      :group => 'fundings.year'
    ).inject({}) {|totals, rec| totals[rec.year] = rec.pay.to_currency(currency, rec.year); totals} 
  end
  
  # Fetches the donor's payment forecasts per year from the database
  # They are returned as Currency objects in the donor's currency and 
  # can be accessed as an array (e.g. Donor.annual_payments_forecasts[2011])
  def annual_payments_forecasts
    # Use lazy loading to minimize database queries
    @annual_payments_forecasts ||= FundingForecast.find(:all, :select=>'SUM(funding_forecasts.payments) AS forecast, funding_forecasts.year as year',
      :joins => 'JOIN projects ON funding_forecasts.project_id = projects.id',
      :conditions => ['projects.donor_id = ? AND projects.data_status = ?', self.id, Project::PUBLISHED],
      :group => 'funding_forecasts.year'
    ).inject({}) {|totals, rec| totals[rec.year] = rec.forecast.to_currency(currency, rec.year); totals} 
  end
  
  def payments_by_type_of_aid(year = Time.now.year)
    # FIXME: This is a very ugly solution to the problem and should be replaced asap.
    # Use lazy loading to minimize database queries
    @annual_payments_by_toa ||= Funding.find(:all, 
      :select=>'SUM(fundings.payments_q1 + fundings.payments_q2 + fundings.payments_q3 + fundings.payments_q4) AS pay, fundings.year as year, projects.type_of_aid_id AS type_of_aid',
      :joins => 'JOIN projects ON fundings.project_id = projects.id',
      :conditions => ['projects.donor_id = ? AND projects.data_status = ? AND fundings.year = ?', self.id, Project::PUBLISHED, year],
      :group => 'type_of_aid, year'
    ).inject([]) {|totals, rec| totals[rec.type_of_aid.to_i] = rec.pay.to_currency(currency, rec.year); totals} 
  end
  
  def payments_forecasts_by_type_of_aid(year = Time.now.year)
    # Use lazy loading to minimize database queries
    @annual_payments_forecasts_by_toa ||= Funding.find(:all, :select=>'SUM(fundings.payments_forecast) AS payments, fundings.year as year, projects.type_of_aid AS type_of_aid',
      :joins => 'JOIN projects ON fundings.project_id = projects.id',
      :conditions => ['projects.donor_id = ? AND projects.data_status = ? AND fundings.year = ?', self.id, Project::PUBLISHED, year],
      :group => 'projects.type_of_aid, fundings.year'
    ).inject([]) {|totals, rec| totals[rec.type_of_aid.to_i] = rec.payments.to_currency(currency, rec.year); totals} 
  end
  
  # Returns total loan payments for a given year (or all if no argument given)
  def total_loan_payments(year = nil)
    # TODO: Speed up!
    query_projects = projects.published.loan
    query_projects.inject(0.to_currency(currency)) { |sum, p| sum + p.fundings.find_by_year(year).andand.payments }
  end 
  
  # Returns total loan forecasts for a given year (or all if no argument given)
  def total_loan_forecasts(year = nil)
    query_projects = projects.published.loan
    query_projects.inject(0.to_currency(currency)) { |sum, p| sum + p.funding_forecasts.find_by_year(year).andand.payments }
  end 
  
  # Returns total grant payments for a given year (or all if no argument given)
  def total_grant_payments(year = nil)
    query_projects = projects.published.grant
    query_projects.inject(0.to_currency(currency)) { |sum, p| sum + p.fundings.find_by_year(year).andand.payments }
  end
  
  # Returns total grant forecasts for a given year (or all if no argument given)
  def total_grant_forecasts(year = nil)
    query_projects = projects.published.grant
    query_projects.inject(0.to_currency(currency)) { |sum, p| sum + p.funding_forecasts.find_by_year(year).andand.payments }
  end
    
  # Returns total payments to national projects (no provinces/regions chosen)
  def payments_to_national_projects(year = nil)
    conditions = if year
      ["fundings.year = ? AND projects.data_status = ? AND projects.donor_id = ? AND provinces.name IS NULL", year, Project::PUBLISHED, self.id]
    else
      ["projects.data_status = ? AND projects.donor_id = ? AND provinces.name IS NULL", Project::PUBLISHED, self.id]
    end
  
    res = Funding.find(:first, 
      :select => "SUM(payments_q1 + payments_q2 + payments_q3 + payments_q4) AS pay",
      :joins => "LEFT OUTER JOIN projects ON fundings.project_id = projects.id LEFT OUTER JOIN geo_relevances ON geo_relevances.project_id = projects.id LEFT OUTER JOIN provinces ON geo_relevances.province_id = provinces.id",
      :conditions => conditions)
  
    res.pay.to_currency(self.currency)
  end
end