/*
 *   RenderTeaser.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Nov 17, 2003
 * 	 CVS-ID: $Id: RenderTeaser.java,v 1.1 2005-07-06 10:34:29 rahul Exp $
 *
 *   This file is part of DiGi project (www.digijava.org).
 *   DiGi is a multi-site portal system written in Java/J2EE.
 *
 *   Confidential and Proprietary, Subject to the Non-Disclosure
 *   Agreement, Version 1.0, between the Development Gateway
 *   Foundation, Inc and the Recipient -- Copyright 2001-2004 Development
 *   Gateway Foundation, Inc.
 *
 *   Unauthorized Disclosure Prohibited.
 *
 *************************************************************************/

package org.digijava.module.calendar.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.calendar.dbentity.CalendarSettings;
import org.digijava.module.calendar.exception.CalendarException;
import org.digijava.module.calendar.form.CalendarForm;
import org.digijava.module.calendar.form.CalendarTeaserItem;
import org.digijava.module.calendar.util.DbUtil;
import org.digijava.module.common.dbentity.ItemStatus;

/**
 * Renders Calendar Teaser
 */
public class RenderTeaser
     extends TilesAction {

    private static Logger logger = Logger.getLogger(RenderTeaser.class);

    public ActionForward execute(ComponentContext context,
				 ActionMapping mapping,
				 ActionForm form,
				 HttpServletRequest request,
				 HttpServletResponse response) throws
	 IOException,
	 ServletException {

	CalendarForm calendForm = (CalendarForm) form;

	ArrayList eventsList = new ArrayList();
	ArrayList eventsListToday = new ArrayList();
	List dbEventsList = null;
	List dbEventsListToday = null;
	CalendarSettings setting = null;

	int numOfItemsInTeaser = 0;
	int numOfCharsInTitle = 0;

	User user = RequestUtils.getUser(request);

	try {

	    ModuleInstance moduleInstance = RequestUtils.getRealModuleInstance(
		 request);

	    // set number of items in page
	    numOfItemsInTeaser = moduleInstance.getNumberOfItemsInTeaser().
		 intValue();

	    // Get Current events
	    dbEventsList = DbUtil.getCalendarEvents(ItemStatus.PUBLISHED,
		 request, 0, numOfItemsInTeaser, false);

	    // Get Current events including future events
	    dbEventsListToday = DbUtil.getCalendarEvents(ItemStatus.PUBLISHED,
		 request, 0, numOfItemsInTeaser, true);

	    // get Setting
	    numOfCharsInTitle = DbUtil.getNumberOfCharsInTitle(moduleInstance.
		 getSite().getSiteId(),
		 moduleInstance.getInstanceName());

	    if (dbEventsList != null) {
		Iterator it = dbEventsList.iterator();
		while (it.hasNext()) {
		    CalendarTeaserItem item = (CalendarTeaserItem) it.next();
		    item.setNumOfCharsInTitle(numOfCharsInTitle);
		}
	    }
	    //
	    if (dbEventsListToday != null) {
		Iterator it = dbEventsListToday.iterator();
		while (it.hasNext()) {
		    CalendarTeaserItem item = (CalendarTeaserItem) it.next();
		    item.setNumOfCharsInTitle(numOfCharsInTitle);
		}
	    }

	}
	catch (CalendarException ex) {
	    logger.debug("Unable to get information from database ", ex);
	    throw new ServletException(ex.getMessage(), ex);
	}

	calendForm.setNumOfItemsInTeaser(numOfItemsInTeaser);
	calendForm.setEventsList(dbEventsList);
	calendForm.setEventsListToday(dbEventsListToday);

	return null;
    }
}