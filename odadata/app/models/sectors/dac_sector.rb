class DacSector < ActiveRecord::Base
  translates :name, :description
  
  has_many :crs_sectors, :dependent => :destroy
  
  has_many :sector_relevances
  has_many :projects, :through => :sector_relevances, :uniq => true, 
    # this is a hack because SELECT DISTINCT requires all fields used in the ORDER BY clause
    # to be present in the select as well.. This could presumably be solved by using JOIN to 
    # load the translations.. (Globalize 2 patch?!)
    :select => "projects.*, donor_translations.name"
  
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
