class CreateGlossaries < ActiveRecord::Migration
  def self.up
    create_table :glossaries do |t|
      t.string  :model
      t.string  :method
      t.string  :locale
      t.text    :description
    end
    
    add_index :glossaries, [:model, :method, :locale]
  end

  def self.down
    drop_table :glossaries
  end
end
