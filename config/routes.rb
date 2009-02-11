ActionController::Routing::Routes.draw do |map|
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
  map.resources :projects,                :member => { :update_status => :get, :map => :get }
                                          
  map.resources :users                   
  map.resources :donor_agencies                
  map.resources :country_strategies,      :collection => { :add_sector => :get }
  
  map.resources :donors, :formatted => :none do |d|
    d.resources :users                   
    d.resources :donor_agencies,          :as => 'agencies'                
    d.resources :country_strategies      
    d.resource  :details,                 :controller => 'donor_details'
  end
  
  map.resource :consistency,              :only => [:index, :show]
  
  # Easily accessible starting points for focal points and admins, respectively:
  map.login     '/login',                 :controller => 'projects' # TODO: Workaround.. Would be better to store a default redirect path in the roles model
                                                                    # and use that one as a default (unless another path was requested before the login redirect.)
  map.admin     '/admin',                 :controller => 'projects'
  map.mfp       '/mfp',                   :controller => 'master_focal_point'
                                          
  # Authentication                        
  map.logout    '/logout',                :controller => 'sessions', :action => 'destroy'
  #map.login     '/login',                 :controller => 'sessions', :action => 'new'
  
  ##
  # EU Blue Book routes
  map.namespace :bluebook do |bluebook|
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
  
  # Install the default route as the lowest priority.
  map.connect ':controller/:action/:id.:format'
  map.connect ':controller/:action.:format'
  map.connect ':controller/:action/:id'
end
