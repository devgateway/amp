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

package org.digijava.kernel.translator.util;

/**
 * Wrapper object for cached countries, translated to particular language
 * @see TrnUtil
 */
public class TrnCountry
    implements Comparable {

    private String iso;
    private String name;

    public TrnCountry(String iso, String name) {
        this.iso = iso;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getIso() {
        return iso;
    }

    public boolean equals(Object another) {
        if (another instanceof TrnCountry) {
            return iso.equals( ( (TrnCountry) another).iso);
        }
        else {
            return false;
        }
    }

    public int hashCode() {
        return iso.hashCode();
    }

    public int compareTo(Object o) {
        if (o instanceof TrnCountry) {
            TrnCountry another = (TrnCountry) o;
            return iso.compareTo(another.iso);
        }
        else {
            return -1;
        }
    }
}
