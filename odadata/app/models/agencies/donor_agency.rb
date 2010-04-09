class DonorAgency < ActiveRecord::Base
  belongs_to  :donor
  has_many    :cofundings
  has_many    :projects
  named_scope :ordered, :order => "name ASC"

  def name_with_code
    "#{code} - #{name}"
  end

end
