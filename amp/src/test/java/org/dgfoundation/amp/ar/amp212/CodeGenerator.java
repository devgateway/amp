package org.dgfoundation.amp.ar.amp212;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.StandaloneAMPInitializer;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.codegenerators.ActivityTitlesGenerator;
import org.dgfoundation.amp.codegenerators.CategoriesTreeGenerator;
import org.dgfoundation.amp.codegenerators.ColumnGenerator;
import org.dgfoundation.amp.codegenerators.FundingColumnGenerator;
import org.dgfoundation.amp.codegenerators.FundingIdsMapper;
import org.dgfoundation.amp.codegenerators.FundingTypesGenerator;
import org.dgfoundation.amp.codegenerators.NaturalTreeGenerator;
import org.dgfoundation.amp.codegenerators.NiDateColumnGenerator;
import org.dgfoundation.amp.codegenerators.NiDimensionGenerator;
import org.dgfoundation.amp.codegenerators.NiPercentageTextColumnGenerator;
import org.dgfoundation.amp.codegenerators.NiTextColumnGenerator;
import org.dgfoundation.amp.codegenerators.OrganizationsTreeGenerator;
import org.dgfoundation.amp.error.AMPException;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;

/**
 * 
 * Code generation for an offline schema
 * Columns can be generated directly to files, the rest can be picked up from the console (System.out.println(new CodeGenerator().generate()).).
 * 
 * All code generation depends on a different schema existing and fully functioning (in AMP: db-dependent AmpReportsSchema)
 * 
 * Every CodeGenerator-derived class contains a .generate() method, which creates an ArrayList of elements
 * (whichever those are, depending on the container).
 * 
 * @author Alexandru Cartaleanu
 *
 */
public class CodeGenerator  {
        
    static Logger log = Logger.getLogger(OffDbNiReportEngineTests.class);
        
//  HardcodedReportsTestSchema schema = new HardcodedReportsTestSchema();

    public ReportSpecificationImpl buildSpecification(String reportName, List<String> columns, List<String> measures, List<String> hierarchies, GroupingCriteria groupingCriteria) {
        return ReportSpecificationImpl.buildFor(reportName, columns, measures, hierarchies, groupingCriteria);
    }
    
    protected void generateColumns() throws FileNotFoundException, UnsupportedEncodingException {
        List<ColumnGenerator> gens = Arrays.asList(
                 new NiPercentageTextColumnGenerator(ColumnConstants.PRIMARY_SECTOR),
                 new NiPercentageTextColumnGenerator(ColumnConstants.SECONDARY_SECTOR),
                 new NiPercentageTextColumnGenerator(ColumnConstants.PRIMARY_SECTOR_SUB_SECTOR),
                 new NiPercentageTextColumnGenerator(ColumnConstants.SECONDARY_SECTOR_SUB_SECTOR),
                 new NiPercentageTextColumnGenerator(ColumnConstants.COUNTRY),
                 new NiPercentageTextColumnGenerator(ColumnConstants.REGION),
                 new NiPercentageTextColumnGenerator(ColumnConstants.ZONE),
                 new NiPercentageTextColumnGenerator(ColumnConstants.DISTRICT),
                 //new NiPercentageTextColumnGenerator(ColumnConstants.PRIMARY_PROGRAM),
                 new NiPercentageTextColumnGenerator(ColumnConstants.PRIMARY_PROGRAM_LEVEL_1),
                 new NiPercentageTextColumnGenerator(ColumnConstants.PRIMARY_PROGRAM_LEVEL_2),
                 
                 new NiPercentageTextColumnGenerator(ColumnConstants.SECONDARY_PROGRAM_LEVEL_1),
                 new NiPercentageTextColumnGenerator(ColumnConstants.SECONDARY_PROGRAM_LEVEL_2),
                 
                 new NiPercentageTextColumnGenerator(ColumnConstants.IMPLEMENTING_AGENCY),
                 new NiPercentageTextColumnGenerator(ColumnConstants.IMPLEMENTING_AGENCY_GROUPS),
                 new NiPercentageTextColumnGenerator(ColumnConstants.IMPLEMENTING_AGENCY_TYPE),
                 
                 new NiTextColumnGenerator(ColumnConstants.PROJECT_TITLE),
                 new NiTextColumnGenerator(ColumnConstants.STATUS),
                 new NiTextColumnGenerator(ColumnConstants.IMPLEMENTATION_LEVEL),
                 new NiTextColumnGenerator(ColumnConstants.MODE_OF_PAYMENT),
                 new NiTextColumnGenerator(ColumnConstants.FUNDING_STATUS),
                 new NiTextColumnGenerator(ColumnConstants.TYPE_OF_ASSISTANCE),
                 new NiTextColumnGenerator(ColumnConstants.FINANCING_INSTRUMENT),
                 new NiTextColumnGenerator(ColumnConstants.DONOR_AGENCY),
                 new NiTextColumnGenerator(ColumnConstants.DONOR_GROUP),
                 new NiTextColumnGenerator(ColumnConstants.DONOR_TYPE),
                 new NiDateColumnGenerator(ColumnConstants.ACTIVITY_CREATED_ON),
                 new NiDateColumnGenerator(ColumnConstants.ACTIVITY_UPDATED_ON)
                 
                );
        for (ColumnGenerator gen : gens)
            gen.generateToFile();
    }
    
