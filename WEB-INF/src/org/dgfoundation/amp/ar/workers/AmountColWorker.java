/**
 * 
 */
package org.dgfoundation.amp.ar.workers;

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
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;

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
		double amount = rs.getDouble(3);
		String currency=rs.getString(4);
		java.sql.Date date= rs.getDate(5);
		
		AmpARFilter filter=(AmpARFilter) generator.getFilter();
		AmountCell ac=new AmountCell(ownerId);
		ac.setId(objectId);
		ac.setAmount(amount);
		ac.setCurrencyDate(date);
		ac.setCurrencyCode(currency);
		ac.setFromExchangeRate(Util.getExchange(currency, date));
		
		String baseCurrCode		= FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.BASE_CURRENCY);
		if ( baseCurrCode == null ) 
			baseCurrCode	= "USD";
		if(filter.getCurrency()!=null ) {
			/* If source and destination currency are the same we need to set exactly the same exchange rate for 'toExchangeRate' and 'fromExchangeRate.
			 * That way, AmountCell.convert won't do any computation' */
			if ( currency.equals(filter.getCurrency().getCurrencyCode())   ) 
				ac.setToExchangeRate( ac.getFromExchangeRate() );
			else if ( !baseCurrCode.equals(filter.getCurrency().getCurrencyCode()))  
				ac.setToExchangeRate(Util.getExchange(filter.getCurrency().getCurrencyCode(),date));
		}
		else 
			logger.error("The filter.currency property should not be null !");
		
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
