module MultiCurrency
  module Conversions
    def to_currency(currency = nil, base_year = Time.now.year)
      MultiCurrency::ConvertibleCurrency.new(self, currency, base_year)
    end
  end
end