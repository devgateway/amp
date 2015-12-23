package org.dgfoundation.amp.nireports.amp;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dgfoundation.amp.algo.AlgoUtils;
import org.dgfoundation.amp.ar.viewfetcher.DatabaseViewFetcher;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.ar.viewfetcher.ViewFetcher;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.NiUtils;
import org.dgfoundation.amp.nireports.SqlSourcedColumn;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.digijava.kernel.request.TLSUtils;


/**
 * a column which fetches its input from a view, row-by-row
 * @author Dolghier Constantin
 *
 */
public abstract class AmpSqlSourcedColumn<K extends Cell> extends PsqlSourcedColumn<K> {

	public AmpSqlSourcedColumn(String columnName, NiDimension.LevelColumn levelColumn, Map<String, String> fundingViewFilter, String viewName, String mainColumn) {
		super(columnName, levelColumn, fundingViewFilter, viewName, mainColumn);
	}
	
	@Override
	public final List<K> fetchColumn(NiReportsEngine engine) {
		String locale = TLSUtils.getEffectiveLangCode();
		String queryCondition = buildCondition(engine);
		ViewFetcher fetcher = DatabaseViewFetcher.getFetcherForView(viewName, queryCondition, locale, AmpReportsScratchpad.get(engine).columnCachers, AmpReportsScratchpad.get(engine).connection, "*");
		List<K> res = new ArrayList<>();
		try(RsInfo rs = fetcher.fetch(null)) {
			while (rs.rs.next()) {
				res.add(extractCell(engine, rs.rs));
			}
		}
		catch(Exception e) {
			throw AlgoUtils.translateException(e);
		}
		return res;
	}
	
	protected abstract K extractCell(NiReportsEngine engine, ResultSet rs) throws SQLException;
}
