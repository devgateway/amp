class Reports::DistrictsController < ReportsController
  def index
    @province = Province.find(params[:province_id])
    @districts = @province.districts.ordered.select { |d| d.projects.published.any? }
    render :layout => 'report_window'
  end
  
  def show
    @district = District.find(params[:id])
    @projects = @district.projects.published.ordered.all
    
    @currency_selector = true
    render :layout => 'report_window'
  end
  
  def map
    @district = District.find(params[:id])
    @projects = @district.projects.published.paginate(:all,
      :page => params[:page], :per_page => 5, :order => "donor_project_number ASC")
      
    respond_to do |format|
      format.html do 
        @currency_selector = false
        render :layout => 'report_window'
      end
      
      format.png do
        map_file = OdaMap::Report.new(@projects).save

        send_file map_file, :content_type => 'image/png', :disposition => 'inline'
      end
    end
  end
end