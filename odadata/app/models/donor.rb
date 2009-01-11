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
  
  has_attached_file :flag,            :styles => { :regular => "80x54", :avatar => "30x30" }
  has_attached_file :profile_picture, :styles => { :regular => "200x200" }
  
  ##
  # Validation
  validates_presence_of :name, :name_es
  
  # Protect attributes that can only be modified by admin
  attr_protected :name, :name_es, :code, :currency, :cofunding_only, :flag, :profile_picture
  
  # List of main donors (not cofunding only!)
  named_scope :main, :conditions => "cofunding_only IS DISTINCT FROM true"
  
  # TODO: This is a hack to order by the translated donor name
  # This should better be done in the globalization plugin directly but joining in the translation
  named_scope :ordered, :order => "name ASC"
end
