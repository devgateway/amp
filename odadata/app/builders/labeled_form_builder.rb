class LabeledFormBuilder < ActionView::Helpers::FormBuilder    
  include I18nHelper
      
  (field_helpers - %w(label check_box radio_button hidden_field date_select fields_for)).each do |method_name|
    define_method(method_name) do |field_name, *args|
      options = args.extract_options!
      label_opts = extract_label_options!(options)
      wrap_in_label_row(field_name, super, label_opts)
    end
  end
  
  def fields_for(record_or_name_or_array, *args, &block)
    options = args.extract_options!
    options.merge!(:builder => LabeledFormBuilder)

    super(record_or_name_or_array, *(args << options), &block)      
  end
  
  def field_set(legend = nil, options = {}, &block)
    # TODO: Way to refactor?!
    content = @template.capture(&block)
    @template.concat(@template.tag(:fieldset, {}, true))
    @template.concat(@template.content_tag(:legend, legend)) unless legend.blank?
    @template.concat(@template.content_tag(:ol, content))
    @template.concat("</fieldset>")
  end
  
  def hidden_field(method, options = {})
    @template.hidden_field(@object_name, method, objectify_options(options))
  end
  
  def date_select(method, options = {}, html_options = {})
    label_opts = extract_label_options!(options)
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
    options.delete(:required)
    options.delete(:glossary)
    
    caption ||= localized_caption(method)
    glossary = glossary_tooltip(method, caption).to_s
    caption += " *" if field_required?(method)
    @template.label(@object_name, method, glossary + caption, options)
  end
  
  # Creates a link for removing an associated element from the form, by removing its containing element from the DOM.
  #
  # Must be called from within an associated form.
  #     
  # An options hash can be specified to override the default behaviors.
  #
  # Options are:
  # * <tt>:selector</tt>  - The CSS selector with which to find the element to remove.
  # * <tt>:function</tt>  - Additional javascript to be executed before the element is removed.
  #
  # Any remaining options are passed along to link_to_function
  #
  def remove_link(name, *args)
    options = args.extract_options!

    css_selector = options.delete(:selector) || ".#{@object.class.name.split("::").last.underscore}"
    function     = options.delete(:function) || ""
    function    << "$(this).parent('#{css_selector}').hide(); $(this).prev('input').value = '1'"
    
    out = ''
    out << hidden_field(:_delete)
    out << @template.link_to_function(name, function, *args.push(options))
    out 
  end
  
  # Creates a link that adds a new associated form to the page using Javascript.
  #
  # Must be called from within an associated form.
  #
  # Must be provided with a new instance of the associated object.
  #
  #   e.g. f.add_associated_link 'Add Task', @project.tasks.build
  #
  # An options hash can be specified to override the default behaviors.
  #
  # Options are:
  # * <tt>:partial</tt>    - specify the name of the partial in which the form is located.
  # * <tt>:container</tt>  - specify the DOM id of the container in which to insert the new element.
  # * <tt>:expression</tt> - specify a javascript expression with which to select the container to insert the new form in to (i.e. $(this).up('.tasks'))
  # * <tt>:name</tt>       - specify an alternate class name for the associated model (underscored)
  #
  # Any additional options are forwarded to link_to_function. See its documentation for available options.
  #
  def add_associated_link(name, object, opts = {})
    associated_name  = extract_option_or_class_name(opts, :name, object)
    
    opts.symbolize_keys!
    partial          = opts.delete(:partial)    || associated_name
    container        = opts.delete(:expression) || "##{opts.delete(:container) || associated_name.pluralize}"
    
    form_builder     = self # because the value of self changes in the block
    
    @template.link_to_function(name, opts) do |page|
      tmpl = form_builder.render_associated_form(object, :partial => partial)        
      page << %{$('#{container}').append("#{escape_javascript(tmpl)}".replace(/new_\\d+/g, "new_" + (new Date().getTime())))}
    end
  end
  
  # Renders the form of an associated object, wrapping it in a fields_for_associated call.
  #
  # The associated argument can be either an object, or a collection of objects to be rendered.
  #
  # An options hash can be specified to override the default behaviors.
  # 
  # Options are:
  # * <tt>:new</tt>        - specify a certain number of new elements to be added to the form. Useful for displaying a 
  #   few blank elements at the bottom.
  # * <tt>:name</tt>       - override the name of the association, both for the field names, and the name of the partial
  # * <tt>:partial</tt>    - specify the name of the partial in which the form is located.
  # * <tt>:fields_for</tt> - specify additional options for the fields_for call
  # * <tt>:locals</tt>     - specify additional variables to be passed along to the partial
  # * <tt>:render</tt>     - specify additional options to be passed along to the render :partial call
  #
  def render_associated_form(associated, opts = {})
    associated = associated.is_a?(Array) ? associated : [associated] # preserve association proxy if this is one
    
    opts.symbolize_keys!
    # Make sure we are using the correct builder by default
    (opts[:fields_for] ||= {}).reverse_merge!(:builder => self)
    (opts[:new] - associated.select(&:new_record?).length).times { associated.build } if opts[:new]

    unless associated.empty?
      name              = extract_option_or_class_name(opts, :name, associated.first)
      partial           = opts[:partial] || name
      local_assign_name = partial.split('/').last.split('.').first
    
      output = associated.map do |element|
        fields_for(association_name(name), element, (opts[:fields_for] || {}).merge(:name => name)) do |f|
          @template.render({:partial => "#{partial}", :locals => {local_assign_name.to_sym => f.object, :f => f}.merge(opts[:locals] || {})}.merge(opts[:render] || {}))
        end
      end
      
      output.join
    end
  end
  
private
  def field_required?(method)
    return false unless object.class.respond_to?(:reflect_on_validations_for)
    object.class.reflect_on_validations_for(method).map(&:macro).include?(:validates_presence_of)
  end
  
  # Returns a glossary tooltip icon if an attribute description can be found in the localization file
  def glossary_tooltip(method, caption = "")
    underscore_class_name = @object ? @object.class.name.underscore : @object_name
    method_or_association_name = method.to_s.sub(/_id$/, '')
    begin
      I18n.translate("#{underscore_class_name}.#{method_or_association_name}", :scope => [:glossary, :attributes], :raise => true)
      @template.glossary_icon_for("#{underscore_class_name}/#{method_or_association_name}", caption)
    rescue I18n::MissingTranslationData
      nil
    end
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
  
  def extract_option_or_class_name(hash, option, object)
    (hash.delete(option) || object.class.name.split('::').last.underscore)
  end
  
  # Return singular name for singular associations and plural name for plural associations,
  # for proper use with fields_for
  def association_name(class_name)
    @object.respond_to?("#{class_name}_attributes=") ? class_name : class_name.pluralize
  end
end