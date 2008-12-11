class AddCurrencyAttributeToFundingTables < ActiveRecord::Migration
  def self.up
    add_column :fundings,           :currency, :string
    add_column :funding_forecasts,  :currency, :string
    add_column :historic_fundings,  :currency, :string
    
    Funding.all.each do |f|
      f.currency = f.project.donor.currency
      f.save
    end
    
    FundingForecast.all.each do |f|
      f.currency = f.project.donor.currency
      f.save
    end
    
    HistoricFunding.all.each do |f|
      f.currency = f.project.donor.currency
      f.save
    end
  end

  def self.down
    remove_column :historic_fundings, :currency
    remove_column :funding_forecasts, :currency
    remove_column :fundings,          :currency
  end
end
