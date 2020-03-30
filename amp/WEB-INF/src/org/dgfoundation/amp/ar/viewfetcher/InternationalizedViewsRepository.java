package org.dgfoundation.amp.ar.viewfetcher;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpActor;
import org.digijava.module.aim.dbentity.AmpAgreement;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpComponentType;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpLineMinistryObservation;
import org.digijava.module.aim.dbentity.AmpLineMinistryObservationActor;
import org.digijava.module.aim.dbentity.AmpLineMinistryObservationMeasure;
import org.digijava.module.aim.dbentity.AmpMeasure;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpRegionalObservation;
import org.digijava.module.aim.dbentity.AmpRegionalObservationActor;
import org.digijava.module.aim.dbentity.AmpRegionalObservationMeasure;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpStructure;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTheme;

/**
 * repository class holding the configuration for all the internationalized views' i18n columns
 * @author Dolghier Constantin
 *
 */
public class InternationalizedViewsRepository {
    public final static String AGREEMENT_TITLE_AND_CODE = "Agreement Title + Code";
    
    public final static ColumnValueCalculator agreement_title_code_calculator = new AgreementTitleCodeCalculator("Agreement Code", null);
    
    /**
     * class which computes the title_code of an agreement
     * @author simple
     *
     */
    public static class AgreementTitleCodeCalculator extends SimpleColumnValueCalculator {
        
        public final String agreementCodeColumnName;
        public final String nullColumnName;
        
        public AgreementTitleCodeCalculator(String agrCodeColName, String nllColumnName) {
            agreementCodeColumnName = agrCodeColName;
            nullColumnName = nllColumnName;
        }
        
        @Override public String calculateValue(ResultSet resultSet) throws SQLException{
            if (nullColumnName != null) {
                Long id = resultSet.getLong(nullColumnName);
                if (id == null || id.longValue() == 0 || id.longValue() == 999999999)
                    return null;
            }
            return sqlconcat(resultSet.getString("agreement_title"), " - ", resultSet.getString(agreementCodeColumnName));
        }
    }
    
    public final static ColumnValueCalculator budget_sector_calculator = new SimpleColumnValueCalculator() {
        
        @Override
        public String calculateValue(ResultSet resultSet) throws SQLException{
            //(org.name::text || ' - '::text) || org.budget_org_code::text
            return sqlconcat(resultSet.getString("orgname"), " - ", resultSet.getString("budget_org_code"));
        }
    };  

    public final static ColumnValueCalculator budget_program_calculator = new SimpleColumnValueCalculator() {
        
        @Override
        public String calculateValue(ResultSet resultSet) throws SQLException{
            // prog.theme_code::text || ' - '::text) || prog.name::text
            return sqlconcat(resultSet.getString("theme_code"), " - ", resultSet.getString("progname"));
        }
    };
    
    public final static ColumnValueCalculator organization_projectid_calculator = new SimpleColumnValueCalculator() {
        
        @Override
        public String calculateValue(ResultSet resultSet) throws SQLException{
            // (org.name::text || ' -- '::text) || aaii.internal_id::text
            return sqlconcat(resultSet.getString("orgname"), " - ", resultSet.getString("internal_id"));
        }
    };      

    public final static ColumnValueCalculator projectid_calculator = new SimpleColumnValueCalculator() {
        
        @Override
        public String calculateValue(ResultSet resultSet) throws SQLException{
            // (org.name::text || ' -- '::text) || aaii.internal_id::text
            return sqlconcat(resultSet.getString("internal_id"), " (", resultSet.getString("orgname"), ")");
        }
    };
    
    /**
     * concatenates all of the input String's. If <b>any</b> of them is null, returns null
     * @param inputs
     * @return
     */
    public final static String sqlconcat(String... inputs)
    {
        StringBuilder bld = new StringBuilder();
        for(String str:inputs)
        {
            if (str == null)
                return null;
            bld.append(str);
        }
        return bld.toString();
    }

