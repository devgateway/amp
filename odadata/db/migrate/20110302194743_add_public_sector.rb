class AddPublicSector < ActiveRecord::Migration
  def self.up
    add_column :projects, :public_sector, :integer, { :default => 1 }
    execute "UPDATE projects SET public_sector = 1"
  end

  def self.down
    remove_column :projects, :public_sector
  end
end
