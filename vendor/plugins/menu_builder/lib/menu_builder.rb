class MenuBuilder < ActionView::TemplateHandler
  include ActionView::TemplateHandlers::Compilable
  
  def compile(template)
    eval(template.source)
  end
end