class CreateSettings < ActiveRecord::Migration
  def self.up
    create_table :settings, :id => false do |t|
      t.string  :key,     :primary => true
      t.text    :value
    end
  end

  def self.down
    drop_table :settings
  end
end
