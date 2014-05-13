package org.dgfoundation.amp.testmodels;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.dgfoundation.amp.ar.Column;
import org.dgfoundation.amp.ar.ColumnReportData;
import org.dgfoundation.amp.ar.cell.AmountCell;
import org.dgfoundation.amp.testutils.ColumnComparator;

/**
 * a model (sketch) for a flat (sub)report - a ColumnReportData instance
 * @author Dolghier Constantin
 *
 */
public class ColumnReportDataModel extends ReportModel {
	
	/**
	 * ColumnModel instances
	 */
	ReportModel[] columnsModels;
	private String[] trailCells;
	
	private ColumnReportDataModel(String name, ColumnModel[] columns)
	{
		super(name);
		this.columnsModels = sort(columns);
	}
	
	/**
	 * optional-column-enumeration NOT implemented yet
	 * @param name
	 * @param columns
	 * @return
	 */
	public static ColumnReportDataModel withColumns(String name, ColumnModel... columns)
	{
		return new ColumnReportDataModel(name, columns);
	}
	
	public ColumnReportDataModel withTrailCells(String... trailCells)
	{
		this.trailCells = trailCells;
		return this;
	}
	
	public String matches(ColumnReportData crd)
	{
		if (this.getName().compareTo(crd.getName()) != 0)
			return String.format("CRD name mismatch: %s vs %s", crd.getName(), this.getName());
			
		if (trailCells != null)
		{
			String trailCellComparisonResult = matches_trail_cells(crd);
			if (trailCellComparisonResult != null)
				return trailCellComparisonResult;
		}
		
		Column[] sortedColumns = getAndSortColumns(crd);
		if (sortedColumns.length != columnsModels.length)
			return String.format("CRD %s #of columns (%d) != model #of columns (%d)", crd.getName(), sortedColumns.length, columnsModels.length);
		
		for(int i = 0; i < sortedColumns.length; i++)
		{
			Column column = sortedColumns[i];
			ReportModel columnModel = columnsModels[i];
			
			String compareResult = null;
			if (columnModel instanceof SimpleColumnModel)
				compareResult = ((SimpleColumnModel) columnModel).matches(column);
			else
				if (columnModel instanceof GroupColumnModel)
					compareResult = ((GroupColumnModel) columnModel).matches(column);
				else
					compareResult = String.format("unknown column mode in CRD model %s", this.getName()); 
			if (compareResult != null)
				return compareResult;
		}
		return null;
	}
	
	protected String matches_trail_cells(ColumnReportData grd)
	{
		List<AmountCell> cells = grd.getTrailCells();
		if (cells == null)
			return String.format("CRD %s has no trail cells, but should have %d", this.getName(), trailCells.length);
		
		if (cells.size() != trailCells.length)
			return String.format("CRD %s has %d trail cells, but should have %d", this.getName(), cells.size(), trailCells.length);
			
		for(int i = 0; i < trailCells.length; i++)		
		{
			AmountCell cell = cells.get(i);
			
			String cellContents = cell == null ? null : cell.toString();			
			String correctCell = trailCells[i];

			String rs = compareCells(cellContents, correctCell, i);
			if (rs != null)
				return rs;
		}
		return null;
	}
	
	protected String compareCells(String cellContents, String correctCell, int i)
	{
		if (cellContents == null)
			cellContents = "<null>";
		
		if (correctCell == null)
			correctCell = "<null>";
		
		if (!correctCell.equals(cellContents))
			return String.format("CRD %s has trail cell %d equal %s instead of %s", this.getName(), i, cellContents, correctCell);
		
		return null;
	}	
	
	/**
	 * returns list of children sorted by name
	 * @param grd
	 * @return
	 */
	protected Column[] getAndSortColumns(ColumnReportData grd)
	{
		Column[] res = new Column[grd.getItems().size()];
		for(int i = 0; i < res.length; i++)
		{
			res[i] = (Column) grd.getItems().get(i);			
		}
		Arrays.sort(res, 0, res.length, new ColumnComparator());
		return res;
	}
	
}
