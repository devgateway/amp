class DacSector < ActiveRecord::Base
  has_many :payments, :class_name => "SectorPayment"
end