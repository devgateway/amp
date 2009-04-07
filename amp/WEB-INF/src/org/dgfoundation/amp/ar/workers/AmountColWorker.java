/**
 * 
 */
package org.dgfoundation.amp.ar.workers;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.CellColumn;
import org.dgfoundation.amp.ar.GroupColumn;
import org.dgfoundation.amp.ar.ReportGenerator;
import org.dgfoundation.amp.ar.TotalAmountColumn;
import org.dgfoundation.amp.ar.cell.AmountCell;
import org.dgfoundation.amp.ar.cell.Cell;

/**
 * @author mihai
 *
 */
public class AmountColWorker extends ColumnWorker {

	/**
	 * @param condition
	 * @param viewName
	 * @param columnName
	 * @param generator
	 */
	public AmountColWorker(String condition, String viewName,
			String columnName, ReportGenerator generator) {
		super(condition, viewName, columnName, generator);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param destName
	 * @param source
	 * @param generator
	 */
	public AmountColWorker(String destName, GroupColumn source,
			ReportGenerator generator) {
		super(destName, source, generator);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.workers.ColumnWorker#getCellFromCell(org.dgfoundation.amp.ar.cell.Cell)
	 */
	@Override
	protected Cell getCellFromCell(Cell src) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.dgfoundation.amp.ar.workers.ColumnWorker#getCellFromRow(java.sql.ResultSet)
	 * @deprecated
	 */
	@Override
	protected Cell getCellFromRow(ResultSet rs) throws SQLException {
		long ownerId=rs.getLong(1);
		long objectId=rs.getLong(2);
		BigDecimal amount = rs.getBigDecimal(3);
		String currency=rs.getString(4);
		java.sql.Date date= rs.getDate(5);
		
		AmpARFilter filter=(AmpARFilter) generator.getFilter();
		AmountCell ac=new AmountCell(ownerId);
		ac.setId(objectId);
		ac.setAmount(amount);
		ac.setCurrencyDate(date);
		ac.setCurrencyCode(currency);
		ac.setFromExchangeRate(Util.getExchange(currency, date));
		
		if(filter.getCurrency()!=null && !"USD".equals(filter.getCurrency().getCurrencyCode()))  
			ac.setToExchangeRate(Util.getExchange(filter.getCurrency().getCurrencyCode(),date));
		else 
			ac.setToExchangeRate(1);
		
		return ac;
	}
	
	

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.workers.ColumnWorker#newCellInstance()
	 */
	@Override
	public Cell newCellInstance() {
		return new AmountCell();
	}


}
