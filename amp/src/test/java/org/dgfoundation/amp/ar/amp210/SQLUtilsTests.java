package org.dgfoundation.amp.ar.amp210;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.mondrian.MondrianETL;
import org.dgfoundation.amp.mondrian.PercentagesDistribution;
import org.dgfoundation.amp.newreports.NumberedTypedEntity;
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
public class SQLUtilsTests extends AmpTestCase
{
	
	private SQLUtilsTests(String name)
	{
		super(name);
	}
	
	public static Test suite()
	{
		TestSuite suite = new TestSuite(SQLUtilsTests.class.getName());
		suite.addTest(new SQLUtilsTests("testColumnTypeFetching"));
		suite.addTest(new SQLUtilsTests("testColumnList"));
		return suite;
	}
	
	/**
	 * tests that SQLUtils fetches column types in the correct order and with the correct column types
	 */
	public void testColumnTypeFetching() {
		LinkedHashMap<String, String> cols = SQLUtils.getTableColumnsWithTypes("dg_site_domain", true);
		assertEquals("{site_domain_id=bigint, site_domain=character varying, site_path=character varying, site_id=bigint, language_code=character varying, is_default=boolean, enable_security=boolean}", cols.toString());
		
		cols = SQLUtils.getTableColumnsWithTypes("v_regions", true);
		assertEquals("{amp_activity_id=bigint, region_name=character varying, region_id=bigint, percentage=real}", cols.toString());
	}
	
	/**
	 * tests that SQLUtils fetches columns in correct order
	 */
	public void testColumnList() {
		Set<String> colNames = SQLUtils.getTableColumns("amp_role");
		assertEquals("[amp_role_id, role_code, name, type, description, language]", colNames.toString());
	}

	@Override
    protected void setUp() throws Exception
    {
		//TLSUtils.getThreadLocalInstance().setForcedLangCode("en");
		super.setUp();
        // do nothing now                
    }
}