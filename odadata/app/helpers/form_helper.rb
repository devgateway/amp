module FormHelper
  # Define helper method for the LabeledFormBuilder
  def labeled_form_for(record_or_name_or_array, *args, &block)
    options = args.extract_options!
    options.merge!(:builder => LabeledFormBuilder)
    form_for(record_or_name_or_array, *(args << options), &block)
  end
  
  # Generates a button that calls a remote (AJAX) function  
  def button_to_remote(name, options = {}, html_options = {})
    button_to_function(name, remote_function(options), html_options) 
  end
end