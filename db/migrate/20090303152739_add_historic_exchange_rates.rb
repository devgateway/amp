class AddHistoricExchangeRates < ActiveRecord::Migration
  def self.up
    ExchangeRate.find_all_by_year(2007).each do |r|
      ExchangeRate.create({
        :year => 2006,
        :currency => r.currency,
        :euro_rate => r.euro_rate
      })
    end
  end

  def self.down
  end
end
