class AddAmountToGeoRelevance < ActiveRecord::Migration
  def self.up
    add_column :geo_relevances, :amount, :float
  end

  def self.down
    remove_column :geo_relevances, :amount
  end
end
