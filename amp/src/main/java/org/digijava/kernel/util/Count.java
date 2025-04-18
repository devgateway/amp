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

package org.digijava.kernel.util;

/**
 * This class can be used as a Value Object for
 * sending the count of any entity.
*/
public class Count implements java.io.Serializable {

    /**
    * Stores the count
    */
    private long count;

    public Count() {
    }

    public Count(long count) {
        this.count = count;
    }

    /**
     * Gets the count value
     * @return The count
    */
    public long getCount() {
        return this.count;
    }

    /**
     * Sets the count value
     * @param count The new Count value
    */

    public void setCount(long count) {
        this.count = count;
    }

    public String toString() {
        return "Count :" + count;
    }
}
