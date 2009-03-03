class AddDelegatedCooperation < ActiveRecord::Migration
  def self.up
    add_column :projects, :delegated_cooperation_id, :integer
    add_index  :projects, :delegated_cooperation_id
  end

  def self.down
    drop_column :projects, :delegated_cooperation_id
    remove_index :projects, :delegated_cooperation_id
  end
end
