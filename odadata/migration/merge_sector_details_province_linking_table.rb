#!ruby

$KCODE = 'UTF8'

require 'rubygems'
require 'ya2yaml'
require 'yaml'

css = YAML::load(File.open('../db/fixtures/provinces_sector_details.yml'))
mappings = {}
sp = {}
css.each do |k, values|
  sid = values.delete("sector_detail_id")
  p = values.delete("province").gsub(/geo_level1s/, 'province')
  
  (sp[sid] ||= []) << p
end

sd = YAML::load(File.open('../db/fixtures/sector_details.yml'))
sd.each do |k, v|
  provs = sp[v["id"]]
  v["provinces"] = provs unless provs.nil?
end

puts sd.ya2yaml
