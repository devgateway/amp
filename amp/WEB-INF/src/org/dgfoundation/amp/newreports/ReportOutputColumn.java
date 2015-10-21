package org.dgfoundation.amp.newreports;

import java.util.ArrayList;
import java.util.List;

import org.digijava.kernel.translator.TranslatorWorker;

/**
 * class holding metadata about a report-output-column. It also holds  A "column" in this context might be either a Measure or a Column - anything which sits on the X axis, actually
 * @author Dolghier Constantin
 *
 */
public class ReportOutputColumn implements Comparable<ReportOutputColumn> {
	
	/**
	 * the <strong>localized</strong> name of the column<br />
	 * This one is not <strong>final</strong> because of a hack I had to do for AMP-21282<br />
	 * No, I am not a shitcoder - the decision to use Mondrian in AMP is shit thing and it wasn't me who wanted it :P
	 */
	public String columnName;
	
	/**
	 * the parent column, if any. Might be null for top-level columns, like "Funding" or "Project Title"
	 */
	public final ReportOutputColumn parentColumn;
	
	
	/**
	 * the <strong> unlocalized</strong> name of the column
	 */
	public final String originalColumnName;
	
	public String description;
	
	transient
	public final List<ReportOutputColumn> children = new ArrayList<ReportOutputColumn>();
	
	public ReportOutputColumn(String columnName, ReportOutputColumn parentColumn, String originalColumnName) {
		this.columnName = columnName;
		if (columnName == null || columnName.isEmpty())
			throw new NullPointerException();
		this.parentColumn = parentColumn;
		this.originalColumnName = originalColumnName;
		if (parentColumn !=null) {
			this.parentColumn.children.add(this);
		}
	}
	
	public static ReportOutputColumn buildTranslated(String originalColumnName, String locale, ReportOutputColumn parentColumn){
		return new ReportOutputColumn(TranslatorWorker.translateText(originalColumnName, locale, 3l), parentColumn, originalColumnName);
	}
	
	/**
	 * moves up a number of levels OR until null is reached
	 * @param levels
	 * @return
	 */
	public ReportOutputColumn moveUp(int levels) {
		if (levels == 0) return this;
		if (this.parentColumn == null) return parentColumn;
		return this.parentColumn.moveUp(levels - 1);
	}
	
	/**
	 * computes the full name of the column like, for example, [Funding][2007][Actual Commitments]
	 * <strong>unlocalized</strong>
	 * @return
	 */
	public String getHierarchicalName() {
		String res = String.format("[%s]", this.originalColumnName);
		if (parentColumn != null)
			res = parentColumn.getHierarchicalName() + res;
		return res;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		return this.getHierarchicalName();
	}
	
	@Override
	public int hashCode() {
		return getHierarchicalName().hashCode();
	}
	
	@Override public boolean equals(Object oth) {
		return this.getHierarchicalName().equals(((ReportOutputColumn) oth).getHierarchicalName());
	}
	
	@Override public int compareTo(ReportOutputColumn oth) {
		return this.getHierarchicalName().compareTo(oth.getHierarchicalName());
	}

}
