ActionController::Routing::Routes.draw do |map|
  # Static
  map.with_options :controller => "static" do |static|
    static.root                           :action => "home"
    static.links      "/links",           :action => "links"
    static.downloads  "/downloads",       :action => "downloads"
  end
  
  map.resources :agencies                                                       
  map.resources :exchange_rates          
  map.resources :projects,                :member => { :update_status => :get, :map => :get }
                                          
  map.resources :users                   
  map.resources :donor_agencies                
  map.resources :country_strategies,      :collection => { :add_sector => :get }
  
  map.resources :donors do |d|
    d.resources :users                   
    d.resources :donor_agencies,          :as => 'agencies'                
    d.resources :country_strategies      
    d.resource  :details,                 :controller => 'donor_details'
  end
  
  map.resource :consistency,              :only => [:index, :show]
  
  map.resource :user_session,             :as => 'session'
  # Easily accessible starting points for focal points and admins, respectively:
  map.login     '/login',                 :controller => 'user_sessions', :action => 'new'
  map.admin     '/admin',                 :controller => 'user_sessions', :action => 'new'
  map.mfp       '/mfp',                   :controller => 'user_sessions', :action => 'new'
                                          
  # Authentication                        
  map.logout    '/logout',                :controller => 'user_sessions', :action => 'destroy'
  
  ##
  # EU Blue Book routes
  map.namespace :bluebook do |bluebook|
    bluebook.with_options :path_prefix => '/bluebook/:year', :year => /\d{4}/ do |bb|
      bb.root :controller => 'pages', :action => 'contents'
      bb.pages '/pages/:page_name', :controller => 'pages'
  
      bb.resources :donor_profiles, :only => [:show], :formatted => :none
  
      # Chart routes
      bb.connect 'charts/:action/:id.:format', :controller => 'charts'
      bb.connect 'charts/:action.:format', :controller => 'charts'
      bb.connect 'charts/:action/:id', :controller => 'charts'
    end
  end
  
  # Route for AJAX glossary tooltip requests
  map.glossary '/glossary/:model/:method', :controller => 'glossaries', :action => 'show'
  
  # Install the default route as the lowest priority.
  map.connect ':controller/:action/:id.:format'
  map.connect ':controller/:action.:format'
  map.connect ':controller/:action/:id'
end
