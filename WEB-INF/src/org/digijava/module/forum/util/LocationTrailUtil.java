/*
*   LocationTrailUtil.java
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

import java.util.Collection;
import java.util.ArrayList;
import java.util.Map;
import java.util.Collections;
import java.util.List;

public class LocationTrailUtil {
    public LocationTrailUtil() {
    }

    public static List getTrailItems (Object obj, Map callbackMap) {
        ArrayList retVal = new ArrayList();
        do {
            LocationTrailItemCallback callback =
                (LocationTrailItemCallback) callbackMap.get(obj.getClass());

            LocationTrailItem item = new LocationTrailItem();

            item.setCaption(callback.getCaption(obj));
            item.setActionName(callback.getActionName(obj));
            item.setParamName(callback.getParameteName(obj));
            item.setParamValue(callback.getNavigationParameter(obj));
            if (callback.getParentObject(obj) == null) {
                item.setItemType(LocationTrailItem.TOP_ITEM);
            }
            retVal.add(item);
            obj = callback.getParentObject(obj);
        } while (obj != null);
        Collections.reverse(retVal);
        return retVal;
    }

}