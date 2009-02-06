class RenameTypeOfAid < ActiveRecord::Migration
  def self.up
    rename_table :types_of_aid, :aid_modalities
    rename_column :projects, :type_of_aid_id, :aid_modality_id
    
    add_column :aid_modalities, :group_name, :string
    add_column :aid_modalities, :group_name_es, :string
  end

  def self.down
    remove_column :aid_modalities, :group_name
    remove_column :aid_modalities, :group_name_es
    
    rename_table :aid_modalities, :types_of_aid
    rename_column :projects, :aid_modality_id, :type_of_aid_id
  end
end
