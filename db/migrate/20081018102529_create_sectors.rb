class CreateSectors < ActiveRecord::Migration
  def self.up
    create_table :crs_sectors do |t|
      t.integer :code
      
      t.references :dac_sector
    end
    
    create_table :crs_sector_translations do |t|
      t.string    :locale
      t.string    :name
      t.text      :description    
      
      t.references :crs_sector
    end

    create_table :dac_sectors do |t|
      t.integer :code
    end
    
    create_table :dac_sector_translations do |t|
      t.string    :locale
      t.string    :name
      t.text      :description    
      
      t.references :dac_sector
    end
    
    add_index :crs_sectors, :dac_sector_id
  end

  def self.down
    drop_table :crs_sectors
    drop_table :dac_sectors
    drop_table :crs_sector_translations
    drop_table :dac_sector_translations
  end
end
