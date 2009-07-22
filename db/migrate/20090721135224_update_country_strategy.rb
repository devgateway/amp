class UpdateCountryStrategy < ActiveRecord::Migration
  def self.up
    add_column :country_strategies, :applies_to_bluebook, :boolean

    CountryStrategy.find(:all).each do |c|
        c.update_attribute :applies_to_bluebook, true
      end
  end

  def self.down
    remove_column :country_strategies, :applies_to_bluebook
  end
end