    public final static Map<String, I18nViewDescription> i18Models =  // Map<view_name, view_description>
            Collections.<String, I18nViewDescription>unmodifiableMap(
            new HashMap<String, I18nViewDescription>()
            {{
                addSimpleTranslatedView(this, "amp_category_value", "category_value", "id");
                
                addViewDef(this, new I18nViewDescription("amp_team").
                                addColumnDef(new I18nViewColumnDescription("name", "amp_team_id", AmpTeam.class, "name")));
                
                addViewDef(this, new I18nViewDescription("amp_organisation").
                                addColumnDef(new I18nViewColumnDescription("description", "amp_org_id", AmpOrganisation.class, "description")).
                                addColumnDef(new I18nViewColumnDescription("name", "amp_org_id", AmpOrganisation.class, "name")));

                addViewDef(this, new I18nViewDescription("amp_theme").
                        addColumnDef(new I18nViewColumnDescription("description", "amp_theme_id", AmpTheme.class, "description")).
                        addColumnDef(new I18nViewColumnDescription("name", "amp_theme_id", AmpTheme.class, "name")));
                
                addViewDef(this, new I18nViewDescription("amp_activity").
                        addColumnDef(new I18nViewColumnDescription("name", "amp_activity_id", AmpActivityVersion.class, "name")));
                addViewDef(this, new I18nViewDescription("amp_category_value_location").
                        addColumnDef(new I18nViewColumnDescription("location_name", "id", AmpCategoryValueLocations.class, "name")));


                addViewDef(this, new I18nViewDescription("amp_org_group").
                                addColumnDef(new I18nViewColumnDescription("org_grp_name", "amp_org_grp_id", AmpOrgGroup.class, "orgGrpName")));
        
                addViewDef(this, new I18nViewDescription("amp_org_type").
                        addColumnDef(new I18nViewColumnDescription("org_type", "amp_org_type_id", AmpOrgType.class, "orgType")));               
                
                addViewDef(this, new I18nViewDescription("amp_sector").
                        addColumnDef(new I18nViewColumnDescription("name", "amp_sector_id", AmpSector.class, "name")));             
                                                
                addViewDef(this, new I18nViewDescription("v_actors").
                        addColumnDef(new I18nViewColumnDescription("name", "amp_actor_id", AmpActor.class, "name")));
    
                addViewDef(this, new I18nViewDescription("v_regional_observations_actors").
                        addColumnDef(new I18nViewColumnDescription("name", "amp_reg_obs_actor_id",
                                AmpRegionalObservationActor.class, "name")));
    
                addViewDef(this, new I18nViewDescription("v_agreement_close_date").
                        addColumnDef(new I18nViewColumnDescription("agreement_title", "id", AmpAgreement.class, "title")).
                        addCalculatedColDef(AGREEMENT_TITLE_AND_CODE, agreement_title_code_calculator).
                        addColumnDef(new I18nViewColumnDescription("Donor Agency", "amp_org_id", AmpOrganisation.class, "name")));

                addViewDef(this, new I18nViewDescription("v_agreement_code").
                        addColumnDef(new I18nViewColumnDescription("agreement_title", "id", AmpAgreement.class, "title")).
                        addCalculatedColDef(AGREEMENT_TITLE_AND_CODE, agreement_title_code_calculator).
                        addColumnDef(new I18nViewColumnDescription("Donor Agency", "amp_org_id", AmpOrganisation.class, "name")));
                
                addViewDef(this, new I18nViewDescription("v_agreement_effective_date").
                        addColumnDef(new I18nViewColumnDescription("agreement_title", "id", AmpAgreement.class, "title")).
                        addCalculatedColDef(AGREEMENT_TITLE_AND_CODE, agreement_title_code_calculator).
                        addColumnDef(new I18nViewColumnDescription("Donor Agency", "amp_org_id", AmpOrganisation.class, "name")));          

                addViewDef(this, new I18nViewDescription("v_agreement_signature_date").
                        addColumnDef(new I18nViewColumnDescription("agreement_title", "id", AmpAgreement.class, "title")).
                        addCalculatedColDef(AGREEMENT_TITLE_AND_CODE, agreement_title_code_calculator).
                        addColumnDef(new I18nViewColumnDescription("Donor Agency", "amp_org_id", AmpOrganisation.class, "name")));          

                addViewDef(this, new I18nViewDescription("v_agreement_title_code").
                        addColumnDef(new I18nViewColumnDescription("agreement_title", "id", AmpAgreement.class, "title")).
                        addCalculatedColDef(AGREEMENT_TITLE_AND_CODE, agreement_title_code_calculator).
                        addColumnDef(new I18nViewColumnDescription("Donor Agency", "amp_org_id", AmpOrganisation.class, "name")));          


                addViewDef(this, new I18nViewDescription("v_budget_organization").
                        addCalculatedColDef("budget_sector", budget_sector_calculator).
                        addColumnDef(new I18nViewColumnDescription("orgname", "amp_org_id", AmpOrganisation.class, "name")));

                addViewDef(this, new I18nViewDescription("v_budget_program").
                        addCalculatedColDef("budget_program", budget_program_calculator).
                        addColumnDef(new I18nViewColumnDescription("progname", "amp_theme_id", AmpTheme.class, "name")));

                addViewDef(this, new I18nViewDescription("v_organization_projectid").
                        addCalculatedColDef("name", organization_projectid_calculator).
                        addColumnDef(new I18nViewColumnDescription("orgname", "amp_org_id", AmpOrganisation.class, "name")));

                addViewDef(this, new I18nViewDescription("v_project_id").
                        addCalculatedColDef("proj_id", projectid_calculator).
                        addColumnDef(new I18nViewColumnDescription("orgname", "amp_org_id", AmpOrganisation.class, "name")));

                
                // QUESTION: AmpComponent should not be translateable? If it should - v_component_* should be added to the conf, too
                // backlog: v_organization_projectid does concatenation with orgname, but not used in reports
                // backlog: v_project_id does concat with orgname
                
                addViewDef(this, new I18nViewDescription("v_amp_theme").
                        addColumnDef(new I18nViewColumnDescription("value", "id", AmpTheme.class, "name")));

                addViewDef(this, new I18nViewDescription("v_beneficiary_agency").
                        addColumnDef(new I18nViewColumnDescription("org_name", "org_id", AmpOrganisation.class, "name")));

                addViewDef(this, new I18nViewDescription("v_beneficiary_agency_groups").
                        addColumnDef(new I18nViewColumnDescription("org_grp_name", "org_grp_id", AmpOrgGroup.class, "orgGrpName")));
    
                addViewDef(this, new I18nViewDescription("v_beneficiary_agency_type").
                        addColumnDef(new I18nViewColumnDescription("org_type_name", "org_type_id",
                                AmpOrgType.class, "orgType")));

                addViewDef(this, new I18nViewDescription("v_beneficiary_agency_country").
                        addColumnDef(new I18nViewColumnDescription("org_grp_country_name", "org_grp_country_id",
                                AmpCategoryValueLocations.class, "name")));

                addViewDef(this, new I18nViewDescription("v_component_funding_organization_name").
                        addColumnDef(new I18nViewColumnDescription("org_name", "org_id", AmpOrganisation.class, "name")));

                addViewDef(this, new I18nViewDescription("v_contracting_agency").
                        addColumnDef(new I18nViewColumnDescription("org_name", "org_id", AmpOrganisation.class, "name")));

                addViewDef(this, new I18nViewDescription("v_contracting_agency_groups").
                        addColumnDef(new I18nViewColumnDescription("org_grp_name", "org_grp_id", AmpOrgGroup.class, "orgGrpName")));
    
                addViewDef(this, new I18nViewDescription("v_contracting_agency_type").
                        addColumnDef(new I18nViewColumnDescription("org_type_name", "org_type_id",
                                AmpOrgType.class, "orgType")));

                addViewDef(this, new I18nViewDescription("v_contribution_funding").
                        addColumnDef(new I18nViewColumnDescription("donor_name", "amp_org_id", AmpOrganisation.class, "name")).
                        addTrnColDef("terms_assist_name", "terms_assist_id").
                        addTrnColDef("financing_instrument_name", "financing_instrument_id"));

                addViewDef(this, new I18nViewDescription("v_costing_donors").
                        addColumnDef(new I18nViewColumnDescription("name", "donor_id", AmpOrganisation.class, "name")));

                addViewDef(this, new I18nViewDescription("v_adm_level_0").
                        addColumnDef(new I18nViewColumnDescription("adm_level_0_name", "adm_level_0_id",
                                AmpCategoryValueLocations.class, "name")));
    
                addViewDef(this, new I18nViewDescription("v_adm_level_1").
                        addColumnDef(new I18nViewColumnDescription("adm_level_1_name", "adm_level_1_id",
                                AmpCategoryValueLocations.class, "name")));

                addViewDef(this, new I18nViewDescription("v_adm_level_2").
                        addColumnDef(new I18nViewColumnDescription("adm_level_2_name", "adm_level_2_id",
                                AmpCategoryValueLocations.class, "name")));
    
                addViewDef(this, new I18nViewDescription("v_adm_level_3").
                        addColumnDef(new I18nViewColumnDescription("adm_level_3_name", "adm_level_3_id",
                                AmpCategoryValueLocations.class, "name")));
    
                addViewDef(this, new I18nViewDescription("v_adm_level_4").
                        addColumnDef(new I18nViewColumnDescription("adm_level_4_name", "adm_level_4_id",
                                AmpCategoryValueLocations.class, "name")));
                
                addViewDef(this, new I18nViewDescription("v_donor_cont_org").
                        addColumnDef(new I18nViewColumnDescription("org", "amp_org_id", AmpOrganisation.class, "name")));

                addViewDef(this, new I18nViewDescription("v_donor_funding").
                        addColumnDef(new I18nViewColumnDescription("donor_name", "org_id", AmpOrganisation.class, "name")).
                        addColumnDef(new I18nViewColumnDescription("org_grp_name", "org_grp_id", AmpOrgGroup.class, "orgGrpName")).
                        addColumnDef(new I18nViewColumnDescription("donor_type_name", "org_type_id", AmpOrgType.class, "orgType")).
                        addColumnDef(new I18nViewColumnDescription("recipient_name", "recipient_id", AmpOrganisation.class, "name")).
                        addTrnColDef("terms_assist_name", "terms_assist_id").
                        addTrnColDef("financing_instrument_name", "financing_instrument_id").
                        addTrnColDef("mode_of_payment_name", "mode_of_payment_id").
                        addTrnColDef("funding_status_name", "funding_status_id").
                        addTrnColDef("expenditure_class_name", "expenditure_class_id"));

                addViewDef(this, new I18nViewDescription("v_donor_groups").
                        addColumnDef(new I18nViewColumnDescription("org_grp_name", "org_grp_id", AmpOrgGroup.class, "orgGrpName")));
                
                addViewDef(this, new I18nViewDescription("v_donor_type").
                        addColumnDef(new I18nViewColumnDescription("org_type_name", "org_type_id", AmpOrgType.class, "orgType")));
                
                addViewDef(this, new I18nViewDescription("v_donors").
                        addColumnDef(new I18nViewColumnDescription("name", "amp_donor_org_id", AmpOrganisation.class, "name")));

                addViewDef(this, new I18nViewDescription("v_donor_country").
                        addColumnDef(new I18nViewColumnDescription("donor_org_country_name", "donor_org_country_id",
                                AmpCategoryValueLocations.class, "name")));
                
                addViewDef(this, new I18nViewDescription("v_executing_agency").
                        addColumnDef(new I18nViewColumnDescription("org_name", "org_id", AmpOrganisation.class, "name")));
                
                addViewDef(this, new I18nViewDescription("v_executing_agency_groups").
                        addColumnDef(new I18nViewColumnDescription("org_grp_name", "org_grp_id", AmpOrgGroup.class, "orgGrpName")));

                addViewDef(this, new I18nViewDescription("v_executing_agency_type").
                        addColumnDef(new I18nViewColumnDescription("org_type_name", "org_type_id", AmpOrgType.class, "orgType")));

                addViewDef(this, new I18nViewDescription("v_executing_agency_country").
                        addColumnDef(new I18nViewColumnDescription("org_grp_country_name", "org_grp_country_id",
                                AmpCategoryValueLocations.class, "name")));
                

                addViewDef(this, new I18nViewDescription("v_expenditure_class").
                        addTrnColDef("name", "id"));

                addSimpleTranslatedView(this, "v_financial_instrument", "category_value", "id");
                addSimpleTranslatedView(this, "v_financing_instrument", "category_value", "id");
                addSimpleTranslatedView(this, "v_humanitarian_aid", "val", "val_id");
                addSimpleTranslatedView(this, "v_disaster_response_marker", "val", "val_id");
                addSimpleTranslatedView(this, "v_funding_status", "funding_status_name", "funding_status_code");
                addSimpleTranslatedView(this, "v_implementation_level", "name", "level_code");
                addSimpleTranslatedView(this, "v_ac_chapters", "name", "val_id");
                addSimpleTranslatedView(this, "v_performance_alert_level", "name", "level_code");

                addViewDef(this, new I18nViewDescription("v_raw_locations").
                        addColumnDef(new I18nViewColumnDescription("location_name", "id", AmpCategoryValueLocations
                                .class, "name")));

                addViewDef(this, new I18nViewDescription("v_implementing_agency").
                        addColumnDef(new I18nViewColumnDescription("org_name", "org_id", AmpOrganisation.class, "name")));
                
                addViewDef(this, new I18nViewDescription("v_implementing_agency_groups").
                        addColumnDef(new I18nViewColumnDescription("org_grp_name", "org_grp_id", AmpOrgGroup.class, "orgGrpName")));
                
                addViewDef(this, new I18nViewDescription("v_implementing_agency_type").
                        addColumnDef(new I18nViewColumnDescription("org_type_name", "org_type_id", AmpOrgType.class, "orgType")));

                addViewDef(this, new I18nViewDescription("v_indicator_description").
                        addColumnDef(new I18nViewColumnDescription("name", "me_indicator_id", AmpIndicator.class, "description")));

                addViewDef(this, new I18nViewDescription("v_indicator_name").
                        addColumnDef(new I18nViewColumnDescription("name", "me_indicator_id", AmpIndicator.class, "name")));

                addSimpleTranslatedView(this, "v_indicator_risk", "risk", "id");
                addSimpleTranslatedView(this, "v_indicator_type", "value", "id");
                addSimpleTranslatedView(this, "v_indicator_logframe_category", "logframe_category", "id");

                addViewDef(this, new I18nViewDescription("v_indicator_sectors").
                        addColumnDef(new I18nViewColumnDescription("name", "amp_sector_id", AmpSector.class, "name")));

                addSimpleTranslatedView(this, "v_indirect_on_budget", "iob_text", "iob_id");
                addSimpleTranslatedView(this, "v_institutions", "name", "id");
                
                addViewDef(this, new I18nViewDescription("v_line_ministry_observations").
                        addColumnDef(new I18nViewColumnDescription("name", "amp_line_ministry_observation_id",
                                AmpLineMinistryObservation.class, "name")));
    
                addViewDef(this, new I18nViewDescription("v_line_ministry_observations_actors").
                        addColumnDef(new I18nViewColumnDescription("name", "amp_line_ministry_obs_actor_id",
                                AmpLineMinistryObservationActor.class, "name")));
    
                addViewDef(this, new I18nViewDescription("v_line_ministry_observations_measures").
                        addColumnDef(new I18nViewColumnDescription("name", "amp_line_ministry_obs_measure_id",
                                AmpLineMinistryObservationMeasure.class, "name")));

                addSimpleTranslatedView(this, "v_modalities", "name", "level_code");
                addSimpleTranslatedView(this, "v_mode_of_payment", "mode_of_payment_name", "mode_of_payment_code");
                addSimpleTranslatedView(this, "v_multi_donor", "value", "id");

                addViewDef(this, new I18nViewDescription("v_measures_taken").
                        addColumnDef(new I18nViewColumnDescription("name", "amp_measure_id", AmpMeasure.class, "name")));
    
                addViewDef(this, new I18nViewDescription("v_regional_observations_measures").
                        addColumnDef(new I18nViewColumnDescription("name", "amp_reg_obs_measure_id",
                                AmpRegionalObservationMeasure.class, "name")));
    
                addViewDef(this, new I18nViewDescription("v_mtef_funding").
                        addColumnDef(new I18nViewColumnDescription("donor_name", "org_id", AmpOrganisation.class, "name")).
                        addColumnDef(new I18nViewColumnDescription("org_grp_name", "org_grp_id", AmpOrgGroup.class, "orgGrpName")).
                        addColumnDef(new I18nViewColumnDescription("donor_type_name", "org_type_id", AmpOrgType.class, "orgType")).
                        addTrnColDef("terms_assist_name", "terms_assist_id").
                        addTrnColDef("financing_instrument_name", "financing_instrument_id"));
                                
                addSimpleTranslatedView(this, "v_activity_budget", "budget", "budget_id");
                
                addSimpleTranslatedView(this, "v_pledges_aid_modality", "name", "amp_modality_id");
                
                addViewDef(this, new I18nViewDescription("v_pledges_donor").
                        addColumnDef(new I18nViewColumnDescription("name", "amp_donor_org_id", AmpOrganisation.class, "name")));
                
                addViewDef(this, new I18nViewDescription("v_pledges_donor_group").
                        addColumnDef(new I18nViewColumnDescription("org_grp_name", "org_grp_id", AmpOrgGroup.class, "orgGrpName")));

                addViewDef(this, new I18nViewDescription("v_pledges_donor_type").
                        addColumnDef(new I18nViewColumnDescription("org_type_name", "org_type_id", AmpOrgType.class, "orgType")));
                
                addViewDef(this, new I18nViewDescription("v_pledges_funding_st").
                        addColumnDef(new I18nViewColumnDescription("org_grp_name", "amp_org_grp_id", AmpOrgGroup.class, "orgGrpName")).
                        addColumnDef(new I18nViewColumnDescription("donor_type_name", "org_type_id", AmpOrgType.class, "orgType")).
                        addColumnDef(new I18nViewColumnDescription("related_project", "related_project_id", AmpActivityVersion.class, "name")).
                        addTrnColDef("terms_assist_name", "terms_assist_id").
                        addTrnColDef("financing_instrument_name", "financing_instrument_id"));

                addViewDef(this, new I18nViewDescription("v_ni_pledges_projects").
                        addColumnDef(new I18nViewColumnDescription("title", "related_project_id", AmpActivityVersion.class, "name")));

                addViewDef(this, new I18nViewDescription("v_pledges_programs").
                        addColumnDef(new I18nViewColumnDescription("name", "amp_program_id", AmpTheme.class, "name")));
    
                addViewDef(this, new I18nViewDescription("v_pledges_programs_level_0").
                        addColumnDef(new I18nViewColumnDescription("name", "amp_program_id", AmpTheme.class, "name")));
    
                addViewDef(this, new I18nViewDescription("v_pledges_programs_level_1").
                        addColumnDef(new I18nViewColumnDescription("name", "amp_program_id", AmpTheme.class, "name")));

                addViewDef(this, new I18nViewDescription("v_pledges_programs_level_2").
                        addColumnDef(new I18nViewColumnDescription("name", "amp_program_id", AmpTheme.class, "name")));

                addViewDef(this, new I18nViewDescription("v_pledges_programs_level_3").
                        addColumnDef(new I18nViewColumnDescription("name", "amp_program_id", AmpTheme.class, "name")));

                addViewDef(this, new I18nViewDescription("v_pledges_secondary_programs").
                        addColumnDef(new I18nViewColumnDescription("name", "amp_program_id", AmpTheme.class, "name")));
    
                addViewDef(this, new I18nViewDescription("v_pledges_secondary_programs_level_0").
                        addColumnDef(new I18nViewColumnDescription("name", "amp_program_id", AmpTheme.class, "name")));
    
                addViewDef(this, new I18nViewDescription("v_pledges_secondary_programs_level_1").
                        addColumnDef(new I18nViewColumnDescription("name", "amp_program_id", AmpTheme.class, "name")));

                addViewDef(this, new I18nViewDescription("v_pledges_secondary_programs_level_2").
                        addColumnDef(new I18nViewColumnDescription("name", "amp_program_id", AmpTheme.class, "name")));

                addViewDef(this, new I18nViewDescription("v_pledges_secondary_programs_level_3").
                        addColumnDef(new I18nViewColumnDescription("name", "amp_program_id", AmpTheme.class, "name")));

                addSimpleTranslatedView(this, "v_pledges_status", "name", "amp_status_id");
                
                addViewDef(this, new I18nViewDescription("v_pledges_tertiary_programs").
                        addColumnDef(new I18nViewColumnDescription("name", "amp_program_id", AmpTheme.class, "name")));
    
                addViewDef(this, new I18nViewDescription("v_pledges_tertiary_programs_level_0").
                        addColumnDef(new I18nViewColumnDescription("name", "amp_program_id", AmpTheme.class, "name")));
    
                addViewDef(this, new I18nViewDescription("v_pledges_tertiary_programs_level_1").
                        addColumnDef(new I18nViewColumnDescription("name", "amp_program_id", AmpTheme.class, "name")));

                addViewDef(this, new I18nViewDescription("v_pledges_tertiary_programs_level_2").
                        addColumnDef(new I18nViewColumnDescription("name", "amp_program_id", AmpTheme.class, "name")));

                addViewDef(this, new I18nViewDescription("v_pledges_tertiary_programs_level_3").
                        addColumnDef(new I18nViewColumnDescription("name", "amp_program_id", AmpTheme.class, "name")));

                addSimpleTranslatedView(this, "v_pledges_type_of_assistance", "category_value", "id");
                
                addViewDef(this, new I18nViewDescription("v_pledges_npd_objectives").
                        addColumnDef(new I18nViewColumnDescription("name", "amp_program_id", AmpTheme.class, "name")));
    
                addViewDef(this, new I18nViewDescription("v_pledges_npd_objectives_level_0").
                        addColumnDef(new I18nViewColumnDescription("name", "amp_program_id", AmpTheme.class, "name")));
    
                addViewDef(this, new I18nViewDescription("v_pledges_npd_objectives_level_1").
                        addColumnDef(new I18nViewColumnDescription("name", "amp_program_id", AmpTheme.class, "name")));

                addViewDef(this, new I18nViewDescription("v_pledges_npd_objectives_level_2").
                        addColumnDef(new I18nViewColumnDescription("name", "amp_program_id", AmpTheme.class, "name")));

                addViewDef(this, new I18nViewDescription("v_pledges_npd_objectives_level_3").
                        addColumnDef(new I18nViewColumnDescription("name", "amp_program_id", AmpTheme.class, "name")));

                addViewDef(this, new I18nViewDescription("v_pledges_projects").
                        addColumnDef(new I18nViewColumnDescription("title", "amp_activity_id", AmpActivityVersion.class, "name")));
                

                addViewDef(this, new I18nViewDescription("v_pledges_adm_level_0").
                        addColumnDef(new I18nViewColumnDescription("adm_level_0_name", "adm_level_0_id",
                                AmpCategoryValueLocations.class, "name")));

                addViewDef(this, new I18nViewDescription("v_pledges_adm_level_1").
                        addColumnDef(new I18nViewColumnDescription("adm_level_1_name", "adm_level_1_id",
                                AmpCategoryValueLocations.class, "name")));
                
                addViewDef(this, new I18nViewDescription("v_pledges_adm_level_2").
                        addColumnDef(new I18nViewColumnDescription("adm_level_2_name", "adm_level_2_id",
                                AmpCategoryValueLocations.class, "name")));
                
                addViewDef(this, new I18nViewDescription("v_pledges_adm_level_3").
                        addColumnDef(new I18nViewColumnDescription("adm_level_3_name", "adm_level_3_id",
                                AmpCategoryValueLocations.class, "name")));
                
                addViewDef(this, new I18nViewDescription("v_pledges_adm_level_4").
                        addColumnDef(new I18nViewColumnDescription("adm_level_4_name", "adm_level_4_id",
                                AmpCategoryValueLocations.class, "name")));
    
                addViewDef(this, new I18nViewDescription("v_pledges_sectors").
                        addColumnDef(new I18nViewColumnDescription("sectorname", "amp_sector_id", AmpSector.class, "name")));
                addViewDef(this, new I18nViewDescription("v_pledges_sectors_subsectors").
                        addColumnDef(new I18nViewColumnDescription("sectorname", "amp_sector_id", AmpSector.class, "name")));
                addViewDef(this, new I18nViewDescription("v_pledges_sectors_subsubsectors").
                        addColumnDef(new I18nViewColumnDescription("sectorname", "amp_sector_id", AmpSector.class, "name")));

                addViewDef(this, new I18nViewDescription("v_pledges_secondary_sectors").
                        addColumnDef(new I18nViewColumnDescription("sectorname", "amp_sector_id", AmpSector.class, "name")));
                addViewDef(this, new I18nViewDescription("v_pledges_secondary_sectors_subsectors").
                        addColumnDef(new I18nViewColumnDescription("sectorname", "amp_sector_id", AmpSector.class, "name")));
                addViewDef(this, new I18nViewDescription("v_pledges_secondary_sectors_subsubsectors").
                        addColumnDef(new I18nViewColumnDescription("sectorname", "amp_sector_id", AmpSector.class, "name")));

                addViewDef(this, new I18nViewDescription("v_pledges_tertiary_sectors").
                        addColumnDef(new I18nViewColumnDescription("sectorname", "amp_sector_id", AmpSector.class, "name")));
                addViewDef(this, new I18nViewDescription("v_pledges_tertiary_sectors_subsectors").
                        addColumnDef(new I18nViewColumnDescription("sectorname", "amp_sector_id", AmpSector.class, "name")));
                addViewDef(this, new I18nViewDescription("v_pledges_tertiary_sectors_subsubsectors").
                        addColumnDef(new I18nViewColumnDescription("sectorname", "amp_sector_id", AmpSector.class, "name")));
                                
                addSimpleTranslatedView(this, "v_procurement_system", "name", "id");
                                
                addViewDef(this, new I18nViewDescription("v_regional_funding").
                        addColumnDef(new I18nViewColumnDescription("region_name", "region_id", AmpCategoryValueLocations.class, "name")));

                addViewDef(this, new I18nViewDescription("v_regional_group").
                        addColumnDef(new I18nViewColumnDescription("org_name", "org_id", AmpOrganisation.class, "name")));

                addViewDef(this, new I18nViewDescription("v_regional_observations").
                        addColumnDef(new I18nViewColumnDescription("name", "amp_regional_observation_id", AmpRegionalObservation.class, "name")));

                addViewDef(this, new I18nViewDescription("v_responsible_org_groups").
                        addColumnDef(new I18nViewColumnDescription("org_grp_name", "org_grp_id", AmpOrgGroup.class, "orgGrpName")));

                addViewDef(this, new I18nViewDescription("v_responsible_organisation").
                        addColumnDef(new I18nViewColumnDescription("org_name", "org_id", AmpOrganisation.class, "name")));
    
                addViewDef(this, new I18nViewDescription("v_responsible_org_type").
                        addColumnDef(new I18nViewColumnDescription("org_type_name", "org_type_id",
                                AmpOrgType.class, "orgType")));

                addViewDef(this, new I18nViewDescription("v_secondary_sectors").
                        addColumnDef(new I18nViewColumnDescription("name", "amp_sector_id", AmpSector.class, "name")));

                addViewDef(this, new I18nViewDescription("v_secondary_sub_sectors").
                        addColumnDef(new I18nViewColumnDescription("name", "amp_sector_id", AmpSector.class, "name")));
                        //.addColumnDef(new I18nViewColumnDescription("sec_scheme_name", "amp_sec_scheme_id", AmpSectorScheme.class, "secSchemeName")));

                addViewDef(this, new I18nViewDescription("v_secondary_sub_sub_sectors").
                        addColumnDef(new I18nViewColumnDescription("name", "amp_sector_id", AmpSector.class, "name")));
                
                addViewDef(this, new I18nViewDescription("v_sect_min_cont_org").
                        addColumnDef(new I18nViewColumnDescription("org", "amp_org_id", AmpOrganisation.class, "name")));

                addViewDef(this, new I18nViewDescription("v_sector_group").
                        addColumnDef(new I18nViewColumnDescription("org_name", "org_id", AmpOrganisation.class, "name")));

                addViewDef(this, new I18nViewDescription("v_sectors").
                        addColumnDef(new I18nViewColumnDescription("name", "amp_sector_id", AmpSector.class, "name")));
                        //.addColumnDef(new I18nViewColumnDescription("sec_scheme_name", "amp_sector_scheme_id", AmpSectorScheme.class, "secSchemeName")));

                addSimpleTranslatedView(this, "v_status", "name", "amp_status_id");
                
                addViewDef(this, new I18nViewDescription("v_structures").
                        addColumnDef(new I18nViewColumnDescription("title", "amp_structure_id", AmpStructure.class, "title")));

                addViewDef(this, new I18nViewDescription("v_sub_sectors").
                        addColumnDef(new I18nViewColumnDescription("name", "amp_sector_id", AmpSector.class, "name")));
                        //.addColumnDef(new I18nViewColumnDescription("sec_scheme_name", "amp_sec_scheme_id", AmpSectorScheme.class, "secSchemeName")));

                addViewDef(this, new I18nViewDescription("v_sub_sub_sectors").
                        addColumnDef(new I18nViewColumnDescription("name", "amp_sector_id", AmpSector.class, "name")));

                addViewDef(this, new I18nViewDescription("v_tag_sectors").
                        addColumnDef(new I18nViewColumnDescription("name", "amp_sector_id", AmpSector.class, "name")));

                addViewDef(this, new I18nViewDescription("v_tag_sub_sectors").
                        addColumnDef(new I18nViewColumnDescription("name", "amp_sector_id", AmpSector.class, "name")));
                        //.addColumnDef(new I18nViewColumnDescription("sec_scheme_name", "amp_sec_scheme_id", AmpSectorScheme.class, "secSchemeName")));

                addViewDef(this, new I18nViewDescription("v_tag_sub_sub_sectors").
                        addColumnDef(new I18nViewColumnDescription("name", "amp_sector_id", AmpSector.class, "name")));

                addViewDef(this, new I18nViewDescription("v_teams").
                        addColumnDef(new I18nViewColumnDescription("name", "amp_team_id", AmpTeam.class, "name")));

                addSimpleTranslatedView(this, "v_terms_assist", "terms_assist_name", "terms_assist_code");
                                
                addViewDef(this, new I18nViewDescription("v_tertiary_sectors").
                        addColumnDef(new I18nViewColumnDescription("name", "amp_sector_id", AmpSector.class, "name")));

                addViewDef(this, new I18nViewDescription("v_tertiary_sub_sectors").
                        addColumnDef(new I18nViewColumnDescription("name", "amp_sector_id", AmpSector.class, "name")));
                        //.addColumnDef(new I18nViewColumnDescription("sec_scheme_name", "amp_sec_scheme_id", AmpSectorScheme.class, "secSchemeName")));

                addViewDef(this, new I18nViewDescription("v_tertiary_sub_sub_sectors").
                        addColumnDef(new I18nViewColumnDescription("name", "amp_sector_id", AmpSector.class, "name")));
                
                addViewDef(this, new I18nViewDescription("v_titles").
                        addColumnDef(new I18nViewColumnDescription("name", "amp_activity_id", AmpActivityVersion.class, "name"))); // if changing this line: ESRI GIS DataDispatcher references it, change it there too! 

                addViewDef(this, new I18nViewDescription("v_components").
                        addColumnDef(new I18nViewColumnDescription("title", "amp_component_id", AmpComponent.class, "title")));

                addViewDef(this, new I18nViewDescription("v_component_description").
                        addColumnDef(new I18nViewColumnDescription("description", "amp_component_id", AmpComponent.class, "description")));

                addViewDef(this, new I18nViewDescription("v_component_type").
                        addColumnDef(new I18nViewColumnDescription("component_type", "component_type_id", AmpComponentType.class, "name")));
            
                addSimpleTranslatedView(this, "v_yes_no_government_approval_proc", "value", "id");
                addSimpleTranslatedView(this, "v_yes_no_joint_criteria", "value", "id");
                
//              addViewDef(this, new I18nViewDescription("v_activity_pledges_title").
//                      addColumnDef(new I18nViewColumnDescription("pledge_title", "pledge_id", AmpCategoryValue.class, "value")));
                
                // DG_EDITOR-backed translations start HERE
                addViewDef(this, new I18nViewDescription("v_purposes").
                        addDgEditorColumnDef("ebody", "locale"));

                addViewDef(this, new I18nViewDescription("v_description").
                        addDgEditorColumnDef("ebody", "locale"));
                
                addViewDef(this, new I18nViewDescription("v_objectives").
                        addDgEditorColumnDef("ebody", "locale"));
                
                addViewDef(this, new I18nViewDescription("v_results").
                        addDgEditorColumnDef("ebody", "locale"));

                addViewDef(this, new I18nViewDescription("v_proj_impact").
                        addDgEditorColumnDef("ebody", "locale"));

                addViewDef(this, new I18nViewDescription("v_project_comments").
                        addDgEditorColumnDef("ebody", "locale"));
    
                addViewDef(this, new I18nViewDescription("v_project_management").
                        addDgEditorColumnDef("ebody", "locale"));
                
                addViewDef(this, new I18nViewDescription("v_equalopportunity").
                        addDgEditorColumnDef("ebody", "locale"));

                addViewDef(this, new I18nViewDescription("v_environment").
                        addDgEditorColumnDef("ebody", "locale"));

                addViewDef(this, new I18nViewDescription("v_minorities").
                        addDgEditorColumnDef("ebody", "locale"));

                
                addViewDef(this, new I18nViewDescription("ni_all_orgs_dimension")
                        .addColumnDef(new I18nViewColumnDescription("org_name", "org_id", AmpOrganisation.class, "name"))
                        .addColumnDef(new I18nViewColumnDescription("org_grp_name", "org_grp_id", AmpOrgGroup.class, "orgGrpName"))
                        .addColumnDef(new I18nViewColumnDescription("org_type_name", "org_type_id", AmpOrgType.class, "orgType"))
                        );
                buildProgramsMultilingualData();
                
                addViewDef(this, new I18nViewDescription("v_ni_donor_orgs")
                    .addColumnDef(new I18nViewColumnDescription("org_name", "org_id", AmpOrganisation.class, "name")));
                
                addViewDef(this, new I18nViewDescription("v_ni_donor_orgsgroups")
                    .addColumnDef(new I18nViewColumnDescription("org_grp_name", "org_grp_id", AmpOrgGroup.class, "orgGrpName")));

                addViewDef(this, new I18nViewDescription("v_ni_donor_orgstypes")
                    .addColumnDef(new I18nViewColumnDescription("org_type_name", "org_type_id", AmpOrgType.class, "orgType")));             
                
            }
            
            private void buildProgramsMultilingualData(){
                addViewDef(this, new I18nViewDescription("v_primaryprogram").
                        addColumnDef(new I18nViewColumnDescription("name", "amp_theme_id", AmpTheme.class, "name")));

                addViewDef(this, new I18nViewDescription("v_secondaryprogram").
                        addColumnDef(new I18nViewColumnDescription("name", "amp_theme_id", AmpTheme.class, "name")));

                addViewDef(this, new I18nViewDescription("v_tertiaryprogram").
                        addColumnDef(new I18nViewColumnDescription("name", "amp_theme_id", AmpTheme.class, "name")));

                addViewDef(this, new I18nViewDescription("v_nationalobjectives").
                        addColumnDef(new I18nViewColumnDescription("name", "amp_theme_id", AmpTheme.class, "name")));

                for(int i = 0; i <= 8; i++)
                {
                    addViewDef(this, new I18nViewDescription("v_primaryprogram_level_" + i).
                            addColumnDef(new I18nViewColumnDescription("name", "amp_program_id", AmpTheme.class, "name")));
                }

                for(int i = 0; i <= 8; i++)
                {
                    addViewDef(this, new I18nViewDescription("v_secondaryprogram_level_" + i).
                            addColumnDef(new I18nViewColumnDescription("name", "amp_program_id", AmpTheme.class, "name")));
                }               

                for(int i = 0; i <= 8; i++)
                {
                    addViewDef(this, new I18nViewDescription("v_tertiaryprogram_level_" + i).
                            addColumnDef(new I18nViewColumnDescription("name", "amp_program_id", AmpTheme.class, "name")));
                }

                for(int i = 0; i <= 8; i++)
                {
                    addViewDef(this, new I18nViewDescription("v_nationalobjectives_level_" + i).
                            addColumnDef(new I18nViewColumnDescription("name", "amp_program_id", AmpTheme.class, "name")));
                }
            }
            });
    
