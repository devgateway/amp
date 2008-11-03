class ImplementingAgenciesController < ApplicationController
  access_rule 'admin', :only => [:list, :new, :edit]
  
  def index
    @implementing_agencies = ImplementingAgency.ordered
  end
  
  def new
    @implementing_agency = ImplementingAgency.new
  end

  def create
    @implementing_agency = ImplementingAgency.create(params[:implementing_agency])
    if @implementing_agency.save
      flash[:notice] = 'Implementing Agency was successfully created.'
      redirect_to implementing_agencies_path
    else
      flash[:error] = 'There was an error creating an Implementing Agency, please check next to the fields for more information'
      render :action => 'new'
    end
  end

  def edit
    @implementing_agency = ImplementingAgency.find(params[:id])
  end

  def update
    @implementing_agency = ImplementingAgency.find(params[:id])
    if @implementing_agency.update_attributes(params[:implementing_agency])
      flash[:notice] = 'Implementing Agency was successfully updated.'
      redirect_to implementing_agencies_path
    else
      flash[:error] = 'There was an error updating the Implementing Agency, please check next to the fields for more information'
      render :action => 'edit'
    end
  end
end