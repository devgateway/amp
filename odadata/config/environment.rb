# Be sure to restart your web server when you modify this file.

# Specifies gem version of Rails to use when vendor/rails is not present
# RAILS_GEM_VERSION = '2.0.1' unless defined? RAILS_GEM_VERSION

# Bootstrap the Rails environment, frameworks, and default configuration
require File.join(File.dirname(__FILE__), 'boot')
require 'desert'

Rails::Initializer.run do |config|
  config.action_controller.session = { 
    :session_key => "_odanic_session", 
    :secret => "odahr324323asdata249896045328956rocks!!!!!_ashik_alex__frank_pascal" 
  }
  
  ##
  # Gem Dependencies
  config.gem "rubyist-aasm", :source => 'http://gems.github.com', :lib => "aasm"
  config.gem "andand"
    
  # Add any subdirectories of app/models to the load path, so that we can use
  # sort of pseudo namespacing.
  config.load_paths += Dir["#{RAILS_ROOT}/app/models/*[^.rb]"]
  
  # Add reports directory to the load path
  config.load_paths << "#{RAILS_ROOT}/app/reports"
end

# Custom validation error handling
ActionView::Base.field_error_proc = Proc.new do |html_tag, instance|
  error_style = "border: 2px solid #c00"
  
  if html_tag =~ /<(input|textarea|select)[^>]+style=/
    style_attribute = html_tag =~ /style=['"]/
    html_tag.insert(style_attribute + 7, "#{error_style}; ")
  elsif html_tag =~ /<(input|textarea|select)/
    first_whitespace = html_tag =~ /\s/
    html_tag[first_whitespace] = " style='#{error_style}' "
  end
  
  %{#{html_tag}<span class="error">#{[instance.error_message].flatten.first}</span>}
end
