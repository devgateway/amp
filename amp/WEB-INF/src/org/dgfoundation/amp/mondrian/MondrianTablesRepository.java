package org.dgfoundation.amp.mondrian;

import java.util.Arrays;
import java.util.List;

import org.dgfoundation.amp.ar.viewfetcher.I18nViewColumnDescription;
import org.dgfoundation.amp.ar.viewfetcher.I18nViewDescription;
import org.dgfoundation.amp.mondrian.currencies.CurrencyAmountGroup;
import org.dgfoundation.amp.mondrian.jobs.Fingerprint;
import org.dgfoundation.amp.mondrian.monet.DatabaseTableColumn;
import org.dgfoundation.amp.mondrian.monet.DatabaseTableDescription;

import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpSectorScheme;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTheme;

/**
 * class holding the Mondrian tables/views which are translated
 * @author Dolghier Constantin
 *
 */
public class MondrianTablesRepository {
	public final static MondrianTableDescription MONDRIAN_LOCATIONS_DIMENSION_TABLE = 
			new MondrianTableDescription("mondrian_locations", "id", Arrays.asList("id", "parent_location", "country_id", "region_id", "zone_id", "district_id"))
				.withFingerprintedJob(Arrays.asList(
							Fingerprint.buildTableHashingQuery("v_mondrian_locations", "id"), 
							Fingerprint.buildTranslationHashingQuery(AmpCategoryValueLocations.class)))
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
			new MondrianTableDescription("mondrian_sectors", "amp_sector_id", Arrays.asList("amp_sector_id", "parent_sector_id", "level0_sector_id", "level1_sector_id", "level2_sector_id", "amp_sec_scheme_id"))
				.withFingerprintedJob(Arrays.asList(
						Fingerprint.buildTableHashingQuery("v_mondrian_sectors", "amp_sector_id"),
						//Fingerprint.buildTableHashingQuery("amp_sector_scheme"),
						//Fingerprint.buildTableHashingQuery("amp_classification_config"),
						Fingerprint.buildTranslationHashingQuery(AmpSector.class),
						Fingerprint.buildTranslationHashingQuery(AmpSectorScheme.class),
						Fingerprint.buildTranslationHashingQuery(AmpClassificationConfiguration.class)
						))
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
			new MondrianTableDescription("mondrian_programs", "amp_theme_id",
					Arrays.asList("amp_theme_id", "parent_theme_id", "program_setting_id", "program_setting_name", "id2", "id3", "id4", "id5", "id6", "id7", "id8"))
				.withFingerprintedJob(Arrays.asList(
						Fingerprint.buildTableHashingQuery("v_mondrian_programs", "amp_theme_id"),
						Fingerprint.buildTranslationHashingQuery(AmpTheme.class),
						Fingerprint.buildTranslationHashingQuery(AmpActivityProgramSettings.class)
						))
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
			new MondrianTableDescription("mondrian_organizations", "amp_org_id", Arrays.asList("amp_org_id", "amp_org_grp_id", "amp_org_type_id"))
				.withFingerprintedJob(Arrays.asList(
						Fingerprint.buildTableHashingQuery("v_mondrian_organizations", "amp_org_id"),
						Fingerprint.buildTranslationHashingQuery(AmpOrganisation.class),
						Fingerprint.buildTranslationHashingQuery(AmpOrgGroup.class),
						Fingerprint.buildTranslationHashingQuery(AmpOrgType.class)
						))
				.withInternationalizedColumns(new ObjectSource<I18nViewDescription>() {
					@Override public I18nViewDescription getObject() {
						return new I18nViewDescription("mondrian_organizations")
						.addColumnDef(new I18nViewColumnDescription("org_name", "amp_org_id", AmpOrganisation.class, "name"))
						.addColumnDef(new I18nViewColumnDescription("org_grp_name", "amp_org_grp_id", AmpOrgGroup.class, "orgGrpName"))
						.addColumnDef(new I18nViewColumnDescription("org_type_name", "amp_org_type_id", AmpOrgType.class, "orgType"))
						.addColumnDef(new I18nViewColumnDescription("org_type_code", "amp_org_type_id", AmpOrgType.class, "orgTypeCode"));
					}});
	
