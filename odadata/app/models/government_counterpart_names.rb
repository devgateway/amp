class GovernmentCounterpartNames < ActiveRecord::Base
  def self.government_counterpart_name_list
    self.find(:all, :select => "id, name", :order => "id ASC")
  end
  
  validates_presence_of :name
end
