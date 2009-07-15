class UpdateDonors < ActiveRecord::Migration
  def self.up
    create_table :donor_details do |t|
      t.integer   :total_staff_in_country
      t.integer   :total_expatriate_staff
      t.integer   :total_local_staff
      t.integer   :year
      t.references :donor
    end
  end

  def self.down
    drop_table :donor_details
  end
end
