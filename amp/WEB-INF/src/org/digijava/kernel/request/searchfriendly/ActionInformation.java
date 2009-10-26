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

package org.digijava.kernel.request.searchfriendly;

/**
 * Information about DiGi action, needed for search-friendly system
 */
public interface ActionInformation {
    /**
     * Returns module name
     * @return String
     */
    String getModule();

    /**
     * Returns instance name
     * @return String
     */
    String getInstance();

    /**
     * Returns action (without module and instance). Used for filder config
     * patterns
     * @return String
     */
    String getAction();

    /**
     * Returns prefix, which should be cutted away during action forward. In
     * most cases this is a servlet context
     * @return String
     */
    String getPrefix();
}
