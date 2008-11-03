class UsersController < ApplicationController
  before_filter :fetch_donor
  access_rule 'focal_point || admin'
  
  def index
    # Get all users if no donor is set and we are logged in as admin,
    # otherwise get users of the respective donor
    @users = @donor.nil? ? User.all : @donor.users
  end
  
  def new
    @user = @donor.nil? ? User.new : @donor.users.build
  end
 
  def create
    @user = @donor.nil? ? User.new : @donor.users.build
    @user.attributes = params[:user]
    # Assigns role focal_point in case the user is added by another focal point
    if @donor.nil?
      @user.role = Role.find_by_title('admin')
    else
      @user.role = Role.find_by_title('focal_point')
    end
    
    if @user.save
      flash[:notice] = "User successfully added."
      redirect_to list_path
    else
      flash[:error]  = "An error occured."
      render :action => 'new'
    end
  end
  
  def edit
    @user = @donor.nil? ? User.find(params[:id]) : @donor.users.find(params[:id])
  end
  
  def update
    @user = @donor.nil? ? User.find(params[:id]) : @donor.users.find(params[:id])
    if @user.update_attributes(params[:user])
      flash[:notice] = 'User was successfully updated.'
      redirect_to list_path
    else
      flash[:error] = 'There was an error updating this User, please check next to the fields for more information'           
      render :action => 'edit'
    end
  end
  
  def destroy
    @user = @donor.nil? ? User.find(params[:id]) : @donor.users.find(params[:id]) 
    @user.destroy
    
    redirect_to list_path
  end
  
protected
  def list_path
    @donor.nil? ? users_path : donor_users_path(@user.donor)
  end
  helper_method :list_path
  
  def fetch_donor
    @donor = if params[:donor_id] && has_permission?('admin')
      Donor.find(params[:donor_id])
    else
      current_donor
    end
  end
end
