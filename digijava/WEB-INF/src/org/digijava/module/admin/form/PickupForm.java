/*
 *   PickupForm.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Sep 22, 2003
 * 	 CVS-ID: $Id: PickupForm.java,v 1.1 2005-07-06 10:34:13 rahul Exp $
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

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import java.util.Collection;
import org.digijava.kernel.entity.Locale;
import java.util.ArrayList;
import java.util.List;
import org.digijava.kernel.util.SiteConfigUtils;
import java.util.HashMap;
import java.util.Collections;
import java.util.HashSet;
import org.digijava.kernel.request.SiteDomain;
import java.util.Iterator;
import org.digijava.kernel.request.Site;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import java.util.Collection;
import org.digijava.kernel.entity.Locale;
import java.util.ArrayList;
import java.util.List;
import org.digijava.kernel.util.SiteConfigUtils;
import java.util.HashMap;
import java.util.Collections;
import java.util.HashSet;
import org.digijava.kernel.request.SiteDomain;
import java.util.Iterator;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.security.ResourcePermission;
import org.digijava.module.admin.helper.pickup.PickupItem;

public class PickupForm
    extends ActionForm {

    private ArrayList items;
    private String targetAction;

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        items = new ArrayList();
    }

    public ActionErrors validate(ActionMapping actionMapping,
                                 HttpServletRequest httpServletRequest) {
        return null;
    }

    public String getTargetAction() {
        return targetAction;
    }

    public void setTargetAction(String targetAction) {
        this.targetAction = targetAction;
    }

    public PickupItem getItem(int index) {
        PickupItem info = null;
        int actualSize = items.size();
        if (index >= actualSize) {
            // Expand the list
            for (int i = 0; i <= index - actualSize; i++) {
                items.add(new PickupItem());
            }
        }
        return (PickupItem) items.get(index);
    }

    public ArrayList getItems() {
        return items;
    }

    public void setItems(ArrayList items) {
        this.items = items;
    }

}