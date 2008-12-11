class HistoricFunding < ActiveRecord::Base
  belongs_to :project
  before_create :set_currency
  
  # Formatted output for all currency fields
  currency_columns :payments, :commitments,
    :currency => lambda { |f| f.currency }, :year => Project::FIRST_YEAR_OF_RELEVANCE-1, 
    :validations => false
    
protected
  def set_currency
    self.currency ||= self.project.donor.currency
  end
end
