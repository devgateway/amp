class DelegatedCooperation < ActiveRecord::Base
  belongs_to :project
  belongs_to :delegating_donor, :class_name => "Donor"
  belongs_to :delegating_agency, :class_name => "DonorAgency"
  
  validate :agency_belongs_to_donor
  
  
protected
  def agency_belongs_to_donor
    unless delegating_agency.donor_id == delegating_donor.id
      # TODO: Translation
      errors.add_to_base "Selected delegating agency does not belong to selected donor!"
    end
  end
end