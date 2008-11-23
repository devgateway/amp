class Donor < ActiveRecord::Base
  translates :name
  
  has_many :users
    
  has_many :projects
  has_many :country_strategies, :dependent => :delete_all
  has_many :agencies, :class_name => "DonorAgency", :dependent => :delete_all
  
  has_many :cofundings
  has_many :cofinanced_projects, :through => :cofundings, :source => :project
  
  has_many :accessible_fundings
  has_many :accessible_forecasts
  
  ##
  # Validation
  validates_presence_of :name
  
  # Protect attributes that can only be modified by admin
  attr_protected :name, :code, :currency, :cofunding_only
  
  # List of main donors (not cofunding only!)
  named_scope :main, :conditions => "cofunding_only IS DISTINCT FROM true"
  
  # TODO: This is a hack to order by the translated donor name
  # This should better be done in the globalization plugin directly but joining in the translation
  named_scope :ordered, lambda { {
    :joins => "LEFT OUTER JOIN donor_translations ON (donor_translations.donor_id = donors.id AND donor_translations.locale = '#{I18n.locale.to_s.split('-').first}')", 
    :order => "name ASC"
  } }
end
