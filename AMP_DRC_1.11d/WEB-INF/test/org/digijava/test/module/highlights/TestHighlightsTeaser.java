package org.digijava.test.module.highlights;

import org.digijava.module.highlights.dbentity.Highlight;
import org.digijava.module.highlights.form.HighlightTeaserItem;
import org.digijava.module.highlights.util.DbUtil;
import org.digijava.test.util.DigiTestBase;

public class TestHighlightsTeaser
     extends DigiTestBase {
    public TestHighlightsTeaser(String name) {
	super(name,
	     "org/digijava/test/module/highlights/conf/highlightsTeaserProperties.properties");
    }

    public void testCalendarTeaser() throws Exception {
	String siteId = getTestProperty("testHighlightsTeaser.siteId");
	String instanceId = getTestProperty("testHighlightsTeaser.instanceId");
	int shortTopicLength = Integer.parseInt(getTestProperty(
	     "testHighlightsTeaser.shortTopicLength"));

	HighlightTeaserItem teaserItem = DbUtil.getTeaserItem(siteId,
	     instanceId);

	if (shortTopicLength < Highlight.MAX_VISIBLE_TEXT_LENGTH) {
	    assertEquals("Assert shortened topic length",
			 teaserItem.getShortenedTopic().length() <=
			 shortTopicLength, true);

	}
    }
}