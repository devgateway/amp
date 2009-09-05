class Reports::ProjectAggregator < Ruport::Aggregator
  def find_records(options)
    options[:projects]
  end
  
  provides :factsheet_link do |p|
    %{<a href="/projects/#{p.id}"><img src="/images/details.gif" width="10" height="14" alt="Factsheet" /></a>}
  end
  
  provides :donor do |p|
    p.donor.name
  end
  
  provides :agency do |p|
    p.donor_agency.andand.name
  end
  
  provides :sectors do |p|
    sector_names_with_percentages = p.sector_relevances.ordered_by_relevance.map do |sr|
      "#{sr.sector.name_with_code} (#{sr.amount}%)"
    end
  
    sector_names_with_percentages.join(', ')
  end
  
  provides :grant_loan do |p|
    option_text_by_id(Project::GRANT_LOAN_OPTIONS, p.grant_loan)
  end 
  
  provides :prj_status do |p|
    option_text_by_id(Project::STATUS_OPTIONS, p.prj_status)
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