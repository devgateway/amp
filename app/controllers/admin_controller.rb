class AdminController < ApplicationController
  before_filter :authorize_donor, :except => [:data_input_closed]
  
  def index
    redirect_to projects_path
  end

  # =====================================
  # = Status page for closed data input =
  # =====================================
  def data_input_closed
    @referer = session[:redirect_url] || root_url
    session[:redirect_url] = nil
  end

end
