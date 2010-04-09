class GovernmentCounterpart < ActiveRecord::Base
  validates_presence_of :name, :code
  has_many    :projects

  named_scope :ordered, :order => "code ASC"
  def name_with_code
    "#{code} - #{name}"
  end
end
