class DelegatedCooperation < ActiveRecord::Base
  belongs_to :project
  belongs_to :delegating_donor, :class_name => "Donor"
  belongs_to :delegating_agency, :class_name => "DonorAgency"
  
  validates_presence_of :delegating_donor_id
  validate :agency_belongs_to_donor
  
  # Pretty output for fact sheets: "Donor[ - Agency]"
  def to_s
    out = delegating_donor.name
    out += " - #{delegating_agency.name}" if delegating_agency
    
    out
  end
  
protected
  def agency_belongs_to_donor
    return unless delegating_agency
    
    unless delegating_agency.donor_id == delegating_donor.id
      # TODO: Translation (¡La agencia mandante seleccionada no pertenece al donante seleccionado!)
      errors.add_to_base "Selected delegating agency does not belong to selected donor!"
    end
  end
end