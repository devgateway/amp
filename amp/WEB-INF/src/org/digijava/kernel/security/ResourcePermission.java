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

package org.digijava.kernel.security;

import java.io.Serializable;
import java.security.BasicPermission;
import java.security.Permission;
import java.security.PermissionCollection;
import java.util.*;

public abstract class ResourcePermission
    extends BasicPermission implements Serializable  {

    public static final String READ = "READ";
    public static final String WRITE = "WRITE";
    public static final String CONTENT_ADMIN = "CONTENT_ADMIN";
    public static final String ADMIN = "ADMIN";
    public static final String SUPER_ADMIN = "SUPER_ADMIN";
    public static final String TRANSLATE = "TRANSLATE";

    public static final int INT_READ = 0x1;
    public static final int INT_WRITE = 0x3;
    //public final static int INT_ADMIN = 0x7;
    public static final int INT_CONTENT_ADMIN = 0xFF3;
    public static final int INT_ADMIN = 0xFFF3;
    public static final int INT_SUPER_ADMIN = 0xFFFF3;
    public static final int INT_TRANSLATE = 0x8;

    private static final Map actionsToCodes;
    private static final SortedMap actionCodesToActions;
    private static final Comparator reverseComparator;
    protected int actionMask;

    public ResourcePermission(String resource, String actions) {
        super(resource);
        this.actionMask = this.createMask(actions);
    }

    public ResourcePermission(String resource, int action) {
        super(resource);
        this.actionMask = modifySecurityAction(action);
    }

    public ResourcePermission(String resource, Integer action) {
        this(resource, action.intValue());
    }


    public boolean equals(Object obj) {
        if (obj instanceof ResourcePermission) {
            ResourcePermission otherPerm = (ResourcePermission) obj;
            return this.getName().equals(otherPerm.getName()) &&
                this.actionMask == otherPerm.actionMask;
        }
        else {
            return false;
        }
    }

    public int hashCode() {
        return this.getName().hashCode();
    }

    private int createMask(String actions) {
        //parsing the actions into int
        int mask = 0;
        String upperActions = (actions.toUpperCase());
        String[] actionArray = upperActions.split(",");
        //I assume that hashtable is fast enough
        Map actionsMap = getActionsToCodes();
        for (int i = 0; i < actionArray.length; i++) {
            String action = actionArray[i].trim();
            Integer v = (Integer) actionsMap.get(action);
            if (v == null) {
                throw new RuntimeException("Unkonow security action: " + action);
            } else {
                mask = mask | modifySecurityAction(( (Integer) v).intValue());
            }
        }
        return mask;
    }

    public int getActionMask() {
        return actionMask;
    }

    public boolean implies(Permission p) {
        boolean retVal = false;
        if (p instanceof ResourcePermission) {
            ResourcePermission resourcePermission =
                (ResourcePermission) p;
            retVal = this.getName().equals(resourcePermission.getName()) &&
                ( (this.actionMask | resourcePermission.actionMask) ==
                 this.actionMask);
        }

        return retVal;
    }


    static {

        reverseComparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                Comparable c1 = (Comparable)o1;
                return - c1.compareTo(o2);
            }
        };

        actionsToCodes = new HashMap();
        actionsToCodes.put(READ, new Integer(INT_READ));
        actionsToCodes.put(WRITE, new Integer(INT_WRITE));
        actionsToCodes.put(CONTENT_ADMIN, new Integer(INT_CONTENT_ADMIN));
        actionsToCodes.put(ADMIN, new Integer(INT_ADMIN));
        actionsToCodes.put(SUPER_ADMIN, new Integer(INT_SUPER_ADMIN));
        actionsToCodes.put(TRANSLATE,
                           new Integer(INT_TRANSLATE));

        actionCodesToActions = createActionCodeMap(actionsToCodes);
    }

    protected static SortedMap createActionCodeMap(Map actionsToCodes) {
        SortedMap result = new TreeMap(reverseComparator);
        Iterator iter = actionsToCodes.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry item = (Map.Entry)iter.next();
            result.put(item.getValue(), item.getKey());
        }
        return result;
    }

    /**
     * Contains action name to code mapping. Subclasses with different action
     * sets must override this methid
     * @return Map action name to code mapping
     */
    protected Map getActionsToCodes() {
        return actionsToCodes;
    }

    /**
     * Contains action code to name mapping, sorted by codes in
     * <b>descending</b> order. Subclasses with different action sets must
     * override this method.
     * @return SortedMap
     */
    protected SortedMap getCodesToActions() {
        return actionCodesToActions;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<permission name=\"").append(this.getName()).append(
            "\" actionMask=\"").append(actionMask).append("\">");
        buffer.append(getActions());
        buffer.append("</permission>");

        return buffer.toString();
    }

    public PermissionCollection newPermissionCollection() {
        return new ResourcePermissionCollection();
    }

    public static int getActionCode(String action) {
        Integer actionCode = (Integer)actionsToCodes.get(action);
        if (actionCode == null) {
            throw new IllegalArgumentException("Unknown security action " + action);
        }
        return actionCode.intValue();
    }

    public Set getSecurityActions() {
        Set result = new HashSet();
        int tmpMask = actionMask;
        Iterator iter = getCodesToActions().entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry item = (Map.Entry)iter.next();
            int code = ((Integer)item.getKey()).intValue();
            String name = (String)item.getValue();

            if ((code | tmpMask) == tmpMask) {
                result.add(name);
                tmpMask ^= code;
            }
        }
        return result;
    }

    public String getActions() {
        boolean first = true;
        StringBuffer result = new StringBuffer(10);
        Iterator iter = getSecurityActions().iterator();
        while (iter.hasNext()) {
            String item = (String)iter.next();
            if (!first) {
                result.append(',');
            } else {
                first = false;
            }
            result.append(item);
        }

        return result.toString();
    }

    /**
     * Returns array of objects, which will be persisted into database and then
     * used to reconstruct this permission object. Values are sorted by types
     * and order of constructor's parameter
     * @return array of objects
     */
    public Object[] getParameters() {
        return new Object[] { getName(), new Integer(actionMask)};
    }

    /**
     * Returns array of objects with their type names, which will be persisted
     * into database and then used to reconstruct this permission object.
     * Values are sorted by types and order of constructor's parameter
     * @return array of objects
     */
    public TypedParameter[] getTypedParameters() {
        Object[] parameters = getParameters();
        TypedParameter[] result = new TypedParameter[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Class parameterType;
            if (parameters[i] == null) {
                parameterType = Object.class;
            } else {
                parameterType = parameters[i].getClass();
            }
            TypedParameter tp = new TypedParameter(parameterType, parameters[i]);
            result[i] = tp;
        }

        return result;
    }

    public static class TypedParameter {
        private Class parameterClass;
        private Object value;

        public TypedParameter(Class parameterClass, Object value) {
            this.parameterClass = parameterClass;
            this.value = value;
        }

        public Class getParameterClass() {
            return parameterClass;
        }

        public Object getValue() {
            return value;
        }
    }

    /**
     * Change security action, stored in DB to real security action, used by
     * system. This mainly helps during migration process when you want to
     * modify security action to another value. Say, change INT_ADMIN from 0x7
     * to 0xFFF3
     * @param oldAction int action code, passed during creation of permission
     * @return int real security action
     */
    protected int modifySecurityAction(int oldAction) {
        return oldAction;
    }
}
