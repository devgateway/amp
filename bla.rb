#!/usr/bin/env ruby
require(File.join(File.dirname(__FILE__), 'config', 'environment'))

Donor.all.each do |donor|
  ActiveRecord::Base.connection.execute("INSERT INTO donor_translations (donor_id, locale, name) VALUES ('#{donor.id}', 'en', '#{donor.read_attribute('name').andand.gsub(/\\/, '\&\&').andand.gsub(/'/, "''")}')")
  ActiveRecord::Base.connection.execute("INSERT INTO donor_translations (donor_id, locale, name) VALUES ('#{donor.id}', 'es', '#{donor.read_attribute('name_es').andand.gsub(/\\/, '\&\&').andand.gsub(/'/, "''")}')")
end

CrsSector.all.each do |donor|
  ActiveRecord::Base.connection.execute("INSERT INTO crs_sector_translations (crs_sector_id, locale, name, description) VALUES ('#{donor.id}', 'en', '#{donor.read_attribute('name').andand.gsub(/\\/, '\&\&').andand.gsub(/'/, "''")}', '#{donor.read_attribute('description').andand.gsub(/\\/, '\&\&').andand.gsub(/'/, "''")}')")
  ActiveRecord::Base.connection.execute("INSERT INTO crs_sector_translations (crs_sector_id, locale, name, description) VALUES ('#{donor.id}', 'es', '#{donor.read_attribute('name_es').andand.gsub(/\\/, '\&\&').andand.gsub(/'/, "''")}', '#{donor.read_attribute('description_es').andand.gsub(/\\/, '\&\&').andand.gsub(/'/, "''")}')")
end

DacSector.all.each do |donor|
  ActiveRecord::Base.connection.execute("INSERT INTO dac_sector_translations (dac_sector_id, locale, name, description) VALUES ('#{donor.id}', 'en', '#{donor.read_attribute('name').andand.gsub(/\\/, '\&\&').andand.gsub(/'/, "''")}', '#{donor.read_attribute('description').andand.gsub(/\\/, '\&\&').andand.gsub(/'/, "''")}')")
  ActiveRecord::Base.connection.execute("INSERT INTO dac_sector_translations (dac_sector_id, locale, name, description) VALUES ('#{donor.id}', 'es', '#{donor.read_attribute('name_es').andand.gsub(/\\/, '\&\&').andand.gsub(/'/, "''")}', '#{donor.read_attribute('description_es').andand.gsub(/\\/, '\&\&').andand.gsub(/'/, "''")}')")
end

Mdg.all.each do |donor|
  ActiveRecord::Base.connection.execute("INSERT INTO mdg_translations (mdg_id, locale, name, description) VALUES ('#{donor.id}', 'en', '#{donor.read_attribute('name').andand.gsub(/\\/, '\&\&').andand.gsub(/'/, "''")}', '#{donor.read_attribute('description').andand.gsub(/\\/, '\&\&').andand.gsub(/'/, "''")}')")
  ActiveRecord::Base.connection.execute("INSERT INTO mdg_translations (mdg_id, locale, name, description) VALUES ('#{donor.id}', 'es', '#{donor.read_attribute('name_es').andand.gsub(/\\/, '\&\&').andand.gsub(/'/, "''")}', '#{donor.read_attribute('description_es').andand.gsub(/\\/, '\&\&').andand.gsub(/'/, "''")}')")
end

Target.all.each do |donor|
  ActiveRecord::Base.connection.execute("INSERT INTO target_translations (target_id, locale, name) VALUES ('#{donor.id}', 'en', '#{donor.read_attribute('name').andand.gsub(/\\/, '\&\&').andand.gsub(/'/, "''")}')")
  ActiveRecord::Base.connection.execute("INSERT INTO target_translations (target_id, locale, name) VALUES ('#{donor.id}', 'es', '#{donor.read_attribute('name_es').andand.gsub(/\\/, '\&\&').andand.gsub(/'/, "''")}')")
end

CountryStrategy.all.each do |donor|
  ActiveRecord::Base.connection.execute("INSERT INTO country_strategy_translations (country_strategy_id, locale, strategy_number, description) VALUES ('#{donor.id}', 'en', '#{donor.read_attribute('strategy_number').andand.gsub(/\\/, '\&\&').andand.gsub(/'/, "''")}', '#{donor.read_attribute('description').andand.gsub(/\\/, '\&\&').andand.gsub(/'/, "''")}')")
  ActiveRecord::Base.connection.execute("INSERT INTO country_strategy_translations (country_strategy_id, locale, strategy_number, description) VALUES ('#{donor.id}', 'es', '#{donor.read_attribute('strategy_number_es').andand.gsub(/\\/, '\&\&').andand.gsub(/'/, "''")}', '#{donor.read_attribute('description_es').andand.gsub(/\\/, '\&\&').andand.gsub(/'/, "''")}')")
end