#!ruby

RAILS_ENV = 'development'
require File.dirname(__FILE__) + '/../config/environment'

require 'ya2yaml'


mappings_source = YAML::load(File.open('../db/fixtures/donor_agencies.yml'))
mappings = {}
mappings_source.each do |k, values|
  mappings[values["id"]] = k
end

prj_source = YAML::load(File.open('../db/fixtures/projects.yml'))

prj_source.each do |k, prj|
  puts "Adding donor agency for project: #{prj['donor_project_number']}"
  
  agency_id = mappings[prj["donor_agency_id"]].hash.abs
  p = Project.find(prj["id"]) rescue next
  p.donor_agency_id = agency_id
  p.save!
end