# Methods added to this helper will be available to all templates in the application.
module ApplicationHelper
  # Renders a specific menu partial depending on the user's role
  # and associated layout attribute
  def render_user_navigation
    nav_tmpl = logged_in? ? current_user.layout : 'default'
    render :partial => "layouts/navigation/#{nav_tmpl}"
  end
  
  def option_text_by_id(options_array, id)
    translate_options(options_array).rassoc(id).first rescue "n/a"
  end
  
  # Use to savely get a value from a model.
  # If anything should go wrong it returns "n/a"
  def safe_access(record, method, rescue_val = nil)
      record.send(method) rescue rescue_val
  end
  
  def null_to_na(value)
    (value.blank? || value == 0 || value == "0") ? "n/a" : value
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
end
