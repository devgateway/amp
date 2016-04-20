package org.dgfoundation.amp.nireports.amp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.newreports.ReportRenderWarning;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.TextCell;
import org.dgfoundation.amp.nireports.amp.diff.TextColumnKeyBuilder;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.dgfoundation.amp.nireports.schema.TextualTokenBehaviour;

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
public class SimpleTextColumn extends AmpDifferentialColumn<TextCell, String> {
		
	public SimpleTextColumn(String columnName, NiDimension.LevelColumn levelColumn, String viewName) {
		super(columnName, levelColumn, viewName, "amp_activity_id", TextColumnKeyBuilder.instance, TextualTokenBehaviour.instance);
	}

	@Override
	protected TextCell extractCell(NiReportsEngine engine, ResultSet rs) throws SQLException {
		String text = rs.getString(2);
		
		if (text == null)
			return null;
		
		if (withoutEntity)
			return new TextCell(text, rs.getLong(1), rs.getLong(1), this.levelColumn);
		else
			return new TextCell(text, rs.getLong(1), rs.getLong(3), this.levelColumn);
	}
		
	public static SimpleTextColumn fromView(String columnName, String viewName, NiDimension.LevelColumn levelColumn) {
		return new SimpleTextColumn(columnName, levelColumn, viewName);
	}
	
	public static SimpleTextColumn fromViewWithoutEntity(String columnName, String viewName) {
		return new SimpleTextColumn(columnName, null, viewName).withoutEntity();
	}
	
	private boolean withoutEntity = false;
	
	private SimpleTextColumn withoutEntity() {
		this.withoutEntity = true;
		return this;
	}

	@Override
	public List<ReportRenderWarning> performCheck(){
		return null;
	}
}
