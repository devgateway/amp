package org.dgfoundation.amp.esri;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.dgfoundation.amp.testutils.*;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.esrigis.helpers.*;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.helper.Constants;
import org.hibernate.Query;

import org.hibernate.cfg.*;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * testcases for ESRI Funding fetching (AMP-15458)
 * this can only be run IFF amp has been started standalone. For this, AllTests.setUp() should have been run previously (typically called by AllTests.suite() as part of the JUnit discovery process)
 * please see report "AMP-15458-all-funding" (in AMP proper) to see the data used/displayed/filtered by the testcases of here
 * @author Dolghier Constantin
 *
 */
public class EsriTestCases extends EsriTestCase
{
	public EsriTestCases(String name) {
		super(name);
	}
	
	
	public static Test suite()
	{
		TestSuite suite = new TestSuite(EsriTestCases.class.getName());
		suite.addTest(new EsriTestCases("testMtefFunding"));
		suite.addTest(new EsriTestCases("testCommitmentsFunding"));
		suite.addTest(new EsriTestCases("testDisbursementsFunding"));
		return suite;
	}
	
	
	public void testMtefFunding() throws Exception
	{
		ActivityNameFilteringMapFilter mapFilter = new ActivityNameFilteringMapFilter(Constants.MTEFPROJECTION, "mtef activity 1", "mtef activity 2", "SSC Project 1");
		
		List<AmpActivityVersion> activities = DbHelper.getActivities(mapFilter);
		checkActivitiesList(activities, activityDigestList(new ActivityDigest("mtef activity 1", "8721133"), new ActivityDigest("mtef activity 2", "8721134")));
		
//		List<Long> locs = DbHelper.getLocations(mapFilter, "Region");
//		checkLocationsList(locs, "Anenii Noi County");
		
		List<SimpleLocation> fundings = DbHelper.getFundingByRegionList(null, "Region", "USD", new Date(1980 - 1900, 0, 1),
	            new Date(2020 - 1900, 0, 1), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL, 2, new BigDecimal(1), mapFilter);
		
		SimpleLocation[] bla = 
			{
				new SimpleLocation("Anenii Noi County", "111333.00", "555111.00", "123654.00"),
				new SimpleLocation("Moldova", "0.00", "0.00", "789123.00")
			};
		checkSimpleLocations(fundings, bla);
		
		//System.out.println(fundings.size());
	}

	public void testCommitmentsFunding() throws Exception
	{
		ActivityNameFilteringMapFilter mapFilter = new ActivityNameFilteringMapFilter(Constants.COMMITMENT, "mtef activity 1", "mtef activity 2", "SSC Project 1", "SSC Project 2");
		
		List<AmpActivityVersion> activities = DbHelper.getActivities(mapFilter);
		checkActivitiesList(activities, activityDigestList(new ActivityDigest("SSC Project 1", "8721137"), new ActivityDigest("SSC Project 2", "87211311")));
		
//		List<Long> locs = DbHelper.getLocations(mapFilter, "Region");
//		checkLocationsList(locs, "Anenii Noi County", "Edinet County");
		
		List<SimpleLocation> fundings = DbHelper.getFundingByRegionList(null, "Region", "USD", new Date(1980 - 1900, 0, 1),
	            new Date(2020 - 1900, 0, 1), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL, 2, new BigDecimal(1), mapFilter);
		
		SimpleLocation[] bla = {
				new SimpleLocation("Anenii Noi County", "111333.00", "555111.00", "123654.00"), 
				new SimpleLocation("Edinet County", "567421.00", "131845.00", "0.00"),
				new SimpleLocation("Moldova", "0.00", "0.00", "789123.00")};
		
		checkSimpleLocations(fundings, bla);
		
		//System.out.println(fundings.size());
	}

	public void testDisbursementsFunding() throws Exception
	{
		ActivityNameFilteringMapFilter mapFilter = new ActivityNameFilteringMapFilter(Constants.COMMITMENT, "mtef activity 1", "mtef activity 2", "SSC Project 1", "SSC Project 2");
		
		List<AmpActivityVersion> activities = DbHelper.getActivities(mapFilter);
		checkActivitiesList(activities, activityDigestList(new ActivityDigest("SSC Project 1", "8721137"), new ActivityDigest("SSC Project 2", "87211311")));
		
//		List<Long> locs = DbHelper.getLocations(mapFilter, "Region");
//		checkLocationsList(locs, "Anenii Noi County", "Edinet County");
		
		List<SimpleLocation> fundings = DbHelper.getFundingByRegionList(null, "Region", "USD", new Date(1980 - 1900, 0, 1),
	            new Date(2020 - 1900, 0, 1), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL, 2, new BigDecimal(1), mapFilter);
		
		SimpleLocation[] bla = {
				new SimpleLocation("Anenii Noi County", "111333.00", "555111.00", "123654.00"), 
				new SimpleLocation("Edinet County", "567421.00", "131845.00", "0.00"),
				new SimpleLocation("Moldova", "0.00", "0.00", "789123.00")};
		checkSimpleLocations(fundings, bla);
		
		//System.out.println(fundings.size());
	}

}
