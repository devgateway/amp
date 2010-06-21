class AddCounterpartsProjects < ActiveRecord::Migration
  def self.up
    # Create Table
    create_table "government_counterparts_projects", :id => false, :force => true do |t|
      t.integer "project_id"
      t.integer "government_counterpart_id"
    end
    # Add reference to this table from government_counterpart_id field

  end

  def self.down
    # remove government_counterpart_relevances
    remove_table "government_counterparts_projects"
  end
end
