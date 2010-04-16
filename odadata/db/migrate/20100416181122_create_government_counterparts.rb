class CreateGovernmentCounterparts < ActiveRecord::Migration
  def self.up
    create_table :government_counterparts do |t|
      t.string :code
      t.string :name

      t.timestamps
    end
    change_table :projects do |t|
      t.references :government_counterpart
    end
    add_index :projects, :government_counterpart_id
    remove_column :projects, :government_counterpart
  end

  def self.down
    drop_table :government_counterparts
    add_column :projects, :government_counterpart, :string
    remove_index :projects, :government_counterpart
  end
end
