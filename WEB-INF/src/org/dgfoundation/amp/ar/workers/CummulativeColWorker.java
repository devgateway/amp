/**
 * CummulativeColWorker.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.workers;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.CellColumn;
import org.dgfoundation.amp.ar.GroupColumn;
import org.dgfoundation.amp.ar.MetaInfo;
import org.dgfoundation.amp.ar.ReportGenerator;
import org.dgfoundation.amp.ar.TotalAmountColumn;
import org.dgfoundation.amp.ar.cell.AmountCell;
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
		sourceName=ArConstants.COLUMN_FUNDING;
	}


	public CellColumn newColumnInstance(int initialCapacity) {
		TotalAmountColumn cc=new TotalAmountColumn(columnName,false,initialCapacity);
		return cc;
	}
	
	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.workers.ColumnWorker#getCellFromRow(java.sql.ResultSet)
	 */
	protected Cell getCellFromRow(ResultSet rs) throws SQLException {
		AmpARFilter filter=(AmpARFilter) generator.getFilter();
		Long ownerId=new Long(rs.getLong(1));
		Long id=new Long(rs.getLong(2));
		double value=rs.getDouble(3);
		String currencyCode=rs.getString(4);
		Date currencyDate = rs.getDate(5);
		double exchangeRate=rs.getDouble(6);
		
		
		AmountCell ret=new AmountCell(ownerId);
		ret.setId(id);
		ret.setAmount(value);
		ret.setFromExchangeRate(exchangeRate);
		ret.setCurrencyDate(currencyDate);
		ret.setCurrencyCode(currencyCode);
		ret.setToExchangeRate(1);
		
		//UGLY get exchage rate if cross-rates are needed (if we need to convert from X to USD and then to Y)
		if(filter.getCurrency()!=null && !"USD".equals(filter.getCurrency().getCurrencyCode())) 
			ret.setToExchangeRate(Util.getExchange(filter.getCurrency().getCurrencyCode(),currencyDate));
	
		return ret;
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
		
		try{
			if (MetaInfo.getMetaInfo(c.getMetaData(),ArConstants.TRANSACTION_TYPE) != null) {
				if(MetaInfo.getMetaInfo(c.getMetaData(),ArConstants.TRANSACTION_TYPE).getValue().equals(trStr) && MetaInfo.getMetaInfo(c.getMetaData(),ArConstants.ADJUSTMENT_TYPE).getValue().equals(ArConstants.ACTUAL)) 
					return src;				
			}
		}
		catch (NullPointerException E) {
			logger.error(E.getMessage()) ;
			E.printStackTrace();
			return null;
		}
		return null;
	}

	public Cell newCellInstance() {
		return new CategAmountCell();
		
	}

}
