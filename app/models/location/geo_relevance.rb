class GeoRelevance < ActiveRecord::Base
  attr_protected :province_id
  
  belongs_to :project
  belongs_to :province
  belongs_to :district
  
  before_validation :update_province_id_for_district
  
private
  # If the inserted record is a district, also insert the id of the corresponding province
  def update_province_id_for_district
    if self.district
      self.province_id = self.district.province_id
    end
  end
end