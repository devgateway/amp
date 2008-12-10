class ProjectsController < ApplicationController
  before_filter :ensure_open_data_input, :except => [:show, :show_map]
  access_rule 'focal_point', :except => [:show, :show_map]
  cache_sweeper :reports_sweeper, :only => [:update_finances, :publish, :draft, :destroy]
  cache_sweeper :factsheets_sweeper, :only => [:create, :update, :update_finances, :empty_recycle_bin]

  def index
    @status = params[:status].to_i || 0
    @projects = current_donor.projects.find(:all,
      :conditions => { :data_status => @status })
  end
  
  def show
    unless read_fragment(:id => params[:id], :currency => params[:currency], :lang => I18n.locale)
      @project = Project.find(params[:id], :include => [:donor])
      @finances = @project.fundings.find(:all, :order => "year ASC")
    end
    
    
    @project ||= Project.find(params[:id])
    if !params[:report] && @project.data_status == Project::DRAFT
      @next_draft = Project.draft.find(:first, 
        :conditions => ["donor_id = ? AND donor_project_number > ?", session[:user_id], @project.donor_project_number])
    end
  end
   
  def new
    @project = current_donor.projects.build
  end

  def create    
    @project = current_donor.projects.build(params[:project])
    
    if @project.save
      flash[:notice] = "Successfully added project <i>#{@project.title}</i>. Please do not forget to publish once you verified the input."
      redirect_to projects_path
    else
      render :action => 'new'
    end
  end

  def edit
    @project = current_donor.projects.find(params[:id])
  end
  
  def update  
    @project = current_donor.projects.find(params[:id])
    
    if @project.update_attributes(params[:project])
      redirect_to projects_path(:status => @project.data_status)
    else
      render :action => 'edit'
    end
  end
  
  # Changes project's data status
  def update_status
    @project = current_donor.projects.find(params[:id])
    @project.update_attributes!(:data_status => params[:status])
    
    redirect_to projects_path
  end

   def show_map
     @project = Project.find(params[:id])
     OdaMap::ProjectInfo.new(@project).save

     render :layout => 'report_window'
   end
       
  # ================================================
  # = Actions for changing the status of a project =
  # ================================================
  def destroy
    project = Project.find(params[:id])
    raise Authentication::PrivateEntryByDifferentDonor if project.donor_id != session[:user_id]
    
    list = project.list_action
    project.update_attribute(:data_status, 2)
    
    render :action => list
  end
  
  def publish
    project = Project.find(params[:id])
    raise Authentication::PrivateEntryByDifferentDonor if project.donor_id != session[:user_id]
    
    project.update_attribute(:data_status, 1)
    
    redirect_to :action => 'list_draft'
  end
  
  def draft
    project = Project.find(params[:id])
    raise Authentication::PrivateEntryByDifferentDonor if project.donor_id != session[:user_id]
    
    list = project.list_action
    project.update_attribute(:data_status, 0)
    
    redirect_to :action => list
  end

  # ====================
  # = Empty recycle bin =
  # ====================
  def empty_recycle_bin
    projects = Project.find_all_deleted_by_donor_id(current_donor.id) 
    projects.each { |p| p.destroy } 
     
    redirect_to :action => 'list_recycle_bin'
  end
end
