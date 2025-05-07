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

package org.digijava.module.translation.security;

import org.digijava.kernel.security.AbstractObjectSecurityManager;
import org.digijava.kernel.security.permission.ObjectPermission;

import java.io.Serializable;
import java.security.Permission;
import java.util.*;

public class TranslatePermission
    extends ObjectPermission implements Serializable {

    private static final long serialVersionUID = 1;

    private static TranslateSecurityManager instance = new
        TranslateSecurityManager();

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

    public TranslatePermission(Long siteId, String localeId, String actions) {
        this(new TranslateObject(siteId, localeId), actions);
    }

    public TranslatePermission(Long siteId, String localeId, Integer action) {
        this(new TranslateObject(siteId, localeId), action);
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
        TranslateObject translateObject = getTranslateId();
        TranslateObject otherTranslateObject = translatePermission.
            getTranslateId();
        if (translateObject != null) {
            if (!translateObject.contains(otherTranslateObject)) {
                return false;
            }
        }
        return super.implies(translatePermission);

        /*
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
         */
    }

    public TypedParameter[] getTypedParameters() {
        if (getTranslateId() != null) {
            TypedParameter[] result = new TypedParameter[] {
                new TypedParameter(Long.class, getTranslateId().getSiteId()),
                new TypedParameter(String.class, getTranslateId().getLocaleId()),
                new TypedParameter(Integer.class, new Integer(actionMask))
            };

            return result;
        }
        else {
            TypedParameter[] result = new TypedParameter[] {
                new TypedParameter(Integer.class, new Integer(actionMask))
            };
            return result;
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

    public AbstractObjectSecurityManager getSecurityManager() {
        return instance;
    }

}
