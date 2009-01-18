class SectorRelevance < ActiveRecord::Base
  belongs_to  :project
  belongs_to  :dac_sector
  belongs_to  :crs_sector
  
  # If only a subsector is specified, this is to ensure, that 
  before_validation :set_proper_parent_sector_id
  
  validate                :subsector_matches_parent
  validates_presence_of   :dac_sector_id
  validates_inclusion_of  :amount, :in => 1..100
  
  named_scope :ordered, :joins => [:dac_sector], :order => 'dac_sectors.code ASC'
  named_scope :ordered_by_relevance, :order => 'amount DESC'
  
  # Returns the most specific sector definition that is available, being the subsector
  # if selected or the parent sector if it is not.
  def sector
    crs_sector || dac_sector
  end
  
protected
  def set_proper_parent_sector_id
    if crs_sector_id && dac_sector_id.nil?
      dac_sector_id = crs_sector.dac_sector_id
    end
  end
  
  def subsector_matches_parent
    if crs_sector && (crs_sector.dac_sector_id != dac_sector_id)
      # TODO: Translation
      errors.add_to_base('Selected subsector does not match parent sector')
    end
  end
end