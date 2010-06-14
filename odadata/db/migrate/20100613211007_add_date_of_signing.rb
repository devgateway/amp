class AddDateOfSigning < ActiveRecord::Migration
  def self.up
    add_column :projects, :date_of_signing, :datetime
  end

  def self.down
    remove_column :projects, date_of_signing
  end
end
