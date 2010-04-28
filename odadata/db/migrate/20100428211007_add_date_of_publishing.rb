class AddDateOfPublishing < ActiveRecord::Migration
  def self.up
    add_column :projects, :date_of_publication, :datetime
  end

  def self.down
    remove_column :projects, :date_of_publication
  end
end
