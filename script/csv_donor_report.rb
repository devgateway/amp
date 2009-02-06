#!/usr/bin/env ruby

require "csv"

# Load Rails environment
ENV['RAILS_ENV'] = ARGV.first || ENV['RAILS_ENV'] || 'development'
require File.expand_path(File.dirname(__FILE__) + "/../config/environment")

include ApplicationHelper

CSV.open('report.csv', 'w', ',') do |writer|
  # Get possible locations
  locations = GeoLevel1.find(:all, :order => "name asc")
  # Write headings
  writer << ['Donor', 'Project No.', 'Sector', 'Detailed sector', 'Status', 'Type of Implementation', 'Type of Aid', 'Funding Type', 
    'Commitments 2007', 'Payments 2007', 'Commitments 2008', 'Payments 2008', 
    'Commitments 2007 EUR', 'Payments 2007 EUR ', 'Commitments 2008 EUR', 'Payments 2008 EUR',
    'Commitments Forecast 2008', 'Payments Forecast 2008', 'Commitments Forecast 2008 EUR', 'Payments Forecast 2008 EUR', 
    'Total Cofinancing in EUR', 'National'] + locations.map(&:name)
    
  Donor.main.all(:include => [:projects]).each do |donor|
  print "\n#{donor.name}"

    # Write projects
    donor.projects.find_all_published.each do |project|
      print "."
      $stdout.flush
      row = []
      
      # Donor
      row << project.donor.name
      # Project No.
      row << project.donor_project_number
      # Sector
      row << safe_access(project.dac_sector, :name_with_code, "n/a")
      row << safe_access(project.crs_sector, :name_with_code, "n/a")
      # Status
      row << option_text_by_id(Project::STATUS_OPTIONS, project.prj_status)
      # Implementation Type
      row << option_text_by_id(Project::IMPLEMENTATION_TYPES, project.type_of_implementation)
      # Type of Aid
      row << option_text_by_id(Project::aid_modality_OPTIONS, project.aid_modality)
      # Funding Type
      row << option_text_by_id(Project::GRANT_LOAN_OPTIONS, project.grant_loan)
      
      # Funding Information
      [2007, 2008].each do |year|
        # Commitments
        row << project.finances.total_commitments(year).to_s(false) rescue "n/a"
        # Payments
        row << project.finances.total_payments(year).to_s(false) rescue "n/a"
      end
      
      # Funding information in EUR
      [2007, 2008].each do |year|
        # Commitments in EUR
        row << project.finances.total_commitments(year).in("EUR").to_s(false) rescue "n/a"
        # Payments in EUR
        row << project.finances.total_payments(year).in("EUR").to_s(false) rescue "n/a"
      end
      
      # Forecasts
      row << project.finances.find_by_year(2008).commitments_forecast.to_s(false) rescue "n/a"
      row << project.finances.find_by_year(2008).payments_forecast.to_s(false) rescue "n/a"
      # Forecasts in EUR
      row << project.finances.find_by_year(2008).commitments_forecast.in("EUR").to_s(false) rescue "n/a"
      row << project.finances.find_by_year(2008).payments_forecast.in("EUR").to_s(false) rescue "n/a"
      
      # Cofundings
      row << project.cofundings.inject(0.to_currency("EUR")) { |c, t| c + t.amount }.to_s(false) rescue row << "0"
      
      # Locations
      if project.geo_level1_ids.empty?
        row << "x"
      else
        row << ""
        locations.each do |loc|
          row << if project.geo_level1_ids.include?(loc.id)
            "x"
          else
            ""
          end
        end
      end
      
      # Write row
      writer << row
    end
  end
end