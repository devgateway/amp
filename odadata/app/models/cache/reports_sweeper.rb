class ReportsSweeper < ActionController::Caching::Sweeper
  observe Funding, Project
  
  def expire_cached_content(record)
    # Expire Annual Totals
    expire_fragment(%r{reports/totals.*})
  end
  
  alias_method :after_save, :expire_cached_content
  alias_method :after_destroy, :expire_cached_content
end
