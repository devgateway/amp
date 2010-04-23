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
  map.resources :projects,                :member => { :update_status => :get, :map => :get, :clone => :get }
                                          
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
    reports.resources :donors, :member => { :map => :get }
    reports.resources :sectors, :member => { :map => :get }
    reports.resources :mdgs, :member => { :map => :get }
    reports.resources :provinces, :member => { :map => :get } do |p|
      p.resources :districts, :shallow => true, :member => { :map => :get }
    end
    reports.resources :custom
  end
  
  ##
  # EU Blue Book routes
  map.namespace :donor_info do |donor_info|
    donor_info.with_options :path_prefix => '/donor_info/:year', :year => /\d{4}/ do |bb|
      bb.root :controller => 'pages', :action => 'contents'
      bb.pages '/pages/:page_name', :controller => 'pages'
  
      # Chart routes
      bb.connect 'charts/:action/:id.:format', :controller => 'charts'
      bb.connect 'charts/:action.:format', :controller => 'charts'
      bb.connect 'charts/:action/:id', :controller => 'charts'
    end
    donor_info.with_options :path_prefix => '/donor_info' do |bb|
      bb.root :controller => 'pages', :action => 'contents'
      bb.pages '/pages/:page_name', :controller => 'pages'
      bb.resources :donor_profiles, :only => [:show], :formatted => :none
      bb.connect 'donor_report/:action/:id.:format', :controller => 'donor_report'
    end
  end
  
  # Route for AJAX glossary tooltip requests
  map.glossary '/glossary/:model/:method', :controller => 'glossaries', :action => 'show'
  
  # Install the default route as the lowest priority.
  map.connect ':controller/:action/:id.:format'
  map.connect ':controller/:action.:format'
  map.connect ':controller/:action/:id'
end