	public final static MondrianTableDescription MONDRIAN_ACTIVITY_TEXTS = 
			new MondrianTableDescription("mondrian_activity_texts", "amp_activity_id", Arrays.asList("amp_activity_id"))
				.withFingerprintedJob(Arrays.asList(
						Fingerprint.buildTableHashingQuery("v_mondrian_activity_texts", "amp_activity_id"),
						Fingerprint.buildTranslationHashingQuery(AmpActivityVersion.class),
						Fingerprint.buildTranslationHashingQuery(AmpTeam.class)
						))
				.withInternationalizedColumns(new ObjectSource<I18nViewDescription>() {
					@Override public I18nViewDescription getObject() {
						return new I18nViewDescription("mondrian_activity_texts")
							.addColumnDef(new I18nViewColumnDescription("name", "amp_activity_id", AmpActivityVersion.class, "name"))
							.addColumnDef(new I18nViewColumnDescription("team_name", "team_id", AmpTeam.class, "name"));							
					}});

	
	public final static MondrianTableDescription MONDRIAN_ACTIVITY_FIXED_TEXTS = 
			new MondrianTableDescription("mondrian_activity_fixed_texts", "amp_activity_id", Arrays.asList("amp_activity_id"))
				.withFingerprintedJob(Arrays.asList("SELECT 1"));	

	
	public final static MondrianTableDescription MONDRIAN_ACTIVITY_CURRENCY_NUMBERS = 
			new MondrianTableDescription("mondrian_activity_currency_numbers", "amp_activity_id", Arrays.asList("amp_activity_id"))
				.withFingerprintedJob(Arrays.asList("SELECT 1"));
	
	public final static MondrianTableDescription MONDRIAN_ACTIVITY_TRN_TEXTS = 
			new MondrianTableDescription("mondrian_activity_trn_texts", "amp_activity_id", Arrays.asList("amp_activity_id"))
				.withFingerprintedJob(Arrays.asList(Fingerprint.buildTableHashingQuery("v_mondrian_activity_trn_texts", "amp_activity_id")))
				.withInternationalizedColumns(new ObjectSource<I18nViewDescription>() {
					@Override public I18nViewDescription getObject() {
						return new I18nViewDescription("mondrian_activity_trn_texts")
								.addTrnColDef("governmentapprovalprocedures_text", "governmentapprovalprocedures_id")
								.addTrnColDef("jointcriteria_text", "jointcriteria_id")
								.addTrnColDef("iob_text", "iob_id")
								.addTrnColDef("impl_level_name", "impl_level_id")
								.addTrnColDef("impl_loc_name", "impl_loc_id");
					}});
	
	public final static MondrianTableDescription MONDRIAN_CATEGORY_VALUES = 
			new MondrianTableDescription("mondrian_category_values", "acv_id", Arrays.asList("acv_id"))
			.withFingerprintedJob(Arrays.asList(Fingerprint.buildTableHashingQuery("v_mondrian_category_values", "acv_id")))
			.withInternationalizedColumns(new ObjectSource<I18nViewDescription>() {
				@Override public I18nViewDescription getObject() {
					return new I18nViewDescription("mondrian_category_values")
						.addTrnColDef("acv_name", "acv_id");
				}
			});
	
	public final static MondrianTableDescription MONDRIAN_LONG_TEXTS = 
			new MondrianTableDescription("mondrian_activity_long_texts", "amp_activity_id", Arrays.asList("amp_activity_id", "language")) {
				{
					isFiltering = true;
				}
				@Override public boolean rowIsRelevant(java.sql.ResultSet rs, String locale) throws java.sql.SQLException {
					return rs.getString("language").equals(locale);
				}}
			.withFingerprintedJob(Arrays.asList("SELECT 1"))
			.withInternationalizedColumns(new ObjectSource<I18nViewDescription>() {
				@Override public I18nViewDescription getObject() {
					I18nViewDescription res = new I18nViewDescription("mondrian_activity_long_texts");
					for(String col:Arrays.asList("descr", "lessons_learned", "objectives", "results", "purpose", "projectcomments", "project_impact",
							"activity_summary", "conditionality", "project_management", "equalopportunity", "environment", "minorities", "program_description")) {
						res.addDgEditorColumnDef(col + "_body", "language");
					}
					return res;
				}
			});
	
