require 'multi_currency'
require 'multi_currency/conversions'
require 'multi_currency/columns'

[Fixnum, Float, String, NilClass].each do |klass| 
  klass.send :include, MultiCurrency::Conversions
end

ActiveRecord::Base.send :include, MultiCurrency::Columns