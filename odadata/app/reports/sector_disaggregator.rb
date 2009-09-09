module Reports
  class SectorDisaggregator < Ruport::Middleware
    def initialize(sectors)
      @sectors = sectors
    end
    
    def process_record(object, preprocessed_record)
      sector_relevances = object.sector_relevances.all(:conditions => ['dac_sector_id IN (?)', @sectors.map(&:id)])
      percentage = sector_relevances.sum(&:amount) / 100.0
      
      [:total_commitments, :total_disbursements, :commitments_forecast, :disbursements_forecast].each do |f|
        preprocessed_record[f] = preprocessed_record[f] * percentage if preprocessed_record[f]
      end
    end
  end
end