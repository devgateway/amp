/**
 * CummulativeColWorker.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.workers;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.CellColumn;
import org.dgfoundation.amp.ar.GroupColumn;
import org.dgfoundation.amp.ar.ReportGenerator;
import org.dgfoundation.amp.ar.TotalComputedCountingAmountColumn;
import org.dgfoundation.amp.ar.cell.AmountCell;
import org.dgfoundation.amp.ar.cell.CategAmountCell;
import org.dgfoundation.amp.ar.cell.Cell;
import org.dgfoundation.amp.ar.cell.ComputedAmountCell;
import org.dgfoundation.amp.ar.cell.ComputedCountingAmountCell;

public class ComputedCountingAmountColWorker extends ColumnWorker {

	/**
	 * @param destName
	 * @param sourceGroup
	 */
	public ComputedCountingAmountColWorker(String condition, String viewName, String columnName, ReportGenerator generator) {
		super(condition, viewName, columnName, generator);
	}

	public ComputedCountingAmountColWorker(String columnName, GroupColumn rawColumns, ReportGenerator generator) {
		super(columnName, rawColumns, generator);
		sourceName = ArConstants.COLUMN_FUNDING;
	}

	public CellColumn newColumnInstance(int initialCapacity) {
		TotalComputedCountingAmountColumn cc = new TotalComputedCountingAmountColumn(columnName, false, initialCapacity);
		return cc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.dgfoundation.amp.ar.workers.ColumnWorker#getCellFromRow(java.sql.
	 * ResultSet)
	 */
	protected Cell getCellFromRow(ResultSet rs) throws SQLException {
		AmpARFilter filter = (AmpARFilter) generator.getFilter();
		Long ownerId = new Long(rs.getLong(1));
		Long id = new Long(rs.getLong(2));
		BigDecimal value = rs.getBigDecimal(3);
		String currencyCode = rs.getString(4);
		Date currencyDate = rs.getDate(5);
		double exchangeRate = rs.getDouble(6);

		AmountCell ret = new AmountCell(ownerId);
		ret.setId(id);
		ret.setAmount(value);
		ret.setFromExchangeRate(exchangeRate);
		ret.setCurrencyDate(currencyDate);
		ret.setCurrencyCode(currencyCode);
		ret.setToExchangeRate(1);

		// UGLY get exchage rate if cross-rates are needed (if we need to
		// convert from X to USD and then to Y)
		if (filter.getCurrency() != null && !"USD".equals(filter.getCurrency().getCurrencyCode()))
			ret.setToExchangeRate(Util.getExchange(filter.getCurrency().getCurrencyCode(), currencyDate));

		return ret;
	}

	protected Cell getCellFromCell(Cell src) {
		CategAmountCell categ = (CategAmountCell) src;
		ComputedCountingAmountCell cell = new ComputedCountingAmountCell();

		cell.setId(categ.getId());

		cell.setOwnerId(categ.getOwnerId());
		cell.setValue(categ.getValue());
		cell.setFromExchangeRate(categ.getFromExchangeRate());
		cell.setCurrencyDate(categ.getCurrencyDate());
		cell.setCurrencyCode(categ.getCurrencyCode());
		cell.setToExchangeRate(categ.getToExchangeRate());
		cell.setColumn(categ.getColumn());
		cell.setColumnCellValue(categ.getColumnCellValue());
		cell.setColumnPercent(categ.getColumnPercent());
		cell.setCummulativeShow(categ.isCummulativeShow());

		cell.setShow(categ.isShow());
		cell.setRenderizable(categ.isRenderizable());
		cell.setCummulativeShow(categ.isCummulativeShow());
		cell.setMetaData(categ.getMetaData());

		return cell;

	}

	public Cell newCellInstance() {
		return new ComputedCountingAmountCell();

	}

}
