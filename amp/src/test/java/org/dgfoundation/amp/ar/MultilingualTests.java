package org.dgfoundation.amp.ar;

import org.dgfoundation.amp.testutils.ReportTestingUtils;
import org.digijava.kernel.request.TLSUtils;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class MultilingualTests extends TestCase
{
	public MultilingualTests(String name)
	{
		super(name);
	}
	
	public static Test suite()
	{
		TestSuite suite = new TestSuite(MultilingualTests.class.getName());
		suite.addTest(new MultilingualTests("testStickyTranslations"));
		suite.addTest(new MultilingualTests("testAmpActivityMappedCorrectly"));
		return suite;
	}
	
	public void testStickyTranslations() throws Exception
	{
		for(int i = 0; i < 500; i++)
		{
			TLSUtils.getThreadLocalInstance().setLocale(org.digijava.module.um.util.DbUtil.getLanguageByCode("ru"));
			String ruVer = ReportTestingUtils.getActivityName(2L);
			TLSUtils.getThreadLocalInstance().setLocale(org.digijava.module.um.util.DbUtil.getLanguageByCode("en"));
			String enVer = ReportTestingUtils.getActivityName(2L);
			
			assertEquals("Вода Eth", ruVer);
			assertEquals("Eth Water", enVer);
		}
	}
	
	public void testAmpActivityMappedCorrectly() throws Exception
	{
		TLSUtils.getThreadLocalInstance().setLocale(org.digijava.module.um.util.DbUtil.getLanguageByCode("ru"));
		String ruVer = ReportTestingUtils.getActivityName(2L);
		String ruVerView = ReportTestingUtils.getActivityName_notVersion(2L);
		
		TLSUtils.getThreadLocalInstance().setLocale(org.digijava.module.um.util.DbUtil.getLanguageByCode("en"));
		String enVer = ReportTestingUtils.getActivityName(2L);
		String enVerView = ReportTestingUtils.getActivityName_notVersion(2L);
		
		assertEquals("Вода Eth", ruVer);
		assertEquals("Eth Water", enVer);
		
		assertEquals("Вода Eth", ruVerView);
		assertEquals("Eth Water", enVerView);		

	}
}