    protected void generateFunding() throws IOException {
         FundingColumnGenerator gen = new FundingColumnGenerator();
         gen.generateToFile();
         gen.binaryDump();
         //System.out.println(gen.generate());
    }
    
    protected void generateFundingTypesNames() throws IOException {
         FundingTypesGenerator gen = new FundingTypesGenerator(new FundingIdsMapper().getAllParams());
         gen.generateToFile();
         //System.out.println(gen.generate());
    }
    
    protected void generateActivityNames() throws IOException {
        ActivityTitlesGenerator gen = new ActivityTitlesGenerator();
        gen.generateToFile();
    }
    

    public void generateDimensions() throws IOException {
        NiDimensionGenerator programGen = new NiDimensionGenerator(new NaturalTreeGenerator("amp_theme", "amp_theme_id", "parent_theme_id", "name").getRoots(), "ProgramsTestDimension", "progs", 4);
        NiDimensionGenerator sectorsGen = new NiDimensionGenerator(new NaturalTreeGenerator("amp_sector", "amp_sector_id", "parent_sector_id", "name").getRoots(), "SectorsTestDimension", "sectors", 3);
        NiDimensionGenerator locationsGen = new NiDimensionGenerator(new NaturalTreeGenerator("amp_category_value_location", "id", "parent_location", "location_name").getRoots(), "LocationsTestDimension", "locs", 4);
        NiDimensionGenerator orgGen = new NiDimensionGenerator(new OrganizationsTreeGenerator().getRoots(), "OrganizationsTestDimension", "orgs", 3);
        NiDimensionGenerator catGen = new NiDimensionGenerator(new CategoriesTreeGenerator().getRoots(), "CategoriesTestDimension", "cats", 2);

        locationsGen.generateToFile();
        programGen.generateToFile();
        sectorsGen.generateToFile();
        orgGen.generateToFile();
        catGen.generateToFile();
    }
    
    public void generateCode() throws IOException  {
        generateFunding();
        generateActivityNames();
        generateFundingTypesNames();
        generateDimensions();
        generateColumns();
    }
    
    /**
     * Prior to startup one must make sure than organization names are unique. This is true in the testcases database.
     * 
     * 
------------------------------------------------------------------------------------ 
For real production databases used for benchmarking, one may just prune the duplicate DBs. The SQL queries to do it are, in order:

create table temp_duplicate_orgs AS
select z.* from (
select org_name, count(*) cnt from ni_all_orgs_dimension where org_id > 0 group by org_name) z
where z.cnt > 1;


create table temp_orgs_to_delete AS (select amp_org_id from 
amp_organisation where name in (select org_name from temp_duplicate_orgs));


delete from amp_org_role where organisation in (select amp_org_id from temp_orgs_to_delete);
delete from amp_funding_detail where amp_funding_id in (select amp_funding_id from amp_funding where amp_donor_org_id  in (select amp_org_id from temp_orgs_to_delete));

delete from amp_funding where amp_donor_org_id  in (select amp_org_id from temp_orgs_to_delete);

delete from amp_user_ext where amp_org_id  in (select amp_org_id from temp_orgs_to_delete);
delete from amp_funding_detail where recipient_org_id in (select amp_org_id from temp_orgs_to_delete);
truncate amp_ahsurvey cascade;
truncate amp_activity_internal_id;
truncate amp_org_recipient;
truncate amp_organisation_sector;
truncate amp_org_location;
delete from amp_organisation where amp_org_id in (select amp_org_id from temp_orgs_to_delete);

DROP TABLE temp_duplicate_orgs;
DROP TABLE temp_orgs_to_delete;

-----------------------------------------
to export a database in a good-enough-for-benchmarks-use state, run the following SQL (warning: it will corrupt the history)

TRUNCATE mondrian_fact_table;
TRUNCATE mondrian_raw_donor_transactions;
TRUNCATE dg_editor;
TRUNCATE cached_v_pledges_funding_st;
TRUNCATE cached_v_donor_funding;

truncate amp_org_role_budget;
delete from amp_org_role where activity NOT IN (select amp_activity_id from amp_activity);

TRUNCATE amp_message CASCADE;
delete from amp_funding_detail where amp_fund_detail_id not in (select amp_fund_detail_id from amp_activity aa join v_donor_funding vdf on aa.amp_activity_id = vdf.amp_activity_id);
delete from amp_funding where amp_funding_id not in (select amp_funding_id from amp_activity aa join v_donor_funding vdf on aa.amp_activity_id = vdf.amp_activity_id);


TRUNCATE amp_audit_logger;

delete from amp_activity_sector where amp_activity_id NOT IN (select amp_activity_id from amp_activity);
delete from amp_activities_categoryvalues where amp_activity_id NOT IN (select amp_activity_id from amp_activity);

     * 
     * @param args
     * @throws AMPException
     * @throws IOException
     */
    public static void main(String[] args)  throws AMPException, IOException {
        StandaloneAMPInitializer.initialize();
        new CodeGenerator().generateCode();
    }
}