    /**
     * adds to a Map<view_name, view_description> the description of a view's mapped columns. Does sanity checks while doing so and fails hard in case it doesn't.
     * @param map
     * @param viewDesc
     */
    private static void addViewDef(HashMap<String, I18nViewDescription> map, I18nViewDescription viewDesc)
    {       
        Set<String> columns = SQLUtils.getTableColumns(viewDesc.viewName);
        
        // sanity checks...
        if (columns.isEmpty())
            throw new RuntimeException("trying to define an i18n description of a non-existing table/view: " + viewDesc.viewName);

        for(I18nViewColumnDescription column:viewDesc.columns.values())
        {
            String columnName = column.columnName; // the translatable colum we are replacing
            String replacingIndexColumnName = column.indexColumnName; // the index column we are using
            if (!columns.contains(columnName))
                throw new RuntimeException("trying to override an nonexisting column: " + columnName + " of table " + viewDesc.viewName);
            if ((!column.isCalculated()) && (!columns.contains(replacingIndexColumnName)))
                throw new RuntimeException("trying to override an i18n by using a non-existant ID column " + replacingIndexColumnName + " of table " + viewDesc.viewName);
        }
        
        // double-definition checks
        if (map.containsKey(viewDesc.viewName))
            throw new RuntimeException("doubly-defined description of view " + viewDesc.viewName + ", please merge descriptions!");
        
        map.put(viewDesc.viewName, viewDesc);
        if (SQLUtils.tableExists(viewDesc.viewName + "_cached"))
        {
            I18nViewDescription clonedView = viewDesc.cloneView(viewDesc.viewName + "_cached");
            map.put(clonedView.viewName, clonedView);
        }
        if (SQLUtils.tableExists("cached_" + viewDesc.viewName))
        {
            I18nViewDescription clonedView = viewDesc.cloneView("cached_" + viewDesc.viewName);
            map.put(clonedView.viewName, clonedView);
        }
        
    }
    
    private static void addSimpleTranslatedView(HashMap<String, I18nViewDescription> map, String viewName, String payloadColumn, String idColumn) {
        addViewDef(map, new I18nViewDescription(viewName).
                addTrnColDef(payloadColumn, idColumn));
    }
}
