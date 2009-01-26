class LabeledFormBuilder < ActionView::Helpers::FormBuilder    
  include ActionView::Helpers
  include I18nHelper
      
  (field_helpers - %w(label check_box radio_button hidden_field date_select fields_for)).each do |method_name|
    class_eval %{
      def #{method_name}(field_name, *args)
        options = args.extract_options!
        label_opts = extract_label_options!(options)
        wrap_in_label_row(field_name, @template.#{method_name}(@object_name, field_name, *(args << options)), label_opts)
      end
    }
  end
  
  def fields_for(record_or_name_or_array, *args, &block)
    options = args.extract_options!
    options.merge!(:builder => LabeledFormBuilder)
    
    @template.fields_for(record_or_name_or_array, *(args << options), &block)      
  end
  
  def field_set(legend = nil, options = {}, &block)
    # TODO: Way to refactor?!
    content = @template.capture(&block)
    @template.concat(@template.tag(:fieldset, {}, true))
    @template.concat(@template.content_tag(:legend, legend)) unless legend.blank?
    @template.concat(@template.content_tag(:ol, content))
    @template.concat("</fieldset>")
  end
  
  def hidden_field(method, value)
    @template.hidden_field(@object_name, method, {:object => @object, :value => value})
  end
  
  def date_select(method, options = {}, html_options = {})
    label_opts = extract_label_options!(options)
    # Change default order
    options.reverse_merge!(:order => [:day, :month, :year])
    
    wrap_in_label_row(method, @template.date_select(@object_name, method, objectify_options(options), html_options), label_opts)
  end
  
  def select(method, choices, options = {}, html_options = {})
    label_opts = extract_label_options!(options)
    strip_options_for_select!(choices)
    choices = translate_options(choices) if options.delete(:translate)
    
    wrap_in_label_row(method, 
      @template.select(@object_name, method, choices, objectify_options(options), html_options), 
      label_opts)
  end
  
  def collection_select(method, collection, value_method, text_method, options = {}, html_options = {})
    label_opts = extract_label_options!(options)
    wrap_in_label_row(method, 
      @template.collection_select(@object_name, method, collection, value_method, text_method, objectify_options(options), html_options), 
      label_opts)
  end
  
  def related_collection_select(method, parent_element, collection, group_method, group_label_method, option_key_method, option_value_method, options = {}, html_options = {})
    label_opts = extract_label_options!(options)
    wrap_in_label_row(method,
      @template.related_collection_select(@object_name, method, parent_element, collection, group_method, group_label_method, option_key_method, option_value_method, objectify_options(options), html_options),
      label_opts)
  end
  
  def submit(value = nil, options = {})
    value ||= @template.lc(:submit)
    super(value, options)
  end
  
  def year_select(method, first_year, last_year, options = {}, html_options = {})
    label_opts = extract_label_options!(options) 
    wrap_in_label_row(method, 
      @template.year_select(@object_name, method, first_year, last_year, options, html_options), label_opts)
  end
  
  def check_box(method, options = {}, checked_value = "1", unchecked_value = "0")
    label_opts = extract_label_options!(options)
    wrap_in_label_row(method, 
      @template.check_box(@object_name, method, options, checked_value, unchecked_value), 
      label_opts.merge(:class => 'fluid', :reverse => true))
  end
  
  def habtm_check_box(method, options = {}, value = nil)
    label_opts = extract_label_options!(options)
    # We want the label to be on the right side!
    label_opts.merge!({:reverse => true})
    
    assoc_name = "#{@object_name}[#{method}][]"
    box = @template.check_box_tag(assoc_name, value, @object.send(method).include?(value))
    hidden = @template.hidden_field_tag(assoc_name, "")
    wrap_in_label_row(assoc_name, box + hidden, label_opts)
  end

  def labeled_row(method, options = {}, &block)
    @template.concat(wrap_in_label_row(method, @template.capture(&block), options), block.binding)
  end
  
  def label(method, caption = nil, options = {})
    caption ||= localized_caption(method)
    caption += " *" if field_required?(method)
    @template.label(@object_name, method, caption, options)
  end
    
  # This overrides methods from the AttributeFu plugin so that they make use of
  # the LabeledFormBuilder automatically and we don't have to give them a :builder
  # argument on every single call.
  def render_associated_form(associated, opts = {})
    opts[:fields_for] ||= {}
    opts[:fields_for].reverse_merge!(:builder => LabeledFormBuilder)
    super(associated, opts)
  end
  
  def add_associated_link(name, object, opts = {})
    opts[:fields_for] ||= {}
    opts[:fields_for].reverse_merge!(:builder => LabeledFormBuilder)
    super(name, object, opts)
  end
  
  def fields_for_associated(associated, *args, &block)
    opts = args.extract_options!
    opts.reverse_merge!(:builder => LabeledFormBuilder)
    args << opts
    super(associated, *args, &block)
  end
  
private
  def field_required?(field_name)
    return false unless object.class.respond_to?(:reflect_on_validations_for)
    object.class.reflect_on_validations_for(field_name).map(&:macro).include?(:validates_presence_of)
  end
  
  def localized_caption(method)
    underscore_class_name = @object ? @object.class.name.underscore : @object_name
    method_or_association_name = method.to_s.sub(/_id$/, '')

    I18n.translate("#{underscore_class_name}.#{method_or_association_name}", :scope => [:activerecord, :attributes])
  end

  def wrap_in_label_row(field_name, content, options)
    caption, reverse = options.delete(:label), options.delete(:reverse)
    return content if caption == false

    label = label(field_name, caption, options) 
    inner_html = [label, content]
    inner_html.reverse! if reverse

    inner_html.join
  end

  def extract_label_options!(options)
    opts = options.extract!(
      :label => nil,
      :label_class => nil,
      :glossary => false,
      :required => false)

    # Renaming  
    opts[:class] = opts.delete(:label_class)

    opts
  end

  def strip_options_for_select!(options)
    options.map { |e| e.delete_at(2) }
  end
end