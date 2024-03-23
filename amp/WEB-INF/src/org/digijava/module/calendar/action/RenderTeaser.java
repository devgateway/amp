/**
 * This file is part of DiGi project (www.digijava.org).
 * DiGi is a multi-site portal system written in Java/J2EE.
 *
 * Copyright (C) 2002-2007 Development Gateway Foundation, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 */

package org.digijava.module.calendar.action;

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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
