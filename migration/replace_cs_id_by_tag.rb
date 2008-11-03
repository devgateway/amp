#!ruby

$KCODE = 'UTF8'

require 'rubygems'
require 'ya2yaml'
require 'yaml'

css = YAML::load(File.open('../odanic_fixtures/country_strategies.yml'))
mappings = {}
css.each do |k, values|
  mappings[values["id"]] = k
end

sd = YAML::load(File.open('../db/fixtures/sector_details.yml'))
sd.each do |k, v|
  csid = v.delete("country_strategy_id")
  v["country_strategy"] = mappings[csid]
end

puts sd.ya2yaml
