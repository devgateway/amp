class Agency < ActiveRecord::Base
  validates_presence_of :name
  
  has_many :cofundings, :as => :donor
  has_many :cofinanced_projects, :through => :cofundings, :source => :project
  
  default_scope :order => "name ASC"
  named_scope :ordered, :order => "name ASC"
end