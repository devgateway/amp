class Address
  attr_accessor :name, :phone, :email
  
  def initialize(name, phone, email)
    @name, @phone, @email = name, phone, email
  end
  
  def to_s
    [@name, @phone, @email].delete_if(&:blank?).join(' | ')    
  end
end