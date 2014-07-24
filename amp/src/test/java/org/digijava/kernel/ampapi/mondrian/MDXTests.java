/**
 * 
 */
package org.digijava.kernel.ampapi.mondrian;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.dgfoundation.amp.testutils.AmpTestCase;
import org.digijava.kernel.ampapi.exception.AmpApiException;
import org.digijava.kernel.ampapi.mondrian.queries.MDXGenerator;
import org.digijava.kernel.ampapi.mondrian.queries.TestQueries;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXAttribute;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXConfig;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXFilter;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXMeasure;
import org.digijava.kernel.ampapi.mondrian.util.MoConstants;
import org.olap4j.CellSet;

/**
 * MDX Queries generation tests cases
 * @author Nadejda Mandrescu
 */
public class MDXTests extends AmpTestCase {

	private MDXTests(String name) {
		super(name);
	}
	
	public static Test suite()
	{
		TestSuite suite = new TestSuite(MDXTests.class.getName());
		suite.addTest(new MDXTests("testNoTotals"));
		//suite.addTest(new MDXTests("testTotals"));
		//suite.addTest(new MDXTests("testDataSimpleFilter"));
		//suite.addTest(new MDXTests("testDataFilters"));
		return suite;
	}
	
	public void testNoTotals() {
		String expectedRes = "SELECT NON EMPTY CrossJoin([Donor Dates].[Year].Members, {Measures.[Actual Commitments]}) ON COLUMNS, "
				+ "NON EMPTY CrossJoin([Donor Group].[DonorGroup].Members, [Donor Types].[DonorType].Members) ON ROWS  "
				+ "FROM [Donor Funding]";
		generateAndValidateMDX(getDefaultConfig("testNoTotals", false), expectedRes, true);
	}
	
	public void testTotals() {
		String expectedRes = "SELECT NON EMPTY CrossJoin({ [Donor Dates].[Year].Members, [Donor Dates].[All Periods]}, {Measures.[Actual Commitments]}) ON COLUMNS, "
				+ "NON EMPTY CrossJoin({ [Donor Group].[DonorGroup].Members, [Donor Group].[All Donor Groups]}, { [Donor Types].[DonorType].Members, [Donor Types].[All Donor Typess]}) ON ROWS  "
				+ "FROM [Donor Funding]";
		MDXConfig config = getDefaultConfig("testTotals", true);
		generateAndValidateMDX(config, expectedRes, false);
	}
	
	public void testDataSimpleFilter() {
		String expectedRes = "SELECT NON EMPTY CrossJoin({ [Donor Dates].[Year].Members, [Donor Dates].[All Periods]}, {Measures.[Actual Commitments]}) ON COLUMNS, "
				+ "NON EMPTY CrossJoin({ [Donor Group].[DonorGroup].Members, [Donor Group].[All Donor Groups]}, { [Donor Types].[DonorType].Members, [Donor Types].[All Donor Typess]}) ON ROWS  "
				+ "FROM [Donor Funding] "
				+ "WHERE ([Donor Dates Duplicate].[Year].[1980])";
		MDXConfig config = getDefaultConfig("testDataFilter1", true);
		config.addSingleValueFilters(new MDXAttribute("Donor Dates Duplicate", "Year", "1980"));
		generateAndValidateMDX(config, expectedRes, false);
	}
	
	public void testDataFilters() {
		String expectedRes = "SELECT NON EMPTY CrossJoin({ [Donor Dates].[Year].Members, [Donor Dates].[All Periods]}, {Measures.[Actual Commitments]}) ON COLUMNS, "
				+ "NON EMPTY CrossJoin({ [Donor Group].[DonorGroup].Members, [Donor Group].[All Donor Groups]}, { [Donor Types].[DonorType].Members, [Donor Types].[All Donor Typess]}) ON ROWS  "
				+ "FROM [Donor Funding] "
				+ "WHERE (Filter([Donor Dates Duplicate].[Year].Members, [Donor Dates Duplicate].[Year].CurrentMember IN {[Donor Dates Duplicate].[Year].[1980], [Donor Dates Duplicate].[Year].[1994]}))";
		MDXConfig config = getDefaultConfig("testDataFilter1", true);
		config.setMdxName("testDataFilter1");
		MDXAttribute attrYear = new MDXAttribute("Donor Dates Duplicate", "Year");
		MDXFilter filter = new MDXFilter(Arrays.asList("1980", "1994"), true);
		Map<MDXAttribute, MDXFilter> dataFiltersMap = new HashMap<MDXAttribute, MDXFilter>();
		dataFiltersMap.put(attrYear, filter);
		config.setDataFilters(dataFiltersMap);
		generateAndValidateMDX(config, expectedRes, false);
	}
	
	private MDXConfig getDefaultConfig(String testName, boolean doTotals) {
		MDXConfig config = new MDXConfig();
		config.setMdxName(testName);
		config.addColumnAttribute(new MDXAttribute(MoConstants.DATES, MoConstants.ATTR_YEAR));
		config.addColumnMeasure(new MDXMeasure(MoConstants.ACTUAL_COMMITMENTS));
		config.addColumnMeasure(new MDXMeasure(MoConstants.ACTUAL_DISBURSEMENTS));
		config.addRowAttribute(new MDXAttribute(MoConstants.LOCATION, MoConstants.ATTR_COUNTRY_NAME));
		config.addRowAttribute(new MDXAttribute(MoConstants.DONOR_AGENCY, MoConstants.ATTR_ORG_TYPE_NAME));
		config.setAllowEmptyData(false);
		config.setDoColumnsTotals(doTotals);
		config.setDoRowTotals(doTotals);
		return config;
	}
	
	private CellSet generateAndValidateMDX(MDXConfig config, String expectedMdx, boolean runQuery) {
		String err = null;
		String mdx = null;
		CellSet set = null;
		try {
			MDXGenerator generator = new MDXGenerator();
			mdx = generator.getAdvancedOlapQuery(config);
			if (runQuery) {
				set = generator.runQuery(mdx); 
				TestQueries.printResult(set);
			}
		} catch (AmpApiException e) {
			System.err.println(e.getMessage());
			err = e.getMessage();
		}
		assertNull(err);
		if (expectedMdx!=null)
			assertTrue(expectedMdx.equalsIgnoreCase(mdx));
		return set;
	}
	
}
