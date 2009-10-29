module Reports
  class LocationDisaggregator < Ruport::Middleware
    def initialize(disaggregate_by, locations)
      @disaggregate_by = disaggregate_by
      @locations = locations
    end
    
    def process_record(object, preprocessed_record)
      case @disaggregate_by
      when :province
        geo_relevances = object.geo_relevances.all(:conditions => ['province_id IN (?)', @locations.map(&:id)])
      when :district
        geo_relevances = object.geo_relevances.all(:conditions => ['district_id IN (?)', @locations.map(&:id)])
      end
      
      percentage = geo_relevances.sum(&:amount) / 100.0
      funding_attributes = preprocessed_record.keys.select { |a| a =~ /(total_commitments|total_disbursements|commitments_forecast|disbursements_forecast)(_[0-9]{4})?/ }
      funding_attributes.each do |a|
        preprocessed_record[a] = preprocessed_record[a] * percentage if preprocessed_record[a]
      end
    end
  end
end