class Province < ActiveRecord::Base
  has_many :payments, :class_name => "ProvincePayment"
end