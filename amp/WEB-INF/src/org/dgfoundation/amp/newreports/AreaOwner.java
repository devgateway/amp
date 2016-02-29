package org.dgfoundation.amp.newreports;

/**
 * the split cell which has generated a subreport
 * TODO: if there is real need for it, make the field rich
 * @author Dolghier Constantin
 *
 */
public class AreaOwner {
	public final String columnName;
	public final String debugString;
	
	public AreaOwner(String columnName, String debugString) {
		this.columnName = columnName;
		this.debugString = debugString;
	}
	
	@Override
	public boolean equals(Object oth) {
		if (!(oth instanceof AreaOwner))
			return false;
		AreaOwner other = (AreaOwner) oth;
		return this.columnName.equals(other.columnName) && this.debugString.equals(other.debugString);
	}
	
	@Override
	public int hashCode() {
		return this.columnName.hashCode() + 19 * this.debugString.hashCode();
	}
	
	@Override
	public String toString() {
		return String.format("colName: %s, debugString: %s", columnName, debugString);
	}
}
