module Reports
  class DonorsController < ::ReportsController    
    def index
      @donors = Donor.ordered.all.select { |d| d.projects.published.any? }
    end
    
    def show
      @donor = Donor.find(params[:id])
      @projects = @donor.projects.published.ordered.all
      
      @currency_selector = true
      render :layout => 'report_window'
    end
    
    def map
      @donor = Donor.find(params[:id])
      @projects = @donor.projects.published.paginate(:all,
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
end
  