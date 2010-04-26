class DonorInfo::DonorReportController < DonorInfoController
unloadable

  def show
    @currency = params[:currency].nil? ? DEFAULT_CURRENCY : params[:currency]
    @donor = Donor.find(params[:id])

  end

end