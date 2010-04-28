class DataInputAction < ActiveRecord::Base
  named_scope :most_recent, :order => "date DESC"
  
  class << self
    def current_status
      self.most_recent.first.try(:action) || 'closed'
    end
  
    def close!
      raise "Data Input is already closed" if current_status == 'closed'
      create!(:action => 'closed', :date => Time.now)
    end
    
    def open!
      raise "Data Input is already opened" if current_status == 'opened'
      create!(:action => 'opened', :date => Time.now)
    end
  end
end