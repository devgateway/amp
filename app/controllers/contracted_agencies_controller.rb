class ContractedAgenciesController < ApplicationController
  access_rule 'admin', :only => [:list, :new, :edit]
    
  def index
    @contracted_agencies = ContractedAgency.ordered
  end
    
  def new
    @contracted_agency = ContractedAgency.new
  end

  def create
    @contracted_agency = ContractedAgency.new(params[:contracted_agency])
    if @contracted_agency.save
      flash[:notice] = 'Contracting Agency was successfully created.'
      redirect_to contracted_agencies_path
    else
      flash[:error] = 'There was an error creating the Agency, please check next to the fields for more information'
      render :action => 'new'
    end
  end

  def edit
    @contracted_agency = ContractedAgency.find(params[:id])
  end

  def update
    @contracted_agency = ContractedAgency.find(params[:id])
    if @contracted_agency.update_attributes(params[:contracted_agency])
      flash[:notice] = 'Contracted Agency was successfully updated.'
      redirect_to contracted_agencies_path
    else
      flash[:error] = 'There was an error updating the Agency, please check next to the fields for more information'
      render :action => 'edit'
    end
  end
end
