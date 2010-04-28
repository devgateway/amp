class SettingsController < ApplicationController
  access_rule 'admin'
  
  def data_input_status
    @last_action = DataInputAction.most_recent.first
  end
  
  def toggle_data_input
    if params[:open]
      DataInputAction.open!
    elsif params[:close]
      DataInputAction.close!
    end
    
    redirect_to :action => "data_input_status"
  end
end
