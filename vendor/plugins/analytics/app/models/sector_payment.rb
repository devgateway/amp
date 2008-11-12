class SectorPayment < ActiveRecord::Base
  belongs_to :donor
  belongs_to :dac_sector
  
  # Default scope, returns records in default currency
  named_scope :all, :conditions => { 'currency' => Prefs.default_currency }
  named_scope :in_currency, lambda { |cur| { :conditions => { 'currency' => cur} } }
  
  named_scope :published, :conditions => ['data_status = ?', Project::PUBLISHED]
  
  named_scope :totals, {
    :select => 'currency, year, SUM(payments) AS payments, dac_sector_id', 
    :group => 'dac_sector_id, currency, year',
    :order => 'payments DESC' 
  }
  
  currency_columns :payments, :currency => lambda { |p| p.currency }, :year => lambda { |p| p.year }
end