# Include into controller and view base classes so that we can use 't' without a receiver
#ActionView::Base.instance_eval { include I18n }
#ActionController::Base.instance_eval { include I18n }

I18n.load_path << "#{RAILS_ROOT}/lib/locale" 