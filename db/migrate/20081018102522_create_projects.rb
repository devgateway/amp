class CreateProjects < ActiveRecord::Migration
  def self.up
    create_table :projects do |t|
      t.text    :title
      t.text    :description
      t.string  :donor_project_number
      t.integer :oecd_number
      t.string  :recipient_country_budget_nr
                
      t.integer :recipient_code
      t.integer :region_code
      t.string  :income_code
                
      t.date    :start
      t.date    :end          
                
      t.text    :comments
      t.string  :website
      t.integer :grant_loan
      t.integer :national_regional
      t.integer :type_of_implementation
      t.string  :government_counterpart
      
      t.integer :prj_status
      t.integer :data_status,             :default => 0
      t.string  :input_state
      
      # Relations
      t.references :donor, :donor_agency
      t.references :type_of_aid
      t.references :country_strategy
      t.references :government_counterpart
      
      # Markers
      t.integer :gender_policy_marker
      t.integer :environment_policy_marker
      t.integer :biodiversity_marker
      t.integer :climate_change_marker
      t.integer :desertification_marker
      
      # Officer responsible
      t.string  :officer_responsible_name
      t.string  :officer_responsible_phone
      t.string  :officer_responsible_email
      
      t.timestamps
    end
  end

  def self.down
    drop_table :projects
  end
end
