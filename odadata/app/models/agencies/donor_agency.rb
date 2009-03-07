class DonorAgency < ActiveRecord::Base
  belongs_to  :donor
  has_many    :projects
  
  named_scope :ordered, :order => "name ASC"
end