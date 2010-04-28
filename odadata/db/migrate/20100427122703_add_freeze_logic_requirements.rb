class AddFreezeLogicRequirements < ActiveRecord::Migration
  def self.up
    add_column :projects, :date_of_signature, :datetime
    
    create_table :data_input_actions, :force => true do |t|
      t.string :action, :null => false
      t.datetime :date
    end
  end

  def self.down
    drop_table :data_input_actions
    remove_column :projects, :date_of_signature
  end
end
