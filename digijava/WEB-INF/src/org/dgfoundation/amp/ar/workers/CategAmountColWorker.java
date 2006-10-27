/**
 * CategAmountColWorker.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.workers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Set;

import org.dgfoundation.amp.ar.ARUtil;
import org.dgfoundation.amp.ar.AmountCellColumn;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.CellColumn;
import org.dgfoundation.amp.ar.MetaInfo;
import org.dgfoundation.amp.ar.ReportGenerator;
import org.dgfoundation.amp.ar.cell.CategAmountCell;
import org.dgfoundation.amp.ar.cell.Cell;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Jun 13, 2006
 * 
 */
public class CategAmountColWorker extends ColumnWorker {

	/**
	 * @param condition
	 * @param viewName
	 * @param columnName
	 */
	public CategAmountColWorker(String condition, String viewName,
			String columnName,ReportGenerator generator) {
		super(condition, viewName, columnName,generator);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Decides if the CategAmountCell is showable or not, based on the measures selected
	 * in the report wizard.
	 * @param cac the given CategAmountCell
	 * @return true if showable
	 */
	public boolean isShowable(CategAmountCell cac) {
		Set measures=generator.getReportMetadata().getMeasures();
		return ARUtil.containsMeasure(cac.getMetaValueString(ArConstants.FUNDING_TYPE),measures);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dgfoundation.amp.ar.workers.ColumnWorker#getCellFromRow(java.sql.ResultSet,
	 *      java.lang.String)
	 */
	protected Cell getCellFromRow(ResultSet rs) throws SQLException {
		Long ownerId = new Long(rs.getLong(1));
		Long id = new Long(rs.getLong(3));
		CategAmountCell acc = new CategAmountCell(ownerId);

		acc.setId(id);

		int tr_type = rs.getInt("transaction_type");
		int adj_type = rs.getInt("adjustment_type");
		double tr_amount = rs.getDouble("transaction_amount");
		java.sql.Date td = rs.getDate("transaction_date");
		double exchangeRate=rs.getDouble("exchange_rate");
		String currencyCode=rs.getString("currency_code");
		String donorName=rs.getString("donor_name");


		try {
			String termsAssist = rs.getString("terms_assist_name");
			MetaInfo termsAssistMeta = new MetaInfo("Terms of Assistance",
					termsAssist);
			acc.getMetaData().add(termsAssistMeta);
		} catch (SQLException e) {

		}
		try {
			String regionName = rs.getString("region_name");
			MetaInfo regionMeta = new MetaInfo("Region", regionName);
			acc.getMetaData().add(regionMeta);
		} catch (SQLException e) {
		}

		try {
		
		String componentName = rs.getString("component_name");
		MetaInfo compMeta = new MetaInfo("Component", componentName);
		acc.getMetaData().add(compMeta);
		} catch (SQLException e) {
		}

		acc.setAmount(tr_amount);
		acc.setFromExchangeRate(exchangeRate);
		acc.setCurrencyDate(td);
		acc.setCurrencyCode(currencyCode);
		//put toExchangeRate
		acc.setToExchangeRate(1);
		
		MetaInfo donorMs=new MetaInfo("Donor",donorName);
		
		MetaInfo adjMs = new MetaInfo(ArConstants.ADJUSTMENT_TYPE,
				adj_type == 0 ? ArConstants.PLANNED : ArConstants.ACTUAL);
		String trStr = null;

		switch (tr_type) {
		case 0:
			trStr = ArConstants.COMMITMENT;
			break;
		case 1:
			trStr = ArConstants.DISBURSEMENT;
			break;
		case 2:
			trStr = ArConstants.EXPENDITURE;
			break;
		}

		MetaInfo trMs = new MetaInfo(ArConstants.TRANSACTION_TYPE, trStr);
		MetaInfo fundMs = new MetaInfo(ArConstants.FUNDING_TYPE, (String) adjMs
				.getValue()
				+ " " + (String) trMs.getValue());

		Calendar c = Calendar.getInstance();
		c.setTime(td);

		MetaInfo qMs = new MetaInfo("Quarter", "Q"
				+ new Integer(c.get(Calendar.MONTH) / 4 + 1));
		MetaInfo aMs = new MetaInfo("Year", new Integer(c.get(Calendar.YEAR)));

		
		//add the newly created metainfo objects to the virtual funding object
		acc.getMetaData().add(adjMs);
		acc.getMetaData().add(trMs);
		acc.getMetaData().add(fundMs);
		acc.getMetaData().add(aMs);
		acc.getMetaData().add(qMs);
		acc.getMetaData().add(donorMs);

		//set the showable flag, based on selected measures
		acc.setShow(isShowable(acc));
		
		return acc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dgfoundation.amp.ar.workers.ColumnWorker#getCellFromCell(org.dgfoundation.amp.ar.cell.Cell)
	 */
	protected Cell getCellFromCell(Cell src) {
		// TODO Auto-generated method stub
		return null;
	}

	public CellColumn newColumnInstance() {
		return new AmountCellColumn(columnName);
	}

}
