class FixComplexReportsDataType < ActiveRecord::Migration
  def self.up
    drop_table :complex_reports
    create_table :complex_reports do |t|
      t.integer :user_id
      t.binary :data
      t.text :comments

      t.timestamps
    end
  end

  def self.down
    drop_table :complex_reports
    create_table :complex_reports do |t|
      t.integer :user_id
      t.text :data
      t.text :comments

      t.timestamps
    end
  end
end
