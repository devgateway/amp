class DonorDetailsController < ApplicationController
  before_filter :fetch_donor
  access_rule 'admin || focal_point'
  
  def edit
    
  end

  def update
    if @donor.update_attributes(params[:donor])
      flash[:notice] = lc(:saved)
      # TODO: Proper redirect path after donor factsheets are created
      #redirect_to donor_path(@donor)
      redirect_to projects_path
    else
      flash[:error] = lc(:save_failed)
      render :action => 'edit'
    end
  end
  
protected
  def fetch_donor
    @donor = if params[:donor_id] && has_permission?('admin')
      Donor.find(params[:donor_id])
    else
      current_donor
    end
  end
end
