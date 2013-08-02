package org.dgfoundation.amp.testmodels;

import java.util.Arrays;
import java.util.Comparator;

import org.dgfoundation.amp.ar.Column;
import org.dgfoundation.amp.ar.ColumnReportData;
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
	
	
	public String matches(ColumnReportData crd)
	{
		if (this.getName().compareTo(crd.getName()) != 0)
			return String.format("CRD name mismatch: %s vs %s", crd.getName(), this.getName());
				
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
