require 'partial_validation'

ActiveRecord::Base.class_eval do
  include ActiveRecord::Validations::Partial
end

ActionView::Base.class_eval do
  include PartialValidationHelper
end
