class DonorsController < ApplicationController
  access_rule 'admin', :only => [:index, :new, :create]
  
  def index
    @donors = Donor.ordered
  end
  
  def show
    @donor = Donor.find(params[:id])
  end
  
  def new
    @donor = Donor.new
  end

  def create
    @donor = Donor.new
    
    # Manually set attributes because mass-assignment has been disabled for security reasons
    @donor.name             = params[:donor][:name]
    @donor.name_es          = params[:donor][:name_es]
    @donor.code             = params[:donor][:code]
    @donor.currency         = params[:donor][:currency]
    @donor.cofunding_only   = params[:donor][:cofunding_only]
    @donor.bluebook_donor   = params[:donor][:bluebook_donor]
    @donor.flag             = params[:donor][:flag]
    @donor.profile_picture  = params[:donor][:profile_picture]
    
    if @donor.save
      flash[:notice] = 'Donor was successfully created.'
      redirect_to donors_path
    else
      flash[:error] = 'There was an error creating a Donor, please check next to the fields for more information'
      render :action => 'new'
    end
  end

  def edit
    @donor = Donor.find(params[:id])
  end

  def update
    @donor = Donor.find(params[:id])
    
    # Manually set attributes because mass-assignment has been disabled for security reasons
    @donor.name             = params[:donor][:name]
    @donor.name_es          = params[:donor][:name_es]
    @donor.code             = params[:donor][:code]
    @donor.currency         = params[:donor][:currency]
    @donor.cofunding_only   = params[:donor][:cofunding_only]
    @donor.bluebook_donor   = params[:donor][:bluebook_donor]
    @donor.flag             = params[:donor][:flag] unless params[:donor][:flag].nil?
    @donor.profile_picture  = params[:donor][:profile_picture] unless params[:donor][:profile_picture].nil?
     
    if @donor.save
      flash[:notice] = 'Donor was successfully updated.'
      redirect_to donors_path
    else
      flash[:error] = 'There was an error updating a Donor, please check next to the fields for more information'           
      render :action => 'edit'
    end
  end
  

  protected
  # TODO: Move to right place! (admin controller?!)
  # Filter to check whether data input is open
  def data_input_open?
    if Prefs.data_input_open
      true
    else
      session[:redirect_url] = request.referer
      redirect_to :controller => "admin", :action => "data_input_closed"
      false
    end
  end
end