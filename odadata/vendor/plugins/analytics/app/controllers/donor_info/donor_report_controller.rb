class DonorInfo::DonorReportController < DonorInfoController
unloadable

  def show
    @currency = params[:currency].nil? ? DEFAULT_CURRENCY : params[:currency]

    @donor = Donor.find(params[:id])
    @gbs_commitment = []
    @gbs_commitment[2007] = @donor.payments_by_aid_modality(2007)
    @gbs_commitment[2008] = @donor.payments_by_aid_modality(2008)
    @gbs_commitment[2009] = @donor.payments_by_aid_modality(2009)
#    (Project::FIRST_YEAR_OF_RELEVANCE_DONOR_REPORT..Time.now.year+4).to_a.map do |y|
#      @gbs_commitment[y] = @donor.payments_by_aid_modality(y)
#      @gbs_disbursement[y] = @donor.commitments_by_aid_modality(y)
#    end
  end

end