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

import java.util.Collection;
import java.util.Comparator;

/**
 * Helper class, needed for internal needs of CollectionUtils class methods.
 * Defines object's hierarchy.
 * @version $Id: HierarchyMember.java,v 1.1 2008-07-16 09:19:41 ktha Exp $
 */
public class HierarchyMember {
    protected Object member;
    protected Object parentMember;
    protected Collection children;
    protected int level;
    protected boolean visible = true;
    protected boolean expandChildren = true;

    public Collection getChildren() {
        return children;
    }

    public Object getMember() {
        return member;
    }

    public Object getParentMember() {
        return parentMember;
    }

    public int getLevel() {
        return level;
    }

    public boolean isExpandChildren() {
        return expandChildren;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setChildren(Collection children) {
        this.children = children;
    }

    public void setMember(Object member) {
        this.member = member;
    }

    public void setParentMember(Object parentMember) {
        this.parentMember = parentMember;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setExpandChildren(boolean expandChildren) {
        this.expandChildren = expandChildren;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public static Comparator createComparatorByMember(Comparator comparator) {
        return new HierarchyMemberComparatorByMember(comparator);
    }
}

class HierarchyMemberComparatorByMember
    implements Comparator {
    private Comparator objectComparator;

    public HierarchyMemberComparatorByMember(Comparator objectComparato) {
        this.objectComparator = objectComparato;
    }

    public int compare(Object o1, Object o2) {
        HierarchyMember member1 = (HierarchyMember) o1;
        HierarchyMember member2 = (HierarchyMember) o2;
        return objectComparator.compare(member1.getMember(), member2.getMember());
    }

}
