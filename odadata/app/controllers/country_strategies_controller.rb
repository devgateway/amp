class CountryStrategiesController < ApplicationController
  access_rule 'focal_point', :except => :show
  before_filter :ensure_open_data_input, :except => [:show]
  before_filter :fetch_donor
  
  def index
    @country_strategies = current_donor.country_strategies.find(:all)
  end
  
  def show
    @country_strategy = CountryStrategy.find(params[:id])
  end
  
  def new
    @country_strategy = @donor.country_strategies.build
    @country_strategy.sector_details.build if @country_strategy.sector_details.blank?
  end
  
  def create
    @country_strategy = @donor.country_strategies.build(params[:country_strategy])

    if @country_strategy.save
        flash[:notice] = 'Country Strategy was successfully created.'
        redirect_to country_strategies_path
      else
        render :action => 'new'
      end
  end
  
  def edit
    @country_strategy = @donor.country_strategies.find(params[:id])
  end
  
  def update
    @country_strategy = @donor.country_strategies.find(params[:id])
    
    if @country_strategy.update_attributes(params[:country_strategy])
        flash[:notice] = 'Country Strategy was successfully updated.'
        redirect_to country_strategies_path
      else
        render :action => 'edit'
      end
  end

  
  
  
  def add_sector
    @sector_detail = SectorDetail.new
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