class ExchangeRate < ActiveRecord::Base
  # Validation
  validates_presence_of :currency, :euro_rate
  validates_numericality_of :euro_rate
  validates_uniqueness_of :year, :scope => "currency"
  
  class << self
    def find_rate(from, to, year = Time.now.year)
      source = find_by_currency_and_year(from, year).euro_rate
      target = find_by_currency_and_year(to, year).euro_rate

      target / source
    end
    
    def available_currencies
      all.map(&:currency).uniq.sort
    end
  
    def available_currencies_for_select
      available_currencies.collect { |c| [c, c] }
    end
  end
end
