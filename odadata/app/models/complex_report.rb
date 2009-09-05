class ComplexReport < ActiveRecord::Base
  belongs_to :user
  
  serialize :data, Ruport::Data::Table
end
