class CreateComplexReports < ActiveRecord::Migration
  def self.up
    create_table :complex_reports do |t|
      t.integer :user_id
      t.text :data
      t.text :comments

      t.timestamps
    end
  end

  def self.down
    drop_table :complexreports
  end
end
