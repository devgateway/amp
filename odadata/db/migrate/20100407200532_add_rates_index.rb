class AddRatesIndex < ActiveRecord::Migration
  def self.up
    add_index :exchange_rates, [:currency, :year], :unique => true
  end

  def self.down
    remove_index :exchange_rates, :column => [:currency, :year]
  end
end
