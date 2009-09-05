class Reports::MdgsController < ReportsController
  def index
    @mdgs = Mdg.ordered.all.select { |d| d.projects.published.any? }
  end
  
  def show
    @mdg = Mdg.find(params[:id])
    @projects = @mdg.projects.published.ordered.all
    render :layout => 'report_window'
  end
end
  