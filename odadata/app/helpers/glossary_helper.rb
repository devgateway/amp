module GlossaryHelper
  def glossary_icon_for(item, caption = "")
    glossary_path = "/glossary/#{item}"
    link_id = caption.downcase.gsub(/\W+/, '_') 
  
    @template.content_tag("a", 
      '<img src="/images/info_icon.png" width="20" height="17"/>',
      :href => glossary_path, :class => "jTip", :name => caption, :id => link_id)
  end
end