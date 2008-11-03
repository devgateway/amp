class FactsheetsSweeper < ActionController::Caching::Sweeper
  observe Project, Funding
  
  def expire_cached_content(record)
    # Expire Factsheets, regardless of currency
    id = record.class == Project ? record.id : record.project_id
    expire_fragment(%r{project/show/#{id}.*})
  end
  
  alias_method :after_save, :expire_cached_content
  alias_method :after_destroy, :expire_cached_content
end
