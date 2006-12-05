/**
 * CummulativeColWorker.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.workers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.CellColumn;
import org.dgfoundation.amp.ar.GroupColumn;
import org.dgfoundation.amp.ar.ReportGenerator;
import org.dgfoundation.amp.ar.TotalAmountColumn;
import org.dgfoundation.amp.ar.cell.CategAmountCell;
import org.dgfoundation.amp.ar.cell.Cell;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Jul 8, 2006
 *
 */
public class CummulativeColWorker extends ColumnWorker {	

	/**
	 * @param destName
	 * @param sourceGroup
	 */
	public CummulativeColWorker(String condition,String viewName,String columnName,ReportGenerator generator) {
		super(condition,viewName, columnName,generator);
	}

	public CummulativeColWorker(String columnName,GroupColumn rawColumns,ReportGenerator generator) {
		super(columnName,rawColumns,generator);
		sourceName="Funding";
	}


	public CellColumn newColumnInstance() {
		TotalAmountColumn cc=new TotalAmountColumn(columnName,false);
		return cc;
	}
	
	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.workers.ColumnWorker#getCellFromRow(java.sql.ResultSet)
	 */
	protected Cell getCellFromRow(ResultSet rs) throws SQLException {
		return null;
	}

	/**
	 * We show cummulative disbursement UP TO the ToYear selected in the filters (we do not care about FromYear)
	 * @param src
	 * @return 
	 */
	
	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.workers.ColumnWorker#getCellFromCell(org.dgfoundation.amp.ar.cell.Cell)
	 */ 
	protected Cell getCellFromCell(Cell src) {
		CategAmountCell c=(CategAmountCell) src;
		
		String trStr="";
		if(!c.isCummulativeShow() && columnName.equalsIgnoreCase("Cumulative Disbursement")) return null;
		if(columnName.equalsIgnoreCase("Cumulative Commitment")) trStr=ArConstants.COMMITMENT;
		if(columnName.equalsIgnoreCase("Cumulative Disbursement")) trStr=ArConstants.DISBURSEMENT;
			
		if(c.getMetaInfo(ArConstants.TRANSACTION_TYPE).getValue().equals(trStr) && c.getMetaInfo(ArConstants.ADJUSTMENT_TYPE).getValue().equals(ArConstants.ACTUAL)) return src;
		else return null;	
	}

}
