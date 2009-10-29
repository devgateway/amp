module Reports
  class SectorDisaggregator < Ruport::Middleware
    def initialize(sectors)
      @sectors = sectors
    end
    
    def process_record(object, preprocessed_record)
      sector_relevances = object.sector_relevances.all(:conditions => ['dac_sector_id IN (?)', @sectors.map(&:id)])
      percentage = sector_relevances.sum(&:amount) / 100.0
      
      funding_attributes = preprocessed_record.keys.select { |a| a =~ /(total_commitments|total_disbursements|commitments_forecast|disbursements_forecast)(_[0-9]{4})?/ }
      funding_attributes.each do |a|
        preprocessed_record[a] = preprocessed_record[a] * percentage if preprocessed_record[a]
      end
    end
  end
end