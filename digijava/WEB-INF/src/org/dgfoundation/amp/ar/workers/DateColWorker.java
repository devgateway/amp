/**
 * DateColWorker.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.workers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ReportGenerator;
import org.dgfoundation.amp.ar.cell.Cell;
import org.dgfoundation.amp.ar.cell.DateCell;
import org.digijava.module.aim.helper.Constants;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Jun 19, 2006
 *
 */
public class DateColWorker extends ColumnWorker {

	/**
	 * @param condition
	 * @param viewName
	 * @param columnName
	 */
	public DateColWorker(String condition, String viewName, String columnName,ReportGenerator generator) {
		super(condition, viewName, columnName,generator);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.workers.ColumnWorker#getCellFromRow(java.sql.ResultSet, java.lang.String)
	 */
	protected Cell getCellFromRow(ResultSet rs)
			throws SQLException {
		Long id=new Long(rs.getLong(1));
		Date value=rs.getDate(2);
		DateCell ret=new DateCell(id);
		
		//checking the filter calendar
		AmpARFilter filter=(AmpARFilter) generator.getFilter();
		if(value!=null && filter.getCalendarType()!=null && (filter.getCalendarType().intValue()==Constants.ETH_CAL.intValue() || filter.getCalendarType().intValue()==Constants.ETH_FY.intValue()))
			ret.setEthiopianDate(true);
			
		
		ret.setValue(value);
		return ret;
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.workers.ColumnWorker#getCellFromCell(org.dgfoundation.amp.ar.cell.Cell)
	 */
	protected Cell getCellFromCell(Cell src) {
		// TODO Auto-generated method stub
		return null;
	}

}
