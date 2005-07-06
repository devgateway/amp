/*
 *   ShowPickupGroup.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Sep 20, 2003
 * 	 CVS-ID: $Id: ShowPickupGroup.java,v 1.1 2005-07-06 10:34:09 rahul Exp $
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
package org.digijava.module.admin.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.admin.form.PickupForm;
import org.digijava.module.admin.helper.pickup.GroupPickupSource;
import org.digijava.module.admin.helper.pickup.PickupItem;

public class ShowPickupGroup
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {
        PickupForm pickupForm = (PickupForm) form;
        LinkedHashSet expanded = new LinkedHashSet();

        String expandKey = request.getParameter("expand");

        Iterator iter = pickupForm.getItems().iterator();
        while (iter.hasNext()) {
            PickupItem currentInfo = (PickupItem)iter.next();
            if (currentInfo.getKey().equals(expandKey)) {
                currentInfo.setExpand(!currentInfo.isExpand());
            }
            if (currentInfo.isExpand()) {
                expanded.add(currentInfo.getKey());
            }
        }

        ArrayList items = GroupPickupSource.getItems(request);
        pickupForm.setItems(new ArrayList());
        generateFlatStructure(0, items, pickupForm.getItems(), expanded);

        return mapping.findForward("forward");
    }

    private void generateFlatStructure(int level, Collection source,
                                           Collection destination, HashSet expanded) {
            if (source == null) {
                return;
            }
            Iterator iter = source.iterator();
            while (iter.hasNext()) {
                PickupItem info = (PickupItem) iter.
                    next();
                info.setLevel(level);
                destination.add(info);
                if (expanded.contains(info.getKey())) {
                    info.setExpand(true);
                    generateFlatStructure(level + 1, info.getChildren(),
                                          destination, expanded);
                }
                else {
                    info.setExpand(false);
                }
            }
        }
}
