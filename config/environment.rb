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
  config.gem "RedCloth"
    
  # Add any subdirectories of app/models to the load path, so that we can use
  # sort of pseudo namespacing.
  config.load_paths += Dir["#{RAILS_ROOT}/app/models/*[^.rb]"]
  
  # Add reports directory to the load path
  config.load_paths << "#{RAILS_ROOT}/app/reports"
end

# Custom validation error handling
ActionView::Base.field_error_proc = lambda do |html_tag, instance|
  if html_tag =~ /<(label)/
    error_tag = %{(<span class="error">#{[instance.error_message].flatten.first}</span>)}
    # Prepend closing tag with error messages
    html_tag.gsub('</label>', " #{error_tag}</label>")
  else
    html_tag
  end
end
