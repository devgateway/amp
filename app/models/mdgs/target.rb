class Target < ActiveRecord::Base
  extend Translator
  translates :name
  
  belongs_to  :mdg
  has_many    :relevant_mdgs
  has_many    :projects, :through => :relevant_mdgs
  
  named_scope :ordered, :order => "id ASC" 
end
