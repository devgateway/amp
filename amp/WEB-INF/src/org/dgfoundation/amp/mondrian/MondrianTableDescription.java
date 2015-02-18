package org.dgfoundation.amp.mondrian;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dgfoundation.amp.ar.viewfetcher.ColumnValuesCacher;
import org.dgfoundation.amp.ar.viewfetcher.I18nDatabaseViewFetcher;
import org.dgfoundation.amp.ar.viewfetcher.I18nViewDescription;
import org.dgfoundation.amp.ar.viewfetcher.PropertyDescription;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.mondrian.currencies.CurrencyAmountGroup;
import org.dgfoundation.amp.mondrian.jobs.Fingerprint;
import org.dgfoundation.amp.mondrian.jobs.MondrianTableLogue;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.SiteUtils;

import com.google.common.base.Function;
import com.google.common.collect.HashBiMap;

import static org.dgfoundation.amp.mondrian.MondrianETL.MONDRIAN_DUMMY_ID_FOR_ETL;

/**
 * describes a table used by Mondrian: table name, columns-with-indices, internationalized-columns
 * @author Dolghier Constantin
 *
 */
public class MondrianTableDescription {
	
	public final static Set<MondrianTableDescription> ALL_TABLES = new HashSet<>();
	
	/**
	 * callback for view fetchers not to try translating pledge-dummy-ids
	 */
	public final static Function<Long, Boolean> EXCLUDE_UNDEFINED_AND_PLEDGES = new Function<Long, Boolean>() {
		@Override public Boolean apply(Long input) {			
			return input == null || input == MONDRIAN_DUMMY_ID_FOR_ETL || input >= 800 * 1000l * 1000l;
		}
	};
	
	public final String tableName;
	public final String primaryKeyColumnName;
	
	/**
	 * list of columns which should have indices created on them. Will be iterated in the order given by the constructor
	 */
	public final Set<String> indexedColumns;	
	
	public Fingerprint fingerprint;
	public boolean isFiltering = false;
	
	/**
	 * companion pledge table
	 */
	public String pledgeView;
	
	public int supplementalRows = 0;
	
	//public MondrianTableLogue epilogue;
	
	//public final Set<String> idColumnNames; 
			
	/**
	 * AMP-15571-compatible i18n specification
	 */
	protected ObjectSource<I18nViewDescription> translations;
	
	/**
	 * constructs and initializes an instance
	 * @param tableName the name of the table
	 * @param idColumnNames - the internal ids column-names of the table. They are checked against 999999999 when multilingualising. If null, will mirror indexedColumns
	 * @param indexedColumns - the column-names of cols to which to add indices
	 */
	public MondrianTableDescription(String tableName, String primaryKeyColumnName, Collection<String> indexedColumns) {
		if (indexedColumns == null)
			indexedColumns = new ArrayList<>();
		this.tableName = tableName;
		this.primaryKeyColumnName = primaryKeyColumnName;
		this.indexedColumns = Collections.unmodifiableSet(new LinkedHashSet<>(indexedColumns));
		
		ALL_TABLES.add(this);
//		this.idColumnNames = idColumnNames == null ? 
//								this.indexedColumns : // not specified
//								Collections.unmodifiableSet(new LinkedHashSet<>(idColumnNames));
	}
	
	public MondrianTableDescription withInternationalizedColumns(ObjectSource<I18nViewDescription> translations) {
		this.translations = translations;
		return this;
	}
			
	public MondrianTableDescription withFingerprintedJob(List<String> hashQueries) {
		if (fingerprint != null)
			throw new RuntimeException("not allowed to respecify fingerprint");
		this.fingerprint = new Fingerprint("v_" + this.tableName, hashQueries);
		return this;
	}
	
	public MondrianTableDescription withPledgeView(String pledgeView) {
		this.pledgeView = pledgeView;
		return this;
	}
	
	public MondrianTableDescription withSupplimentalRows(int sRows) {
		this.supplementalRows = sRows;
		return this;
	}
	
	public CurrencyAmountGroup getCurrencyBlock(String prefix) {
		return new CurrencyAmountGroup(this.tableName, this.tableName, primaryKeyColumnName, primaryKeyColumnName, prefix);
	}
	
//	public MondrianTableDescription withPrologue(MondrianTableLogue prologue) {
//		this.prologue = prologue;
//		return this;
//	}
	
