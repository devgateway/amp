class StaticController < ApplicationController
  def home
  end
  
  def links
    @donors_with_websites = Donor.main.ordered.delete_if { |d| !d.field_office_website }
  end
  
  def downloads
  end
  
  # =====================================
  # = Status page for closed data input =
  # =====================================
  def data_input_closed
    @referer = session[:redirect_url] || root_url
    session[:redirect_url] = nil
  end
end
