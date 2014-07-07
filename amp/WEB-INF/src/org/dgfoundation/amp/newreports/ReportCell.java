package org.dgfoundation.amp.newreports;

/**
 * class describing a report cell
 * @author Dolghier Constantin
 *
 */
public abstract class ReportCell implements Comparable<ReportCell> {
	public final Comparable value;
	public final String displayedValue;
	
	public ReportCell(Comparable<?> value) {
		this.value = value;
		if (this.value == null)
			throw new NullPointerException();
		this.displayedValue = value.toString();
	}
	
	@Override public int compareTo(ReportCell oth) {
		return this.value.compareTo(oth.value);
	}
	
	@Override public boolean equals(Object oth) {
		if (!(oth instanceof ReportCell))
			return false;
		ReportCell other = (ReportCell) oth;
		return this.compareTo(other) == 0;
	}
	
	@Override public int hashCode() {
		return this.value.hashCode();
	}
	
	@Override public String toString() {
		return String.format("[%s]", this.displayedValue);
	}
}
