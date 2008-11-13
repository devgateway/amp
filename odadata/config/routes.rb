ActionController::Routing::Routes.draw do |map|
  map.routes_from_plugin :analytics

  # Static
  map.with_options :controller => "static" do |static|
    static.root :action => "home"
  end
  
  map.resources :implementing_agencies,   :formatted => :none
  map.resources :contracted_agencies,     :formatted => :none
  
  map.resources :glossaries,              :formatted => :none
  map.resources :exchange_rates,          :formatted => :none
  map.resource  :session
  map.resources :projects,                :member => { :update_status => :get }
                                          
  map.resources :users,                   :formatted => :none
  map.resources :agencies,                :formatted => :none
  map.resources :country_strategies,      :collection => { :add_sector => :get }
  
  map.resources :donors, :formatted => :none do |d|
    d.resources :users,                   :formatted => :none
    d.resources :agencies,                :formatted => :none
    d.resources :country_strategies,      :formatted => :none
  end
  
  map.resources :settings, :collection => {
    :data_input_status => :get,
    :toggle_data_input => :post
  }
  
  map.resource :consistency,              :only => [:index, :show], :formatted => :none
  
  # Easily accessible starting points for focal points and admins, respectively:
  map.admin     '/admin',                 :controller => 'projects'
  map.mfp       '/mfp',                   :controller => 'master_focal_point'
                                          
  # Authentication                        
  map.logout    '/logout',                :controller => 'sessions', :action => 'destroy'
  map.login     '/login',                 :controller => 'sessions', :action => 'new'  
  
  # Install the default route as the lowest priority.
  map.connect ':controller/:action/:id.:format'
  map.connect ':controller/:action.:format'
  map.connect ':controller/:action/:id'
end
