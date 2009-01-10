class SettingsController < ApplicationController
  access_rule 'admin'
  
  def data_input_status
    @status = Prefs.data_input_open
  end
  
  def toggle_data_input
    Prefs.data_input_open = (params[:new_status] == "1") ? true : false
    redirect_to data_input_status_settings_path
  end
  
  def scrollbars
    if request.post?
      Prefs.scroll_bar_public = params[:prefs][:scroll_bar_public]
      Prefs.scroll_bar_admin  = params[:prefs][:scroll_bar_admin]
      Prefs.scroll_bar_mfp    = params[:prefs][:scroll_bar_mfp]
      
      flash[:notice] = lc(:successful)
    end
  end
  
  def update_scrollbars
    #
  end
end
