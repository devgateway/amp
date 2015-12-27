package org.dgfoundation.amp.nireports.amp;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.nireports.schema.TabularSourcedNiDimension;
import org.digijava.kernel.persistence.PersistenceManager;

/**
 * a NiDimension which fetches its data from an SQL SELECT statement
 * @author Dolghier Constantin
 *
 */
public abstract class SqlSourcedNiDimension extends TabularSourcedNiDimension {

	public final String sourceViewName;
	public final List<String> idColumnsNames;
	
	/**
	 * @param name
	 * @param sourceViewName
	 * @param idColumnsNames must be enumerated from top to bottom
	 */
	public SqlSourcedNiDimension(String name, String sourceViewName, List<String> idColumnsNames) {
		super(name, idColumnsNames.size());
		this.sourceViewName = sourceViewName;
		this.idColumnsNames = Collections.unmodifiableList(new ArrayList<>(idColumnsNames));
		check();
	}

	@Override
	protected List<List<Long>> getTabularData() {
		return PersistenceManager.getSession().doReturningWork(connection -> {
			String query = String.format("SELECT %s FROM %s", SQLUtils.generateCSV(idColumnsNames), sourceViewName);
			return SQLUtils.collect(connection, query, this::fetchLine);
		});
	}	
	
	protected List<Long> fetchLine(ResultSet row) {
		return idColumnsNames.stream().map(z -> SQLUtils.getLong(row, z)).collect(Collectors.toList());
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
