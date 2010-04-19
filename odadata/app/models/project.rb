class Project < ActiveRecord::Base
  acts_as_reportable
  
  include ActionView::Helpers::TextHelper
  extend AttributeDecorator
  
  ##
  # Constants
  NATIONAL_REGIONAL_OPTIONS = [['national', 1], ['regional', 2], ['regional_with_nicaraguan_component_available', 3]]
  STATUS_OPTIONS            = [['planned', 2, true], ['signed', 4, true], ['ongoing', 1, true], ['completed', 3]]
  
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


  ON_OFF_CUT                = [['on_cut', true], ['off_cut', false]]




  ENTRY_TYPES               = [['in_kind_cash', 1], ['in_kind_non_cash', 2], ['in_cash_under_donor', 3], ['in_cash_under_beneficiary', 4]]
  LOAN_TO_PUBLIC_ENTERPRISES = [['yes', true], ['no', false]]

  
  FIRST_YEAR_OF_RELEVANCE   = 2007
  FORECAST_RANGE            = 3
  
    
  ##
  # Relations
  belongs_to              :donor
  belongs_to              :donor_agency
  belongs_to              :government_counterpart
  
  belongs_to              :country_strategy
    
  # Agencies (using habtm here is not ideal, but using a rich linking model with a role argument does not work well with Rails)
  has_and_belongs_to_many :implementing_agencies, :class_name => "Agency", :join_table => "implementing_agencies_projects"
  has_and_belongs_to_many :contracted_agencies, :class_name => "Agency", :join_table => "contracted_agencies_projects"
  
  # MDG relevance
  has_many                :mdg_relevances, :dependent => :delete_all
  has_many                :mdgs, :through => :mdg_relevances, :uniq => true
  has_many                :targets, :through => :mdg_relevances  
  
  # Geographic relevance
  has_many                :geo_relevances, :dependent => :delete_all
  has_many                :provinces, :through => :geo_relevances, :uniq => true
  has_many                :districts, :through => :geo_relevances
  
  # Sector relevance
  has_many                :sector_relevances, :dependent => :delete_all
  has_many                :dac_sectors, :through => :sector_relevances, :uniq => true
  has_many                :crs_sectors, :through => :sector_relevances
  
  # Funding Information
  has_one                 :delegated_cooperation
  #has_one                 :delegating_donor,  :through => :delegated_cooperation
  #has_one                 :delegating_agency, :through => :delegated_cooperation
  
  belongs_to              :aid_modality
    
  has_many                :cofundings, :dependent => :delete_all
  has_many                :cofinancing_donors, :through => :cofundings, :source => :donor
  
  has_many                :fundings, :extend => AggregatedFundings, :dependent => :delete_all
  has_many                :funding_forecasts, :dependent => :delete_all
  has_one                 :historic_funding, :dependent => :delete
  
  has_many                :accessible_fundings
  has_many                :accessible_forecasts

  ##
  # Nested attributes
  accepts_nested_attributes_for :sector_relevances, :reject_if => lambda { |a| a['dac_sector_id'].blank? }, :allow_destroy => true
  accepts_nested_attributes_for :cofundings, :reject_if => lambda { |a| a['donor_id'].blank? }, :allow_destroy => true
  accepts_nested_attributes_for :geo_relevances, :reject_if => lambda { |a| a['province_id'].blank? }, :allow_destroy => true
  accepts_nested_attributes_for :fundings, :funding_forecasts, :historic_funding, :allow_destroy => true
  accepts_nested_attributes_for :delegated_cooperation, :allow_destroy => true
  
  
  ##
  # Decorated attributes
  attribute_decorator :officer_responsible, :class => Address, 
    :decorates => [:officer_responsible_name, :officer_responsible_phone, :officer_responsible_email]
  
  ##
  # Custom finders
  named_scope :ordered, :order => "donor_id ASC, donor_project_number ASC"
  
  named_scope :draft, :conditions => ['data_status = ?', DRAFT]
  named_scope :published, :conditions => ['data_status = ?', PUBLISHED]
  named_scope :deleted, :conditions => ['data_status = ?', DELETED]
  
  named_scope :grant, :conditions => ['grant_loan = ?', 1]
  named_scope :loan, :conditions => ['grant_loan = ?', 2]
  
  named_scope :national, :joins => "LEFT OUTER JOIN geo_relevances ON geo_relevances.project_id = projects.id", 
    :conditions => "geo_relevances.province_id IS NULL"
      
  ##
  # Callbacks
  before_validation :set_funding_currency 
  before_save :set_equal_location_shares
  
  ##
  # Validation
    
  validates_inclusion_of    :data_status, :in => [Project::DRAFT, Project::PUBLISHED, Project::DELETED], 
                            :message => "has invalid code: {{value}}"
  
  # STATE: general
  validates_presence_of     :donor_project_number, :title, :description, :donor_agency_id, :prj_status
  validates_uniqueness_of   :donor_project_number, :scope => :donor_id
  
  # STATE: categorization
  validates_presence_of     :national_regional, :type_of_implementation, :aid_modality_id, :grant_loan, 
                            :officer_responsible_name

  validates_presence_of     :government_counterpart, :government_project_code, :single_treasury_account, :if => :on_budget_validation?
  validates_associated      :sector_relevances, :geo_relevances, :mdg_relevances
  validates_associated      :fundings, :funding_forecasts, :historic_funding
  
  validate                  :total_sector_amount_is_100
  validate                  :total_location_amount_is_100
  validate                  :dates_consistency
  
  attr_accessor :project_currency
  
  def project_currency
    @project_currency || self.historic_funding.try(:currency) || self.donor.currency 
  end
  
  # This gives us nicer URLs with the project number in it instead of just the id
  def to_param
    "#{id}-#{donor_project_number.strip.downcase.gsub(/[^[:alnum:]]/,'-')}".gsub(/-{2,}/,'-')
  end
  
  # Method to clone a model with all it's associations
  def clone_with_associations
    associations = self.nested_attributes_options.keys
    cloned_instance = self.clone_without_associations
    
    associations.each do |assoc_name|
      objects = self.send(assoc_name)
      # to-many association
      if objects.is_a?(Array)
        objects.each do |obj|
          attributes = obj.clone.attributes.except(:project_id)
          cloned_instance.send(assoc_name).send(:build, attributes)
        end
      # to-one association
      elsif !objects.nil? # ensure the association is set-up 
        cloned_instance.send("build_#{assoc_name}", objects.clone.attributes.except(:project_id))
      end
    end
    
    cloned_instance
  end
  alias_method_chain :clone, :associations
  
  ##
  # This returns a list of Provinces and Districts in the following format:
  # {"Province 1" => ["Dist 1.1", "Dist 1.2"], "Province 2" => ["Dist 2.1"] ...}
  def geo_list
    ActiveSupport::Deprecation.warn("Ugly method! Will be removed ASAP! Don't use it anymore")
    provinces.inject({}) do |list, province|
      list[province.name] = districts.find_all_by_province_id(province.id).map(&:name)
      
      list
    end
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
  
