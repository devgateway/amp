class AccessibleFunding < ActiveRecord::Base
  belongs_to  :donor
  belongs_to  :project
  
  # Default scope, returns records in default currency
  # This needs to be a lambda because we can't initialize the rails application
  # without a valid preferences table otherwise.
  named_scope :all, lambda { { :conditions => { 'currency' => Prefs.default_currency } } }
  named_scope :in_currency, lambda { |cur| { :conditions => { 'currency' => cur} } }
  
  named_scope :draft, :conditions => { 'data_status' => Project::DRAFT }
  named_scope :published, :conditions => { 'data_status' => Project::PUBLISHED }
  
  currency_columns :payments_q1, :payments_q2, :payments_q3, :payments_q4, :commitments,
    :currency => lambda { |f| f.currency }, 
    :year => lambda { |f| f.year }, :validations => false
end