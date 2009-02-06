#!ruby

RAILS_ENV = 'development'
require File.dirname(__FILE__) + '/../config/environment'

CON = ActiveRecord::Base.connection

Donor.all.each do |d|
  rec_en = CON.execute("SELECT * FROM donor_translations WHERE donor_id = '#{d.id}' AND locale = 'en' LIMIT 1")[0]
  rec_es = CON.execute("SELECT * FROM donor_translations WHERE donor_id = '#{d.id}' AND locale = 'es' LIMIT 1")[0]
  
  d.name = rec_en['name']
  d.name_es = rec_es['name']
  
  d.save(false)
  puts "Donor Saved: #{d.name}"
end

CrsSector.all.each do |d|
  rec_en = CON.execute("SELECT * FROM crs_sector_translations WHERE crs_sector_id = '#{d.id}' AND locale = 'en' LIMIT 1")[0]
  rec_es = CON.execute("SELECT * FROM crs_sector_translations WHERE crs_sector_id = '#{d.id}' AND locale = 'es' LIMIT 1")[0]
  
  d.name = rec_en['name']
  d.name_es = rec_es['name']
  
  d.description = rec_en['description']
  d.description_es = rec_es['description']
  
  d.save(false)
  puts "CrsSector Saved: #{d.name}"
end

DacSector.all.each do |d|
  rec_en = CON.execute("SELECT * FROM dac_sector_translations WHERE dac_sector_id = '#{d.id}' AND locale = 'en' LIMIT 1")[0]
  rec_es = CON.execute("SELECT * FROM dac_sector_translations WHERE dac_sector_id = '#{d.id}' AND locale = 'es' LIMIT 1")[0]
  
  d.name = rec_en['name']
  d.name_es = rec_es['name']
  
  d.description = rec_en['description']
  d.description_es = rec_es['description']
  
  d.save(false)
  puts "CrsSector Saved: #{d.name}"
end

CountryStrategy.all.each do |d|
  rec_en = CON.execute("SELECT * FROM country_strategy_translations WHERE country_strategy_id = '#{d.id}' AND locale = 'en' LIMIT 1")[0]
  rec_es = CON.execute("SELECT * FROM country_strategy_translations WHERE country_strategy_id = '#{d.id}' AND locale = 'es' LIMIT 1")[0]
  
  d.strategy_number = rec_en['strategy_number']
  d.strategy_number_es = rec_es['strategy_number']
  
  d.description = rec_en['description']
  d.description_es = rec_es['description']
  
  d.save(false)
  puts "Strategy Saved: #{d.strategy_number}"
end

Mdg.all.each do |d|
  rec_en = CON.execute("SELECT * FROM mdg_translations WHERE mdg_id = '#{d.id}' AND locale = 'en' LIMIT 1")[0]
  rec_es = CON.execute("SELECT * FROM mdg_translations WHERE mdg_id = '#{d.id}' AND locale = 'es' LIMIT 1")[0]
  
  d.name = rec_en['name']
  d.name_es = rec_es['name']
  
  d.description = rec_en['description']
  d.description_es = rec_es['description']
  
  d.save(false)
  puts "MDG Saved: #{d.name}"
end

Target.all.each do |d|
  rec_en = CON.execute("SELECT * FROM target_translations WHERE target_id = '#{d.id}' AND locale = 'en' LIMIT 1")[0]
  rec_es = CON.execute("SELECT * FROM target_translations WHERE target_id = '#{d.id}' AND locale = 'es' LIMIT 1")[0]
  
  d.name = rec_en['name']
  d.name_es = rec_es['name']
  
  d.save(false)
  puts "Target Saved: #{d.name}"
end

AidModality.all.each do |d|
  rec_en = CON.execute("SELECT * FROM aid_modality_translations WHERE aid_modality_id = '#{d.id}' AND locale = 'en' LIMIT 1")[0]
  rec_es = CON.execute("SELECT * FROM aid_modality_translations WHERE aid_modality_id = '#{d.id}' AND locale = 'es' LIMIT 1")[0]
  
  d.name = rec_en['name']
  d.name_es = rec_es['name']
  
  d.save(false)
  puts "Type of Aid Saved: #{d.name}"
end