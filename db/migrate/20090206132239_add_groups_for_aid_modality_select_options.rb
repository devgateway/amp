class AddGroupsForAidModalitySelectOptions < ActiveRecord::Migration
  def self.up
    ["FC Prorural", "FC FONSALUD", "FC Promipyme", "FC PROASE", "FC Fonim"].each do |a|
      AidModality.find_by_name(a).update_attributes!({
        :group_name => "Sectoral Budget Support",
        :group_name_es => "Ayuda Presupuestaria Sectoral"
      })
    end
    
    ["FC FISE", "FC Civil Society", "FC Anti Corruption"].each do |a|
      AidModality.find_by_name(a).update_attributes!({
        :group_name => "Fondo Común",
        :group_name_es => "Fondo Común"
      })
    end
  end

  def self.down
  end
end
