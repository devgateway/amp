#!ruby

$KCODE = 'UTF8'

require 'rubygems'
require 'ya2yaml'
require 'yaml'

css = YAML::load(File.open('../odanic_fixtures/geo_level1s.yml'))
mappings = {}
css.each do |k, values|
  mappings[values["id"]] = k
end

sd = YAML::load(File.open('../db/fixtures/provinces_sector_details.yml'))
sd.each do |k, v|
  csid = v.delete("geo_level1_id")
  v["province"] = mappings[csid]
end

puts sd.ya2yaml
