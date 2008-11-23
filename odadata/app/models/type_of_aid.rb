class TypeOfAid < ActiveRecord::Base
  translates :name
  
  has_many :projects
  has_many :donors, :through => :projects
end
