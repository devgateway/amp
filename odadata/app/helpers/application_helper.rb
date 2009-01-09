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
    content_for(:header) { content_tag(:h2, title) }    
    @_page_title = title
  end
  
  def render_page_title
    ll(:layout, :title, :page_title => (@_page_title ? "- #{strip_html_tags(@_page_title)}" : nil))
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
    Markdown.new(text, :filter_html => true).to_html
  end
  
private
  def strip_html_tags(string)
    string.gsub(/\<\/?.*?\>/, '')
  end
end
