class AidModality < ActiveRecord::Base
  translates :name, :group_name
  default_scope :order => 'group_name DESC, name DESC'
end
