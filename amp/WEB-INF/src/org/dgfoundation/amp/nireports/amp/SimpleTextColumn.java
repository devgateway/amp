package org.dgfoundation.amp.nireports.amp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.TextCell;
import org.dgfoundation.amp.nireports.schema.NiDimension;

/**
 * a simple text column which fetches its input from a view which contains 3 or more columns: <br />
 * 	1. amp_activity_id (or pledge_id)
 *  2. payload (text)
 *  3. entity_id (e.g. sector_id, donor_id etc)
 *  
 *  All the extra columns are ignored
 * @author Dolghier Constantin
 *
 */
public class SimpleTextColumn extends AmpSqlSourcedColumn<TextCell> {
	
	public SimpleTextColumn(String columnName, NiDimension.LevelColumn levelColumn, Map<String, String> fundingViewFilter, String viewName) {
		super(columnName, levelColumn, fundingViewFilter, viewName, "amp_activity_id");
	}

	@Override
	protected TextCell extractCell(NiReportsEngine engine, ResultSet rs) throws SQLException {
		return new TextCell(rs.getString(2), rs.getLong(1), rs.getLong(3));
	}
		
	public static SimpleTextColumn fromView(String columnName, String viewName, NiDimension.LevelColumn levelColumn) {
		return new SimpleTextColumn(columnName, levelColumn, null, viewName);
	}
}
