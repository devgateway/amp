#!ruby

$KCODE = 'UTF8'

require 'rubygems'
require 'ya2yaml'
require 'yaml'

donors = YAML::load(File.open('../odanic_fixtures/donors.yml'))
mappings = {}
donors.each do |k, values|
  mappings[values["id"]] = k
end

cs = YAML::load(File.open('../db/fixtures/country_strategies.yml'))
cs.each do |k, v|
  v.delete("id")
  v.delete("focal_provinces")
  did = v.delete("donor_id")
  v["donor"] = mappings[did]
end

puts cs.ya2yaml