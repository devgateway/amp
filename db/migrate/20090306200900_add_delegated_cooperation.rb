class AddDelegatedCooperation < ActiveRecord::Migration
  def self.up
    create_table :delegated_cooperations, :force => true do |t|
      t.references :project
      t.references :delegating_donor
      t.references :delegating_agency
    end
    
    add_index  :delegated_cooperations, :project_id
    add_index  :delegated_cooperations, :delegating_donor_id
    add_index  :delegated_cooperations, :delegating_agency_id
  end

  def self.down
    drop_table :delegated_cooperations
  end
end
