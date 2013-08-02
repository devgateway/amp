package org.dgfoundation.amp.testmodels;

import java.util.Arrays;
import java.util.Comparator;

import org.dgfoundation.amp.ar.ColumnReportData;
import org.dgfoundation.amp.ar.GroupReportData;
import org.dgfoundation.amp.ar.ReportData;
import org.dgfoundation.amp.testutils.*;

public class GroupReportModel extends ReportModel
{
	private ReportModel[] childModels;
	
	private enum GRModelType {GROUP_REPORTS, COLUMN_REPORTS};
	
	GRModelType type;
	
	private GroupReportModel(String name, ReportModel[] models, GRModelType type)
	{
		super(name);
		this.childModels = sort(models);
		this.type = type;
	}
	
	public static GroupReportModel withGroupReports(String name, GroupReportModel...groupReportModels)
	{
		return new GroupReportModel(name, groupReportModels, GRModelType.GROUP_REPORTS);
	}
	
	public static GroupReportModel withColumnReports(String name, ColumnReportDataModel...columnReportDataModels)
	{
		return new GroupReportModel(name, columnReportDataModels, GRModelType.COLUMN_REPORTS);
	}
	
	public String matches(GroupReportData grd)
	{
		if (this.getName().compareTo(grd.getName()) != 0)
			return String.format("name mismatch: %s vs %s", grd.getName(), this.getName());

		if (childModels != null)
		{
			// comparison is relevant iff childModels is specified
			if (grd.getItems().size() != childModels.length)
				return String.format("GRD %s has %d children in lieu of %d", this.getName(), grd.getItems().size(), childModels.length);
		}

		switch(type)
		{
			case GROUP_REPORTS:
				return matches_grd(grd);
				
			case COLUMN_REPORTS:
				return matches_crd(grd);
				
			default:
				throw new RuntimeException("unknown GRModelType: " + type);
		}
	}
	
	/**
	 * grd should have children of type GroupReportData
	 * @param grd
	 * @return
	 */
	protected String matches_grd(GroupReportData grd)
	{		
		try
		{			
			GroupReportData[] children = getAndSortGRDChildren(grd);
			
			for(int i = 0; i < children.length; i++)
			{
				GroupReportModel childModel = (GroupReportModel) childModels[i];
				String compareResult = childModel.matches(children[i]);
				if (compareResult != null)
					return compareResult;
			}
			return null;

		}
		catch(TestCaseException e)
		{
			return e.getMessage();
		}
	}
	
	/**
	 * grd should have children of type GroupReportData
	 * @param grd
	 * @return
	 */
	protected String matches_crd(GroupReportData grd)
	{		
		try
		{			
			ColumnReportData[] children = getAndSortCRDChildren(grd);
			
			for(int i = 0; i < children.length; i++)
			{
				ColumnReportDataModel childModel = (ColumnReportDataModel) childModels[i];
				String compareResult = childModel.matches(children[i]);
				if (compareResult != null)
					return compareResult;
			}
			return null;

		}
		catch(TestCaseException e)
		{
			return e.getMessage();
		}
	}
	
	/**
	 * returns list of children sorted by name
	 * @param grd
	 * @return
	 */
	protected GroupReportData[] getAndSortGRDChildren(GroupReportData grd)
	{
		GroupReportData[] res = new GroupReportData[grd.getItems().size()];
		for(int i = 0; i < res.length; i++)
		{
			if (!(grd.getItems().get(i) instanceof GroupReportData))
				throw new TestCaseException(String.format("GroupReportData %s child #%d !instanceof GroupReportData", grd.getName(), i));
			
			res[i] = (GroupReportData) grd.getItems().get(i);			
		}
		Arrays.sort(res, 0, res.length, new ReportDataComparator());
		return res;
	}
	
	/**
	 * returns list of children sorted by name
	 * @param grd
	 * @return
	 */
	protected ColumnReportData[] getAndSortCRDChildren(GroupReportData grd)
	{
		ColumnReportData[] res = new ColumnReportData[grd.getItems().size()];
		for(int i = 0; i < res.length; i++)
		{
			if (!(grd.getItems().get(i) instanceof ColumnReportData))
				throw new TestCaseException(String.format("GroupReportData %s child #%d !instanceof ColumnReportData", grd.getName(), i));
			
			res[i] = (ColumnReportData) grd.getItems().get(i);			
		}
		Arrays.sort(res, 0, res.length, new ReportDataComparator());
		return res;
	}
	
	public class ReportDataComparator implements Comparator<ReportData>
	{
		public int compare(ReportData a, ReportData b)
		{
			return a.getName().compareTo(b.getName());
		}
	}
	
}