	public final static MondrianTableDescription MONDRIAN_RAW_DONOR_TRANSACTIONS_TABLE = 
			new MondrianTableDescription("mondrian_raw_donor_transactions", "amp_fund_detail_id", Arrays.asList("amp_activity_id", "amp_fund_detail_id", "donor_id"));
	
	public final static List<MondrianTableDescription> MONDRIAN_DIMENSION_TABLES = Arrays.asList(
			MONDRIAN_LOCATIONS_DIMENSION_TABLE,
			MONDRIAN_SECTORS_DIMENSION_TABLE,
			MONDRIAN_PROGRAMS_DIMENSION_TABLE,
			MONDRIAN_ORGANIZATIONS_DIMENSION_TABLE,
			MONDRIAN_CATEGORY_VALUES);
	
	public final static List<MondrianTableDescription> TRN_BACKED_DIMENSIONS = Arrays.asList(MONDRIAN_CATEGORY_VALUES, MONDRIAN_ACTIVITY_TRN_TEXTS, MONDRIAN_LOCATIONS_DIMENSION_TABLE);
	
	public final static List<MondrianTableDescription> MONDRIAN_ACTIVITY_DIMENSIONS = Arrays.asList(
			MONDRIAN_ACTIVITY_TEXTS,
			MONDRIAN_ACTIVITY_FIXED_TEXTS,
			MONDRIAN_LONG_TEXTS, 
			MONDRIAN_ACTIVITY_TRN_TEXTS,
			MONDRIAN_ACTIVITY_CURRENCY_NUMBERS);
		
	public final static List<MondrianTableDescription> MONDRIAN_RAW_TRANSACTIONS_TABLES = Arrays.asList(MONDRIAN_RAW_DONOR_TRANSACTIONS_TABLE);
	
//	public final static Set<MondrianTableDescription> MONDRIAN_NON_TRANSLATED_DIMENSIONS = new HashSet<MondrianTableDescription>() {{
//		add(MONDRIAN_ACTIVITY_FIXED_TEXTS);
//		addAll(MONDRIAN_RAW_TRANSACTIONS_TABLES);
//	}};
	
