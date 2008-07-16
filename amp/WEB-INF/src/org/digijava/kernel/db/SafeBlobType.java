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

import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

public class SafeBlobType
    implements UserType {

    private Collection createdLobs;

    public SafeBlobType() {
        createdLobs = Collections.synchronizedCollection(new ArrayList());
    }

    private static Logger logger = Logger.getLogger(SafeBlobType.class);

    public int[] sqlTypes() {
        return new int[] {
            Types.BLOB};
    }

    public Class returnedClass() {
        return byte[].class;
    }

    public boolean equals(Object x, Object y) {
        return (x == y)
            || (x != null
                && y != null
                && java.util.Arrays.equals( (byte[]) x, (byte[]) y));
    }

    public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws
        HibernateException, SQLException {
        Blob blob = rs.getBlob(names[0]);
        return blob == null ? null : blob.getBytes(1, (int) blob.length());
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

        byte[] arrayVal = (byte[]) value;
        if ( (arrayVal == null) || (arrayVal.length == 0)) {
            // We could do the same for Oracle blobs with size < 4k.
            // Maybe it's a goood idea...
            st.setBlob(index, (Blob) null);
            logger.debug(
                "Blob is NULL. Update is performed by convertional JDBC routines");
            return;
        }

        LobCreator lobCreator = LOBManager.getLobCreator(st);
        lobCreator.setBlobAsBytes(st, index, arrayVal);

        createdLobs.add(lobCreator);
    }

    public Object deepCopy(Object value) {
        if (value == null)
            return null;

        byte[] bytes = (byte[]) value;
        byte[] result = new byte[bytes.length];
        System.arraycopy(bytes, 0, result, 0, bytes.length);

        return result;
    }

    public boolean isMutable() {
        return true;
    }

    protected void finalize() {
        Iterator iter = createdLobs.iterator();
        while (iter.hasNext()) {
            LobCreator item = (LobCreator) iter.next();
            item.close();
        }
    }
}