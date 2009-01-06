# Add your custom routes here.  If in config/routes.rb you would 
# add <tt>map.resources</tt>, here you would add just <tt>resources</tt>

# resources :bluebooks

namespace :bluebook do |bluebook|
  bluebook.with_options :path_prefix => '/bluebook/:year', :year => /\d{4}/ do |bb|
    bb.root :controller => 'pages', :action => 'contents'
    bb.pages '/pages/:action', :controller => 'pages'
  
    bb.resources :donor_profiles, :only => [:show], :formatted => :none
    
    # Chart routes
    bb.connect 'charts/:action/:id.:format', :controller => 'charts'
    bb.connect 'charts/:action.:format', :controller => 'charts'
    bb.connect 'charts/:action/:id', :controller => 'charts'
  end
end