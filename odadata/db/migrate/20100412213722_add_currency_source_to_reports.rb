class AddCurrencySourceToReports < ActiveRecord::Migration
  def self.up
    add_column :complex_reports, :currency, :string
    add_column :complex_reports, :historic_rates_source, :string
    add_column :complex_reports, :current_rates_source, :string
    add_column :complex_reports, :forecasts_rates_source, :string
  end

  def self.down
    remove_column :complex_reports, :forecasts_rates_source
    remove_column :complex_reports, :current_rates_source
    remove_column :complex_reports, :historic_rates_source
    remove_column :complex_reports, :currency
  end
end
