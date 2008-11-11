# Add your custom routes here.  If in config/routes.rb you would 
# add <tt>map.resources</tt>, here you would add just <tt>resources</tt>

# resources :bluebooks

namespace :bluebook do |bb|
  bb.resources :donor_profiles
    
  # Static routes
  bb.with_options :controller => 'static' do |static|
    static.connect ':action'
    static.connect ':action.:format'
  end
end