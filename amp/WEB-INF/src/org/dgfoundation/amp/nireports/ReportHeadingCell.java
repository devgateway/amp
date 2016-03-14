package org.dgfoundation.amp.nireports;

/**
 * The cell holds information about a cell which is to be displayed in a report's heading <br />
 * <b>IMMUTABLE</b> class, please keep it as such!
 * @author Dolghier Constantin
 *
 */
public class ReportHeadingCell {
	
	/**
	 * the starting row in the heading
	 */
	protected final int startRow;
	
	/**
	 * the rowspan in the heading
	 */
	protected final int rowSpan;
	
	/**
	 * the <b>total</b> rowspan (including all the subcolumns) in the heading
	 */
	protected final int totalRowSpan;
	
	/**
	 * the column relative to the heading start where this is in the heading (0-based)
	 */
	protected final int startColumn;
	
	/**
	 * the colspan in the heading
	 */
	protected final int colSpan;
	
	/**
	 * the string to be displayed in the heading cell
	 */
	protected final String name;

	public int getStartRow() {
		return startRow;
	}

	public int getRowSpan() {
		return rowSpan;
	}
	
	public int getTotalRowSpan() {
		return totalRowSpan;
	}

	public int getStartColumn(){
		return startColumn;
	}
	
	public int getColSpan() {
		return colSpan;
	}

	public String getName() {
		return name;
	}

	public ReportHeadingCell(int startRow, int totalRowSpan, int rowSpan, int startColumn, int colSpan, String name)
	{
		if (totalRowSpan <= 0)
			throw new RuntimeException(String.format("column <%s> should have a totalRowSpan of at least 1 in headings, but has %d", name, totalRowSpan));

		if (rowSpan <= 0)
			throw new RuntimeException(String.format("column <%s> should have a rowSpan of at least 1 in headings, but has %d", name, rowSpan));

		if (rowSpan > totalRowSpan)
			throw new RuntimeException(String.format("column <%s> cannot have rowSpan > totalRowSpan, but has %d > %d", name, rowSpan, totalRowSpan));
		
		if (startColumn < 0)
			throw new RuntimeException(String.format("column <%s> cannot have startColumn < 0, but has %d", name, startColumn));
		if (colSpan <= 0)
			throw new RuntimeException(String.format("column <%s> should have a colSpan of at least 1, but has %d", name, colSpan));

		this.startRow = startRow;
		this.totalRowSpan = totalRowSpan;
		this.rowSpan = rowSpan;
		this.startColumn = startColumn;
		this.colSpan = colSpan;
		this.name = name;
	}
		
	@Override
	public String toString()
	{
		return getStringDigest(false);
	}
	
	public String getStringDigest(boolean total) {
		return String.format("RHLC %s: %s", this.getName(), getNumbers(total));
	}
	
	public String getNumbers(boolean totals) {
		return String.format("(startRow: %d, rowSpan: %d, totalRowSpan: %d, colSpan: %d)", this.startRow, this.rowSpan, this.totalRowSpan, this.colSpan);
	}
	
}
