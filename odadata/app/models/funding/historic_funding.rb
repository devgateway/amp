class HistoricFunding < ActiveRecord::Base
  belongs_to :project
  
  # Formatted output for all currency fields
  currency_columns :payments, :commitments,
    :currency => lambda { |f| f.project.donor.currency }, :year => Project::FIRST_YEAR_OF_RELEVANCE-1, 
    :validations => false
end
