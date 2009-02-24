class AddHomePathToRoles < ActiveRecord::Migration
  def self.up
    add_column :roles, :home_path, :string
    Role.find_by_title('focal_point').update_attribute('home_path', '/projects')
  end

  def self.down
    remove_column :roles, :home_path
  end
end
