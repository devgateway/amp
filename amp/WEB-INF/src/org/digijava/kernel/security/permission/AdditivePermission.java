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

package org.digijava.kernel.security.permission;

import java.security.Permission;

public interface AdditivePermission  {

    /**
     * Returns result of plus operation on permissions. For example, you can
     * add EDIT permission on some object to ADMIN permission on the same
     * object
     * @param permission Permission which must be added
     * @return result of addition operation
     */
    public AdditivePermission plus(Permission permission);

    /**
     * Returns result of minus operation on permissions. For example, you can
     * exclude EDIT permission on some object from ADMIN permission on the same
     * object
     * @param permission Permission which must be excluded
     * @return result of exclude operation
     */
    public AdditivePermission minus(Permission permission);

    /**
     * Verifies if permission, passed by parameter is "relative" or, in other
     * words, can be used in "plus" and "minus" operations. For example - same
     * target classes, same object ID, etc
     * @param permission Permission for which relativeness must be checked
     * @return true, if permission is "relative". false - if not
     */
    public boolean isRelativePermission(Permission permission);

}
