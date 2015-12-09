package org.dgfoundation.amp.nireports;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.viewfetcher.DatabaseViewFetcher;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.dgfoundation.amp.nireports.schema.NiReportColumn;


/**
 * a column which fetches its input from a view which contains 3 or more columns: <br />
 * 	1. amp_activity_id (or pledge_id)
 *  2. (undefined, but normally payload)
 *  3. entity_id (e.g. sector_id, donor_id etc)
 *  
 *  All the extra columns are ignored by this class and are to be used / ignored by the subclasses
 * @author Dolghier Constantin
 *
 */
public abstract class SqlSourcedColumn<K extends Cell> extends NiReportColumn<K> {
	public final String viewName;
	public final String mainColumn;
	
	public SqlSourcedColumn(String columnName, NiDimension.LevelColumn levelColumn, Map<String, String> fundingViewFilter, String viewName, String mainColumn) {
		super(columnName, levelColumn, fundingViewFilter);
		this.viewName = viewName;
		this.mainColumn = mainColumn;
	}
		
	protected String buildPrimaryFilteringQuery(NiReportsEngine engine) {
		return String.format("%s IN (%s)", mainColumn, Util.toCSStringForIN(engine.filters.getActivityIds(engine)));
	}

	protected String buildCondition(NiReportsEngine engine) {
		StringBuilder columnFilters = new StringBuilder("(1 = 1)");
		for(String filterField:filtering.keySet()) {
			Set<Long> ids = engine.filters.getSelectedIds(engine, filterField);
			if (ids != null) columnFilters.append(String.format(" AND (%s IN (%s))", filtering.get(filterField), Util.toCSStringForIN(ids)));
		}
		String condition = String.format("WHERE (%s) AND (%s)", buildPrimaryFilteringQuery(engine), columnFilters);
		return condition;
	}
	
	protected String buildQuery(NiReportsEngine engine) {
		String condition = buildCondition(engine);
		String query = String.format("SELECT * FROM %s %s", viewName, condition);
		return query;
	}
	
	protected IdValuePair readIdValuePair(ResultSet rs, String idColumnName, Map<Long, Optional<String>> mp) throws SQLException {
		long id = rs.getLong(idColumnName);
		if (id <= 0)
			return null;
		Optional<String> v = mp.get(id);
		return new IdValuePair(id, v);
	}
	
	protected Map<Long, Optional<String>> fetchValues(String viewName, String idColumnName, String payloadColumnName) {
		Map<Long, Optional<String>> res = new HashMap<>(); 
		Map<Long, String> raw = DatabaseViewFetcher.fetchInternationalizedView(viewName, null, idColumnName, payloadColumnName);
		raw.forEach((key, value) -> {res.put(key, Optional.ofNullable(value));});
		return res;
	}
		
}
