/*
 *   TranslatePermission.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Oct 10, 2004
 * 	 CVS-ID: $Id: TranslatePermission.java,v 1.1 2005-07-06 10:34:25 rahul Exp $
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

package org.digijava.module.translation.security;

import java.io.Serializable;
import java.security.Permission;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.digijava.kernel.security.permission.ObjectPermission;

public class TranslatePermission
      extends ObjectPermission
      implements Serializable {

    private static final long serialVersionUID = 1;

    public static final String TRANSLATE = "TRANSLATE";
    public static final int INT_TRANSLATE = 0x1;

    private static Map actionList;
    private static SortedMap reverseActionList;

    static {
	actionList = new HashMap();

	actionList.put(TRANSLATE, new Integer(INT_TRANSLATE));
	reverseActionList = createActionCodeMap(actionList);
    }

    public TranslatePermission(TranslateObject translation, String actions) {
	super(TranslateObject.class.getName(), translation, actions);
    }

    public TranslatePermission(TranslateObject translation, int action) {
	super(TranslateObject.class.getName(), translation, action);
    }

    public TranslatePermission(TranslateObject translation, Integer action) {
	super(TranslateObject.class.getName(), translation, action);
    }

    public TranslatePermission(int action) {
	super(TranslateObject.class.getName(), action);
    }

    public TranslatePermission(Integer action) {
	super(TranslateObject.class.getName(), action.intValue());
    }

    public TranslateObject getTranslateId() {
	return (TranslateObject) instanceId;
    }

    public boolean implies(Permission permission) {

	boolean result = false;
	if (permission instanceof TranslatePermission) {
	    result = implies( (TranslatePermission) permission);
	}

	return result;
    }

    private boolean implies(TranslatePermission translatePermission) {
	boolean result = false;

	TranslateObject translateObject = getTranslateId();
	TranslateObject otherTranslateObject = translatePermission.
	      getTranslateId();

	if (translateObject != null && otherTranslateObject != null &&
	    !translateObject.equals(otherTranslateObject)) {

	    if (translateObject.getSiteId().equals(new Long(0))) {
		if (!translateObject.getLocaleId().equals(TranslateObject.
		      LOCALE_CODE_ALL) && !translateObject.getLocaleId().equals(
		      otherTranslateObject.getLocaleId())) {
		    return false;
		}
		else {
		    return true;
		}
	    }
	    else {
		if (!translateObject.getSiteId().equals(otherTranslateObject.
		      getSiteId())) {
		    return false;
		}
		if (!translateObject.getLocaleId().equals(TranslateObject.
		      LOCALE_CODE_ALL) && !translateObject.getLocaleId().equals(
		      otherTranslateObject.getLocaleId())) {
		    return false;
		}
		else {
		    return true;
		}
	    }
	}
	else {
	    return super.implies(translatePermission);
	}
    }

    /**
     * Returns array of objects, which will be persisted into database and then
     * used to reconstruct this permission object. Values are sorted by types
     * and order of constructor's parameter
     * @return array of objects
     */
    public Object[] getParameters() {

	if (getTranslateId() != null) {
	    return new Object[] {
		  getTranslateId(),
		  new Integer(actionMask)};
	}
	else {
	    return new Object[] {
		  new Integer(actionMask)};
	}
    }

    protected Map getActionsToCodes() {
	return actionList;
    }

    protected SortedMap getCodesToActions() {
	return reverseActionList;
    }

    public static int getActionsCode(String action) {
	Integer actionCode = (Integer) actionList.get(action);
	if (actionCode == null) {
	    throw new IllegalArgumentException("Unknown security action " +
					       action);
	}
	return actionCode.intValue();
    }

    protected static SortedMap createActionCodeMap(Map actionList) {
	reverseActionList = new TreeMap();

	Set keySet = actionList.keySet();
	Iterator iter = keySet.iterator();
	while (iter.hasNext()) {
	    String key = (String) iter.next();
	    reverseActionList.put( (Integer) actionList.get(key), key);
	}
	return reverseActionList;
    }
}