# AggregatedFundings Association Proxy Extension
#
# This can be used to aggregate project's financial information.
# Use like: 
# Project.find(103).fundings.total_payments to get the total payments to the project.
module AggregatedFundings

  # ============
  # = Disbursements =
  # ============
  def total_payments
    inject(0.to_currency) { |sum, cur| cur.payments + sum }
  end

  # ===============
  # = Commitments =
  # ===============
  def total_commitments
    inject(0.to_currency) { |sum, cur| cur.commitments + sum }
  end
  
  # ===============
  # = Undisbursed =
  # ===============
  def undisbursed(year = nil)
    total_commitments(year) - total_payments(year)
  end  
end