	/**
	 * this one goes through a layer of indirection, because the tables might be unavailable at MTD construction time - but they will be available when needed at ETL time
	 * @return
	 */
	public I18nViewDescription getI18nDescription() {
		if (translations == null) return null;
		I18nViewDescription res = translations.getObject();
		if (!res.viewName.equals(tableName))
			throw new RuntimeException("I18nViewDescription source should return a view describing " + tableName + ", but id describes " + res.viewName + " instead");
		return res;
	}
	
	/**
	 * reads a (potentially translated through indirection) table as a list of rows, each row being a list of column values
	 * @param rs
	 * @param locale
	 * @return
	 * @throws SQLException
	 */
	public List<List<Object>> readFetchedTable(java.sql.ResultSet rs, String locale) throws SQLException {
		List<List<Object>> vals = new ArrayList<>();
		LinkedHashSet<String> columns = SQLUtils.collectColumnNames(rs);
		// Map<value-column-name, index-column-name>
		Map<String, String> valueColToIndex = getI18nDescription() == null ? new HashMap<String, String>() : getI18nDescription().getMappedColumns();
		Set<String> indexColumns = new HashSet<>(valueColToIndex.values());
		String UNDEFINED_VALUE = "Undefined";
		String translated_undefined = TranslatorWorker.translateText(UNDEFINED_VALUE, locale, SiteUtils.getDefaultSite());;
		while (rs.next()) {
			if (!rowIsRelevant(rs, locale))
				continue;
			List<Object> row = readMondrianDimensionRow(valueColToIndex, indexColumns, columns, UNDEFINED_VALUE, translated_undefined, rs);
			vals.add(row);
		}
		return vals;
	}
	
	/**
	 * reads a translated table as a list of rows, each row being a list of column values
	 * @param conn
	 * @param locale
	 * @param condition
	 * @return
	 * @throws SQLException
	 */
	public List<List<Object>> readTranslatedTable(java.sql.Connection conn, String locale, String condition) throws SQLException {
		Map<PropertyDescription, ColumnValuesCacher> cachers = new HashMap<>();
		I18nDatabaseViewFetcher fetcher = new I18nDatabaseViewFetcher(getI18nDescription(), condition, locale, cachers, conn, "*");
		fetcher.indicesNotToTranslate = EXCLUDE_UNDEFINED_AND_PLEDGES;

		try(RsInfo rsi = fetcher.fetch(null)) {
			return readFetchedTable(rsi.rs, locale);
		}
	}
	
	/**
	 * to be overridden in filtered tables
	 * @param rs
	 * @param locale
	 * @return
	 * @throws SQLException
	 */
	protected boolean rowIsRelevant(ResultSet rs, String locale) throws SQLException {
		return true;
	}
	
	
	/**
	 * 
	 * @param mondrianTable
	 * @param columns
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	protected List<Object> readMondrianDimensionRow(Map<String, String> valueColToIndex, Set<String> indexColumns, Collection<String> columns, String undefined, String translated_undefined, ResultSet rs) throws SQLException {
		List<Object> row = new ArrayList<>();
		String UNDEFINED_ID = MONDRIAN_DUMMY_ID_FOR_ETL.toString();	
		for(String colName:columns) {
			Object colValue = rs.getObject(colName);			
			if (indexColumns.contains(colName) && (colValue != null && colValue.toString().equals(UNDEFINED_ID))) {
				colValue = MONDRIAN_DUMMY_ID_FOR_ETL;
			}
			if (valueColToIndex.containsKey(colName) && (colValue == null || colValue.toString().isEmpty() || colValue.toString().equalsIgnoreCase(undefined))) {
				colValue = translated_undefined;
			}
			row.add(colValue);
		}
		return row;
	}
	
	public boolean isTranslated() {
		return this.translations != null;
	}
	
	@Override public String toString() {
		return this.tableName;
	}
	
	@Override public int hashCode() {
		return toString().hashCode();
	}
	
	@Override public boolean equals(Object obj) {
		return toString().equals(obj);
	}

}
