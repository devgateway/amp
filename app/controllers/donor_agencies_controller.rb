class DonorAgenciesController < ApplicationController
  before_filter :fetch_donor
  access_rule 'admin'
  
  def index
    @agencies = @donor.agencies
  end
  
  def new
    @agency = @donor.agencies.build
  end
  
  def create
    @agency = @donor.agencies.build(params[:donor_agency])
    if @agency.save  
      flash[:notice] = 'Agency was successfully created.'
      redirect_to donor_donor_agencies_path(@agency.donor)
    else
      flash[:error] = 'There was an error creating this Agency, please check next to the fields for more information'
      render :action => 'new'
    end
  end
  
  def edit
    @agency = @donor.nil? ? DonorAgency.find(params[:id]) : @donor.agencies.find(params[:id])
  end
  
  def update
    @agency = @donor.nil? ? DonorAgency.find(params[:id]) : @donor.agencies.find(params[:id])
    if @agency.update_attributes(params[:donor_agency])
      flash[:notice] = 'Agency was successfully updated.'
      redirect_to donor_donor_agencies_path(@agency.donor)
    else
      flash[:error] = 'There was an error updating this Agency, please check next to the fields for more information'           
      render :action => 'edit'
    end
  end
  
  def destroy
    @agency = @donor.nil? ? DonorAgency.find(params[:id]) : @donor.agencies.find(params[:id]) 
    @agency.destroy
    
    redirect_to donor_donor_agencies_path(@agency.donor)
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