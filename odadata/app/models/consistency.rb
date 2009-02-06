## NOTES FROM TALK 8/7/08 WITH ALEX:
#  
# - Jedes Quartal checks
# - Report über Felder die zwar nicht Pflicht beim Input sind, aber die möglich sind und bei denen z.B. n/a
#   angegeben wurde.
# - Context Checks, z.B. bei DAC Sector Budget Support sollte Type of Financing auch Budget Support sein.
# - Wenn z.B. eine Province in der Description angegeben ist, die nicht als Geo Location eingegeben wurde,
#   Warnung ausgeben. Are you sure?! ;)
# - Wenn Status Pipeline ist und Startdatum verstrichen ist, Warnung ausgeben.
# - Wenn Enddatum verstrichen ist und Status noch nicht Completed, Warnung ausgeben.
# - Es kann nicht mehr Disbursements als Commitments geben! Auch pro Projekt schon Check beim Data Input.
# - Summe der Projekte darf Consistency Finances nicht übersteigen.

class Consistency
  attr_reader :donor, :year
  
  def initialize(donor, year)
    @donor, @year = donor, year
  end
  
  def total_commitments
    donor.annual_commitments[year] || 0.to_currency(donor.currency)
  end
  
  def total_payments
    donor.annual_payments[year] || 0.to_currency(donor.currency)
  end
  
  def payments_by_aid_modality(type)
    donor.payments_by_aid_modality(year)[type] || 0.to_currency(donor.currency)
  end
end
