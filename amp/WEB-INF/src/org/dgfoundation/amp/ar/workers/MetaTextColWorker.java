/**
 * 
 */
package org.dgfoundation.amp.ar.workers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.dgfoundation.amp.ar.ARUtil;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.MetaInfo;
import org.dgfoundation.amp.ar.ReportGenerator;
import org.dgfoundation.amp.ar.cell.Cell;
import org.dgfoundation.amp.ar.cell.MetaTextCell;
import org.dgfoundation.amp.ar.cell.TextCell;

/**
 * @author mihai
 *
 */
public class MetaTextColWorker extends TextColWorker {

	/**
	 * @param condition
	 * @param viewName
	 * @param columnName
	 * @param generator
	 */
	public MetaTextColWorker(String condition, String viewName,
			String columnName, ReportGenerator generator) {
		super(condition, viewName, columnName, generator);
		// TODO Auto-generated constructor stub
	}
	
	protected Cell getCellFromRow(ResultSet rs) throws SQLException
	{
		TextCell tc=(TextCell) super.getCellFromRow(rs);
		MetaTextCell mtc=new MetaTextCell(tc);
		
		boolean thisViewHasPercentage = (columnName.indexOf("National Planning Objectives") > -1);
		thisViewHasPercentage |= (columnName.contains(ArConstants.COLUMN_ANY_SECTOR) && (!columnName.equalsIgnoreCase(ArConstants.COLUMN_SECTOR_GROUP))); 
		thisViewHasPercentage |= (columnName.equals("Executing Agency"));
		
		//TODO I think the columnName comparisons with ArConstants should be replaced by comparisons with the values from 
		// category manager "Implementation Location"
		thisViewHasPercentage |= ((columnName.equals(ArConstants.COUNTRY) || columnName.equals(ArConstants.REGION) || columnName.equals(ArConstants.DISTRICT) || 
				columnName.equals(ArConstants.ZONE)) &&  				 
				(generator.getReportMetadata().getType()==ArConstants.DONOR_TYPE || generator.getReportMetadata().getType()==ArConstants.COMPONENT_TYPE));
		thisViewHasPercentage |= (columnName.equals("Componente"));
		thisViewHasPercentage |= (columnName.equals("Primary Program") || columnName.equals("Secondary Program") || columnName.equals("Tertiary Program"));
		thisViewHasPercentage |= (ArConstants.COLUMN_ANY_RELATED_ORGS.contains(columnName.trim()));
		
		if (thisViewHasPercentage)
		{
			Double percentage = rs.getDouble(4);
			if (rs.getObject(4) == null)
				percentage = null;
			if (percentage != null)
			{
				if (percentage < 0.00001)
				{
					// zero percentage -> this cell will be ignored
					return null;
				}
				mtc.getMetaData().add(new MetaInfo(ArConstants.PERCENTAGE, percentage));
			}
		}
		else
		if(columnName.equals("Project Title") || columnName.equals("Contracting Arrangements") || columnName.equals("AMP ID"))
		{
			mtc.getMetaData().add(new MetaInfo(ArConstants.DRAFT,rs.getBoolean(4)));
			mtc.getMetaData().add(new MetaInfo(ArConstants.STATUS,rs.getString(5)));
		}
		
//		MetaInfo percentageMeta = mtc.getMetaData().getMetaInfo(ArConstants.PERCENTAGE);
//		if(percentageMeta!=null && ((Double)percentageMeta.getValue()).doubleValue()==0 && columnName.equals(ArConstants.COLUMN_REGION) && ARUtil.hasHierarchy(generator.getReportMetadata().getHierarchies(),ArConstants.COLUMN_REGION )){ 
//			mtc.setValue(ArConstants.UNALLOCATED);
//		}
		return mtc;
	}

}
