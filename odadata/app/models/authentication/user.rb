class User < ActiveRecord::Base
  acts_as_authentic do |c|
    c.transition_from_restful_authentication = true
  end
  
  belongs_to  :donor
  belongs_to  :role
               
  delegate    :layout,      :to => :role
  delegate    :title,       :to => :role, :prefix => true # Becomes role_title
  delegate    :home_path,   :to => :role
  
end
