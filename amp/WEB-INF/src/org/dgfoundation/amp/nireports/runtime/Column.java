package org.dgfoundation.amp.nireports.runtime;

import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.ComparableValue;

/**
 * column-with-contents
 *
 */
public abstract class Column {
	public final String name;
	
	/**
	 * might be null
	 */
	protected GroupColumn parent;
	
	/** returns the ids of all the primary entities enclosed in this column and all of its subcolumns (if any) */
	public abstract Set<Long> getIds();
	public abstract void forEachCell(Consumer<NiCell> acceptor);
	public abstract GroupColumn verticallySplitByCategory(VSplitStrategy strategy);
	public abstract String debugDigest(boolean withContents);
			
	protected Column(String name, GroupColumn parent) {
		this.name = name;
		this.parent = parent;
	}
	
	public String getHierName() {
		if (parent != null)
			return String.format("%s / %s", parent.getHierName(), name);
		return name;
	}
	
	public GroupColumn getParent() {
		return parent;
	}

	public void setParent(GroupColumn parent) {
		this.parent = parent;
	}

	@Override
	public String toString() {
		return debugDigest(false);
		//return String.format("%s %s", this.getClass().getSimpleName(), getHierName());
	}
	
	@Override
	public int hashCode() {
		return getHierName().hashCode();
	}
	
	@Override
	public boolean equals(Object oth) {
		Column other = (Column) oth;
		return getHierName().equals(other.getHierName());
	}
}
