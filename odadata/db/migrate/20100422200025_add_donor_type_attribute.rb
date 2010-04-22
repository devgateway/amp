class AddDonorTypeAttribute < ActiveRecord::Migration
  def self.up
    add_column :donors, :donor_type, :string, :null => false, :default => "country"
  end

  def self.down
    remove_column :donors, :donor_type
  end
end
