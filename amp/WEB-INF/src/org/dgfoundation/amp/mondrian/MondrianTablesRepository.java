package org.dgfoundation.amp.mondrian;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

import org.dgfoundation.amp.ar.viewfetcher.I18nViewColumnDescription;
import org.dgfoundation.amp.ar.viewfetcher.I18nViewDescription;
import org.dgfoundation.amp.ar.viewfetcher.InternationalizedViewsRepository;
import org.dgfoundation.amp.ar.viewfetcher.SimpleColumnValueCalculator;
import org.dgfoundation.amp.mondrian.currencies.CurrencyAmountGroup;
import org.dgfoundation.amp.mondrian.jobs.Fingerprint;
import org.dgfoundation.amp.mondrian.jobs.MondrianTableLogue;
import org.dgfoundation.amp.mondrian.monet.DatabaseTableColumn;
import org.dgfoundation.amp.mondrian.monet.DatabaseTableDescription;
import org.dgfoundation.amp.mondrian.monet.MonetConnection;
import org.dgfoundation.amp.mondrian.monet.OlapDbConnection;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpAgreement;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpComponentType;
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
	public final static String DOUBLE_COLUMN_NAME = MondrianETL.DOUBLE_COLUMN_NAME;
	
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
			new MondrianTableDescription("mondrian_sectors", "amp_sector_id, amp_sec_scheme_id", Arrays.asList("amp_sector_id", "parent_sector_id", "level0_sector_id", "level1_sector_id", "level2_sector_id", "amp_sec_scheme_id"))
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
			new MondrianTableDescription("mondrian_programs", "amp_theme_id, program_setting_id",
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
						.addCalculatedColDef("org_name", new SimpleColumnValueCalculator() {
							@Override protected String calculateValue(ResultSet resultSet) throws SQLException {
								long id = resultSet.getLong("amp_org_id");
								if (id >= MondrianETL.PLEDGE_ID_ADDER && id != MondrianETL.MONDRIAN_DUMMY_ID_FOR_ETL)
									return String.format("%s %s", TranslatorWorker.translateText("Org for group"), resultSet.getString("org_grp_name"));
								return resultSet.getString("org_name_raw");
							}
						})
						.addColumnDef(new I18nViewColumnDescription("org_name_raw", "amp_org_id", AmpOrganisation.class, "name"))
						.addColumnDef(new I18nViewColumnDescription("org_grp_name", "amp_org_grp_id", AmpOrgGroup.class, "orgGrpName"))
						.addColumnDef(new I18nViewColumnDescription("org_type_name", "amp_org_type_id", AmpOrgType.class, "orgType"))
						.addColumnDef(new I18nViewColumnDescription("org_type_code", "amp_org_type_id", AmpOrgType.class, "orgTypeCode"));
					}})
				.withEpilogue(new MondrianTableLogue() {

					@Override public void run(EtlConfiguration etlConfiguration, Connection conn, OlapDbConnection monetConn, LinkedHashSet<String> locales) throws SQLException {
						for(String locale:locales) {
							String viewName = String.format("mondrian_organizations_%s_no_pledges", locale);
							if (!monetConn.tableExists(viewName)) {
								monetConn.executeQuery(String.format("CREATE VIEW %s AS SELECT * FROM mondrian_organizations_%s WHERE amp_org_id < 800000000 OR amp_org_id = " + MondrianETL.MONDRIAN_DUMMY_ID_FOR_ETL, 
										viewName, locale));
							}
						}
					}});
				
	
	public final static MondrianTableDescription MONDRIAN_ACTIVITY_TEXTS = 
			new MondrianTableDescription("mondrian_activity_texts", "amp_activity_id", Arrays.asList("amp_activity_id"))
				/*.withFingerprintedJob(Arrays.asList(
						Fingerprint.buildTableHashingQuery("v_mondrian_activity_texts", "amp_activity_id"),
						Fingerprint.buildTranslationHashingQuery(AmpActivityVersion.class),
						Fingerprint.buildTranslationHashingQuery(AmpTeam.class)
						))*/
				.withInternationalizedColumns(new ObjectSource<I18nViewDescription>() {
					@Override public I18nViewDescription getObject() {
						return new I18nViewDescription("mondrian_activity_texts")
							.addColumnDef(new I18nViewColumnDescription("name", "amp_activity_id", AmpActivityVersion.class, "name"))
							.addColumnDef(new I18nViewColumnDescription("team_name", "team_id", AmpTeam.class, "name"));							
					}})
				.withSupplementalRows(1)
				.withPledgeView("v_mondrian_pledge_texts");

	public final static MondrianTableDescription MONDRIAN_COMPONENTS =
			new MondrianTableDescription("mondrian_components", "amp_component_id", Arrays.asList("amp_component_id", "component_type_id"))
				.withFingerprintedJob(Arrays.asList(
						Fingerprint.buildTableHashingQuery("v_mondrian_components", "amp_component_id"),
						Fingerprint.buildTranslationHashingQuery(AmpComponent.class),
						Fingerprint.buildTranslationHashingQuery(AmpComponentType.class)
					))
				.withInternationalizedColumns(new ObjectSource<I18nViewDescription>() {
					@Override public I18nViewDescription getObject() {
						return new I18nViewDescription("mondrian_components")
							.addColumnDef(new I18nViewColumnDescription("component_name", "amp_component_id", AmpComponent.class, "title"))
							.addColumnDef(new I18nViewColumnDescription("component_description", "amp_component_id", AmpComponent.class, "description"))
							.addColumnDef(new I18nViewColumnDescription("component_type_name", "component_type_id", AmpComponentType.class, "name"));
				}});
	
	public final static MondrianTableDescription MONDRIAN_AGREEMENTS = 
			new MondrianTableDescription("mondrian_agreements", "amp_agreement_id", Arrays.asList("amp_agreement_id"))
				.withFingerprintedJob(Arrays.asList(
						Fingerprint.buildTableHashingQuery("v_mondrian_agreements", "amp_agreement_id"),
						Fingerprint.buildTranslationHashingQuery(AmpAgreement.class)
						))
				.withInternationalizedColumns(new ObjectSource<I18nViewDescription>() {
					@Override public I18nViewDescription getObject() {
						return new I18nViewDescription("mondrian_agreements")
							.addColumnDef(new I18nViewColumnDescription("agreement_title", "amp_agreement_id", AmpAgreement.class, "title"))
							.addCalculatedColDef("agreement_title_code", new InternationalizedViewsRepository.AgreementTitleCodeCalculator("agreement_code", "amp_agreement_id"));
					}});
		
	
	public final static MondrianTableDescription MONDRIAN_ACTIVITY_FIXED_TEXTS = 
			new MondrianTableDescription("mondrian_activity_fixed_texts", "amp_activity_id", Arrays.asList("amp_activity_id"))
				//.withFingerprintedJob(Arrays.asList("SELECT 1"))
				.withPledgeView("v_mondrian_pledge_fixed_texts");

	
	public final static MondrianTableDescription MONDRIAN_ACTIVITY_CURRENCY_NUMBERS = 
			new MondrianTableDescription("mondrian_activity_currency_numbers", "amp_activity_id", Arrays.asList("amp_activity_id"))
				//.withFingerprintedJob(Arrays.asList("SELECT 1"))
			/* no pledges addon */ ;
	
	public final static MondrianTableDescription MONDRIAN_ACTIVITY_TRN_TEXTS =
			new MondrianTableDescription("mondrian_activity_trn_texts", "amp_activity_id", Arrays.asList("amp_activity_id"))
				//.withFingerprintedJob(Arrays.asList(Fingerprint.buildTableHashingQuery("v_mondrian_activity_trn_texts", "amp_activity_id")))
				.withInternationalizedColumns(new ObjectSource<I18nViewDescription>() {
					@Override public I18nViewDescription getObject() {
						return new I18nViewDescription("mondrian_activity_trn_texts")
								.addTrnColDef("governmentapprovalprocedures_text", "governmentapprovalprocedures_id")
								.addTrnColDef("jointcriteria_text", "jointcriteria_id")
								.addTrnColDef("iob_text", "iob_id")
								.addTrnColDef("impl_level_name", "impl_level_id")
								.addTrnColDef("on_off_budget_name", "on_off_budget_id")
								.addTrnColDef("ac_chapter_name", "ac_chapter_id");
					}})
				/* no pledges addon */ ;
	
	public final static MondrianTableDescription MONDRIAN_CATEGORY_VALUES = 
			new MondrianTableDescription("mondrian_category_values", "acv_id", Arrays.asList("acv_id"))
			.withFingerprintedJob(Arrays.asList(Fingerprint.buildTableHashingQuery("v_mondrian_category_values", "acv_id")))
			.withInternationalizedColumns(new ObjectSource<I18nViewDescription>() {
				@Override public I18nViewDescription getObject() {
					return new I18nViewDescription("mondrian_category_values")
						.addTrnColDef("acv_name", "acv_id");
				}
			});
	
	public final static MondrianTableDescription MONDRIAN_HARDCODED_TEXTS = 
			new MondrianTableDescription("mondrian_generic_texts", "id", Arrays.asList("id"))
			.withFingerprintedJob(Arrays.asList(Fingerprint.buildTableHashingQuery("v_mondrian_generic_texts", "id")))
			.withInternationalizedColumns(new ObjectSource<I18nViewDescription>() {
				@Override public I18nViewDescription getObject() {
					return new I18nViewDescription("mondrian_generic_texts")
						.addTrnColDef("val", "id");
				}
			});
	
	public final static MondrianTableDescription MONDRIAN_RAW_DONOR_TRANSACTIONS_TABLE = 
			new MondrianTableDescription("mondrian_raw_donor_transactions", "amp_fund_detail_id", Arrays.asList("amp_activity_id", "date_code"))
				.withPledgeView("v_mondrian_raw_pledge_transactions");
	
	public final static List<MondrianTableDescription> MONDRIAN_DIMENSION_TABLES = Arrays.asList(
			MONDRIAN_LOCATIONS_DIMENSION_TABLE,
			MONDRIAN_SECTORS_DIMENSION_TABLE,
			MONDRIAN_PROGRAMS_DIMENSION_TABLE,
			MONDRIAN_ORGANIZATIONS_DIMENSION_TABLE,
			MONDRIAN_CATEGORY_VALUES,
			MONDRIAN_HARDCODED_TEXTS);
	
	public final static List<MondrianTableDescription> TRN_BACKED_DIMENSIONS = Arrays.asList(
			MONDRIAN_CATEGORY_VALUES, 
			MONDRIAN_ACTIVITY_TRN_TEXTS, 
			MONDRIAN_LOCATIONS_DIMENSION_TABLE,
			MONDRIAN_HARDCODED_TEXTS);
	
	public final static List<MondrianTableDescription> MONDRIAN_ACTIVITY_DIMENSIONS = Arrays.asList(
			MONDRIAN_ACTIVITY_TEXTS,
			MONDRIAN_ACTIVITY_FIXED_TEXTS,
			//MONDRIAN_LONG_TEXTS, 
			MONDRIAN_ACTIVITY_TRN_TEXTS,
			MONDRIAN_ACTIVITY_CURRENCY_NUMBERS);
	
	public final static List<MondrianTableDescription> MONDRIAN_RAW_TRANSACTIONS_TABLES = Arrays.asList(MONDRIAN_RAW_DONOR_TRANSACTIONS_TABLE/*, MONDRIAN_RAW_COMPONENT_TRANSACTIONS_TABLE*/);
	
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
	 * AMP-19721
	 * Transaction type column name in the fact table
	 */
	public static final String TRANSACTION_TYPE = "transaction_type";
		
	public final static boolean INDEX_FACT_TABLE = false;
	
	/**
	 * order of iteration is important, thus LinkedHashSet
	 */
	public final static DatabaseTableDescription FACT_TABLE = new DatabaseTableDescription("mondrian_fact_table", Arrays.asList(
				new DatabaseTableColumn("entity_id", "integer NOT NULL", true), // P/A id 
				new DatabaseTableColumn("funding_id", "integer NOT NULL", INDEX_FACT_TABLE), // P/A id
				new DatabaseTableColumn("entity_internal_id", "integer NOT NULL", INDEX_FACT_TABLE), // amp_funding_detail_id, amp_mtef_detail_id, amp_funding_pledges_detail_id
				new DatabaseTableColumn(TRANSACTION_TYPE, "integer NOT NULL", INDEX_FACT_TABLE), // ACV
				new DatabaseTableColumn("adjustment_type", "integer NOT NULL", INDEX_FACT_TABLE),  // ACV
				new DatabaseTableColumn("transaction_date", "date NOT NULL", INDEX_FACT_TABLE),
				new DatabaseTableColumn("date_code", "integer NOT NULL", INDEX_FACT_TABLE), // for currency reasons
				
				/**
				 * regarding currencies: if a transaction has a fixed_exchange_rate, BASE_CURRENCY would have been written in currency_id and transaction_amount would be translated
				 */
				new DatabaseTableColumn("transaction_amount", DOUBLE_COLUMN_NAME + " NOT NULL", false), // comment
				
				new DatabaseTableColumn("expenditure_class", "integer", INDEX_FACT_TABLE),

				new DatabaseTableColumn("currency_id", "integer NOT NULL", INDEX_FACT_TABLE), // comment 
		
				new DatabaseTableColumn("donor_id", "integer", INDEX_FACT_TABLE), // amp_org_id, might be null for example for pledges (which originate in donor groups)
				new DatabaseTableColumn("financing_instrument_id", "integer", INDEX_FACT_TABLE), // ACV
				new DatabaseTableColumn("terms_of_assistance_id", "integer", INDEX_FACT_TABLE),  // ACV
				new DatabaseTableColumn("funding_status_id", "integer", INDEX_FACT_TABLE),  // ACV
				new DatabaseTableColumn("mode_of_payment_id", "integer", INDEX_FACT_TABLE),  // ACV
				new DatabaseTableColumn("status_id", "integer", INDEX_FACT_TABLE),  // ACV
				new DatabaseTableColumn("modality_id", "integer", INDEX_FACT_TABLE),  // ACV
				new DatabaseTableColumn("type_of_cooperation_id", "integer", INDEX_FACT_TABLE),  // ACV
				new DatabaseTableColumn("type_of_implementation_id", "integer", INDEX_FACT_TABLE),  // ACV
				new DatabaseTableColumn("procurement_system_id", "integer", INDEX_FACT_TABLE),  // ACV
		
				new DatabaseTableColumn("primary_sector_id", "integer NOT NULL", INDEX_FACT_TABLE),   // amp_sector_id, subject to Cartesian product
				new DatabaseTableColumn("secondary_sector_id", "integer NOT NULL", INDEX_FACT_TABLE), // amp_sector_id, subject to Cartesian product
				new DatabaseTableColumn("tertiary_sector_id", "integer NOT NULL", INDEX_FACT_TABLE),  // amp_sector_id, subject to Cartesian product
		
				new DatabaseTableColumn("location_id", "integer NOT NULL", INDEX_FACT_TABLE), // amp_category_value_location_id, subject to Cartesian product
		
				new DatabaseTableColumn("primary_program_id", "integer NOT NULL", INDEX_FACT_TABLE),   // amp_theme_id, subject to Cartesian product
				new DatabaseTableColumn("secondary_program_id", "integer NOT NULL", INDEX_FACT_TABLE), // amp_theme_id, subject to Cartesian product
				new DatabaseTableColumn("tertiary_program_id", "integer NOT NULL", INDEX_FACT_TABLE),  // amp_theme_id, subject to Cartesian product
				new DatabaseTableColumn("national_objectives_program_id", "integer NOT NULL", INDEX_FACT_TABLE),  // amp_theme_id, subject to Cartesian product
		
				new DatabaseTableColumn("ea_org_id", "integer NOT NULL", INDEX_FACT_TABLE), // EXEC amp_org_id, subject to Cartesian product
				new DatabaseTableColumn("ba_org_id", "integer NOT NULL", INDEX_FACT_TABLE), // BENF amp_org_id, subject to Cartesian product
				new DatabaseTableColumn("ia_org_id", "integer NOT NULL", INDEX_FACT_TABLE), // IMPL amp_org_id, subject to Cartesian product
				new DatabaseTableColumn("ro_org_id", "integer NOT NULL", INDEX_FACT_TABLE), // RESP amp_org_id, subject to Cartesian product
				new DatabaseTableColumn("ca_org_id", "integer NOT NULL", INDEX_FACT_TABLE), // contracting agency, subject to Cartesian product
				new DatabaseTableColumn("rg_org_id", "integer NOT NULL", INDEX_FACT_TABLE), // regional group amp_org_id, subject to Cartesian product
				new DatabaseTableColumn("sg_org_id", "integer NOT NULL", INDEX_FACT_TABLE), // sector group amp_org_id, subject to Cartesian product
				
				new DatabaseTableColumn("component_id", "integer NOT NULL", INDEX_FACT_TABLE), // amp_component_id
				new DatabaseTableColumn("agreement_id", "integer NOT NULL", INDEX_FACT_TABLE), // amp_agreement_id
						
				new DatabaseTableColumn("capital_spend_percent", DOUBLE_COLUMN_NAME, false),
				new DatabaseTableColumn("disaster_response", "integer NOT NULL", INDEX_FACT_TABLE), // 1 - yes, 2 - no, UNDEFINED - undefined
								
				new DatabaseTableColumn("src_role", "varchar(10)", false),  // amp_role.role_name
				new DatabaseTableColumn("dest_role", "varchar(10)", false), // amp_role.role_name
				new DatabaseTableColumn("dest_org_id", "integer", INDEX_FACT_TABLE),   // amp_org_id
				new DatabaseTableColumn("flow_name", "varchar(25)", false),
				
				new DatabaseTableColumn("related_entity_id", "integer", INDEX_FACT_TABLE) // mondrian_activity_texts id
		));
	
	public final static List<CurrencyAmountGroup> CURRENCY_GROUPS = Arrays.asList(
			MONDRIAN_ACTIVITY_CURRENCY_NUMBERS.getCurrencyBlock("ppc_"),
			MONDRIAN_ACTIVITY_CURRENCY_NUMBERS.getCurrencyBlock("rpc_"),
			new CurrencyAmountGroup(MONDRIAN_RAW_DONOR_TRANSACTIONS_TABLE.tableName, FACT_TABLE.tableName, "amp_activity_id", "entity_id", ""));
}

