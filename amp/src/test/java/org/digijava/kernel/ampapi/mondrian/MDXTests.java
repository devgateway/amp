/**
 * 
 */
package org.digijava.kernel.ampapi.mondrian;

import java.util.Arrays;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.dgfoundation.amp.testutils.AmpTestCase;
import org.digijava.kernel.ampapi.exception.AmpApiException;
import org.digijava.kernel.ampapi.mondrian.queries.MDXGenerator;
import org.digijava.kernel.ampapi.mondrian.queries.TestQueries;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXAttribute;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXConfig;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXFilter;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXLevel;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXMeasure;
import org.digijava.kernel.ampapi.mondrian.util.MoConstants;
import org.olap4j.CellSet;
import org.olap4j.query.SortOrder;

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
		suite.addTest(new MDXTests("testTotals"));
		suite.addTest(new MDXTests("testNoColumnAttr"));
		suite.addTest(new MDXTests("testDataSingleValueFilter"));
		suite.addTest(new MDXTests("testDataFilters"));
		suite.addTest(new MDXTests("testMultipleHierarchies"));
		suite.addTest(new MDXTests("testSorting"));
		return suite;
	}
	
	public void testNoTotals() {
		String expectedRes = null;
		generateAndValidateMDX(getDefaultConfig("testNoTotals", false), expectedRes, false);
	}
	
	public void testTotals() {
		String expectedRes = null;
		MDXConfig config = getDefaultConfig("testTotals", true);
		generateAndValidateMDX(config, expectedRes, false);
	}
	
	public void testNoColumnAttr() {
		MDXConfig config = getDefaultConfig("testNoColumnAttr", true);
		config.getColumnAttributes().clear();
		generateAndValidateMDX(config, null, false);
	}
	
	public void testDataSingleValueFilter() {
		String expectedRes = null;
		MDXConfig config = getDefaultConfig("testDataSingleValueFilter", true);
		config.addSingleValueFilters(new MDXLevel(MoConstants.DATES, MoConstants.H_DATES, MoConstants.ATTR_YEAR, "2014"));
		generateAndValidateMDX(config, expectedRes, false);
	}
	
	public void testDataFilters() {
		String expectedRes = null;
		MDXConfig config = getDefaultConfig("testDataFilters", true);
		addSimpleFilter(config);
		generateAndValidateMDX(config, expectedRes, false);
	}
	
	private void addSimpleFilter(MDXConfig config) {
		MDXAttribute attrYear = new MDXLevel(MoConstants.DATES, MoConstants.H_DATES, MoConstants.ATTR_YEAR);
		MDXFilter filter = new MDXFilter(Arrays.asList("1980", "1994"), true);
		config.getDataFilters().put(attrYear, filter);
	}
	
	public void testMultipleHierarchies() {
		String expectedRes = null;
		MDXConfig config = getDefaultConfig("testDataFilter1", true);
		config.getColumnAttributes().add(0, new MDXAttribute(MoConstants.APPROVAL_STATUS, MoConstants.ATTR_APPROVAL_STATUS));
		config.getRowAttributes().add(0, new MDXLevel(MoConstants.DONOR_AGENCY, MoConstants.H_ORG_GROUP_NAME, MoConstants.ATTR_ORG_GROUP_NAME));
		addSimpleFilter(config); //filter for easier testing
		generateAndValidateMDX(config, expectedRes, false);
	}
	
	public void testSorting() {
		String expectedRes = null;
		MDXConfig config = getDefaultConfig("testSorting", false);
		config.getSortingOrder().put(config.getRowAttributes().get(1), SortOrder.BASC);
		config.getSortingOrder().put(config.getRowAttributes().get(0), SortOrder.DESC);
		generateAndValidateMDX(config, expectedRes, false);
	}
	
	private MDXConfig getDefaultConfig(String testName, boolean doTotals) {
		MDXConfig config = new MDXConfig();
		config.setMdxName(testName);
		config.addColumnAttribute(new MDXLevel(MoConstants.DATES, MoConstants.H_DATES, MoConstants.ATTR_YEAR));
		config.addColumnMeasure(new MDXMeasure(MoConstants.ACTUAL_COMMITMENTS));
		config.addColumnMeasure(new MDXMeasure(MoConstants.ACTUAL_DISBURSEMENTS));
		config.addRowAttribute(new MDXAttribute(MoConstants.LOCATION, MoConstants.ATTR_COUNTRY_NAME));
		config.addRowAttribute(new MDXLevel(MoConstants.DONOR_AGENCY, MoConstants.H_ORG_TYPE_NAME, MoConstants.ATTR_ORG_TYPE_NAME));
		config.setAllowEmptyData(false);
		config.setDoColumnsTotals(doTotals);
		config.setDoRowTotals(doTotals);
		return config;
	}
	
	private CellSet generateAndValidateMDX(MDXConfig config, String expectedMdx, boolean runQuery) {
		String err = null;
		String mdx = null;
		CellSet set = null;
		MDXGenerator generator = null;
		try {
			generator = new MDXGenerator();
			mdx = generator.getAdvancedOlapQuery(config);
			if (runQuery) {
				set = generator.runQuery(mdx); 
				TestQueries.printResult(set);
			}
		} catch (AmpApiException e) {
			System.err.println(e.getMessage());
			err = e.getMessage();
		}
		if (generator != null) generator.tearDown();
		assertNull(err);
		if (expectedMdx!=null)
			assertTrue(expectedMdx.equalsIgnoreCase(mdx));
		return set;
	}
	
}
