# Filters added to this controller apply to all controllers in the application.
# Likewise, all the methods added will be available for all controllers.
class ApplicationController < ActionController::Base
  AVAILABLE_LAYOUTS = %(report_window)
  
  include Authorization
  include I18nHelper
  
  helper :all
  before_filter :set_output_currency
  after_filter :reset_exchange_rate_sources
  before_filter :set_locale
  layout :set_layout
  
  filter_parameter_logging :password, :password_confirmation
  
protected
  # This allows us to adjust the layout for different purposes via the layout query param
  # All available layouts must be specified in the AVAILABLE_LAYOUTS constant
  def set_layout
    return "application" unless params[:layout]
    
    if AVAILABLE_LAYOUTS.include?(params[:layout])
      params[:layout] 
    else
      raise "Invalid layout: #{params[:layout]}"
    end
  end
  
  def set_locale
    # Update session if passed
    session[:locale] = params[:locale] if params[:locale]
    
    # Set locale based on session or default
    I18n.locale = session[:locale] || I18n.default_locale
  end
    
  def set_output_currency
    MultiCurrency.output_currency = params[:currency] || "EUR"
  end
  
  def reset_exchange_rate_sources
    MultiCurrency.historic_rates_source = nil
    MultiCurrency.current_rates_source = nil
    MultiCurrency.forecasts_rates_source = nil
  end
  
  def ensure_open_data_input
    return true if Prefs.data_input_open
    
    session[:redirect_url] = request.referrer
    redirect_to :controller => 'static', :action => 'data_input_closed'  
  end
end