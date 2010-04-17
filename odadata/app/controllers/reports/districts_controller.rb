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
end