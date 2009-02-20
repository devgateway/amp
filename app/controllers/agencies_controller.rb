class AgenciesController < ApplicationController
  access_rule 'admin'
  
  def index
    @agencies = Agency.ordered.paginate(:all, :page => params[:page], :per_page => 30)
  end
  
  def new
    @agency = Agency.new
  end

  def create
    @agency = Agency.create(params[:agency])
    if @agency.save
      flash[:notice] = 'Agency was successfully created.'
      redirect_to agencies_path
    else
      flash[:error] = 'There was an error creating this agency, please check next to the fields for more information'
      render :action => 'new'
    end
  end

  def edit
    @agency = Agency.find(params[:id])
  end

  def update
    @agency = Agency.find(params[:id])
    if @agency.update_attributes(params[:agency])
      flash[:notice] = 'Agency was successfully updated.'
      redirect_to agencies_path
    else
      flash[:error] = 'There was an error updating the agency, please check next to the fields for more information'
      render :action => 'edit'
    end
  end
end