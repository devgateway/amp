class MergeAgencyTables < ActiveRecord::Migration  
  def self.up
    conn = ActiveRecord::Base.connection
    unique = conn.select_all("SELECT name FROM agencies GROUP BY name HAVING COUNT(*) = 1").map(&:values).flatten
    unique_contracted_names = unique.map do |ag|
      result = conn.select_all("SELECT name FROM agencies WHERE name = '#{ag}' AND type = 'ContractedAgency'")
      result.empty? ? nil : result.first["name"]
    end
    
    unique_contracted_names.compact.each do |ag_name|
      conn.insert("INSERT INTO agencies (name, type) VALUES ('#{ag_name}', 'ImplementingAgency')")
      puts "Added missing agency: #{ag_name}"
    end
          
    impl_link = conn.select_all("SELECT * FROM implementing_agencies_projects")
    contr_link = conn.select_all("SELECT * FROM contracted_agencies_projects ORDER BY project_id ASC")
    
    contr_link.each do |cl|
      agency_name = conn.select_all("SELECT name FROM agencies WHERE id = '#{cl["contracted_agency_id"]}'").first["name"]
      puts "processing contracted link for agency name: #{agency_name}, project: #{cl['project_id']}"
      corresponding_impl_id = conn.select_all("SELECT id FROM agencies WHERE name = '#{agency_name}' AND type = 'ImplementingAgency'").first["id"]
      
      conn.update("UPDATE contracted_agencies_projects SET contracted_agency_id = '#{corresponding_impl_id}' WHERE contracted_agency_id = '#{cl["contracted_agency_id"]}' AND project_id = '#{cl["project_id"]}'")
    end
    
    conn.delete("DELETE FROM agencies WHERE type = 'ContractedAgency'")
    remove_column :agencies, :type 
    
    rename_column :implementing_agencies_projects, :implementing_agency_id, :agency_id
    rename_column :contracted_agencies_projects, :contracted_agency_id, :agency_id
  end

  def self.down
    
  end
end
