class CreateSectors < ActiveRecord::Migration
  def self.up
    create_table :crs_sectors do |t|
      t.string  :name
      t.string  :name_es
      
      t.text    :description
      t.text    :description_es
      
      t.integer :code
      
      t.references :dac_sector
    end

    create_table :dac_sectors do |t|
      t.string  :name
      t.string  :name_es
      
      t.text    :description
      t.text    :description_es
      
      t.integer :code
    end
    
    create_table :sector_relevances do |t|
      t.references  :project
      
      t.references  :dac_sector
      t.references  :crs_sector
      
      t.integer     :amount
    end
    
    add_index :crs_sectors, :dac_sector_id
    add_index :sector_relevances, :dac_sector_id
    add_index :sector_relevances, :crs_sector_id
  end

  def self.down
    drop_table :crs_sectors
    drop_table :dac_sectors
    drop_table :sector_relevances
  end
end
