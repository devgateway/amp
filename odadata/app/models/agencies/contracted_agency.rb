class ContractedAgency < Agency
  has_and_belongs_to_many :projects
end
