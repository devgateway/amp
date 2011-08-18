INSERT INTO amp_columns(columnName, aliasName, cellType, extractorView, relatedContentPersisterClass, filterRetrievable) 
	SELECT "Country", "countryCol", "org.dgfoundation.amp.ar.cell.MetaTextCell", "v_countries", 
	"org.digijava.module.aim.dbentity.AmpCategoryValueLocations", true FROM dual WHERE 0 = (SELECT count(columnName) FROM amp_columns WHERE columnName="Country" );

INSERT INTO amp_columns(columnName, aliasName, cellType, extractorView, relatedContentPersisterClass, filterRetrievable) 
	SELECT "Zone", "zoneCol", "org.dgfoundation.amp.ar.cell.MetaTextCell", "v_zones", 
	"org.digijava.module.aim.dbentity.AmpCategoryValueLocations", true FROM dual WHERE 0 = (SELECT count(columnName) FROM amp_columns WHERE columnName="Zone" );
	
INSERT INTO amp_columns(columnName, aliasName, cellType, extractorView, relatedContentPersisterClass, filterRetrievable) 
	SELECT "District", "districtCol", "org.dgfoundation.amp.ar.cell.MetaTextCell", "v_districts", 
	"org.digijava.module.aim.dbentity.AmpCategoryValueLocations", true FROM dual WHERE 0 = (SELECT count(columnName) FROM amp_columns WHERE columnName="District" );

UPDATE amp_columns SET aliasName ="countryCol", cellType="org.dgfoundation.amp.ar.cell.MetaTextCell", 
		extractorView="v_countries", relatedContentPersisterClass="org.digijava.module.aim.dbentity.AmpCategoryValueLocations", 
		filterRetrievable=true WHERE columnName="Country"; 
	
UPDATE amp_columns SET aliasName ="zoneCol", cellType="org.dgfoundation.amp.ar.cell.MetaTextCell", 
		extractorView="v_zones", relatedContentPersisterClass="org.digijava.module.aim.dbentity.AmpCategoryValueLocations", 
		filterRetrievable=true WHERE columnName="Zone"; 
		
UPDATE amp_columns SET aliasName ="districtCol", cellType="org.dgfoundation.amp.ar.cell.MetaTextCell", 
		extractorView="v_districts", relatedContentPersisterClass="org.digijava.module.aim.dbentity.AmpCategoryValueLocations", 
		filterRetrievable=true WHERE columnName="District"; 
	
	
update amp_columns set relatedContentPersisterClass="org.digijava.module.aim.dbentity.AmpCategoryValueLocations" 
	where extractorView="v_regions";
