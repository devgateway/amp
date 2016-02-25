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
public class DateColumn extends AmpSqlSourcedColumn<DateCell> {
	
	public DateColumn(String columnName, NiDimension.LevelColumn levelColumn, Map<String, String> fundingViewFilter, String viewName) {
		super(columnName, levelColumn, fundingViewFilter, viewName, "amp_activity_id", DateTokenBehaviour.instance);
	}

	public DateColumn(String columnName, String viewName) {
		this(columnName, null, null, viewName);
	}

	@Override
	protected DateCell extractCell(NiReportsEngine engine, ResultSet rs) throws SQLException {
		java.sql.Date sqlDate = rs.getDate(2);
		if (sqlDate == null)
			return null;
		LocalDate date = sqlDate.toLocalDate();
//		if (date == null)
//			return null;
		return new DateCell(date, rs.getLong(1), this.levelColumn.isPresent() ? rs.getLong(3) : rs.getLong(1), this.levelColumn);
	}
		
	public static DateColumn fromView(String columnName, String viewName, NiDimension.LevelColumn levelColumn) {
		return new DateColumn(columnName, levelColumn, null, viewName);
	}
	
	@Override
	public List<ReportRenderWarning> performCheck() {
		return null;
	}
}
