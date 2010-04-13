class AddRatesSourceAttribute < ActiveRecord::Migration
  def self.up
    add_column :exchange_rates, :source, :string, :null => false, :default => "partners"
    remove_index :exchange_rates, :column => [:currency, :year]
    add_index :exchange_rates, [:source, :currency, :year], :unique => true
  end

  def self.down
    remove_index :exchange_rates, :column => [:source, :currency, :year]
    add_index :exchange_rates, [:currency, :year], :unique => true
    remove_column :exchange_rates, :source
  end
end
