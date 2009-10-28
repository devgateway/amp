class ReportsController < ApplicationController

end


__END__
def draft
  donor = Donor.main.find_by_name(params[:id].upcase)
  redirect_to :action => "project_list", :query => "draft", :value => donor.id
end

def custom
  # If someone clicked the "Annual Totals" button redirect
  # TODO: Move out into a separate action
  if params[:totals_button]
    redirect_to :action => "totals", :currency => DEFAULT_CURRENCY
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