module Reports
  class LocationDisaggregator < Ruport::Middleware
    def initialize(disaggregate_by, locations)
      @disaggregate_by = disaggregate_by
      @locations = locations
    end
    
    def process_record(object, preprocessed_record)
      if @disaggregate_by == :province
        geo_relevances = object.geo_relevances.all(:conditions => ['province_id IN (?)', @locations.map(&:id)])
      elsif @disaggregate_by == :district
        geo_relevances = object.geo_relevances.all(:conditions => ['district_id IN (?)', @locations.map(&:id)])
      end
      
      percentage = geo_relevances.sum(&:amount) / 100.0
      [:total_commitments, :total_disbursements, :commitments_forecast, :disbursements_forecast].each do |f|
        preprocessed_record[f] = preprocessed_record[f] * percentage if preprocessed_record[f]
      end
    end
  end
end