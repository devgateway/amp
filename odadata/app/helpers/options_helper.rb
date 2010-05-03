module OptionsHelper
  def option_text_by_id(klass, method, options_array, id)
    option_key = options_array.rassoc(id).try(:first)
    klass.send(:human_option_name, method, option_key) unless id.nil?
  end
  
  # Use to savely get a value from a model.
  # If anything should go wrong it returns "n/a"
  def safe_access(record, method, rescue_val = nil)
      record.send(method) rescue rescue_val
  end
  
  def null_to_na(value)
    (value.blank? || value == 0 || value == "0") ? "n/a" : value
  end
end