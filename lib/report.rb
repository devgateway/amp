# This module contains classes, structures and configuration
# used to build ODAdata's "Custom Reports"
module Report
  class << self
    include I18nHelper  
  end
  
  # Exception for general problems with a query
  class QueryError < StandardError; end
  
  # Specifies the way to get the data from the database
  # for Reports::Base#build_sql_conditions
  # TODO: Documentation
  RELATIONS = {
    :donors => lambda { |m, v| ["donors.id IN (?)", v] },
    :targets => lambda { |m, v| ["mdg_relevances.mdg_id IN (?)", v] },
    :sectors => lambda { |m, v| ["sector_relevances.dac_sector_id IN (?)", v] },
    :provinces => lambda { |m, v| ["geo_relevances.province_id IN (?)", v] },
    :markers => lambda { |m, v| 
      v.map { |name| "projects.#{name}_marker >= 1"}.join(" OR ") 
    },
    [:prj_status, :grant_loan, :aid_modality_id, :type_of_implementation] => 
      lambda { |m, v| ["#{m} IN (?)", v] },
    [:on_off_budget] =>
      lambda { |m, v| ["fundings.on_budget IN (?)", v] }
  }
  
  # Options for custom reports (fields available) => [field, caption, checked?]
  # TODO: Use translate_options
  def self.query_options    
    [
      ['donor', ll(:terms, :donor), true],
      ['agency', ll(:reports, :donor_agency), true],
      ['donor_project_number', ll(:reports, :project_number), true],
      ['oecd_number', ll(:reports, :oecd_id)],
      ['recipient_country_budget_nr', ll(:reports, :budget_number)],
      ['title', ll(:reports, :project_title), true],
      ['description', ll(:reports, :project_desc)],
      ['strategy_link', ll(:reports, :cs_link)],
      ['prj_status', ll(:reports, :project_status)],
      ['start', ll(:reports, :start), true],
      ['end', ll(:reports, :end), true],
      ['total_commitments', ll(:reports, :commitments_td), true],
      ['total_payments', ll(:reports, :disbursements_td), true]
    ] +
      (Project::FIRST_YEAR_OF_RELEVANCE..Time.now.year+3).to_a.map { |y| ["funds_#{y}", "#{ll(:reports, :funds_for)} #{y}"] } +
    [
      ['total_cofunding', ll(:reports, :total_cofunding)],
      ['cofunding_donors', ll(:reports, :cofunding_donors)],
      ['national_regional', ll(:reports, :national_regional)],
      ['type_of_implementation', ll(:reports, :toi)],
      ['sectors', ll(:terms, :dac_sector)],
      ['aid_modality', ll(:reports, :toa)],
      ['grant_loan', "#{ll(:options, :grant)}/#{ll(:options, :loan)}"],
      ['impl_agencies', ll(:reports, :impl_agencies)],
      ['contr_agencies', ll(:reports, :contr_agencies)],
      ['markers', ll(:terms, :markers)],
      ['mdg_goals', ll(:reports, :mdg_goals)],
      ['focal_regions', ll(:reports, :focal_regions)],
      ['officer_responsible', ll(:reports, :officer_responsible)],
      ['website', ll(:terms, :internet_link)],
      ['comments', ll(:reports, :comments)],
      ['updated_at', ll(:reports, :last_update)]
    ]

  #['gov_counterpart', ll(:reports, :gov_counterpart)], removed by 
  end
end