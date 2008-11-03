class SettingsController < ApplicationController
  def data_input_status
    @status = Prefs.data_input_open
  end
  
  def toggle_data_input
    Prefs.data_input_open = (params[:new_status] == "1") ? true : false
    redirect_to data_input_status_settings_path
  end
end
