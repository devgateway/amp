package org.dgfoundation.amp.mondrian;

import java.util.Arrays;

import org.dgfoundation.amp.ar.viewfetcher.I18nViewColumnDescription;
import org.dgfoundation.amp.ar.viewfetcher.I18nViewDescription;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpSectorScheme;
import org.digijava.module.aim.dbentity.AmpTheme;

/**
 * class holding the Mondrian tables/views which are translated
 * @author Dolghier Constantin
 *
 */
public class MondrianTablesRepository {
	public final static MondrianTableDescription MONDRIAN_LOCATIONS_DIMENSION_TABLE = 
			new MondrianTableDescription("mondrian_locations", Arrays.asList("id", "parent_location", "country_id", "region_id", "zone_id", "district_id"))
				.withInternationalizedColumns(new ObjectSource<I18nViewDescription>() {
					@Override public I18nViewDescription getObject() {
						return new I18nViewDescription("mondrian_locations")
							.addColumnDef(new I18nViewColumnDescription("location_name", "id", AmpCategoryValueLocations.class, "name"))
							.addColumnDef(new I18nViewColumnDescription("country_name", "country_id", AmpCategoryValueLocations.class, "name"))
							.addColumnDef(new I18nViewColumnDescription("region_name", "region_id", AmpCategoryValueLocations.class, "name"))
							.addColumnDef(new I18nViewColumnDescription("zone_name", "zone_id", AmpCategoryValueLocations.class, "name"))
							.addColumnDef(new I18nViewColumnDescription("district_name", "district_id", AmpCategoryValueLocations.class, "name"));
					}});

	public final static MondrianTableDescription MONDRIAN_SECTORS_DIMENSION_TABLE = 
			new MondrianTableDescription("mondrian_sectors", Arrays.asList("amp_sector_id", "parent_sector_id", "level0_sector_id", "level1_sector_id", "level2_sector_id", "amp_sec_scheme_id"))
				.withInternationalizedColumns(new ObjectSource<I18nViewDescription>() {
					@Override public I18nViewDescription getObject() {
						return new I18nViewDescription("mondrian_sectors")
							.addColumnDef(new I18nViewColumnDescription("sector_name", "amp_sector_id", AmpSector.class, "name"))
							.addColumnDef(new I18nViewColumnDescription("level0_sector_name", "level0_sector_id", AmpSector.class, "name"))
							.addColumnDef(new I18nViewColumnDescription("level1_sector_name", "level1_sector_id", AmpSector.class, "name"))
							.addColumnDef(new I18nViewColumnDescription("level2_sector_name", "level2_sector_id", AmpSector.class, "name"))
							.addColumnDef(new I18nViewColumnDescription("sec_scheme_name", "amp_sec_scheme_id", AmpSectorScheme.class, "secSchemeName"));
					}});
	
	public final static MondrianTableDescription MONDRIAN_PROGRAMS_DIMENSION_TABLE = 
			new MondrianTableDescription("mondrian_programs",
					Arrays.asList("amp_theme_id", "parent_theme_id", "program_setting_id", "program_setting_name", "id2", "id3", "id4", "id5", "id6", "id7", "id8"))
				.withInternationalizedColumns(new ObjectSource<I18nViewDescription>() {
					@Override public I18nViewDescription getObject() {
						return new I18nViewDescription("mondrian_programs")
						.addColumnDef(new I18nViewColumnDescription("name", "amp_theme_id", AmpTheme.class, "name"))
						.addColumnDef(new I18nViewColumnDescription("name0", "id0", AmpTheme.class, "name"))
						.addColumnDef(new I18nViewColumnDescription("name1", "id1", AmpTheme.class, "name"))
						.addColumnDef(new I18nViewColumnDescription("name2", "id2", AmpTheme.class, "name"))
						.addColumnDef(new I18nViewColumnDescription("name3", "id3", AmpTheme.class, "name"))
						.addColumnDef(new I18nViewColumnDescription("name4", "id4", AmpTheme.class, "name"))
						.addColumnDef(new I18nViewColumnDescription("name5", "id5", AmpTheme.class, "name"))
						.addColumnDef(new I18nViewColumnDescription("name6", "id6", AmpTheme.class, "name"))
						.addColumnDef(new I18nViewColumnDescription("name7", "id7", AmpTheme.class, "name"))
						.addColumnDef(new I18nViewColumnDescription("name8", "id8", AmpTheme.class, "name"));
					}});

			
	public final static MondrianTableDescription MONDRIAN_ORGANIZATIONS_DIMENSION_TABLE = 
			new MondrianTableDescription("mondrian_organizations", Arrays.asList("amp_org_id", "amp_org_grp_id", "amp_org_type_id"))
				.withInternationalizedColumns(new ObjectSource<I18nViewDescription>() {
					@Override public I18nViewDescription getObject() {
						return new I18nViewDescription("mondrian_organizations")
						.addColumnDef(new I18nViewColumnDescription("org_name", "amp_org_id", AmpOrganisation.class, "name"))
						.addColumnDef(new I18nViewColumnDescription("org_grp_name", "amp_org_grp_id", AmpOrgGroup.class, "orgGrpName"))
						.addColumnDef(new I18nViewColumnDescription("org_type_name", "amp_org_type_id", AmpOrgType.class, "orgType"))
						.addColumnDef(new I18nViewColumnDescription("org_type_code", "amp_org_type_id", AmpOrgType.class, "orgTypeCode"));
					}});
	
	public final static MondrianTableDescription MONDRIAN_ACTIVITY_TEXTS = 
			new MondrianTableDescription("mondrian_activity_texts", Arrays.asList("amp_activity_id"))
				.withInternationalizedColumns(new ObjectSource<I18nViewDescription>() {
					@Override public I18nViewDescription getObject() {
						return new I18nViewDescription("mondrian_activity_texts")
						.addColumnDef(new I18nViewColumnDescription("name", "amp_activity_id", AmpActivityVersion.class, "name"))
						.addTrnColDef("amp_status_name", "amp_status_id");
					}});

	public final static MondrianTableDescription[] MONDRIAN_TRANSLATED_TABLES = new MondrianTableDescription[] {
			MONDRIAN_LOCATIONS_DIMENSION_TABLE, 
			MONDRIAN_SECTORS_DIMENSION_TABLE, 
			MONDRIAN_PROGRAMS_DIMENSION_TABLE, 
			MONDRIAN_ORGANIZATIONS_DIMENSION_TABLE, 
			MONDRIAN_ACTIVITY_TEXTS
	};
}
