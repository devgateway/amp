class DifferentiatePlannedActualDates < ActiveRecord::Migration
  def self.up
    rename_column :projects, :start, :actual_start
    rename_column :projects, :end, :actual_end
    
    add_column :projects, :planned_start, :date
    add_column :projects, :planned_end, :date
  end

  def self.down
    remove_column :projects, :planned_end
    remove_column :projects, :planned_start
    
    rename_column :projects, :actual_end, :end
    rename_column :projects, :actual_start, :start
  end
end
