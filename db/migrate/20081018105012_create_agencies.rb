class CreateAgencies < ActiveRecord::Migration
  def self.up
    create_table :agencies do |t|
      t.string :name
      t.string :type
      
      t.string :contact_name
      t.string :contact_phone
      t.string :contact_email
    end

    create_table :implementing_agencies_projects, :id => false do |t|
      t.references :project, :implementing_agency
    end
    
    create_table :contracted_agencies_projects, :id => false do |t|
      t.references :project, :contracted_agency
    end
  end

  def self.down
    drop_table :agencies
    drop_table :implementing_agencies_projects
    drop_table :contracted_agencies_projects
  end
end
