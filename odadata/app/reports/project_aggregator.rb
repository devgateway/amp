class Reports::ProjectAggregator < Ruport::Aggregator
  def find_records(options)
    options[:projects]
  end
  
  def prepare
    @options[:funding_details].each do |y|
      @fields << :"total_commitments_#{y}"
      @fields << :"total_disbursements_#{y}"
      @fields << :"commitments_forecast_#{y}"
      @fields << :"disbursements_forecast_#{y}"
    end
  end
  
  def process_record(project, record)
    # Add funding details
    @options[:funding_details].each do |y|
      record[:"total_commitments_#{y}"] = project.total_commitments(y)
      record[:"total_disbursements_#{y}"] = project.total_payments(y)
      record[:"commitments_forecast_#{y}"] = project.funding_forecasts.find_by_year(y).andand.commitments
      record[:"disbursements_forecast_#{y}"] = project.funding_forecasts.find_by_year(y).andand.payments
    end
  end
  
  provides :factsheet_link do |p|
    p.id
  end
  
  provides :donor do |p|
    p.donor.name
  end
  
  provides :agency do |p|
    p.donor_agency.andand.name
  end
  
  provides :sectors do |p|
    p.sector_relevances.ordered_by_relevance
  end
  
  provides :markers do |p|
    Project::AVAILABLE_MARKERS.inject({}) do |list, marker|
      column_name = "#{marker[0]}_marker"
      list[marker[0]] = p.send(column_name)
      list
    end
  end
  
  provides :focal_regions do |p|
    p.provinces
  end
  
  provides :total_commitments do |p|
    p.total_commitments(Time.now.year - 1)
  end
  
  provides :total_disbursements do |p|
    p.total_payments(Time.now.year - 1)
  end
  
  provides :commitments_forecast do |p|
    p.funding_forecasts.find_by_year(Time.now.year).andand.commitments
  end
  
  provides :disbursements_forecast do |p|
    p.funding_forecasts.find_by_year(Time.now.year).andand.payments
  end
  
  provides :start_date do |p|
    p.start
  end
  
  provides :end_date do |p|
    p.end
  end
  
  provides :cofunding_donors do |p|
    p.cofundings.map(&:donor).map(&:name).join(', ')
  end
  
  provides :aid_modality do |p|
    p.aid_modality.andand.name
  end
end