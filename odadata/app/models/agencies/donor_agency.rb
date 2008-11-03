class DonorAgency < ActiveRecord::Base
  belongs_to  :donor
  has_many    :projects
end