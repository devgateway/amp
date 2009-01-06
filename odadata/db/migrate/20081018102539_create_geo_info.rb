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
    
    execute <<-END_SQL
      CREATE TABLE "public"."geometry_columns" (
        "f_table_catalog" VARCHAR(256) NOT NULL, 
        "f_table_schema" VARCHAR(256) NOT NULL, 
        "f_table_name" VARCHAR(256) NOT NULL, 
        "f_geometry_column" VARCHAR(256) NOT NULL, 
        "coord_dimension" INTEGER NOT NULL, 
        "srid" INTEGER NOT NULL, 
        "type" VARCHAR(30) NOT NULL, 
        CONSTRAINT "geometry_columns_pk" PRIMARY KEY("f_table_catalog", "f_table_schema", "f_table_name", "f_geometry_column")
      ) WITH OIDS;

      COMMENT ON TABLE "public"."geometry_columns"
      IS 'PostGIS table to be populated with each project, linked to said projects geography, which will serve as the base against which GIS operations are performed (eg shapfile/geodatabase creation)';
    END_SQL
    
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
    drop_table :geometry_columns
  end
end
