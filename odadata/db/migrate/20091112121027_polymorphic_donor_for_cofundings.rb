class PolymorphicDonorForCofundings < ActiveRecord::Migration
  def self.up
    add_column :cofundings, :donor_type, :string, :null => false, :default => "Donor"
  end

  def self.down
    remove_column :cofundings, :donor_type
  end
end
