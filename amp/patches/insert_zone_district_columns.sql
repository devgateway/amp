INSERT INTO amp_columns(columnName, aliasName, cellType, extractorView, relatedContentPersisterClass, filterRetrievable) 
	VALUES("Zone", "zoneCol", "org.dgfoundation.amp.ar.cell.MetaTextCell", "v_zones", 
	"org.digijava.module.aim.dbentity.AmpCategoryValueLocations", true);
	
INSERT INTO amp_columns(columnName, aliasName, cellType, extractorView, relatedContentPersisterClass, filterRetrievable) 
	VALUES("District", "districtCol", "org.dgfoundation.amp.ar.cell.MetaTextCell", "v_districts", 
	"org.digijava.module.aim.dbentity.AmpCategoryValueLocations", true);
	
update amp_columns set relatedContentPersisterClass="org.digijava.module.aim.dbentity.AmpCategoryValueLocations" 
	where extractorView="v_regions";
