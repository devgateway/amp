package org.dgfoundation.amp.nireports.amp;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.algo.AlgoUtils;
import org.dgfoundation.amp.ar.viewfetcher.DatabaseViewFetcher;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.ViewFetcher;
import org.dgfoundation.amp.newreports.ReportRenderWarning;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.PercentageTextCell;
import org.dgfoundation.amp.nireports.amp.PercentagesCorrector.Snapshot;
import org.dgfoundation.amp.nireports.schema.Behaviour;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.dgfoundation.amp.nireports.schema.PercentageTokenBehaviour;
import org.digijava.kernel.request.TLSUtils;


/**
 * a column which fetches items with percentages from a view with a "nonNulls" column and associated {@link PercentagesCorrector} instance
 * @author Dolghier Constantin
 *
 */
public class NormalizedPercentagesColumn extends PsqlSourcedColumn<PercentageTextCell> {

	final PercentagesCorrector percentagesCorrector;
	final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100l);
	
	public NormalizedPercentagesColumn(String columnName, NiDimension.LevelColumn levelColumn, Map<String, String> fundingViewFilter, 
			String viewName, String mainColumn, PercentagesCorrector percentagesCorrector) {
		super(columnName, levelColumn, fundingViewFilter, viewName, mainColumn, PercentageTokenBehaviour.instance);
		this.percentagesCorrector = percentagesCorrector;
	}
	
	@Override
	public final List<PercentageTextCell> fetch(NiReportsEngine engine) {
		String locale = TLSUtils.getEffectiveLangCode();
		AmpReportsScratchpad scratchpad = AmpReportsScratchpad.get(engine);
		PercentagesCorrector.Snapshot percsSnapshot = scratchpad.buildOrGetSnapshot(percentagesCorrector, engine.getMainIds());
		
		String queryCondition = buildCondition(engine);
		ViewFetcher fetcher = DatabaseViewFetcher.getFetcherForView(viewName, queryCondition, locale, scratchpad.columnCachers, scratchpad.connection, "*");
		List<PercentageTextCell> res = new ArrayList<>();
		try(RsInfo rs = fetcher.fetch(null)) {
			while (rs.rs.next()) {
				PercentageTextCell cell = extractCell(engine, rs.rs, percsSnapshot);
				if (cell != null)
					res.add(cell);
			}
		}
		catch(Exception e) {
			throw AlgoUtils.translateException(e);
		}
		return res;
	}
	
	protected PercentageTextCell extractCell(NiReportsEngine engine, ResultSet rs, Snapshot percsSnapshot) throws SQLException {
		long activityId = rs.getLong(1);
		String text = rs.getString(2);
		long entityId = rs.getLong(3);
		Double rawPercentage = rs.getDouble(4);
		if (rs.wasNull()) rawPercentage = null;
		int nrNulls = rs.getInt(5);
		if (text == null || (rawPercentage != null && rawPercentage <= 0)) 
			return null;
		double percentage = percsSnapshot.correctPercentage(activityId, rawPercentage, nrNulls);
		return new PercentageTextCell(text, activityId, entityId, levelColumn, engine.schemaSpecificScratchpad.getPrecisionSetting().adjustPrecision(BigDecimal.valueOf(percentage / 100)));
	}

	@Override
	public List<ReportRenderWarning> performCheck() {
		// do nothing here, as underlying m2m tables are checked as part of AmpReportsSchema.performColumnChecks()
		return null;
	}
}
