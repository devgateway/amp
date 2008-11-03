#!/usr/bin/env ruby

require "csv"

# Load Rails environment
ENV['RAILS_ENV'] = ARGV.first || ENV['RAILS_ENV'] || 'development'
require File.expand_path(File.dirname(__FILE__) + "/../config/environment")

include ApplicationHelper

CSV.open('region_report.csv', 'w', ',') do |writer|
  Donor.main.all(:include => [:projects]).each do |donor|
    payments_per_region = donor.provinces_by_amount(2007)
    national_payments = donor.payments_to_national_projects(2007)
    
    # Write headings
    writer << ['Donor', 'Region', 'Amount']
    regions = GeoLevel1.find(:all).map(&:name).sort
    puts donor.name
    
    # Write data   
    payments_per_region.each do |region, payments|
      writer << [donor.name, region, payments.in("EUR").to_s(false)]
    end    
    writer << [donor.name, "National", national_payments.in("EUR").to_s(false)]    
  end
end