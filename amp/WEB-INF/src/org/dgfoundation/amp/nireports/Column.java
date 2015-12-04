package org.dgfoundation.amp.nireports;

/**
 * <strong>immutable</strong>. It is an almost-mirror of ReportOutputColumn, but we can't use that one, because that one has broken 2 very important invariants for the sake of crappy Mondrian: <br />
 * 1. immutability 1 (ReportOutputColumn mutates its parent on construction)
 * 2. immutability 2 (ReportOutputColumn.name is not final because of MTEF hacks).
 * 
 * The long-term plan is to get rid of Mondrian, thus being able to reuse the API class here
 * @author Dolghier Constantin
 *
 */
public abstract class Column {
	public final String name;
	public final GroupColumn parent;
			
	protected Column(String name, GroupColumn parent) {
		this.name = name;
		this.parent = parent;
	}
	
	public String getHierName() {
		if (parent != null)
			return String.format("%s / %s", parent.getHierName(), name);
		return name;
	}
	
	@Override
	public String toString() {
		return getHierName();
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
