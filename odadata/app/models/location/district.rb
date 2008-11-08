class District < ActiveRecord::Base
  CAPTION = "Municipality"
  
  belongs_to  :province  
  has_many    :geo_relevances, :dependent => :delete_all
  has_many    :projects, :through => :geo_relevances
  
  named_scope :ordered, :order => "name ASC"
  
  def <=>(comp)
    self.id <=> comp.id
  end
end
