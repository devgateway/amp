class DonorDetailsController < ApplicationController
  before_filter :fetch_donor
  access_rule 'admin || focal_point'
  
  def show
    render :text => "Donor: #{@donor.name}"
  end
  
  def edit
    
  end

  def update
    if @donor.update_attributes[:donor]
      flash[:notice] = lc(:saved)
      redirect_to donor_detail_path
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
