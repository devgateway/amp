class ChartsController < ApplicationController
  
  # Renders chart_name.xml.erb template from settings/ directory
  def settings
    render :action => File.join("settings", params[:id]), :layout => false
  end
end
