class GovernmentCounterpart < ActiveRecord::Base
  validates_presence_of :name, :code
  has_many    :projects
  named_scope :ordered, :order => "code ASC"
end
