package org.digijava.test.module.calendar;

import servletunit.struts.MockStrutsTestCase;
import org.digijava.test.util.DigiTestBase;
import org.digijava.module.common.dbentity.ItemStatus;
import java.util.List;
import java.util.Iterator;
import org.digijava.module.calendar.util.DbUtil;
import org.digijava.module.calendar.form.CalendarTeaserItem;

public class TestCalendarTeaser
     extends DigiTestBase {
    public TestCalendarTeaser(String name) {
	super(name,
	     "org/digijava/test/module/calendar/conf/calendarTeaserProperties.properties");
    }

    public void testCalendarTeaser() throws Exception {
	String siteId = getTestProperty("testCalendarTeaser.siteId");
	String instanceId = getTestProperty("testCalendarTeaser.instanceId");
	String status = ItemStatus.PUBLISHED;
	Long numOfItemsInTeaser =
	     new Long(getTestProperty(
	     "testCalendarTeaser.numberOfItemsInTeaser"));

	int numOfCharsInTitle =
	     new Integer(getTestProperty("testCalendarTeaser.titleCharLimit")).
	     intValue();

	List calendarItems = DbUtil.getCalendarEvents(siteId,
	     instanceId, status, 0, numOfItemsInTeaser.intValue(), false);

	assertEquals("Assert number of items", numOfItemsInTeaser.intValue() >=
		     calendarItems.size(), true);

	if (calendarItems != null) {
	    Iterator iter = calendarItems.iterator();
	    while (iter.hasNext()) {
		CalendarTeaserItem item = (CalendarTeaserItem) iter.next();
		item.setNumOfCharsInTitle(numOfCharsInTitle);
		assertEquals("Assert title length", item.getTitle().length() <=
			     numOfCharsInTitle, true);
	    }
	}
    }
}