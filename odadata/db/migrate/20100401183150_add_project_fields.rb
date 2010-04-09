class AddProjectFields < ActiveRecord::Migration
  def self.up
    add_column :projects, :single_treasury_account, :boolean
    add_column :projects, :enters_in, :integer
    add_column :projects, :loan_to_public_enterprises, :boolean
    add_column :projects, :government_project_code, :string
  end

  def self.down
    remove_column :projects, :single_treasury_account
    remove_column :projects, :enters_in
    remove_column :projects, :loan_to_public_enterprises
    remove_column :projects, :government_project_code
  end
end
