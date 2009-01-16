class SectorRelevance < ActiveRecord::Base
  belongs_to  :project
  
  belongs_to  :dac_sector
  belongs_to  :crs_sector

  validates_inclusion_of  :amount, :in => 1..100
  
  named_scope :ordered, :joins => [:dac_sector], :order => 'dac_sectors.code ASC'
  
end