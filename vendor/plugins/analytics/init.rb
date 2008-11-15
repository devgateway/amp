Dir["#{File.dirname(__FILE__)}/locale/**/*.{rb,yml}"].collect do |locale_file|
  I18n.load_path << locale_file
end