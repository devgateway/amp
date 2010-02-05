package org.dgfoundation.amp.test.example;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.dgfoundation.amp.test.util.Configuration;
import org.digijava.kernel.Constants;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.util.SiteCache;
import org.digijava.module.translation.taglib.TrnTag;

import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpSession;
import com.mockrunner.tag.BasicTagTestCaseAdapter;
import com.mockrunner.tag.NestedTag;

public class MockTagLibTest extends BasicTagTestCaseAdapter {

	public static Test suite() {
		junit.framework.TestSuite suite = new junit.framework.TestSuite();
		suite.addTest(new TestSuite(MockTagLibTest.class));
		return suite;

	}

	private NestedTag nestedTag;

	protected void setUp() throws Exception {
		super.setUp();
		Map<String, String> attributeMap = new HashMap<String, String>();
		attributeMap.put("key", "admin:addusers");
		Configuration.initConfig();
		nestedTag = createNestedTag(TrnTag.class, attributeMap);
		setRelatedObjects();
	}

	protected void setRelatedObjects() {
		MockHttpSession session = getWebMockObjectFactory().getMockSession();
		MockHttpServletRequest request = getWebMockObjectFactory().getMockRequest();
		Locale testLocale = new Locale();
		testLocale.setCode("en");
		request.setAttribute(Constants.NAVIGATION_LANGUAGE, testLocale);

		SiteDomain siteDomain = new SiteDomain();
		Site site = new Site("newdemo", "newdemo");
		site.setId(new Long(3));

		SiteCache siteCache = SiteCache.getInstance();

		siteDomain.setSite(site);
		request.setAttribute(Constants.CURRENT_SITE, siteDomain);
	}

	public void testNoBody() throws Exception {
		processTagLifecycle();
		BufferedReader reader = getOutputAsBufferedReader();
		assertEquals("Add Users", reader.readLine().trim());

	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(MockTagLibTest.class);
	}

}
