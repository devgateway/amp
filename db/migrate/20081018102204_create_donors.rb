class CreateDonors < ActiveRecord::Migration
  def self.up
    create_table :donors do |t|
      t.string  :code
      
      t.string  :currency
      t.boolean :cofunding_only
      
      t.text    :institutions_responsible_for_oda
      t.integer :total_staff_in_country
      t.integer :total_expatriate_staff
      t.integer :total_local_staff
      t.string  :officer_responsible
      
      # Field Office
      t.text    :field_office_address
      t.string  :field_office_phone
      t.string  :field_office_email
      t.string  :field_office_website
      
      # Head of Mission
      t.string  :head_of_mission_name
      t.string  :head_of_mission_email
      
      # Head of Cooperation
      t.string  :head_of_cooperation_name
      t.string  :head_of_cooperation_email
      
      # First Focal Point
      t.string  :first_focal_point_name
      t.string  :first_focal_point_email
      
      # Second Focal Point
      t.string  :second_focal_point_name
      t.string  :second_focal_point_email
    end
    
    create_table :donor_translations do |t|
      t.string     :locale
      t.string     :name
      
      t.references :donor
    end
    
    create_table :donor_agencies do |t|
      t.string  :name
      t.string  :code
      t.string  :acronym
      
      t.references :donor
    end
  end

  def self.down
    drop_table :donors
    drop_table :donor_translations
    drop_table :donor_agencies
  end
end
