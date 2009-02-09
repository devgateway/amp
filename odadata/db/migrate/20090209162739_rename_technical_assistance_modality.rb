class RenameTechnicalAssistanceModality < ActiveRecord::Migration
  def self.up
    AidModality.find_by_name("Techn. Assistance").update_attribute(:name, "Technical Assistance")
  end

  def self.down
    AidModality.find_by_name("Technical Assistance").update_attribute(:name, "Techn. Assistance")
  end
end
