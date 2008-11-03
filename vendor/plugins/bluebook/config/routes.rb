# Add your custom routes here.  If in config/routes.rb you would 
# add <tt>map.resources</tt>, here you would add just <tt>resources</tt>

# resources :bluebooks

namespace :blue_book do |bb|
  bb.resources :donor_profiles
end
