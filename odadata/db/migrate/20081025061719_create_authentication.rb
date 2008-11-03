class CreateAuthentication < ActiveRecord::Migration
  def self.up
    create_table :users do |t|
      # Identity
      t.string      :name,                      :limit => 100, :default => '', :null => true
      t.string      :email,                     :limit => 100
      t.references  :role
      t.timestamps
      # for Authentication::ByPassword
      t.string      :crypted_password,          :limit => 40
      t.string      :salt,                      :limit => 40
      # for Authentication::ByCookieToken
      t.string      :remember_token,            :limit => 40
      t.datetime    :remember_token_expires_at
      # for Focal Points
      t.references :donor
    end
    
    create_table :roles do |t|
      t.string      :title
      t.text        :description
      t.string      :layout
    end
    
    add_index :users, :email, :unique => true
  end

  def self.down
    drop_table :roles
    drop_table :users
  end
end
