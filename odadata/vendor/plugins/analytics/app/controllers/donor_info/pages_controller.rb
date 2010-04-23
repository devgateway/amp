class DonorInfo::PagesController < DonorInfoController
  def contents
    render :file => "donor_info/pages/contents", :layout => "donor_info"
  end

  def index
    render :file => "donor_info/pages/#{params[:page_name]}", :layout => "donor_info"
  end
end