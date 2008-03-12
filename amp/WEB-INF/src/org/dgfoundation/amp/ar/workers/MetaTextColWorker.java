/**
 * 
 */
package org.dgfoundation.amp.ar.workers;

import java.sql.ResultSet;
import java.sql.SQLException;

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
	
	protected Cell getCellFromRow(ResultSet rs) throws SQLException {
		TextCell tc=(TextCell) super.getCellFromRow(rs);
		MetaTextCell mtc=new MetaTextCell(tc);
		if(columnName.equals("Project Title")) 
			mtc.getMetaData().add(new MetaInfo(ArConstants.DRAFT,rs.getBoolean(4))); else
		if(columnName.equals("National Planning Objectives")) 
			mtc.getMetaData().add(new MetaInfo(ArConstants.NPO_PERCENTAGE,rs.getDouble(4))); else
		if(columnName.equals("Sector") || columnName.equals("Sub-Sector")) 
			mtc.getMetaData().add(new MetaInfo(ArConstants.SECTOR_PERCENTAGE,rs.getDouble(4))); else
			
		if(columnName.equals("Executing Agency")) 
			mtc.getMetaData().add(new MetaInfo(ArConstants.EXECUTING_AGENCY_PERCENTAGE,rs.getDouble(4))); else
				
		if(columnName.equals("Region") && generator.getReportMetadata().getType()==ArConstants.DONOR_TYPE)
			mtc.getMetaData().add(new MetaInfo(ArConstants.LOCATION_PERCENTAGE,rs.getDouble(4))); else
		if(columnName.equals("Componente") && generator.getReportMetadata().getType()==ArConstants.DONOR_TYPE) 
			mtc.getMetaData().add(new MetaInfo(ArConstants.COMPONENTE_PERCENTAGE,rs.getDouble(4))); 
		if(columnName.equals("Componente") && generator.getReportMetadata().getType()==ArConstants.DONOR_TYPE) 
			mtc.getMetaData().add(new MetaInfo(ArConstants.COMPONENTE_PERCENTAGE,rs.getDouble(4))); 
		if((columnName.equals("Primary Program") || columnName.equals("Secondary Program")) && generator.getReportMetadata().getType()==ArConstants.DONOR_TYPE) 
			mtc.getMetaData().add(new MetaInfo(ArConstants.PROGRAM_PERCENTAGE,rs.getDouble(4))); 
		
		return mtc;
	}

}
