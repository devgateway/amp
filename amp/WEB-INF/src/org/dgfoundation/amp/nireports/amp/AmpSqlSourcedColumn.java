package org.dgfoundation.amp.nireports.amp;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.algo.AlgoUtils;
import org.dgfoundation.amp.ar.viewfetcher.DatabaseViewFetcher;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.ViewFetcher;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.schema.Behaviour;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.digijava.kernel.request.TLSUtils;


/**
 * a column which fetches its input from a view, row-by-row
 * @author Dolghier Constantin
 *
 */
public abstract class AmpSqlSourcedColumn<K extends Cell> extends PsqlSourcedColumn<K> {

	public AmpSqlSourcedColumn(String columnName, NiDimension.LevelColumn levelColumn, Map<String, String> fundingViewFilter, String viewName, String mainColumn, Behaviour behaviour) {
		super(columnName, levelColumn, fundingViewFilter, viewName, mainColumn, behaviour);
	}
	
	@Override
	public final List<K> fetch(NiReportsEngine engine) {
		String locale = TLSUtils.getEffectiveLangCode();
		String queryCondition = buildCondition(engine);
		ViewFetcher fetcher = DatabaseViewFetcher.getFetcherForView(viewName, queryCondition, locale, AmpReportsScratchpad.get(engine).columnCachers, AmpReportsScratchpad.get(engine).connection, "*");
		List<K> res = new ArrayList<>();
		try(RsInfo rs = fetcher.fetch(null)) {
			while (rs.rs.next()) {
				K cell = extractCell(engine, rs.rs);
				if (cell != null)
					res.add(cell);
			}
		}
		catch(Exception e) {
			throw AlgoUtils.translateException(e);
		}
		return res;
	}
	
	protected abstract K extractCell(NiReportsEngine engine, ResultSet rs) throws SQLException;
}
