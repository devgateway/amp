package org.dgfoundation.amp.nireports.amp;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.PercentageTextCell;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.dgfoundation.amp.nireports.schema.PercentageTokenBehaviour;

/**
 * a text column which fetches its input from a view which contains 4 or more columns: <br />
 * 	1. amp_activity_id (or pledge_id)
 *  2. payload (text)
 *  3. entity_id (e.g. sector_id, donor_id etc)
 *  4. percentage  TODO: do we allow NULLs here?
 *  
 *  All the extra columns are ignored
 * @author Dolghier Constantin
 *
 */
public class PercentageTextColumn extends AmpSqlSourcedColumn<PercentageTextCell> {
	
	public PercentageTextColumn(String columnName, NiDimension.LevelColumn levelColumn, Map<String, String> fundingViewFilter, String viewName) {
		super(columnName, levelColumn, fundingViewFilter, viewName, "amp_activity_id", PercentageTokenBehaviour.instance);
	}

	@Override
	protected PercentageTextCell extractCell(NiReportsEngine engine, ResultSet rs) throws SQLException {
		String text = rs.getString(2);
		if (text == null)
			return null;
		BigDecimal percentage = rs.getBigDecimal(4);
		if (percentage == null)
			return null; // TODO: how do we want to treat nulls?
		return new PercentageTextCell(text, rs.getLong(1), rs.getLong(3), rs.getBigDecimal(4));
	}
		
	public static PercentageTextColumn fromView(String columnName, String viewName, NiDimension.LevelColumn levelColumn) {
		return new PercentageTextColumn(columnName, levelColumn, null, viewName);
	}
}
