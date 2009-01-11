class CrsSector < ActiveRecord::Base
  translates :name, :description
  
  belongs_to :dac_sector
  
  has_many :sector_relevances
  has_many :projects, :through => :sector_relevances
  has_many :sector_details, :as => :focal_sector
  
  
  alias_attribute :five_digit_code, :code
    
  named_scope :ordered, :order => "code ASC"
  
  def name_with_code(maxlength = nil)
    string = "#{code} #{name}"
    if maxlength and string.length > maxlength
      string.slice!(0, maxlength-3)
      string += "..."
    end
    string
  end
  
  def <=>(comp)
    self.five_digit_code <=> comp.five_digit_code
  end
end
