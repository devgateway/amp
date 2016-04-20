package org.dgfoundation.amp.ar.amp212;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.AllTests_amp212;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.codegenerators.ActivityTitlesGenerator;
import org.dgfoundation.amp.codegenerators.CategoriesTreeGenerator;
import org.dgfoundation.amp.codegenerators.ColumnGenerator;
import org.dgfoundation.amp.codegenerators.FundingColumnGenerator;
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
		
//	HardcodedReportsTestSchema schema = new HardcodedReportsTestSchema();

	public ReportSpecificationImpl buildSpecification(String reportName, List<String> columns, List<String> measures, List<String> hierarchies, GroupingCriteria groupingCriteria) {
		return ReportSpecificationImpl.buildFor(reportName, columns, measures, hierarchies, groupingCriteria);
	}
	
	private void generateColumns() throws FileNotFoundException, UnsupportedEncodingException {
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
	
	private void generateFunding() {
		 FundingColumnGenerator gen = new FundingColumnGenerator();
		 System.out.println(gen.generate());
	}
	
	
	private void generateActivityNames() {
		ActivityTitlesGenerator gen = new ActivityTitlesGenerator();
		System.out.println(gen.generate());
	}
	

	public void generateDimensions() {
		NiDimensionGenerator programGen = new NiDimensionGenerator(new NaturalTreeGenerator("amp_theme", "amp_theme_id", "parent_theme_id", "name").getRoots());
		NiDimensionGenerator sectorsGen = new NiDimensionGenerator(new NaturalTreeGenerator("amp_sector", "amp_sector_id", "parent_sector_id", "name").getRoots());
		NiDimensionGenerator locationsGen = new NiDimensionGenerator(new NaturalTreeGenerator("amp_category_value_location", "id", "parent_location", "location_name").getRoots());
		NiDimensionGenerator orgGen = new NiDimensionGenerator(new OrganizationsTreeGenerator().getRoots());
		NiDimensionGenerator catGen = new NiDimensionGenerator(new CategoriesTreeGenerator().getRoots());

		System.out.println(locationsGen.generate());
		System.out.println(programGen.generate());
		System.out.println(sectorsGen.generate());
		System.out.println(orgGen.generate());
		System.out.println(catGen.generate());
	}
	
	public void generateCode() throws FileNotFoundException, UnsupportedEncodingException  {
//			generateFunding();
//			generateActivityNames();
//			generateFundingTypesNames();
//			generateDimensions();
			generateColumns();
	}
	
	public static void main(String[] args)  throws AMPException, FileNotFoundException, UnsupportedEncodingException {
		AllTests_amp212.configureLog4j();
		AllTests_amp212.setUp();
		new CodeGenerator().generateCode();
	}
}