	/**
	 * not used for now
	 */
	public final static List<String> FACT_TABLE_PRIMARY_KEY_COLUMNS = Arrays.asList("entity_type", "entity_internal_id", "primary_sector_id", "secondary_sector_id", "tertiary_sector_id", "location_id",
			"primary_program_id", "secondary_program_id", "tertiary_program_id", "national_objectives_program_id", "ea_org_id", "ba_org_id", "ia_org_id", "ro_org_id"); 

		
	/**
	 * order of iteration is important, thus LinkedHashSet
	 */
	public final static DatabaseTableDescription FACT_TABLE = new DatabaseTableDescription("mondrian_fact_table", Arrays.asList(
				new DatabaseTableColumn("entity_id", "integer NOT NULL", true), // P/A id 
				new DatabaseTableColumn("entity_internal_id", "integer NOT NULL", true), // amp_funding_detail_id, amp_mtef_detail_id, amp_funding_pledges_detail_id
				new DatabaseTableColumn("transaction_type", "integer NOT NULL", true), // ACV
				new DatabaseTableColumn("adjustment_type", "integer NOT NULL", true),  // ACV
				new DatabaseTableColumn("transaction_date", "date NOT NULL", true),
				new DatabaseTableColumn("date_code", "integer NOT NULL", true),
		
				/**
				 * regarding currencies: if a transaction has a fixed_exchange_rate, BASE_CURRENCY would have been written in currency_id and transaction_amount would be translated
				 */
				new DatabaseTableColumn("transaction_amount", "double NOT NULL", false), // comment 
				new DatabaseTableColumn("currency_id", "integer NOT NULL", true), // comment 
		
				new DatabaseTableColumn("donor_id", "integer", true), // amp_org_id, might be null for example for pledges (which originate in donor groups)
				new DatabaseTableColumn("financing_instrument_id", "integer", true), // ACV
				new DatabaseTableColumn("terms_of_assistance_id", "integer", true),  // ACV
				new DatabaseTableColumn("funding_status_id", "integer", true),  // ACV
				new DatabaseTableColumn("mode_of_payment_id", "integer", true),  // ACV
				new DatabaseTableColumn("status_id", "integer", true),  // ACV
				new DatabaseTableColumn("modality_id", "integer", true),  // ACV
				new DatabaseTableColumn("type_of_cooperation_id", "integer", true),  // ACV
				new DatabaseTableColumn("type_of_implementation_id", "integer", true),  // ACV
				new DatabaseTableColumn("procurement_system_id", "integer", true),  // ACV
		
				new DatabaseTableColumn("primary_sector_id", "integer NOT NULL", true),   // amp_sector_id, subject to Cartesian product
				new DatabaseTableColumn("secondary_sector_id", "integer NOT NULL", true), // amp_sector_id, subject to Cartesian product
				new DatabaseTableColumn("tertiary_sector_id", "integer NOT NULL", true),  // amp_sector_id, subject to Cartesian product
		
				new DatabaseTableColumn("location_id", "integer NOT NULL", true), // amp_category_value_location_id, subject to Cartesian product
		
				new DatabaseTableColumn("primary_program_id", "integer NOT NULL", true),   // amp_theme_id, subject to Cartesian product
				new DatabaseTableColumn("secondary_program_id", "integer NOT NULL", true), // amp_theme_id, subject to Cartesian product
				new DatabaseTableColumn("tertiary_program_id", "integer NOT NULL", true),  // amp_theme_id, subject to Cartesian product
				new DatabaseTableColumn("national_objectives_program_id", "integer NOT NULL", true),  // amp_theme_id, subject to Cartesian product
		
				new DatabaseTableColumn("ea_org_id", "integer NOT NULL", true), // EXEC amp_org_id, subject to Cartesian product
				new DatabaseTableColumn("ba_org_id", "integer NOT NULL", true), // BENF amp_org_id, subject to Cartesian product
				new DatabaseTableColumn("ia_org_id", "integer NOT NULL", true), // IMPL amp_org_id, subject to Cartesian product
				new DatabaseTableColumn("ro_org_id", "integer NOT NULL", true), // RESP amp_org_id, subject to Cartesian product
		
				new DatabaseTableColumn("src_role", "text", true),  // amp_role.role_name
				new DatabaseTableColumn("dest_role", "text", true), // amp_role.role_name
				new DatabaseTableColumn("dest_org_id", "integer", true)   // amp_org_id
		));
	
	public final static List<CurrencyAmountGroup> CURRENCY_GROUPS = Arrays.asList(
			MONDRIAN_ACTIVITY_CURRENCY_NUMBERS.getCurrencyBlock("ppc_"),
			new CurrencyAmountGroup(MONDRIAN_RAW_DONOR_TRANSACTIONS_TABLE.tableName, FACT_TABLE.tableName, "amp_activity_id", "entity_id", ""));
}

