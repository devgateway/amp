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

package org.digijava.kernel.util.collections;

import java.util.Comparator;

/**
 * This class used by CollectionUtils.synchronizeSets metod. It defines rules
 * for items, which exist in source set and not in destination and items, which
 * exist in both of them
 * @author not attributable
 * @version 1.0
 */
public interface CollectionSynchronizer
    extends Comparator {

    /**
     * synchronizeSets method will call this method for the object which exists
     * in the source list and not in destionation. If method returns true, it
     * means that object will not be included in result. If false, then
     * synchronizeSets will assume that the object will modified by this method
     * and include it in the result.
     * @param item Object from the source list
     * @return boolean value which defines action, taken by synchronizeSets
     * method
     */
    public boolean removeEvent(Object item);

    /**
     * synchronizeSets method will call this method for the object which exists
     * both in the source an destination lists. If this method returns true,
     * synchronizeSets will assume that existingObj object was modified during
     * the call and include it in the result set. Otherwise newObj will be
     * included there
     * @param existingObj Object from the source list
     * @param newObj Object from the destination list
     * @return boolean value which defines action, taken by synchronizeSets
     * method
     */
    public boolean synchronizeEvent(Object existingObj, Object newObj);

}
