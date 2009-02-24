module Authorization
  def self.included(base)
    base.send :helper_method, :current_user_session, :current_user, :current_donor, :restrict_to, :logged_in?
    base.extend ClassMethods
  end
  
  def current_user_session
    return @current_user_session if defined?(@current_user_session)
    @current_user_session = UserSession.find
  end
  
  def logged_in?
    !!current_user_session
  end

  def current_user
    return @current_user if defined?(@current_user)
    @current_user = current_user_session && current_user_session.user
  end
  
  def current_donor
    current_user.andand.donor
  end
  
  # As in AAA you have login_required, here you have permission_required. Pass it a
  # rule and it will use SimpleAccessControl#has_permission? to evaluate against the
  # current user. Use SimpleAccessControl#has_permission? if you are not guarding an
  # action or whole controller. An empty or nil rule will always return true.
  #     permission_required('admin')
  def permission_required(rule = nil)
    if logged_in? && has_permission?(rule)
      send(:permission_granted) if respond_to?(:permission_granted)
      true
    else
      if respond_to?(:permission_denied)
        permission_denied
      else
        access_denied
      end
    end
  end

  # For use in both controllers and views.
  #     has_permission?('role')
  #     has_permission?('admin', other_user)
  def has_permission?(rule, user = nil)
    user ||= (send(:current_user) if respond_to?(:current_user)) || nil
    access_controller.process(rule, user)
  end

  # A much shortened version of Ezra's acl_system2 version.
  #     restrict_to "admin | moderator" do
  #       link_to "foo"
  #     end
  def restrict_to(rule, user = nil)
    yield if block_given? && has_permission?(rule, user)
  end
  
  def access_controller #:nodoc:
    @access_controller ||= AccessControlHandler.new
  end


  # Redirect as appropriate when an access request fails.
  #
  # The default action is to redirect to the login screen.
  #
  # Override this method in your controllers if you want to have special
  # behavior in case the user is not authorized
  # to access the requested action.  For example, a popup window might
  # simply close itself.
  def access_denied
    respond_to do |format|
      format.html do
        store_location
        redirect_to new_user_session_path
      end
      # format.any doesn't work in rails version < http://dev.rubyonrails.org/changeset/8987
      # Add any other API formats here.  (Some browsers, notably IE6, send Accept: */* and trigger 
      # the 'format.any' block incorrectly. See http://bit.ly/ie6_borken or http://bit.ly/ie6_borken2
      # for a workaround.)
      format.any(:json, :xml) do
        request_http_basic_authentication 'Web Password'
      end
    end
  end
  
  def store_location
    session[:return_to] = request.request_uri
  end

  def redirect_back_or_default(default)
    redirect_to(session[:return_to] || default)
    session[:return_to] = nil
  end
  
  module ClassMethods
    # Check if the user is authorized
    #
    # This is the core of the filtering system and it couldn't be simpler:
    #     access_rule '(admin || moderator)', :only => [:edit, :update]
    def access_rule(rule, filter_options = {})
      before_filter (filter_options||{}) { |c| c.send :permission_required, rule }
    end
  end
end