/**
 * to cleanup the mess:
 * 
DROP TABLE etl_fingerprints;
DROP TABLE IF EXISTS mondrian_raw_donor_transactions;
DROP TABLE IF EXISTS mondrian_locations;
DROP TABLE IF EXISTS mondrian_sectors;
DROP TABLE IF EXISTS mondrian_programs;
DROP TABLE IF EXISTS mondrian_organizations;
DROP TABLE IF EXISTS mondrian_activity_texts;
DROP TABLE IF EXISTS mondrian_raw_donor_transactions;
DROP TABLE IF EXISTS etl_executing_agencies;
DROP TABLE IF EXISTS etl_beneficiary_agencies;
DROP TABLE IF EXISTS etl_implementing_agencies;
DROP TABLE IF EXISTS etl_responsible_agencies;
DROP TABLE IF EXISTS etl_locations;
DROP TABLE IF EXISTS etl_activity_program_national_plan_objective;
DROP TABLE IF EXISTS etl_activity_program_primary_program;
DROP TABLE IF EXISTS etl_activity_program_secondary_program;
DROP TABLE IF EXISTS etl_activity_program_tertiary_program;
DROP TABLE IF EXISTS etl_activity_sector_primary;
DROP TABLE IF EXISTS etl_activity_sector_secondary;
DROP TABLE IF EXISTS etl_activity_sector_tertiary;
DROP TABLE IF EXISTS etl_locations;
DROP TABLE IF EXISTS etl_locations;
DROP TABLE IF EXISTS etl_locations;
DROP TABLE IF EXISTS etl_locations;
DROP TABLE IF EXISTS etl_locations;
DROP TABLE IF EXISTS etl_locations;
DROP TABLE IF EXISTS mondrian_dates;
DROP TABLE IF EXISTS mondrian_fact_table;
DROP TABLE IF EXISTS mondrian_exchange_rates;

to get columns of table in monetdb: select c.* from sys.columns c where c.table_id = (select t.id from sys.tables t where t.name='mondrian_fact_table')
INSERT INTO mondrian_fact_table (entity_type, entity_id, entity_internal_id, transaction_type, adjustment_type, transaction_date, date_code, transaction_amount,    currency_id, donor_id, financing_instrument_id, terms_of_assistance_id, primary_sector_id, secondary_sector_id, tertiary_sector_id, location_id,   primary_program_id, secondary_program_id, tertiary_program_id, national_objectives_program_id,   ea_org_id, ba_org_id, ia_org_id, ro_org_id, src_role_id, dest_role_id, dest_org_id)   SELECT  	'A' as entity_type, 	rawdonation.amp_activity_id AS entity_id, 	rawdonation.amp_fund_detail_id AS entity_internal_id,     rawdonation.transaction_type AS transaction_type,     rawdonation.adjustment_type AS adjustment_type,     rawdonation.transaction_date AS transaction_date,     rawdonation.date_code AS date_code,  	rawdonation.transaction_amount * (          COALESCE(location.percentage, 1) *          COALESCE(prim_prog.percentage, 1) *          COALESCE(sec_prog.percentage, 1) * 		 COALESCE(tert_prog.percentage, 1) *          COALESCE(npo_prog.percentage, 1) *          COALESCE(prim_sect.percentage, 1) * 		 COALESCE(sec_sect.percentage, 1) *          COALESCE(tert_sect.percentage, 1) *          COALESCE(ra.percentage, 1) *          COALESCE(ba.percentage, 1) *          COALESCE(ia.percentage, 1) *          COALESCE(ea.percentage, 1)          ) AS transaction_amount,       rawdonation.currency_id AS currency_id, 	 rawdonation.donor_id AS donor_id,      rawdonation.financing_instrument_id AS financing_instrument_id,      rawdonation.terms_of_assistance_id AS terms_of_assistance_id,       COALESCE(prim_sect.ent_id, 999999999) AS primary_sector_id,      COALESCE(sec_sect.ent_id, 999999999) AS secondary_sector_id,      COALESCE(tert_sect.ent_id, 999999999) AS tertiary_sector_id,       COALESCE(location.ent_id, 999999999) AS location_id,       COALESCE(prim_prog.ent_id, 999999999) AS primary_program_id,      COALESCE(sec_prog.ent_id, 999999999) AS secondary_program_id,      COALESCE(tert_prog.ent_id, 999999999) AS tertiary_program_id,      COALESCE(npo_prog.ent_id, 999999999) AS national_objectives_program_id,       COALESCE(ea.ent_id, 999999999) AS ea_org_id,      COALESCE(ba.ent_id, 999999999) AS ba_org_id,      COALESCE(ia.ent_id, 999999999) AS ia_org_id,      COALESCE(ra.ent_id, 999999999) AS ro_org_id,            rawdonation.src_role_id AS src_role_id,      rawdonation.dest_role_id AS dest_role_id,      rawdonation.dest_org_id AS dest_org_id            	FROM mondrian_raw_donor_transactions rawdonation     LEFT JOIN etl_activity_sector_primary prim_sect ON prim_sect.act_id = rawdonation.amp_activity_id     LEFT JOIN etl_activity_sector_secondary sec_sect ON sec_sect.act_id = rawdonation.amp_activity_id     LEFT JOIN etl_activity_sector_tertiary tert_sect ON tert_sect.act_id = rawdonation.amp_activity_id          LEFT JOIN etl_activity_program_national_plan_objective npo_prog ON npo_prog.act_id = rawdonation.amp_activity_id     LEFT JOIN etl_activity_program_primary_program prim_prog ON prim_prog.act_id = rawdonation.amp_activity_id     LEFT JOIN etl_activity_program_secondary_program sec_prog ON sec_prog.act_id = rawdonation.amp_activity_id     LEFT JOIN etl_activity_program_tertiary_program tert_prog ON tert_prog.act_id = rawdonation.amp_activity_id      LEFT JOIN etl_locations location ON location.act_id = rawdonation.amp_activity_id      LEFT JOIN etl_executing_agencies ea ON ea.act_id = rawdonation.amp_activity_id     LEFT JOIN etl_beneficiary_agencies ba ON ba.act_id = rawdonation.amp_activity_id     LEFT JOIN etl_implementing_agencies ia ON ia.act_id = rawdonation.amp_activity_id     LEFT JOIN etl_responsible_agencies ra ON ra.act_id = rawdonation.amp_activity_id  order by rawdonation.amp_activity_id

CREATE TABLE "mondrian_fact_table" (
	"entity_type"                    CHAR(1)       NOT NULL,
	"entity_id"                      INTEGER       NOT NULL,
	"entity_internal_id"             INTEGER       NOT NULL,
	"transaction_type"               INTEGER       NOT NULL,
	"adjustment_type"                INTEGER       NOT NULL,
	"transaction_date"               DATE          NOT NULL,
	"date_code"                      INTEGER       NOT NULL,
	"transaction_amount"             DOUBLE        NOT NULL,
	"currency_id"                    INTEGER       NOT NULL,
	"donor_id"                       INTEGER,
	"financing_instrument_id"        INTEGER,
	"terms_of_assistance_id"         INTEGER,
	"primary_sector_id"              INTEGER       NOT NULL,
	"secondary_sector_id"            INTEGER       NOT NULL,
	"tertiary_sector_id"             INTEGER       NOT NULL,
	"location_id"                    INTEGER       NOT NULL,
	"primary_program_id"             INTEGER       NOT NULL,
	"secondary_program_id"           INTEGER       NOT NULL,
	"tertiary_program_id"            INTEGER       NOT NULL,
	"national_objectives_program_id" INTEGER       NOT NULL,
	"ea_org_id"                      INTEGER       NOT NULL,
	"ba_org_id"                      INTEGER       NOT NULL,
	"ia_org_id"                      INTEGER       NOT NULL,
	"ro_org_id"                      INTEGER       NOT NULL,
	"src_role_id"                    INTEGER,
	"dest_role_id"                   INTEGER,
	"dest_org_id"                    INTEGER,
	"exch_rate_1"                    DOUBLE,
	"exch_rate_2"                    DOUBLE,
	"exch_rate_3"                    DOUBLE,
	"exch_rate_4"                    DOUBLE,
	"exch_rate_5"                    DOUBLE,
	"exch_rate_6"                    DOUBLE,
	"exch_rate_7"                    DOUBLE,
	"exch_rate_8"                    DOUBLE,
	"exch_rate_9"                    DOUBLE,
	"exch_rate_10"                   DOUBLE,
	"exch_rate_11"                   DOUBLE,
	"exch_rate_12"                   DOUBLE,
	"exch_rate_13"                   DOUBLE,
	"exch_rate_14"                   DOUBLE,
	"exch_rate_15"                   DOUBLE
);
*/
