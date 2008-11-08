class AccessibleFunding < ActiveRecord::Base
  belongs_to  :donor
  belongs_to  :project
  
  # Default scope, returns records in default currency
  named_scope :all, :conditions => { 'currency' => Prefs.default_currency }
  named_scope :in_currency, lambda { |cur| { :conditions => { 'currency' => cur} } }
  
  named_scope :draft, :conditions => { 'data_status' => Project::DRAFT }
  named_scope :published, :conditions => { 'data_status' => Project::PUBLISHED }
  
  currency_columns :payments_q1, :payments_q2, :payments_q3, :payments_q4, :commitments,
    :currency => lambda { |f| f.currency }, 
    :year => lambda { |f| f.year }, :validations => false
end