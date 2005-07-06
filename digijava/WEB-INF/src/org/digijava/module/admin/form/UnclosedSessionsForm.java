/*
 *   UnclosedSessionsForm.java
 * 	 @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Jul 16, 2004
       * 	 CVS-ID: $Id: UnclosedSessionsForm.java,v 1.1 2005-07-06 10:34:13 rahul Exp $
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

package org.digijava.module.admin.form;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import net.sf.hibernate.Session;

public class UnclosedSessionsForm
      extends ActionForm {

    public static class UnclosedSessionInfo {
	private Session key;
	private String value;
	private boolean closed;

	public void setKey(Session key) {
	    this.key = key;
	}

	public Session getKey() {
	    return key;
	}

	public void setValue(String value) {
	    this.value = value;
	}

	public String getValue() {
	    return value;
	}

	public void setClosed(boolean closed) {
	    this.closed = closed;
	}

	public boolean isClosed() {
	    return closed;
	}
    }

    private List unclosedSeesions;
    private long totalCount;
    private long totalOpened;
    private long totalClosed;

    private int index;

    private boolean showAll;

    public long getTotalCount() {
	return totalCount;
    }

    public void setTotalCount(long totalCount) {
	this.totalCount = totalCount;
    }

    public List getUnclosedSeesions() {
	return unclosedSeesions;
    }

    public void setUnclosedSeesions(List unclosedSeesions) {
	this.unclosedSeesions = unclosedSeesions;
    }

    public int getIndex() {
	return index;
    }

    public void setIndex(int index) {
	this.index = index;
    }

    public boolean isShowAll() {
	return showAll;
    }

    public void setShowAll(boolean showAll) {
	this.showAll = showAll;
    }

    public long getTotalClosed() {
	return totalClosed;
    }

    public void setTotalClosed(long totalClosed) {
	this.totalClosed = totalClosed;
    }

    public long getTotalOpened() {
	return totalOpened;
    }

    public void setTotalOpened(long totalOpened) {
	this.totalOpened = totalOpened;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
	index = -1;
	totalCount = 0;
	totalOpened = 0;
	totalClosed = 0;
    }
}