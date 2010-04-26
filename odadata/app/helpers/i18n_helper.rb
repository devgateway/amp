module I18nHelper
  # Translates options using the app: options: section in the appropriate language file
  def translate_options(object, method, options)
    options.map do |option|
      # Prevent the modification of the original object
      option = option.clone
      to_translate = option[1].is_a?(String) ? 1 : 0
      key = option[to_translate].downcase.gsub(/\W+/, "_").to_sym
      option[to_translate] = object.class.send(:human_option_name, method, key.to_s)
      
      option
    end
  end
end