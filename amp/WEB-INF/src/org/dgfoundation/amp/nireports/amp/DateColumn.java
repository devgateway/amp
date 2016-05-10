package org.dgfoundation.amp.nireports.amp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.newreports.ReportRenderWarning;
import org.dgfoundation.amp.nireports.DateCell;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.schema.DateTokenBehaviour;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.digijava.module.common.util.DateTimeUtil;

/**
 * 
 * a date column
 * 	<li>amp_activity_id (or pledge_id)</li>
 *  <li>payload (text)</li>
 *  </ol>  
 *  All the extra columns are ignored
 * @author Dolghier Constantin
 *
 */
public class DateColumn extends AmpDifferentialColumn<DateCell, Boolean> {
	
	public DateColumn(String columnName, NiDimension.LevelColumn levelColumn, String viewName) {
		super(columnName, levelColumn, viewName, (engine, col) -> true, DateTokenBehaviour.instance);
	}

	public DateColumn(String columnName, String viewName) {
		this(columnName, null, viewName);
	}

	@Override
	protected DateCell extractCell(NiReportsEngine engine, ResultSet rs) throws SQLException {
		java.sql.Date sqlDate = rs.getDate(2);
		if (sqlDate == null)
			return null;
		LocalDate date = sqlDate.toLocalDate();
//		if (date == null)
//			return null;
		return new DateCell(date, rs.getLong(1), this.levelColumn.isPresent() ? rs.getLong(3) : DateTimeUtil.toJulianDayNumber(date), this.levelColumn);
	}
	
	@Override
	public boolean getKeptInSummaryReports() {
		return false;
	}
	
	public static DateColumn fromView(String columnName, String viewName, NiDimension.LevelColumn levelColumn) {
		return new DateColumn(columnName, levelColumn, viewName);
	}
	
	@Override
	public List<ReportRenderWarning> performCheck() {
		return null;
	}
}
