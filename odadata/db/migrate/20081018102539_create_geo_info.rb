class CreateGeoInfo < ActiveRecord::Migration
  def self.up
    create_table :provinces do |t|
      t.string :name
      t.string :code
    end
    
    create_table :districts do |t|
      t.string  :name
      t.string  :code
      
      t.references :province
    end
    
    execute "SELECT AddGeometryColumn('','provinces','the_geom','-1','MULTIPOLYGON',2);"
    execute "SELECT AddGeometryColumn('','districts','the_geom','-1','MULTIPOLYGON',2);"
    
    create_table :geo_relevances do |t|
      t.references :project, :province, :district
    end
    
    create_table :provinces_sector_details, :id => false do |t|
      t.references :province, :sector_detail
    end
  end

  def self.down
    drop_table :provinces
    drop_table :districts
    drop_table :geo_relevances
    drop_table :provinces_sector_details
  end
end
