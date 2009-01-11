class DacSector < ActiveRecord::Base
  translates :name, :description
  
  has_many :crs_sectors, :dependent => :destroy
  has_many :sector_relevances
  has_many :projects, :through => :sector_relevances, :uniq => true  
  has_many :sector_details, :as => :focal_sector
  
  named_scope :ordered, :order => "code ASC"
  
  def name_with_code(maxlength = nil)
    string = [five_digit_code, name].join(" ")
    if maxlength && string.chars.length > maxlength
      "#{string.chars[0, maxlength]}..."
    else
      string
    end
  end
  
  def five_digit_code
    code * 100
  end
  
  def <=>(comp)
    self.five_digit_code <=> comp.five_digit_code
  end
end
