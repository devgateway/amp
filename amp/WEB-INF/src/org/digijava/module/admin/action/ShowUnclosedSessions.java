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

package org.digijava.module.admin.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.admin.form.UnclosedSessionsForm;
import org.hibernate.Session;
/**
 * 
 * @deprecated
 *
 */
public class ShowUnclosedSessions
      extends Action {

    public ActionForward execute(ActionMapping mapping,
				 ActionForm form,
				 javax.servlet.http.HttpServletRequest
				 request,
				 javax.servlet.http.HttpServletResponse
				 response) throws java.lang.Exception {

	UnclosedSessionsForm formBean = (UnclosedSessionsForm) form;

	if (!RequestUtils.getUser(request).isGlobalAdmin()) {
	    return new ActionForward("/admin/index", true);
	}

	if (request.getParameter("show") == null ||
	    (request.getParameter("show") != null && request.getParameter("show").equalsIgnoreCase("all"))) {
	    formBean.setShowAll(true);
	} else {
	    formBean.setShowAll(false);
	}

	Map unclosedSeesionsMap = null; // PersistenceManager.getUnclosedSessions();
	long totalCount = 0;
	long totalClosed = 0;

	if (unclosedSeesionsMap != null && unclosedSeesionsMap.size() != 0) {
	    boolean deleting = false;

	    List unclosedSessions = new ArrayList();

	    Set keySet = unclosedSeesionsMap.keySet();
	    Iterator iter = keySet.iterator();
	    while (iter.hasNext()) {
		UnclosedSessionsForm.UnclosedSessionInfo si = new
		      UnclosedSessionsForm.UnclosedSessionInfo();
		Session key = (Session) iter.next();
		String value = (String) unclosedSeesionsMap.get(key);

		si.setKey(key);
		si.setValue(value);

		if (!key.isOpen()) {
		    si.setClosed(true);
		    ++totalClosed;
		} else {
		    si.setClosed(false);
		}
		if (formBean.isShowAll()) {
		    unclosedSessions.add(si);
		} else {
		    if (!si.isClosed()) {
			unclosedSessions.add(si);
		    }
		}
		++totalCount;
	    }
	    formBean.setUnclosedSeesions(unclosedSessions);
	    formBean.setTotalCount(totalCount);
	    formBean.setTotalClosed(totalClosed);
	    formBean.setTotalOpened(totalCount - totalClosed);

	    if (formBean.getIndex() >= 0) {

		UnclosedSessionsForm.UnclosedSessionInfo si = (
		      UnclosedSessionsForm.UnclosedSessionInfo)
		      unclosedSessions.get(
		      formBean.getIndex());

		PersistenceManager.releaseSession(si.getKey());
		deleting = true;
	    }


	    if (deleting) {
		return new ActionForward("/admin/showUnclosedSessions.do", true);
	    }
	    else {
		return mapping.findForward("forward");
	    }

	}
	else {
	    formBean.setUnclosedSeesions(null);
	    formBean.setTotalCount(0);
	}
	return mapping.findForward("forward");
    }
}