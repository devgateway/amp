/**
 * CummulativeColWorker.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.workers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.dgfoundation.amp.ar.GroupColumn;
import org.dgfoundation.amp.ar.ReportGenerator;
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
	public CummulativeColWorker(String destName, GroupColumn source,ReportGenerator generator) {
		super(destName, source,generator);
		sourceName="Funding";
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.workers.ColumnWorker#getCellFromRow(java.sql.ResultSet)
	 */
	protected Cell getCellFromRow(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.workers.ColumnWorker#getCellFromCell(org.dgfoundation.amp.ar.cell.Cell)
	 */ 
	protected Cell getCellFromCell(Cell src) {
		CategAmountCell c=(CategAmountCell) src;
		
		String trStr="";
		if(columnName.equalsIgnoreCase("Cumulative Commitment")) trStr="Commitment";
		if(columnName.equalsIgnoreCase("Cumulative Disbursement")) trStr="Disbursement";
	
		if(c.getMetaInfo("Transaction Type").getValue().equals(trStr)) return src;
		else return null;	
	}

}
