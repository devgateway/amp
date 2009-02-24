class Agency < ActiveRecord::Base
  validates_presence_of :name
  
  default_scope :order => "name ASC"
  named_scope :ordered, :order => "name ASC"
end