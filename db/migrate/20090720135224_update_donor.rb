class UpdateDonor < ActiveRecord::Migration
  def self.up
    add_column :donors, :bluebook_donor, :boolean
    Donor.all.each do |d|
      d.update_attribute :bluebook_donor, true
    end
  end

  def self.down
    remove_column :donors, :bluebook_donor
  end
end
