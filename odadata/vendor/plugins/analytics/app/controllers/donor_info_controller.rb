class DonorInfoController < ApplicationController
  layout "donor_info"
  
	# All amounts in bluebooks are in EUR
  before_filter { |c| MultiCurrency.output_currency = "EUR" unless c.params[:currency] }

  def index
    render :file => "donor_info/pages/contents"
  end
  
protected
  # Year from params or default
  helper_method :year
  def year
    (params[:year] || Time.now.year-1).to_i
  end
end