class FundingForecast < ActiveRecord::Base
  belongs_to :project
  before_create :set_currency
  
  class << self
    def annual_payments
      res = {}
      find(:all,
        :select => 'payments AS com, donors.currency AS cur, year',
        :joins => 'JOIN projects ON project_id = projects.id JOIN donors ON projects.donor_id = donors.id',
        :conditions => ['projects.data_status = ?', Project::PUBLISHED]
      ).group_by(&:year).map do |k,v| 
        res[k] = v.inject(0) { |sum, c| c.com.to_currency(c.cur, c.year).in(Prefs.default_currency) + sum }
      end
      
      res
    end
    
    def annual_commitments
      res = {}
      find(:all,
        :select => 'commitments AS com, donors.currency AS cur, year',
        :joins => 'JOIN projects ON project_id = projects.id JOIN donors ON projects.donor_id = donors.id',
        :conditions => ['projects.data_status = ?', Project::PUBLISHED]
      ).group_by(&:year).map do |k,v| 
        res[k] = v.inject(0) { |sum, c| c.com.to_currency(c.cur, c.year).in(Prefs.default_currency) + sum }
      end
      
      res
    end
  end
  
  # Formatted output for all currency fields
  currency_columns :payments, :commitments,
    :currency => lambda { |f| f.currency }, 
    :year => lambda { |f| f.year }, :validations => false
    
protected
  def set_currency
    self.currency ||= self.project.donor.currency
  end
end