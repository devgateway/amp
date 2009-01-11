class CreateDonors < ActiveRecord::Migration
  def self.up
    create_table :donors do |t|
      t.string    :name
      t.string    :name_es
      t.string    :code

      t.string    :currency
      t.boolean   :cofunding_only
                  
      t.text      :institutions_responsible_for_oda
      t.integer   :total_staff_in_country
      t.integer   :total_expatriate_staff
      t.integer   :total_local_staff
      
      # Field Office
      t.text      :field_office_address
      t.string    :field_office_phone
      t.string    :field_office_email
      t.string    :field_office_website
      
      # Head of Mission
      t.string    :head_of_mission_name
      t.string    :head_of_mission_email
      
      # Head of Cooperation
      t.string    :head_of_cooperation_name
      t.string    :head_of_cooperation_email
      
      # First Focal Point
      t.string    :first_focal_point_name
      t.string    :first_focal_point_email
      
      # Second Focal Point
      t.string    :second_focal_point_name
      t.string    :second_focal_point_email
      
      # Flag Image
      t.string    :flag_file_name
      t.string    :flag_content_type
      t.integer   :flag_file_size
      t.datetime  :flag_updated_at
      
      # Profile Picture
      t.string    :profile_picture_file_name
      t.string    :profile_picture_content_type
      t.integer   :profile_picture_file_size
      t.datetime  :profile_picture_updated_at
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
    drop_table :donor_agencies
  end
end
