class RenameProjectOnBudgetOnTreasury < ActiveRecord::Migration
  def self.up
    rename_column :projects, :on_budget, :on_off_budget
    rename_column :projects, :single_treasury_account, :on_off_treasury
  end

  def self.down
    rename_column :projects, :on_off_budget, :on_budget
    rename_column :projects, :on_off_treasury, :single_treasury_account
  end
end
