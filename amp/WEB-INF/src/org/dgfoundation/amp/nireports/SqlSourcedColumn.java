package org.dgfoundation.amp.nireports;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;

import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.viewfetcher.DatabaseViewFetcher;
import org.dgfoundation.amp.nireports.amp.MetaCategory;
import org.dgfoundation.amp.nireports.meta.MetaInfoSet;
import org.dgfoundation.amp.nireports.schema.Behaviour;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.dgfoundation.amp.nireports.schema.NiDimension.Coordinate;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;
import org.dgfoundation.amp.nireports.schema.NiReportColumn;


/**
 * a column which fetches its input from a view which contains 3 or more columns: <br />
 * 	1. amp_activity_id (or pledge_id)
 *  2. (undefined, but normally payload)
 *  3. entity_id (e.g. sector_id, donor_id etc) <br />
 *  
 *  All the extra columns are ignored by this class and are to be used / ignored by the subclasses <br />
 *  This abstract class also functions as a utility methods repository
 * @author Dolghier Constantin
 *
 */
public abstract class SqlSourcedColumn<K extends Cell> extends NiReportColumn<K> {
	
	public final String viewName;
	public final String mainColumn;
	
	public SqlSourcedColumn(String columnName, NiDimension.LevelColumn levelColumn, String viewName, 
			String mainColumn, Behaviour<?> behaviour, String description) {
		super(columnName, levelColumn, behaviour, description);
		this.viewName = viewName;
		this.mainColumn = mainColumn;
	}
		
	/**
	 * builds the condition for filtering view rows based on entity IDs
	 * @param engine
	 * @return
	 */
	protected String buildPrimaryFilteringQuery(Set<Long> mainIds) {
		return String.format("%s IN (%s)", mainColumn, Util.toCSStringForIN(mainIds));
	}

	/**
	 * builds the complete condition for filtering view rows, based both on entity IDs and filters (#fundingFilterRows)
	 * @param engine
	 * @return
	 */
	protected String buildCondition(NiReportsEngine engine) {
		String condition = String.format("WHERE (%s)", buildPrimaryFilteringQuery(engine.getMainIds()));
		return condition;
	}
	
	/**
	 * builds the complete fetching query
	 * @param engine
	 * @return
	 */
	protected String buildQuery(NiReportsEngine engine) {
		String condition = buildCondition(engine);
		String query = String.format("SELECT * FROM %s %s", viewName, condition);
		return query;
	}
	
	/**
	 * reads a long from a ResultSet. If the long is nonnull, looks up its value in the supplied map. If a value exists, returns it as a key-value pair
	 * @param rs
	 * @param idColumnName
	 * @param mp
	 * @return
	 * @throws SQLException
	 */
	protected IdValuePair readIdValuePair(ResultSet rs, String idColumnName, Map<Long, String> mp) throws SQLException {
		long id = rs.getLong(idColumnName);
		if (rs.wasNull())
			return null;
		String v = mp.get(id);
		if (v == null)
			return null;
		return new IdValuePair(id, v);
	}
	
	/**
	 * reads a valuePair from a ResultSet. If present, writes the String value to a metaset and returns it. Please see {@link #readIdValuePair(ResultSet, String, Map)}
	 * @param set
	 * @param idColumnName
	 * @param categ
	 * @param row
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	protected IdValuePair addMetaIfIdValueExists(MetaInfoSet set, String idColumnName, MetaCategory categ, ResultSet row, Map<Long, String> map) throws SQLException {
		IdValuePair pair = readIdValuePair(row, idColumnName, map);
		if (pair != null)
			set.add(categ.category, pair.v);
		return pair;
	}
	
	/**
	 * reads a long from a ResultSet. If present, it is written to a metaset and returns it
	 * @param set
	 * @param categ
	 * @param row
	 * @param columnName
	 * @return
	 * @throws SQLException
	 */
	protected long addMetaIfLongExists(MetaInfoSet set, MetaCategory categ, ResultSet row, String columnName) throws SQLException {
		long val = row.getLong(columnName);
		if (!row.wasNull())
			set.add(categ.category, val);
		return val;
	}
	
	/**
	 * reads a long from a ResultSet. If present, it is used as an id in a given LevelColumn which is inserted in a given coos set
	 * @param coos
	 * @param row
	 * @param viewColName
	 * @param levelColumn
	 * @return
	 * @throws SQLException
	 */
	protected Coordinate addCoordinateIfLongExists(Map<NiDimensionUsage, Coordinate> coos, ResultSet row, String viewColName, LevelColumn levelColumn) throws SQLException {
		long val = row.getLong(viewColName);
		if (row.wasNull())
			return null;
		
		Coordinate newVal = levelColumn.getCoordinate(val);
		coos.put(levelColumn.dimensionUsage, newVal);
		return newVal;
	}
	
}
