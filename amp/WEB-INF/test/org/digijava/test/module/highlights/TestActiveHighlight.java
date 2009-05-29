package org.digijava.test.module.highlights;

import org.digijava.module.highlights.util.DbUtil;
import org.digijava.test.util.DigiTestBase;

public class TestActiveHighlight
     extends DigiTestBase {
    public TestActiveHighlight(String name) {
	super(name,
	     "org/digijava/test/module/highlights/conf/activeHighlightsProperties.properties");
    }

    public void testActiveHighlight() throws Exception {
	String siteId = getTestProperty("testActiveHighlight.siteId");
	String instanceId = getTestProperty("testActiveHighlight.instanceId");
	int expected = Integer.parseInt(getTestProperty(
	     "testActiveHighlight.numOfActiveHighlights"));

	int numOfActiveHighlights = DbUtil.getNumOfActiveHighlights(siteId,
	     instanceId);

	assertEquals("Assert number of active highlights",
		     numOfActiveHighlights ==
		     expected, true);

    }
}