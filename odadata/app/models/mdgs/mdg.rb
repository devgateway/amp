class Mdg < ActiveRecord::Base
  translates :name, :description
  
  
  has_many  :targets
  has_many  :mdg_relevances
  has_many  :projects, :through => :mdg_relevances, :uniq => true,
    :select => "projects.*"

  named_scope :ordered, :order => "id ASC"
  
  def description_tooltip
    "<h1>#{name}</h1>#{description}"
  end
end
