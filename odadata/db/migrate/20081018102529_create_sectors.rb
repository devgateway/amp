class CreateSectors < ActiveRecord::Migration
  def self.up
    create_table :crs_sectors do |t|
      t.integer :code
      t.string  :name
      t.string  :name_es
      t.text    :description
      t.text    :description_es
      
      t.references :dac_sector
    end

    create_table :dac_sectors do |t|
      t.integer :code
      t.string  :name
      t.string  :name_es
      t.text    :description
      t.text    :description_es
    end
    
    add_index :crs_sectors, :dac_sector_id
  end

  def self.down
    drop_table :crs_sectors
    drop_table :dac_sectors
  end
end
