# Filters added to this controller apply to all controllers in the application.
# Likewise, all the methods added will be available for all controllers.
class ApplicationController < ActionController::Base
  include AuthenticatedSystem
  include I18nHelper
  
  helper :all
  before_filter :set_output_currency
  before_filter :set_locale
  layout :smart_layout
  
protected
  def set_locale
    # Update session if passed
    session[:locale] = params[:locale] if params[:locale]
    
    # Set locale based on session or default
    I18n.locale = session[:locale] || I18n.default_locale
  end
    
  def set_output_currency
    MultiCurrency.output_currency = params[:currency]
  end
  
  def smart_layout
    if params[:report]
      "report_window"
    else
      "application"
    end
  end
  
  def ensure_open_data_input
    return true if Prefs.data_input_open
    
    session[:redirect_url] = request.referrer
    redirect_to :controller => 'admin', :action => 'data_input_closed'  
  end
end