class Reports::SectorsController < ReportsController
  def index
    @sectors = DacSector.ordered.all.select { |d| d.projects.published.any? }
  end
  
  def show
    @sector = DacSector.find(params[:id])
    @projects = @sector.projects.published.ordered.all
    render :layout => 'report_window'
  end
end