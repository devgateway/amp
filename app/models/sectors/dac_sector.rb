class DacSector < ActiveRecord::Base
  translates :name, :description
  
  
  has_many :projects
  has_many :crs_sectors
  has_and_belongs_to_many :country_strategies
  has_many :sector_details, :as => :focal_sector
  
  named_scope :ordered, :order => "code ASC"
  
  def name_with_code(maxlength = nil)
    string = [five_digit_code, name].join(" ")
    if maxlength and string.length > maxlength
      string = string[0, maxlength]
      string += "..."
    end
    string
  end
  
  def five_digit_code
    code * 100
  end
  
  def <=>(comp)
    self.five_digit_code <=> comp.five_digit_code
  end
end
