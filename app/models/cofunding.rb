class Cofunding < ActiveRecord::Base
  belongs_to :donor
  belongs_to :project
  
  currency_columns :amount, :currency => lambda { |c| c.currency }

  def blank?
    amount.blank? || amount == 0
  end
end
