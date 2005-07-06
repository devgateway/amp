/*
*   SafeClobType.java
*   @Author Mikheil Kapanadze mikheil@digijava.org
*   Created:
*   CVS-ID: $Id: SafeClobType.java,v 1.1 2005-07-06 10:34:07 rahul Exp $
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

package org.digijava.kernel.db;

import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.springframework.jdbc.support.lob.LobCreator;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.UserType;

public class SafeClobType
    implements UserType {

    private static Logger logger = Logger.getLogger(SafeClobType.class);

    private Collection createdLobs;

    public SafeClobType() {
        createdLobs = Collections.synchronizedCollection(new ArrayList());
    }


    public int[] sqlTypes() {
        return new int[] {
            Types.CLOB};
    }

    public Class returnedClass() {
        return String.class;
    }

    public boolean equals(Object x, Object y) {
        return (x == y)
            || (x != null
                && y != null
                && ( (String) x).equals(y));
    }

    public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws
        HibernateException, SQLException {

        Clob clob = null;
        if (names.length > 0) {

            clob = rs.getClob(names[0]);

        }

        else {
            ResultSetMetaData rsmd = rs.getMetaData();
            int count = rsmd.getColumnCount();
            int i = 1;
            while (i <= count) {
                if (rsmd.getColumnType(i) == Types.CLOB)
                    break;
                i++;
            }
            clob = rs.getClob(i);
        }

        return clob == null ? null : clob.getSubString(1, (int) clob.length());
    }

    /**
     *
     * @param st
     * @param value
     * @param index
     * @throws HibernateException
     * @throws SQLException
     */
    public void nullSafeSet(PreparedStatement st, Object value, int index) throws
        HibernateException, SQLException {

        String strVal = (String) value;
        if ( (strVal == null) || (strVal.length() == 0)) {
            st.setClob(index, null);

            logger.debug(
                "CLOB is NULL. Update is performed by convertional JDBC routines");
            return;
        }

        LobCreator lobCreator = LOBManager.getLobCreator(st);
        lobCreator.setClobAsString(st, index, strVal);

        createdLobs.add(lobCreator);

    }

    public Object deepCopy(Object value) {

        if (value == null)
            return null;
        String retVal = new String( (String) value);
        return retVal;
    }

    public boolean isMutable() {
        return false;
    }

    protected void finalize() {
        Iterator iter = createdLobs.iterator();
        while (iter.hasNext()) {
            LobCreator item = (LobCreator) iter.next();
            item.close();
        }
    }

}