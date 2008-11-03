class GovernmentCounterpartNamesController < ApplicationController

  before_filter :authorize_master_focal_point, :only => [:list, :new, :edit]
    
  def new
    @government_counterpart_names = GovernmentCounterpartNames.new
  end

  def create
    @government_counterpart_names = GovernmentCounterpartNames.create(params[:government_counterpart_names])
    if @government_counterpart_names.save
      flash[:notice] = 'Government Counterpart was successfully created.'
      redirect_to :action => 'list'
    else
      flash[:error] = 'There was an error creating a Government Counterpart, please check next to the fields for more information'
      render :action => 'new'
    end
  end

  def list
    @government_counterpart_names = GovernmentCounterpartNames.find(:all, :order => "name ASC")
  end

  def edit
    @government_counterpart_names = GovernmentCounterpartNames.find(params[:id])
  end

  def update
    @government_counterpart_names = GovernmentCounterpartNames.find(params[:id])
    if @government_counterpart_names.update_attributes(params[:government_counterpart_names])
      flash[:notice] = 'Government Counterpart was successfully updated.'
      redirect_to :action => 'list', :id => @government_counterpart_names
    else
      flash[:error] = 'There was an error updating the Government Counterpart, please check next to the fields for more information'
      render :action => 'edit'
    end
  end
end