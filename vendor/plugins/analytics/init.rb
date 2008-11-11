Dir["#{RAILS_ROOT}/vendor/plugins/analytics/locale/**/*.{rb,yml}"].collect do |locale_file|
  I18n.load_path << locale_file
end