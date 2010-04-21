class AddProjectOnBudget < ActiveRecord::Migration
  def self.up
    add_column :projects, :on_budget, :boolean
  end

  def self.down
    remove_column :projects, :on_budget
  end
end
