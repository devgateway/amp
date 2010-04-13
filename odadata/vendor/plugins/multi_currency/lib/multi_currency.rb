require 'thread_local_accessor'

module MultiCurrency
  # Raised when conversion from one currency to another fails
  class ConversionError < StandardError
  end
  
  # Raised on attempt to convert an anonymous currency object.
  # That is a currency object which has been instanciated without a +currency+ argument.
  # To avoid this make sure your ConvertibleCurreny object has an actual currency
  # assigned before you use the #in method.
  class ConversionOfAnonymousCurrency < ConversionError
  end
  
  # Raised when the ExchangeRate class returns no or an invalid value on
  # ExchangeRate.find_rate(source, target, year) 
  class ExchangeRateUnavailable < ConversionError
  end

  class << self
    # This can be used to set a default output currency for the ConvertibleCurrency#to_s method
    thread_local_accessor :output_currency
    # Options to use different exchange rate sources for data of different natures
    thread_local_accessor :historic_rates_source
    thread_local_accessor :current_rates_source
    thread_local_accessor :forecasts_rates_source
  end
end
