class Agency < ActiveRecord::Base
  validates_presence_of :name
  
  named_scope :ordered, :order => "name ASC"
end