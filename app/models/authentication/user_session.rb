class UserSession < Authlogic::Session::Base
  login_field :email
end