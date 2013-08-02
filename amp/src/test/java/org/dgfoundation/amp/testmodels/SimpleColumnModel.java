package org.dgfoundation.amp.testmodels;

import java.util.*;

import org.dgfoundation.amp.ar.CellColumn;
import org.dgfoundation.amp.ar.Column;
import org.dgfoundation.amp.ar.cell.Cell;
import org.dgfoundation.amp.testutils.ReportTestingUtils;

public class SimpleColumnModel extends ColumnModel {
	
	Map<String, String> correctContents;
	
	private SimpleColumnModel(String name, Map<String, String> contents)
	{
		super(name);
		
		if (contents != null)
			this.correctContents = new HashMap<String, String>(contents);
	}
	
	public static SimpleColumnModel withContents(String name, Object...contents)
	{
		if (contents != null && contents.length == 1 && contents[0].equals(ReportTestingUtils.NULL_PLACEHOLDER))
			contents = null;
		
		if (contents != null && contents.length % 2 == 1)
			throw new RuntimeException("odd length not supported!");
		
		Map<String, String> con;
		
		if (contents == null)
			con = null;
		else
		{
			con = new HashMap<String, String>();
			for(int i = 0; i < contents.length / 2; i++)
			{
				String activityName = (String) (contents[2 * i]);
				String activityAmm = (String) (contents[2 * i + 1]);
				con.put(activityName, activityAmm);
			}
		}
		return new SimpleColumnModel(name, con);
	}
	
	@Override
	public String matches(Column column)
	{
		if (this.getName().compareTo(column.getName()) != 0)
			return String.format("SimpleColumnModel name mismatch: %s vs %s", column.getName(), this.getName());

		if (!(column instanceof CellColumn))
			return String.format("SimpleColumnModel %s not matched by a CellColumn %s", this.getName(), column.getName());
		
		if (correctContents != null)
			return matchContents((CellColumn) column);
		
		return null;
	}
	
	public String matchContents(CellColumn column)
	{
		Set<Long> activityIds = column.getOwnerIds();
		if (activityIds.size() != correctContents.size())
			return String.format("SimpleColumnModel %s: size mismatch: %d vs %d", this.getName(), activityIds.size(), correctContents.size());
		
		for(Long activityId: activityIds)
		{
			String activityName = ReportTestingUtils.getActivityName(activityId);
			String activityCorOutput = correctContents.get(activityName);
			if (activityCorOutput == null)
				return String.format("SimpleColumnModel %s, activity %s should not exist in the output", this.getName(), activityName);
			
			Cell cell = column.getByOwner(activityId);
			String reportOutput = cell.toString();
			if (reportOutput.compareTo(activityCorOutput) != 0)
				return String.format("SimpleColumnModel %s, activity %s has output %s instead of %s", this.getName(), activityName, reportOutput, activityCorOutput);
		}
		return null;
	}
}