protected
  
  ##
  # Callback methods
  def reset_delegated_cooperation
    delegating_agency_id = nil unless delegated_cooperation
  end
  
  def set_funding_currency
    historic_funding.currency = project_currency
    fundings.each { |f| f.currency = project_currency }
    funding_forecasts.each { |f| f.currency = project_currency }
  end
  
  def set_equal_location_shares
    alive = self.geo_relevances.reject(&:marked_for_destruction?)
    total = alive.size
    return unless alive.all? { |g| g.amount.blank? }
    
    alive.each { |a| a.amount =  100 / total }
  end
  
  ##
  # Validation methods
  # Validate that the total sector amount per project is 100%
  def total_sector_amount_is_100
    return true unless self.geo_relevances.any?
    
    if (total = self.sector_relevances.reject(&:marked_for_destruction?).map(&:amount).compact.sum) < 95
      # FIXME: Translation missing
      errors.add('sector_relevances', "The sum of the sector percentages should be 100%, but is #{total}%")
    end
  end  
  
  # Validate that the total location amount per project is nearly 100%
  def total_location_amount_is_100
    return true unless self.geo_relevances.any?
    
    if (total = self.geo_relevances.reject(&:marked_for_destruction?).map(&:amount).compact.sum) < 95
      # FIXME: Translation missing
      errors.add('geo_relevances', "The sum of the location percentages should be 100%, but is #{total}")
    end
  end
  
  def dates_consistency
    unless self.planned_start <= self.planned_end
      # FIXME: Translation missing & errors.add_to_base should be used here after views are fixed
       errors.add('planned_start', 'Start date is previous to End Date')
       errors.add('planned_end', '<br>') #added to avoid breaking the design of fieldset while showing the error
    end if self.planned_start && self.planned_end
    
    unless self.actual_start <= self.actual_end
      # FIXME: Translation missing & errors.add_to_base should be used here after views are fixed
       errors.add('actual_start', 'Start date is previous to End Date')
       errors.add('actual_end', '<br>') #added to avoid breaking the design of fieldset while showing the error
    end if self.actual_start && self.actual_end#
  end

  def on_budget_validation?
   !(fundings.detect {|funding| funding.on_budget == true }).blank?
  end
end