ActionController::Routing::Routes.draw do |map|
  map.routes_from_plugin :analytics

  # Static
  map.with_options :controller => "static" do |static|
    static.root                           :action => "home"
    static.links      "/links",           :action => "links"
    static.downloads  "/downloads",       :action => "downloads"
  end
  
  map.resources :implementing_agencies   
  map.resources :contracted_agencies     
                                          
  map.resources :glossaries              
  map.resources :exchange_rates          
  map.resource  :session
  map.resources :projects,                :member => { :update_status => :get }
                                          
  map.resources :users                   
  map.resources :agencies                
  map.resources :country_strategies,      :collection => { :add_sector => :get }
  
  map.resources :donors, :formatted => :none do |d|
    d.resources :users                   
    d.resources :agencies                
    d.resources :country_strategies      
    d.resource  :details,                 :controller => 'donor_details'
  end
  
  map.resources :settings, :collection => {
    :data_input_status => :get,
    :toggle_data_input => :post
  }
  
  map.resource :consistency,              :only => [:index, :show]
  
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
