class UpdateUserTables < ActiveRecord::Migration
  def self.up
    change_column :users, :crypted_password, :string, :limit => 128,
      :null => false, :default => ""

    change_column :users, :salt, :string, :limit => 128,
      :null => false, :default => ""
    
    add_column :users, :persistence_token, :string
    add_column :users, :login_count, :integer
    add_column :users, :last_request_at, :datetime
    add_column :users, :current_login_at, :datetime
    add_column :users, :last_login_at, :datetime
    add_column :users, :last_login_ip, :string
    add_column :users, :current_login_ip, :string
  end

  def self.down

  end
end
