class Project < ActiveRecord::Base
  include ActionView::Helpers::TextHelper
  extend AttributeDecorator
  
  ##
  # Constants
  NATIONAL_REGIONAL_OPTIONS = [['national', 1], ['regional', 2], ['regional_with_nicaraguan_component_available', 3]]
  STATUS_OPTIONS            = [['ongoing', 1, true], ['pipeline', 2, true], ['completed', 3]]
  
  MARKER_OPTIONS            = [['not_relevant', 0], ['significant', 1], ['principal_objective', 2]]
  AVAILABLE_MARKERS         = [['gender_policy', 'gender_policy'], ['environment_policy', 'environment_policy'], 
                              ['biodiversity', 'biodiversity'], ['climate_change', 'climate_change'], 
                              ['desertification', 'desertification']]
    
  DRAFT                     = 0
  PUBLISHED                 = 1
  DELETED                   = 2
  DATA_STATUS_OPTIONS       = [['draft', DRAFT], ['published', PUBLISHED], ['deleted', DELETED]]
  
  IMPLEMENTATION_TYPES      = [['bilateral', 1], ['multilateral', 2], ['ngo_implementation', 3]]

  GRANT_LOAN_OPTIONS        = [['grant', 1], ['loan', 2]]
  ON_OFF_BUDGET_OPTIONS     = [['on_budget', true], ['off_budget', false]]
  ON_OFF_TREASURY_OPTIONS   = [['on_treasury', true], ['off_treasury', false]]
  
  FIRST_YEAR_OF_RELEVANCE   = 2007
  FORECAST_RANGE            = 3
  
    
  ##
  # Relations
  belongs_to              :donor
  belongs_to              :donor_agency
  
  belongs_to              :country_strategy
  
  belongs_to              :government_counterpart, :class_name => "GovernmentCounterpartNames", :foreign_key => "government_counterpart_id"
  
  # Agencies
  has_and_belongs_to_many :implementing_agencies
  has_and_belongs_to_many :contracted_agencies
  
  # MDG relevance
  has_many                :mdg_relevances, :dependent => :delete_all
  has_many                :mdgs, :through => :mdg_relevances, :uniq => true
  has_many                :targets, :through => :mdg_relevances  
  
  # Geographic relevance
  has_many                :geo_relevances, :dependent => :delete_all, :attributes => true
  has_many                :provinces, :through => :geo_relevances, :uniq => true
  has_many                :districts, :through => :geo_relevances
  
  # Sector relevance
  has_many                :sector_relevances, :dependent => :delete_all, :attributes => true
  has_many                :dac_sectors, :through => :sector_relevances, :uniq => true
  has_many                :crs_sectors, :through => :sector_relevances
  
  # Funding Information
  belongs_to              :type_of_aid
    
  has_many                :cofundings, :dependent => :delete_all, :attributes => true, :discard_if => :blank?
  has_many                :cofinancing_donors, :through => :cofundings, :source => :donor
  
  has_many                :fundings, :attributes => true, :extend => AggregatedFundings, :dependent => :delete_all
  has_many                :funding_forecasts, :attributes => true, :dependent => :delete_all
  has_one                 :historic_funding, :dependent => :delete
  
  has_many                :accessible_fundings
  has_many                :accessible_forecasts

  ##
  # Decorated attributes
  attribute_decorator :officer_responsible, :class => Address, 
    :decorates => [:officer_responsible_name, :officer_responsible_phone, :officer_responsible_email]
  
  ##
  # Custom finders
  # TODO: This is a hack to order by the translated donor name
  # This should better be done in the globalization plugin directly but joining in the translation
  named_scope :ordered, lambda { {
    :joins => "LEFT OUTER JOIN donor_translations ON (donor_translations.donor_id = projects.donor_id AND donor_translations.locale = '#{I18n.locale.to_s.split('-').first}')", 
    :order => "donor_translations.name ASC, donor_project_number ASC"
  } }
  
  named_scope :draft, :conditions => ['data_status = ?', DRAFT]
  named_scope :published, :conditions => ['data_status = ?', PUBLISHED]
  named_scope :deleted, :conditions => ['data_status = ?', DELETED]
  
  named_scope :grant, :conditions => ['grant_loan = ?', 1]
  named_scope :loan, :conditions => ['grant_loan = ?', 2]
      
  ##
  # Callbacks
  
  
  ##
  # Validation
    
  validates_inclusion_of    :data_status, :in => [Project::DRAFT, Project::PUBLISHED, Project::DELETED], 
                            :message => "has invalid code: {{value}}"
  
  # STATE: general
  validates_presence_of     :donor_project_number, :title, :description, :prj_status
  validates_uniqueness_of   :donor_project_number, :scope => :donor_id
  
  # STATE: categorization
  validates_presence_of     :national_regional, :type_of_implementation, :type_of_aid, :grant_loan, 
                            :officer_responsible_name
                            
  validates_associated      :sector_relevances, :geo_relevances, :mdg_relevances
  validates_associated      :fundings, :funding_forecasts, :historic_funding
  
  validate                  :total_sector_amount_is_100
  
  ##
  # Accessors    
  def historic_funding_attributes=(attribs)
    ActiveSupport::Deprecation.warn("This way of accessing attributes is deprecated, but Rails currently lacks an alternative!")
    if new_record?
      build_historic_funding(attribs)
    else
      historic_funding.update_attributes(attribs)
    end
  end    
      
      
  def finances_by_year
    funding.index_by(&:year)
  end
    
  ##
  # Easily readable query information for reports etc.  
  def mdg_list
    mdgs.join("<br />")
  end 
  
  def fmt_description
    textilize_without_paragraph(description)
  end
  
  ##
  # This returns a list of Provinces and Districts in the following format:
  # {"Province 1" => ["Dist 1.1", "Dist 1.2"], "Province 2" => ["Dist 2.1"] ...}
  def geo_list
    ActiveSupport::Deprecation.warn("Ugly method! Will be removed ASAP! Don't use  it anymore")
    provinces.inject({}) do |list, province|
      list[province.name] = districts.find_all_by_province_id(province.id).map(&:name)
      
      list
    end
  end
  
  def impl_agency_names
    implementing_agencies.map(&:name).join("<br />")
  end
  
  
  ##
  # Funding Aggregates
  def total_commitments(year = nil)
    if year
      fundings.find_by_year(year).commitments rescue 0.to_currency(donor.currency)
    else
      (historic_funding.commitments rescue 0.to_currency(donor.currency)) +
        fundings.total_commitments
    end
  end
  
  def total_payments(year = nil)
    if year
      fundings.find_by_year(year).payments rescue 0.to_currency(donor.currency)
    else
      (historic_funding.payments rescue 0.to_currency(donor.currency)) +
        fundings.total_payments
    end
  end

  # Sum up total Co-Funding for this project and return in project donor's currency
  def total_cofunding
    cofundings.to_a.sum(&:amount).in(donor.currency) rescue 0.to_currency(donor.currency)
  end
  
  ##
  # Validation methods
  # Validate that the total amount per project is 100%
  def total_sector_amount_is_100
    unless self.sector_relevances.sum(:amount) == 100
      #errors.add('sector_relevances', 'foo.bar')
    end  
  end
end