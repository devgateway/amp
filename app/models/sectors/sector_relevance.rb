class SectorRelevance < ActiveRecord::Base
  attr_protected :dac_sector_id
  
  belongs_to  :project
  
  belongs_to  :dac_sector
  belongs_to  :crs_sector
  
  before_validation       :update_dac_sector_id_for_crs_sector

  validates_inclusion_of  :amount, :in => 1..100
  
  named_scope :ordered, :joins => [:dac_sector], :order => 'dac_sectors.code ASC'
  
private
  
  # Update dac_sector_id according to crs_sector selected
  # It's better to ignore any user input here as broken browsers could break this
  def update_dac_sector_id_for_crs_sector
    self.dac_sector = 
      self.crs_sector ? self.crs_sector.dac_sector : nil
  end
end