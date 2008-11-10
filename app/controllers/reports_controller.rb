class ReportsController < ApplicationController

  def donors
    @donors = Donor.main.ordered.all.select { |d| d.projects.published.any? }
  end
  
  def sectors
    @sectors = DacSector.ordered.all.select { |d| d.projects.published.any? }
  end
  
  def mdgs
    @mdgs = Mdg.ordered.all.select { |d| d.projects.published.any? }
  end
  
  def locations
    if params[:id]
      @province = Province.find(params[:id])
      @districts = @province.districts.ordered.select { |d| d.projects.published.any? }
      
      render :action => "location_detail"
    else
      @provinces = Province.ordered.all.select { |d| d.projects.published.any? }
      @national = Project.published.all
    end
  end
  
  def project_list
    projects = case params[:query]
    when "donor"
      Donor.find(params[:value]).projects.published.ordered
    when "draft"
      Donor.find(params[:value]).projects.draft.ordered
    when "mdg"
      Mdg.find(params[:value]).projects.published.ordered
    when "sector"
      DacSector.find(params[:value]).projects.published.ordered
    when "location"
      if params[:value].to_i == 0
        # FIXME: Figure out a better way to select national projects..
        #Project.published.all(:include => [:geo_level2s, :donor, :finances], :conditions => "geo_level2_id IS NULL", :order => "donor_id ASC, donor_project_number ASC")
      else
        Province.find(params[:value]).projects.published.ordered
      end
    when "sub_location"
      District.find(params[:value]).projects.published.ordered
    end
    
    fields = [:factsheet_link, :donor, :donor_project_number, :title, :total_commitments, :total_payments, "total_commitments_#{Time.now.year-1}", "total_payments_#{Time.now.year-1}", "commitments_forecast_#{Time.now.year}", "payments_forecast_#{Time.now.year}", :start, :end]
    report = Report::Html.new_from_projects(projects, fields)
    render :inline => report.output, :layout => "currency_report_window"
  end
  
  def draft
    donor = Donor.main.find_by_name(params[:id].upcase)
    redirect_to :action => "project_list", :query => "draft", :value => donor.id
  end

  def custom
    # If someone clicked the "Annual Totals" button redirect
    # TODO: Move out into a separate action
    if params[:totals_button]
      redirect_to :action => "totals", :currency => Prefs.default_currency
    elsif params[:query_button]
      case params[:output][:format]
      when "web"
        report = Report::Html.new_from_params(params)
        render :inline => report.output, :layout => "currency_report_window"
      when "excel"
        report = Report::Excel.new_from_params(params)
        @path = report.output
        render :action => "redirect", :layout => "currency_report_window"
      end
    end
    
    # if request.xhr?
    #       count = Project.count_published(:all, :include => [:donor, :crs_sector, :targets, :geo_level2s], :conditions => builder.find_conditions)
    #       
    #       render :update do |page|
    #         if count == 0
    #           page.replace_html 'project_count', "<b>No projects match your search criteria!</b>"
    #           page[:submit_button].disable
    #           page[:submit_button].addClassName('disabled')
    #         else
    #           page.replace_html 'project_count', "<b>#{count}</b> projects match your search criteria"
    #           page[:submit_button].enable
    #           page[:submit_button].removeClassName('disabled')
    #         end
    #       end
    #     end
  end
  
  def totals
    @type = params[:type] ||= "commitments"
    @years = (Project::FIRST_YEAR_OF_RELEVANCE..Time.now.year+3).to_a
    
    unless read_fragment(:currency => params[:currency], :type => @type)
      @donors = Donor.main
      
      @grand_totals = Funding.send("annual_#{@type}")
      @grand_total_forecasts = FundingForecast.send("annual_#{@type}")
      @total = Funding.send("total_#{@type}")
    end
    
    render :layout => "currency_report_window"
  end
end
