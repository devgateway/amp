class Donor < ActiveRecord::Base
  extend Translator
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

  
  # List of main donors (not cofunding only!)
  named_scope :main, :conditions => "cofunding_only IS DISTINCT FROM true"
  named_scope :ordered, :order => "name ASC"
end
