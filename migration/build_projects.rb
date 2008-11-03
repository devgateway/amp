#!ruby

RAILS_ENV = 'development'
require File.dirname(__FILE__) + '/../config/environment'

require 'ya2yaml'


donor_mappings_source = YAML::load(File.open('../odanic_fixtures/donors.yml'))
donor_mappings = {}
donor_mappings_source.each do |k, values|
  donor_mappings[values["id"]] = k
end

contr_agencies_mappings_source = YAML::load(File.open('../odanic_fixtures/contracted_agencies.yml'))
contr_agencies_mappings = {}
contr_agencies_mappings_source.each do |k, values|
  contr_agencies_mappings[values["id"]] = k
end

impl_agencies_mappings_source = YAML::load(File.open('../odanic_fixtures/implementing_agencies.yml'))
impl_agencies_mappings = {}
impl_agencies_mappings_source.each do |k, values|
  impl_agencies_mappings[values["id"]] = k
end

donor_agency_mappings_source = YAML::load(File.open('../odanic_fixtures/donor_agencies.yml'))
donor_agency_mappings = {}
donor_agency_mappings_source.each do |k, values|
  donor_agency_mappings[values["id"]] = k
end


markers_source = YAML::load(File.open('../odanic_fixtures/markers.yml'))
contact_source = YAML::load(File.open('../odanic_fixtures/contacts.yml'))
mdg_source = YAML::load(File.open('../odanic_fixtures/projects_targets.yml'))
prj_source = YAML::load(File.open('../odanic_fixtures/projects.yml'))
district_source = YAML::load(File.open('../odanic_fixtures/geo_level2s_projects.yml'))
finances_source = YAML::load(File.open('../odanic_fixtures/finances.yml'))
cofundings_source = YAML::load(File.open('../odanic_fixtures/cofundings.yml'))
impl_agencies_source = YAML::load(File.open('../odanic_fixtures/implementing_agencies_projects.yml'))
contr_agencies_source = YAML::load(File.open('../odanic_fixtures/contracted_agencies_projects.yml'))

prj_source.each do |k, prj|
  puts "Adding project: #{prj['donor_project_number']}"
  
  # Delete deprecated fields
  prj.delete("agency_contact_id")
  prj.delete("agency_name")
  prj.delete("finances_id")
  prj.delete("geodata_id")
  prj.delete("government_counterpart")
  prj.delete("government_counterpart_id")
  prj.delete("mdg_id")
  
  # Replace donor_id with donor tag
  did = prj.delete("donor_id")
  donor_id = donor_mappings[did].hash.abs
  
  # Add DAC sector
  begin
    crs = CrsSector.find(prj["crs_sector_id"]) 
    dac_sector_id = crs.dac_sector_id
  rescue
    dac_sector_id = nil
  end
  
  # Markers
  markers = markers_source.detect { |k, v| v["id"] == prj["markers_id"] }
  prj["gender_policy_marker"] = markers[1]["gender_policy"]
  prj["environment_policy_marker"] = markers[1]["environment_policy"]
  prj["biodiversity_marker"] = markers[1]["biodiversity"]
  prj["climate_change_marker"] = markers[1]["climate_change"]
  prj["desertification_marker"] = markers[1]["desertification"]
  prj.delete("markers_id")
  
  # Officer Responsible
  contact = contact_source.detect { |k, v| v["id"] == prj["officer_responsible_id"] }
  prj["officer_responsible_name"] = contact[1]["name"]
  prj["officer_responsible_phone"] = contact[1]["phone"]
  prj["officer_responsible_email"] = contact[1]["email"]
  prj.delete("officer_responsible_id")
  
  # Renamed "nation_regional" to "national_regional"
  natioregio = prj.delete("nation_regional")
  
  # Add targets
  target_ids = mdg_source.select { |k, v| v["project_id"] == prj["id"] }
  target_ids.map! { |t| t[1]["target_id"] }
  
  # Add districts
  district_ids = district_source.select { |k, v| v["project_id"] == prj["id"] }
  district_ids.map! { |t| "district_#{sprintf("%03d", (t[1]['geo_level2_id'].to_i - 479))}".hash.abs }
  
  # Agencies
  impl_agency_ids = impl_agencies_source.select { |k, v| v["project_id"] == prj["id"] }
  impl_agency_ids.map! { |a| impl_agencies_mappings[a[1]["implementing_agency_id"]].hash.abs }
    
  contr_agency_ids = contr_agencies_source.select { |k, v| v["project_id"] == prj["id"] }
  contr_agency_ids.map! { |a| contr_agencies_mappings[a[1]["contracted_agency_id"]].hash.abs }
          
  donor_agency_id = donor_agency_mappings[prj["donor_agency_id"]].hash.abs
          
  # Create Project
  p = Project.new({
    :donor_id => donor_id,
    :dac_sector_id => dac_sector_id,
    :national_regional => natioregio,
    :target_ids => target_ids,
    :district_ids => district_ids,
    :implementing_agency_ids => impl_agency_ids,
    :contracted_agency_ids => contr_agency_ids,
    :donor_agency_id => donor_agency_id
  }.reverse_merge(prj))
   
  # FUNDING!!
  finance_records = finances_source.select { |k, v| v["project_id"] == prj["id"] }
  finance_records.each do |k, info|
    if (info["commitments_forecast"].to_i > 0 ) || (info["payments_forecast"].to_i > 0)
      # This is a forecast record
      p.funding_forecasts.build({
        :year => info["year"], 
        :commitments => info["commitments_forecast"], 
        :payments => info["payments_forecast"],
        :on_budget => info["on_budget"],
        :on_treasury => info["on_treasury"]
      })
    elsif (info["payments_up_to_end_of"].to_i > 0) || (info["commitments_up_to_end_of"].to_i > 0)
      p.build_historic_funding({
        :commitments => info["commitments_up_to_end_of"],
        :payments => info["payments_up_to_end_of"]
      })
    elsif (info["payments_q1"].to_i > 0) || (info["payments_q2"].to_i > 0) || (info["payments_q3"].to_i > 0) || 
            (info["payments_q4"].to_i > 0) || (info["commitments"].to_i > 0)
      p.fundings.build({
        :year => info["year"],
        :commitments => info["commitments"], 
        :payments_q1 => info["payments_q1"],
        :payments_q2 => info["payments_q2"],
        :payments_q3 => info["payments_q3"],
        :payments_q4 => info["payments_q4"],
        :on_budget => info["on_budget"],
        :on_treasury => info["on_treasury"]
      })      
    end  
  end   
  
  # Co-Funding
  cofundings = cofundings_source.select { |k, v| (v["project_id"] == prj["id"]) && !v["currency"].nil? && !v["donor_id"].nil? }
  cofundings.each do |k, cofunding|
    cdid = cofunding["donor_id"]
    cdonor_id = donor_mappings[cdid].hash.abs
    
    p.cofundings.build({
      :amount => cofunding["amount"],
      :currency => cofunding["currency"],
      :donor_id => cdonor_id
    })
  end
  
  p.save!
end
