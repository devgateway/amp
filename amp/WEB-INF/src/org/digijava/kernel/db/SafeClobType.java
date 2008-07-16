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
            st.setClob(index, (Clob) null);

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