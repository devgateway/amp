# Include into controller and view base classes so that we can use 't' without a receiver
#ActionView::Base.instance_eval { include I18n }
#ActionController::Base.instance_eval { include I18n }

LOCALES_DIRECTORY = "#{RAILS_ROOT}/lib/locale/"
Dir["#{LOCALES_DIRECTORY}/**/*.{rb,yml}"].collect do |locale_file|
  I18n.load_path << locale_file
end