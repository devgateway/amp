require 'fastercsv'

module Report
  class CSV < Base
    # ====================================================
    # = Override abstract output methods from base class =
    # ====================================================
    def output_head(csv)
      csv << headings
    end
    
    def output_body(csv)
      rows.each { |row| csv << row }
    end
    
    def output
      file = "/reports/odanic_report_#{Time.now.year}_#{Time.now.month}_#{Time.now.day}_#{rand(100000)}.csv"
      FasterCSV.open(File.join(RAILS_ROOT, 'public', file), "w") do |csv|
        output_head(csv)
        output_body(csv)
      end
      
      file
    end
  end
end