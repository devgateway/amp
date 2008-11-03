class MdgRelevance < ActiveRecord::Base
  belongs_to :project
  belongs_to :mdg
  belongs_to :target
  
  before_save :update_mdg_id_for_target
  
private
  # If the inserted record is a target, also insert the id of the corresponding mdg
  def update_mdg_id_for_target
    if self.target
      self.mdg_id = self.target.mdg_id
    end
  end
end