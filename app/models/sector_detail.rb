class SectorDetail < ActiveRecord::Base
  belongs_to :country_strategy
  belongs_to :focal_sector, :polymorphic => true
  
  has_and_belongs_to_many :provinces
  
  attr_accessor :dac_sector_id
  attr_accessor :crs_sector_id
  
  currency_columns :amount, :currency => lambda { |sd| sd.country_strategy.donor.currency }
    
  def dac_sector_id
    if focal_sector_type == "DacSector"
      focal_sector_id
    else
      focal_sector ? focal_sector.dac_sector_id : nil
    end
  end
  
  def crs_sector_id
    if focal_sector_type == "DacSector"
      nil
    else
      focal_sector_id
    end
  end
  
  def <=>(comp)
    self.focal_sector <=> comp.focal_sector
  end
end
