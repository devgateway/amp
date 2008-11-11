module ReportsHelper
  include ActionView::Helpers::UrlHelper
  
  def format_as_html_list(items, html_options = {})
    items.map! { |r| content_tag("li", r) }
    content_tag("ul", items, html_options)
  end
  
  def format_as_html_table(array, options = {})
    return if array.blank?
    options.reverse_merge!(:lower_class => "list_lower")
    
    row_recs = options[:per_row] ? 
      array.in_groups_of(options.delete(:per_row)) : [array]
    
    out = ""  
    out << row_recs.inject("") do |rows, records|
      first_row = records.map { |a| "<td class=\"sub_title\">#{a[0]}</td>" if a }
      second_row = records.map { |a| "<td class=\"#{options[:lower_class]}\">#{a[1]}</td>" if a }
      rows + content_tag("tr", first_row) + content_tag("tr", second_row)
    end
    
    content_tag("table", out, options)
  end
    
  def markers_list(record, html_options = {})
    rows = Project::AVAILABLE_MARKERS.map do |marker|
      column_name = "#{marker}_marker"
      
      content_tag("tr", 
        content_tag("td", "#{ll(:project, column_name)}:") + 
        content_tag("td", option_text_by_id(Project::MARKER_OPTIONS, record.send(column_name))),
        :class => "simple")
        
    end
    content_tag("table", rows, html_options)
  end
  
  def financial_details(prj, year, html_options = {})
    finances = prj.finances.find_by_year(year)
    
    rows = []
    
    if year >= Time.now.year
      rows << content_tag("tr",
        content_tag("td", "Commitments Forecast") +
        content_tag("td", null_to_na(finances.commitments_forecast)),
        :class => "simple")
                                                                  
      rows << content_tag("tr",                                   
        content_tag("td", "Payments Forecast") +                
        content_tag("td", null_to_na(finances.payments_forecast)),
        :class => "simple")
    else
      rows << content_tag("tr",
        content_tag("td", "Total Commitments") +
        content_tag("td", null_to_na(finances.total_committed)),
        :class => "simple")
                                                                  
      rows << content_tag("tr",                                   
        content_tag("td", "Total Payments") +                
        content_tag("td", null_to_na(finances.total_payments)),
        :class => "simple")
        
      rows += (1..4).map do |quarter|
        content_tag("tr",
          content_tag("td", "#{quarter.ordinalize} Quarter") +
          content_tag("td", null_to_na(finances.send("payments_q#{quarter}"))),
          :class => "simple")
      end
    end
    
    rows << content_tag("tr",
      content_tag("td", option_text_by_id(Project::ON_OFF_BUDGET_OPTIONS, finances.on_budget), :colspan => 2), 
      :class => "simple")
    
    content_tag("table", rows, html_options)
  end
  
  def quarterly_payments(prj, year, html_options = {})
    finances = prj.fundings.find_by_year(year)
    
    rows = []  
    rows += (1..4).map do |quarter|
      content_tag("tr",
        content_tag("td", "#{quarter.ordinalize} Quarter") +
        content_tag("td", (null_to_na(finances.send("payments_q#{quarter}")) rescue 'n/a')),
        :class => "simple")
    end
    
    rows << content_tag("tr",
      content_tag("td", (option_text_by_id(Project::ON_OFF_BUDGET_OPTIONS, finances.on_budget) rescue 'n/a')) +
      content_tag("td", (option_text_by_id(Project::ON_OFF_TREASURY_OPTIONS, finances.on_treasury) rescue 'n/a')),
      :class => "simple")
    
    content_tag("table", rows, html_options)
  end
  
  # Use this helper to create links which open in new windows with the report_window layout
  # win_caption: DEPRECATED
  def reports_link_to(text, url, win_caption = nil)
    if url.respond_to?(:merge)
      url = url.merge({ :report => true })
    else
      url += "?report=true"
    end
    
    link_to(text, url, :popup => ["_blank", 'height=700,width=800,scrollbars=yes,resizable=yes'])
  end
  
  # This helper provides a link to either the factsheet of a project if there is only one
  # or a report of all projects
  def projects_list_link(parent_class)
    if parent_class.projects.published.many?
      reports_link_to(image_tag("/images/list.gif", :size => "14x15", :border => 0), 
  			:controller => "reports", :action => "project_list", :query => parent_class.class.to_s, 
  			:value => parent_class.id)
  	else
  	  link_to(image_tag("/images/details.gif", :size => "14x15", :border => 0), 
  			project_path(parent_class.projects.published.first, :report => true), 
  			:popup => ['Project Details', 'height=600,width=800'])
  	end
  end
  	
  # Adds the currency selector dropdown to the page, if we are in report mode
  def currency_selector
    if params[:report]
      %{<div style="position: absolute; left: 30px; top: 13px">
        #{render :partial => '/layouts/currency_selector'}
      </div>}
    end
  end
end