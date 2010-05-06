# Methods added to this helper will be available to all templates in the application.
module ApplicationHelper
  # Helper to display flash messages in views.
  # If +keys+ parameter has been given, only specific messages will be rendered
  # in the given order
  def flash_messages(keys = nil)
    keys ||= [:error, :warning, :notice, :message]
    
    relevant_keys = keys.select { |k| flash[k] }
    relevant_keys.inject("") { |e, k| e += content_tag(:div, flash[k], :class => "flash #{k}") }
  end
  
  # Renders a specific header partial depending on the user's role and associated layout attribute. 
  # This way we can customize the navigation and user info bars for individual roles
  # If nil is given for the role name, it will use the '_default' partial
  def render_header_content_for_role(role_name)
    role_name ||= 'default'
    render :partial => "layouts/header/#{role_name}"
  end
  
  ##
  # Header Generation
  def header(title)
    @_page_title = title
  end
  
  def render_page_title
    if @_page_title
      t('.custom_title', :page_title => strip_html_tags(@_page_title))
    else
      t('.standard_title')
    end
  end
  
  # =====================================================================
  # = Methods for dynamically loading JS and CSS files from controllers =
  # =====================================================================
  def js_behavior(*files)
    files.map! {|f| "behavior/" + f.to_s}
    javascript(*files)
  end
  
  def javascript(*files)
    content_for(:head) { javascript_include_tag(*files) }
    return nil
  end

  def stylesheet(*files)
    content_for(:head) { stylesheet_link_tag(*files) }
  end
  
  def delayed_redirect(url, delay)
    content_for(:head) do
      javascript_tag <<-END
        setTimeout(function() {
          window.location = "#{url}"
        }, #{delay*1000});
      END
    end
  end
  
  # ===============
  # = Tag helpers =
  # ===============
  def expander_tag(content)
    content_tag(:div, content, :class => "expander")
  end
  
  def short_expander_tag(content)
    content_tag(:div, content, :class => "short_expander")
  end
  
  # This should better be in a formatting helper
  def markdown(text)
    #Markdown.new(text, :filter_html).to_html
    r = RedCloth.new text
    strip_html_tags(r).to_html
  end
  
  def icon_link_to(icon_name, link_path, alt_text = nil, link_options = {})
    icon_tag = image_tag("icons/#{icon_name}.png", :size => "16x16", :alt => alt_text)
    link_to icon_tag, link_path, link_options, { :class => "icon", :title => alt_text }
  end
  
  ##
  # Column helper
  # This splits an array across several column and yields 
  ##
  
  def data_in_columns(count, array_to_split, &block)
    record_groups = array_to_split.in_groups(count)
    columns = returning(columns = []) do |cols|
      record_groups.each_with_index do |records, idx| 
        cols << capture(records.compact, idx + 1, &block)
      end
    end
        
    output = content_tag(:div, :class => "columns") do 
      columns.map do |content|
        content_tag(:div, :style => "width: #{(100- count) / count}%") do
          content_tag(:div, content)
        end
      end.join
    end
    
    concat(output)
  end

private
  def strip_html_tags(string)
    string.gsub(/\<\/?.*?\>/, '')
  end
end
