module OdaTableHelper
  include GlossaryHelper
  
  def odatab_for(record_or_name_or_array, *args, &block)
    raise ArgumentError, "Missing block" unless block_given?
    options = args.extract_options!
    html_opts = tag_options(options.delete(:html))
    
    case record_or_name_or_array
    when String, Symbol
      object_name = record_or_name_or_array
      object = args.first
    else
      object = record_or_name_or_array
      object_name = ActionController::RecordIdentifier.singular_class_name(object)
    end
    
    concat("<table#{html_opts}>", block.binding)
    yield TableBuilder.new(object_name, object, self, options, block)
    concat("</table>", block.binding)
  end
  
  class TableBuilder
    include ActionView::Helpers
    include OdaTableHelper
    include GlossaryHelper
    include ApplicationHelper
    include I18nHelper
        
    def initialize(object_name, object, template, options, proc)
      @object_name, @object, @template, @options, @proc = object_name, object, template, options, proc

      @object ||= @template.instance_variable_get("@#{@object_name}")
      raise ArgumentError, "Can't find given record to build table" unless @object  
    end
    
        
    def title_row(title)
      @template.content_tag(:tr,
        @template.content_tag(:td, title, :colspan => 2, :class => "title"))
    end
    
    def text_field(method, options = {})
      content = safe_access(@object, method, "n/a")
      wrap_in_label_row(method, content, options)
    end
    
    def labeled_row(method, options = {}, &proc)
      @template.concat(wrap_in_label_row(method, @template.capture(&proc), options), proc.binding)
    end
    
    def option_field(method, values, options = {})
      value = safe_access(@object, method)
      content = option_text_by_id(values, value)
      wrap_in_label_row(method, content, options)
    end
  end
  
  def wrap_in_label_row(method, content, options)
    label = build_label(method, options)
    inner_html = [label, @template.content_tag("td", content)]
    inner_html.reverse! if options.delete(:reverse)
    
    @template.content_tag("tr", inner_html.join)
   end
  
  # ================================
  # = Methods for label generation =
  # ================================
  
  def extract_label_options!(options)
    options.extract!(
      :label => nil,
      :label_class => "sub_title",
      :glossary => false)
  end
  
  def localized_caption(method)
    underscore_class_name = @object.class.to_s.underscore
    method_or_relation_name = method.to_s.sub(/_id$/, '')
    
    ll(underscore_class_name, method_or_relation_name)
  end
  
  def build_glossary(method, caption, options)
    if options[:glossary]
      item = options[:glossary].is_a?(String) ? options[:glossary] : method.to_s
      glossary_icon_for(item, caption)
    else
      ""
    end
  end
  
  def build_label(method, options)
    options = extract_label_options!(options)
    caption = options[:label] || localized_caption(method)
    glossary = build_glossary(method, caption, options)
    
    @template.content_tag("td", 
      glossary + @template.content_tag(:span, caption), 
      :class => options[:label_class])
  end
end