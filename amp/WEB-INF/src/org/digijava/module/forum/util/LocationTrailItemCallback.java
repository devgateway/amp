/*
*   LocationTrailItemCallback.java
*   @Author George Kvizhinadze gio@digijava.org
*   Created:
*   CVS-ID: $Id$
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

package org.digijava.module.forum.util;

public interface LocationTrailItemCallback {
    public String getCaption(Object o);
    public String getParameteName(Object o);
    public long getNavigationParameter(Object o);
    public Object getParentObject(Object o);
    public String getActionName(Object o);
}