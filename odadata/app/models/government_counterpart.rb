class GovernmentCounterpart < ActiveRecord::Base
  validates_presence_of :name, :code
  has_many    :projects

  named_scope :ordered, :order => "name ASC"
  def name_with_code
    "#{code} - #{name}"
  end
  def code_with_name
    "#{name} - #{code}"
  end
end
