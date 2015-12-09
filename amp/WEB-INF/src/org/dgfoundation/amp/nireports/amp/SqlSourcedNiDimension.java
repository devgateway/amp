package org.dgfoundation.amp.nireports.amp;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.algo.AlgoUtils;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.schema.NiDimension;

/**
 * a NiDimension which fetches its data from an SQL SELECT statement
 * @author simple
 *
 */
public abstract class SqlSourcedNiDimension extends NiDimension {

	public final String sourceViewName;
	public final List<String> idColumnsNames;
	
	public SqlSourcedNiDimension(String name, String sourceViewName, List<String> idColumnsNames) {
		super(name, idColumnsNames.size());
		this.sourceViewName = sourceViewName;
		this.idColumnsNames = Collections.unmodifiableList(new ArrayList<>(idColumnsNames));
		check();
	}

	@Override
	public List<List<Long>> fetchDimension(NiReportsEngine engine) {
		String query = String.format("SELECT %s FROM %s", Util.toCSString(idColumnsNames), sourceViewName);
		List<List<Long>> res = new ArrayList<>();
		SQLUtils.forEachRow(AmpReportsScratchpad.get(engine).connection, query, row -> {
			res.add(idColumnsNames.stream().map(col -> SQLUtils.getLong(row, col)).collect(Collectors.toList()));
		});
		return res;
	}
	
	protected void addIfOk(Map<Long, String> map, Long key, String value) {
		if (key != null && key > 0 && value != null)
			map.put(key, value);
	}
	
	/**
	 * dies if the view does not exist or does not contain the said columns;
	 */
	private void check() {
		if (!SQLUtils.tableExists(sourceViewName))
			throw new RuntimeException(String.format("SqlSourcedNiDimension %s: view <%s> does not exist", name, sourceViewName));
		Set<String> columns = new TreeSet<>(idColumnsNames);
		columns.removeAll(SQLUtils.getTableColumns(sourceViewName));
		if (!columns.isEmpty())
			throw new RuntimeException(String.format("SqlSourcedNiDimension %s: column(s) <%s> do not exist in the view <%s>", name, columns.toString(), sourceViewName));
	}
	
	@Override
	public String toString() {
		return String.format("SqlSourcedNiDimension %s based on columns <%s> of view <%s>", name, sourceViewName, idColumnsNames.toString());
	}
}
