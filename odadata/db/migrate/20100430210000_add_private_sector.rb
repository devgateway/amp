class AddPrivateSector < ActiveRecord::Migration
  def self.up
    add_column :projects, :private_support, :integer
  end

  def self.down
    remove_column :projects, :private_support
  end
end
