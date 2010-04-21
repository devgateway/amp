class GeoRelevance < ActiveRecord::Base
  belongs_to :project
  belongs_to :province
  belongs_to :district
  
  before_validation :update_province_id_for_district
  before_validation :clear_district_id
  
  validate :district_matches_parent
  # TODO: Proper error message
  validates_inclusion_of :amount, :in => 1..100
  
  named_scope :ordered_by_relevance, :order => 'amount DESC'
  
private
  # If the inserted record is a district, also insert the id of the corresponding province
  def update_province_id_for_district
    unless self.district.blank?
      self.province_id = self.district.province_id
    end
  end
  
  # Sets the district id to NULL instead of 0
  def clear_district_id
    self.district = nil if self.district.blank?
  end
  
  def district_matches_parent
    return true if self.district.blank?
    
    if self.district.province_id != self.province_id
      # TODO: Translation
      errors.add_to_base('Selected district does not match parent sector')
    end
  end
end