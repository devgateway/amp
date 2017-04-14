package org.dgfoundation.amp.nireports.amp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.dgfoundation.amp.newreports.ReportRenderWarning;
import org.dgfoundation.amp.nireports.DateCell;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.behaviours.DateTokenBehaviour;
import org.dgfoundation.amp.nireports.meta.MetaInfoGenerator;
import org.dgfoundation.amp.nireports.meta.MetaInfoSet;
import org.dgfoundation.amp.nireports.schema.Behaviour;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.digijava.module.common.util.DateTimeUtil;

/**
 * a differentially-fetched column which generates {@link DateCell} cells
 *  All the extra columns are ignored
 * @author Dolghier Constantin
 *
 */
public class DateColumn extends AmpDifferentialColumn<DateCell, Boolean> {

	private MetaInfoGenerator metaInfoGenerator;

	private MetaInfoProvider metaInfoProvider = MetaInfoProvider.empty;

	private boolean allowNulls;

	public DateColumn(String columnName, NiDimension.LevelColumn levelColumn, String viewName) {
		this(columnName, levelColumn, viewName, DateTokenBehaviour.instance);
	}

	public DateColumn(String columnName, NiDimension.LevelColumn levelColumn, String viewName, Behaviour<?> behaviour) {
		super(columnName, levelColumn, viewName, (engine, col) -> true, behaviour);
	}

	public DateColumn(String columnName, String viewName) {
		this(columnName, null, viewName);
	}

	@Override
	public synchronized List<DateCell> fetch(NiReportsEngine engine) {
		metaInfoGenerator = new MetaInfoGenerator();
		return super.fetch(engine);
	}

	@Override
	protected DateCell extractCell(NiReportsEngine engine, ResultSet rs) throws SQLException {
		java.sql.Date sqlDate = rs.getDate(2);
		if (!allowNulls && sqlDate == null)
			return null;
		LocalDate date = (sqlDate != null) ? sqlDate.toLocalDate() : null;
		MetaInfoSet metaInfo = metaInfoProvider.provide(engine, rs, metaInfoGenerator);
		return new DateCell(date, rs.getLong(1), this.levelColumn.isPresent() ? rs.getLong(3) : DateTimeUtil.toJulianDayNumber(date), metaInfo, this.levelColumn);
	}
	
	@Override
	public boolean getKeptInSummaryReports() {
		return false;
	}
	
	public static DateColumn fromView(String columnName, String viewName, NiDimension.LevelColumn levelColumn) {
		return new DateColumn(columnName, levelColumn, viewName);
	}

	public DateColumn withMetaInfoProvider(MetaInfoProvider provider) {
		this.metaInfoProvider = provider;
		return this;
	}

	public DateColumn allowNulls(boolean allowNulls) {
		this.allowNulls = allowNulls;
		return this;
	}
	
	@Override
	public List<ReportRenderWarning> performCheck() {
		return null;
	}
}
