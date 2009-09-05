module Reports
  class DonorsController < ::ReportsController    
    def index
      @donors = Donor.ordered.all.select { |d| d.projects.published.any? }
    end
    
    def show
      @donor = Donor.find(params[:id])
      @projects = @donor.projects.published.ordered.all
      render :layout => 'report_window'
    end
  end
end
  