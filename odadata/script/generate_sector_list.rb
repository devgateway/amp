#!/usr/bin/env ruby

require "csv"

# Load Rails environment
ENV['RAILS_ENV'] = ARGV.first || ENV['RAILS_ENV'] || 'development'
require File.expand_path(File.dirname(__FILE__) + "/../config/environment")

@workbook = Spreadsheet::Excel.new(File.join(RAILS_ROOT, 'public', 'sectors_list.xls'))
@worksheet = @workbook.add_worksheet("ODAnic OECD DAC Sectors")

@formats = {
  :heading => @workbook.add_format(:color => "blue", :bold => 1),
  :totals => @workbook.add_format(:bold => 1)
}

current_row_count = 0

DacSector.ordered.all(:include => :crs_sectors).each do |sector|
  current_row_count += 1
	@worksheet.write_row(current_row_count, 0, sector.name_with_code, @formats[:heading])
	
	sector.crs_sectors.ordered.all.each_with_index do |subsector, idx|
	  @worksheet.write_row(current_row_count, 1, subsector.name_with_code, @formats[:heading])
	  current_row_count += 1
	end
end

@workbook.close
