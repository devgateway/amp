module DropdownHelper
  def year_select(object, method, first_year, last_year = Time.now.year, options = {}, html_options = {})
    ActiveSupport::Deprecation.warn("year_select is deprecated. Use existing helpers instead")
    
    @collection = Array.new
    
    if first_year > last_year   
      first_year.downto(last_year) do |year|
        @collection << [year, year]
      end
    else
      first_year.upto(last_year) do |year|
        @collection << [year, year]
      end
    end
    
    select(object, method, @collection, options, html_options)
  end
end