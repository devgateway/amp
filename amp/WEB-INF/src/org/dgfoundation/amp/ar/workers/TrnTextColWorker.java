package org.dgfoundation.amp.ar.workers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ReportGenerator;
import org.dgfoundation.amp.ar.cell.Cell;
import org.dgfoundation.amp.ar.cell.TextCell;
import org.dgfoundation.amp.ar.cell.TrnTextCell;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.action.GetReports;

public class TrnTextColWorker extends TextColWorker {

	/**
	 * @param condition
	 * @param viewName
	 * @param columnName
	 * @param generator
	 */
	public TrnTextColWorker(String condition, String viewName,
			String columnName, ReportGenerator generator) {
		super(condition, viewName, columnName, generator);
		// TODO Auto-generated constructor stub
	}
	
	protected Cell getCellFromRow(ResultSet rs) throws SQLException {
		Long ownerId=new Long(rs.getLong(1));
		String value=rs.getString(2);
		Long id=null;
		if (rsmd.getColumnCount()>2) {
			id=new Long(rs.getLong(3));
		}
		
		TrnTextCell ret=new TrnTextCell(ownerId);
		ret.setId(id);
		try {
			ret.setValue(TranslatorWorker.translateText(value,generator.getReportMetadata().getLocale(),generator.getReportMetadata().getSiteId()));
		} catch (WorkerException e) {
			// TODO check if the Exception is correctly  handled
			ret.setValue(value);
		}
		return ret;
	}
	
	public Cell newCellInstance() {
		TrnTextCell tx= new TrnTextCell();
		tx.setValue(ArConstants.UNALLOCATED);
		return tx;
	}

}
