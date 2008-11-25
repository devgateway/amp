class AccessibleForecast < ActiveRecord::Base
  belongs_to  :donor
  belongs_to  :project
  
  # Default scope, returns records in default currency
  # TODO: Replace by default_scope in Rails 2.3/3.0
  named_scope :all, lambda { { :conditions => { 'currency' => Prefs.default_currency } } }
  named_scope :in_currency, lambda { |cur| { :conditions => { 'currency' => cur} } }
  
  named_scope :draft, :conditions => { 'data_status' => Project::DRAFT }
  named_scope :published, :conditions => { 'data_status' => Project::PUBLISHED }
  
  currency_columns :payments, :commitments, :currency => lambda { |f| f.currency }, 
    :year => lambda { |f| f.year }, :validations => false
end