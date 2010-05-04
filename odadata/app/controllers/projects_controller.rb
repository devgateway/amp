class ProjectsController < ApplicationController
  before_filter :ensure_open_data_input, :except => [:show, :show_map, :map]
  access_rule 'focal_point', :except => [:show, :show_map, :map]
  
  # FIXME: Caching disabled in version 2.6
  #cache_sweeper :reports_sweeper, :only => [:update_finances, :publish, :draft, :destroy]
  #cache_sweeper :factsheets_sweeper, :only => [:create, :update, :update_finances, :empty_recycle_bin]

  def index
    @status = params[:status].to_i || 0
    MultiCurrency.output_currency = params[:currency] || current_donor.currency
    @projects = current_donor.projects.paginate(:all, :conditions => { :data_status => @status },
      :page => params[:page], :per_page => 15, :order => "donor_project_number ASC")
  end
  
  def show
    # No caching currently!
    #unless read_fragment(:id => params[:id], :currency => params[:currency], :lang => I18n.locale)
    #  @project = Project.find(params[:id], :include => [:donor])
    #  @finances = @project.fundings.find(:all, :order => "year ASC")
    #end
    MultiCurrency.output_currency = params[:currency] || current_donor.andand.currency || DEFAULT_CURRENCY

    @project = Project.find(params[:id])
  end
   
  def new
    @project = current_donor.projects.build

    setup_record_range(@project.fundings, Project::FIRST_YEAR_OF_RELEVANCE, Time.now.year)
    setup_record_range(@project.funding_forecasts, Time.now.year+1, (Time.now.year+1)+Project::FORECAST_RANGE)
    @project.build_historic_funding
    set_funding_currency
  end


  def create    
    @project = current_donor.projects.build(params[:project])
    
    if @project.save
      flash[:notice] = "Project <i>#{@project.donor_project_number}</i> has been successfully added."
      redirect_to projects_path
    else
      flash[:error] = "Project could not be saved. Please correct the errors shown below."
      render :action => 'new'
    end
  end

  def edit
    @project = current_donor.projects.find(params[:id])
    
    setup_record_range(@project.fundings, Project::FIRST_YEAR_OF_RELEVANCE, Time.now.year)
    setup_record_range(@project.funding_forecasts, Time.now.year+1, (Time.now.year+1)+Project::FORECAST_RANGE)
#    @project.historic_funding
  end
  
  def update  
    @project = current_donor.projects.find(params[:id])
    
    if @project.update_attributes(params[:project])
      flash[:notice] = "Project <i>#{@project.donor_project_number}</i> has been successfully saved."
      redirect_to projects_path(:status => @project.data_status)
    else
      flash[:error] = "Project could not be saved. Please correct the errors shown below."
      render :action => 'edit'
    end
  end
  
  # Changes project's data status
  def update_status
    @project = current_donor.projects.find(params[:id])
    @project.update_attribute(:data_status, params[:status])
    
    redirect_to projects_path
  end
  
  def clone
    @project = current_donor.projects.find(params[:id]).clone
    @project.donor_project_number += " (copy)"
    @project.title += " (copy)"
    
    render :action => 'new'
  end

   def show_map
     @project = Project.find(params[:id])

     render :layout => 'report_window'
   end
   
  def map
    @project = Project.find(params[:id])
    map_file = OdaMap::ProjectInfo.new(@project).save
    
    send_file map_file, :content_type => 'image/png', :disposition => 'inline'
  end
       
  # ================================================
  # = Actions for changing the status of a project =
  # ================================================
  def destroy
    project = Project.find(params[:id])
    raise Authentication::PrivateEntryByDifferentDonor if project.donor_id != session[:user_id]
    
    list = project.list_action
    project.update_attribute(:data_status, 2)
    
    flash[:notice] = "Project has been deleted."
    render :action => list
  end
  
  def publish
    project = Project.find(params[:id])
    raise Authentication::PrivateEntryByDifferentDonor if project.donor_id != session[:user_id]
    
    project.update_attribute(:data_status, 1)
    
    flash[:notice] = "Project has been published. It is now included into reports and publicly visible."
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
  
private
  # Initializes a number of e.g. funding records with a year attribute
  # for a specific range
  def setup_record_range(association, from, to)
    years = (from..to).to_a - association.all.map(&:year)
    years.each { |year| association.build(:year => year ) }
    
    association
  end

  def set_funding_currency
    @project.fundings.each { |f| f.currency = @project.project_currency }
    @project.funding_forecasts.each { |f| f.currency = @project.project_currency }
    @project.historic_funding.currency = @project.project_currency
  end

end
