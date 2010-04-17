class Reports::ProvincesController < ReportsController
  def index
    @provinces = Province.ordered.all.select { |d| d.projects.published.any? }
  end
  
  def show
    @province = Province.find(params[:id])
    @projects = @province.projects.published.all
    
    @currency_selector = true
    render :layout => 'report_window'
  end
end