class TotalOda < ActiveRecord::Base
  belongs_to :country_strategy
  
  currency_columns :commitments, :disbursements, 
    :currency => lambda { |t| t.country_strategy.donor.currency },
    :year => lambda { |t| t.year }
end
