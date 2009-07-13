class Bluebook::PagesController < BluebookController
  def contents
    render :file => "bluebook/pages/#{year}/contents", :layout => "bluebook"
  end

  def index
    render :file => "bluebook/pages/#{year}/#{params[:page_name]}", :layout => "bluebook"
  end
end