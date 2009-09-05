class Reports::ProvincesController < ReportsController
  def index
    @provinces = Province.ordered.all.select { |d| d.projects.published.any? }
  end
  
  def show
    @province = Province.find(params[:id])
    @projects = @province.projects.published.all
    render :layout => 'report_window'
  end
end