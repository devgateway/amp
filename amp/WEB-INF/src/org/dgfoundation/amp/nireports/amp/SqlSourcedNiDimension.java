package org.dgfoundation.amp.nireports.amp;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.algo.AlgoUtils;
import org.dgfoundation.amp.algo.ExceptionConsumer;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.nireports.NiUtils;
import org.dgfoundation.amp.nireports.schema.DimensionCell;
import org.dgfoundation.amp.nireports.schema.DimensionLevel;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.dgfoundation.amp.nireports.schema.TranslatedEntityImpl;
import org.dgfoundation.amp.nireports.schema.TranslatedNamedEntity;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;

/**
 * a NiDimension which fetches its data from an SQL SELECT statement
 * @author Dolghier Constantin
 *
 */
public abstract class SqlSourcedNiDimension<K> extends NiDimension {

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
	protected List<DimensionLevel> fetchDimension() {
		return PersistenceManager.getSession().doReturningWork(connection -> {
			String query = String.format("SELECT %s FROM %s", Util.toCSString(idColumnsNames), sourceViewName);
			
			// build the list of ids and the parents of each
			List<Map<Long, Long>> parentsPerLevel = new ArrayList<>(); // each level contains Map<element in level K, parent in level K - 1. For level 0 the parents are zero
			List<Map<Long, Set<Long>>> childrenPerLevel = new ArrayList<>();			

			for(int i = 0; i < depth; i++) {
				parentsPerLevel.add(new HashMap<>());
				childrenPerLevel.add(new HashMap<>());
			}
			// fetch parent-child relationships + build parent relationship
			SQLUtils.forEachRow(connection, query, ExceptionConsumer.of(row -> {
				for(int level = 0; level < depth; level++) {
					final int llevel = level;
					Map<Long, Long> levelParents = parentsPerLevel.get(level);
					Map<Long, Set<Long>> levelChildren = level == depth ? null : childrenPerLevel.get(level + 1);
					
					long elemId = row.getLong(idColumnsNames.get(level));
					long parentId = level == 0 ? 0 : row.getLong(idColumnsNames.get(level - 1));
					long childId = level == depth ? 0 : row.getLong(idColumnsNames.get(level + 1));
					NiUtils.failIf(levelParents.containsKey(elemId) && levelParents.get(elemId) != parentId, () -> String.format("error while fetching dimension %s, level %d: element %d has two different parents defined: %d and %d", this.toString(), llevel, elemId, parentId, levelParents.get(elemId)));
					levelParents.putIfAbsent(elemId, parentId);
					levelChildren.computeIfAbsent(elemId, key -> new HashSet<>());
					if (childId > 0)
						levelChildren.get(elemId).add(childId);
				}
			}));
			List<DimensionLevel> res = new ArrayList<>();
			for(int level = 0; level < depth; level++) {
				res.add(new DimensionLevel(level, childrenPerLevel.get(level), parentsPerLevel.get(level)));
			}
			return res;
		});
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
