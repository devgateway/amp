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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Class contains utillities which manipulate collections. Such as
 * synchronization of child lists for hibernate-persisted classes, etc.
 * @author $Id: CollectionUtils.java,v 1.1 2008-07-16 09:19:41 ktha Exp $
 * @version $Version$
 */
public class CollectionUtils {

    private static final HierarchyMemberFactory defaultHierarchyMemberFactory =
        new DefaultHierarchyMemberFactory();

 

    /**
     * Generate object's hierarchy within the collection and returns collection
     * of the parent nodes
     * @param source Source collection
     * @param definition HierarchyDefinition object, which defines hierarchy
     * of objects, contained in the source collection
     * @return Collection of the HierarchyMember objects
     */
    public static Collection getHierarchy(Collection source, HierarchyDefinition definition) {
        return getHierarchy(source, definition, null);
    }

    public static Collection getHierarchy(Collection source, HierarchyDefinition definition, HierarchyMemberFactory factory) {
        if (source == null) {
            return null;
        }
        if (definition == null) {
            throw new IllegalArgumentException("HierarchyDefinition, passed to getHierarchy method must be not-null");
        }
        HierarchyMemberFactory realFactory = factory == null ?
            defaultHierarchyMemberFactory : factory;

        ArrayList result = new ArrayList();
        HashMap objectStorage = new HashMap();

        Iterator iter = source.iterator();
        while (iter.hasNext()) {
            Object item = (Object) iter.next();
            Object itemId = definition.getObjectIdentity(item);
            if (itemId == null) {
                throw new IllegalArgumentException("getObjectIdentity() must return not-null for ALL objects");
            }
            Object parentItemId = definition.getParentIdentity(item);

            HierarchyMember thisMember = ensureHierarchyMember(objectStorage, itemId, item, realFactory);

            if (parentItemId != null) {
                HierarchyMember parentMember = ensureHierarchyMember(objectStorage, parentItemId, null, realFactory);
                thisMember.setParentMember(parentMember);
                parentMember.getChildren().add(thisMember);
            } else {
                result.add(thisMember);
            }
        }

        return result;
    }

    private static HierarchyMember ensureHierarchyMember(HashMap objectStorage,
        Object parentItemId, Object object, HierarchyMemberFactory factory) {
        HierarchyMember hierarchyMember = (HierarchyMember) objectStorage.get(
            parentItemId);
        if (hierarchyMember == null) {
            hierarchyMember = factory.createHierarchyMember();
            objectStorage.put(parentItemId, hierarchyMember);
        }
        if (object != null) {
            hierarchyMember.setMember(object);
        }
        return hierarchyMember;
    }

    public static Collection getFlatHierarchy(Collection source, boolean expandObjects, HierarchyDefinition definition, Comparator comparator) {
        return getFlatHierarchy(source, expandObjects, definition, comparator, null);

    }
    public static Collection getFlatHierarchy(Collection source, boolean expandObjects, HierarchyDefinition definition, Comparator comparator, HierarchyMemberFactory factory) {
        Collection hierarchy = getHierarchy(source, definition, factory);
        if (hierarchy == null) {
            return null;
        }

        ArrayList result = new ArrayList();
        HierarchyMember rootMember = new HierarchyMember();
        rootMember.setMember(null);
        rootMember.setParentMember(null);
        rootMember.setVisible(true);
        rootMember.setChildren(hierarchy);

        putHierarchyMember(result, expandObjects, -1, rootMember, comparator);
        return result;
    }

    private static void putHierarchyMember(Collection storage, boolean expandObjects, int level, HierarchyMember parentMember, Comparator comparator) {
        if (!parentMember.isVisible()) {
            return;
        }

        if (parentMember.getMember() != null) {
            if (expandObjects) {
                parentMember.setLevel(level);
                storage.add(parentMember);
            } else {
            storage.add(parentMember.getMember());
            }
        }
        if (!parentMember.isExpandChildren()) {
            return;
        }

        // We know that this is ArrayList and that sorting will not damage anything
        if (comparator != null) {
            Comparator memberComparator = HierarchyMember.createComparatorByMember(
                comparator);
            Collections.sort( (ArrayList) parentMember.getChildren(),
                             memberComparator);
        }
        Iterator iter = parentMember.getChildren().iterator();
        while (iter.hasNext()) {
            HierarchyMember item = (HierarchyMember) iter.next();
            putHierarchyMember(storage, expandObjects, level+1, item, comparator);
        }
    }
}
class DefaultHierarchyMemberFactory
    implements HierarchyMemberFactory {

    public HierarchyMember createHierarchyMember() {
        HierarchyMember hierarchyMember = new HierarchyMember();
        hierarchyMember.setChildren(new ArrayList());
        hierarchyMember.setParentMember(null);

        return hierarchyMember;
    }
}
