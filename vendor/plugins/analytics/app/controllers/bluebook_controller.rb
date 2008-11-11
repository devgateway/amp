class BluebookController < ApplicationController
  layout "bluebook"
  
	# All amounts in bluebooks are in EUR
  before_filter { |c| MultiCurrency.output_currency = "EUR" unless c.params[:currency] }
end