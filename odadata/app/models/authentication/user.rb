class User < ActiveRecord::Base
  acts_as_authentic :transition_from_restful_authentication => true
  
  belongs_to  :donor
  belongs_to  :role
               
  delegate    :layout,      :to => :role
  delegate    :title,       :to => :role, :prefix => true # Becomes role_title
  delegate    :home_path,   :to => :role
  
end
