package org.dgfoundation.amp.ar.amp210;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.mondrian.PercentagesDistribution;
import org.dgfoundation.amp.newreports.NumberedTypedEntity;
import org.dgfoundation.amp.newreports.ReportEntityType;
import org.dgfoundation.amp.testutils.AmpTestCase;
import org.dgfoundation.amp.visibility.AmpObjectVisibility;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpFeaturesVisibility;
import org.digijava.module.aim.dbentity.AmpFieldsVisibility;
import org.digijava.module.aim.dbentity.AmpModulesVisibility;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * ETL Testcases
 * @author Dolghier Constantin
 *
 */
public class ETLTests extends AmpTestCase
{
	
	private ETLTests(String name)
	{
		super(name);
	}
	
	public static Test suite()
	{
		TestSuite suite = new TestSuite(ETLTests.class.getName());
		suite.addTest(new ETLTests("testPercentagesDistribution"));
		suite.addTest(new ETLTests("testPercentagesDistributionWithNulls"));
		return suite;
	}
	
	protected void testPercentage(String cor, String errors, Pair... entries) {
		NumberedTypedEntity activity = new NumberedTypedEntity(1, ReportEntityType.ENTITY_TYPE_ACTIVITY);
		PercentagesDistribution perc = new PercentagesDistribution(activity, "primary_sector_id");
		for(Pair entry:entries) {
			perc.add(entry.id, entry.perc);
		}
		perc.postProcess();
		assertEquals(cor, perc.toString());
		if (errors == null) {
			assertEquals(0, perc.getErrors().size());
		}
		else
			assertEquals(errors, perc.getErrors().toString());
	}
	
	/**
	 * tests distributing normal percentages (no nulls)
	 */
	public void testPercentagesDistribution() {
		testPercentage("{2=100.0}", null, new Pair(2, 10.0));
		testPercentage("{2=10.0, 3=90.0}", null, new Pair(2, 10.0), new Pair(3, 90.0));
		testPercentage("{2=25.0, 4=25.0, 5=25.0, 17=25.0}", null, new Pair(2, 100.0), new Pair(4, 100.0), new Pair(5, 100.0), new Pair(17, 100.0));
		testPercentage("{}", null);
	}
	
	/**
	 * tests distributing normal percentages, with nulls
	 */
	public void testPercentagesDistributionWithNulls() {
		testPercentage("{2=100.0}", "[WARNING_TYPE_ENTRY_WITH_NULL on (primary_sector_id, 2) of ENTITY_TYPE_ACTIVITY 1]", 
				new Pair(2, null));
		testPercentage("{2=50.0, 3=50.0}", "[WARNING_TYPE_ENTRY_WITH_NULL on (primary_sector_id, 2) of ENTITY_TYPE_ACTIVITY 1, WARNING_TYPE_ENTRY_WITH_NULL on (primary_sector_id, 3) of ENTITY_TYPE_ACTIVITY 1]", 
				new Pair(2, null), new Pair(3, null));
		testPercentage("{2=50.0, 3=50.0}", "[WARNING_TYPE_ENTRY_MIXES_NULL_AND_NOT_NULL on (primary_sector_id, -1) of ENTITY_TYPE_ACTIVITY 1, WARNING_TYPE_ENTRY_WITH_NULL on (primary_sector_id, 3) of ENTITY_TYPE_ACTIVITY 1]", 
				new Pair(2, 15.0), new Pair(3, null));
		testPercentage("{2=12.5, 3=37.5, 4=50.0}", "[WARNING_TYPE_ENTRY_MIXES_NULL_AND_NOT_NULL on (primary_sector_id, -1) of ENTITY_TYPE_ACTIVITY 1, WARNING_TYPE_ENTRY_WITH_NULL on (primary_sector_id, 4) of ENTITY_TYPE_ACTIVITY 1]", 
				new Pair(2, 15.0), new Pair(3, 45.0), new Pair(4, null));
	}

	
	@Override
    protected void setUp() throws Exception
    {
		//TLSUtils.getThreadLocalInstance().setForcedLangCode("en");
		super.setUp();
        // do nothing now                
    }
}