ActionController::Routing::Routes.draw do |map|
  map.resources :government_counterparts

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
  # Reports
  map.namespace :reports do |reports|
    reports.resources :donors
    reports.resources :sectors
    reports.resources :mdgs
    reports.resources :provinces do |p|
      p.resources :districts, :shallow => true
    end
    reports.resources :custom
  end
  
  ##
  # EU Blue Book routes
  map.namespace :donor_atlas do |donor_atlas|
    donor_atlas.with_options :path_prefix => '/donor_atlas/:year', :year => /\d{4}/ do |da|
      da.root :controller => 'pages', :action => 'contents'
      da.pages '/pages/:page_name', :controller => 'pages'
  
      da.resources :donor_profiles, :only => [:show], :formatted => :none
  
      # Chart routes
      da.connect 'charts/:action/:id.:format', :controller => 'charts'
      da.connect 'charts/:action.:format', :controller => 'charts'
      da.connect 'charts/:action/:id', :controller => 'charts'
    end
  end
  
  # Route for AJAX glossary tooltip requests
  map.glossary '/glossary/:model/:method', :controller => 'glossaries', :action => 'show'
  
  # Install the default route as the lowest priority.
  map.connect ':controller/:action/:id.:format'
  map.connect ':controller/:action.:format'
  map.connect ':controller/:action/:id'
end
