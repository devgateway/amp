package org.digijava.test.module.news;

import java.util.Iterator;
import java.util.List;

import org.digijava.module.news.form.NewsTeaserItem;
import org.digijava.module.news.util.DbUtil;
import org.digijava.test.util.DigiTestBase;

public class TestNewsTeaser
     extends DigiTestBase {
    public TestNewsTeaser(String name) {
	super(name,
	     "org/digijava/test/module/news/conf/newsTeaserProperties.properties");
    }

    public void testNewsTeaser() throws Exception {
	String siteId = getTestProperty("testNewsTeaser.siteId");
	String instanceId = getTestProperty("testNewsTeaser.instanceId");
	Long numOfItemsInTeaser =
	     new Long(getTestProperty("testNewsTeaser.numberOfItemsInTeaser"));

	int numOfCharsInTitle =
	     new Integer(getTestProperty("testNewsTeaser.titleCharLimit")).
	     intValue();

	List newsItems = DbUtil.getTeaserNewsList(siteId,
						  instanceId,
						  numOfItemsInTeaser);

	assertEquals("Assert number of items", numOfItemsInTeaser.intValue() >=
		     newsItems.size(), true);

	if (newsItems != null) {
	    Iterator it = newsItems.iterator();
	    while (it.hasNext()) {
		NewsTeaserItem item = (NewsTeaserItem) it.next();
		item.setNumOfCharsInTitle(numOfCharsInTitle);
		assertEquals("Assert title length", item.getTitle().length() <=
			     numOfCharsInTitle, true);
	    }
	}
    }
}