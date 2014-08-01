package org.dgfoundation.amp.mondrian;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.dgfoundation.amp.ar.viewfetcher.I18nViewDescription;

/**
 * describes a table used by Mondrian: table name, columns-with-indices, internationalized-columns
 * @author Dolghier Constantin
 *
 */
public class MondrianTableDescription {
	public final String tableName;
	
	/**
	 * list of columns which should have indices created on them. Will be iterated in the order given by the constructor
	 */
	public final Set<String> indexedColumns;
	//public final Set<String> idColumnNames; 
			
	protected ObjectSource<I18nViewDescription> translations;
	
	/**
	 * constructs and initializes an instance
	 * @param tableName the name of the table
	 * @param idColumnNames - the internal ids column-names of the table. They are checked against 999999999 when multilingualising. If null, will mirror indexedColumns
	 * @param indexedColumns - the column-names of cols to whoch to add indices
	 */
	public MondrianTableDescription(String tableName, Collection<String> indexedColumns) {
		if (indexedColumns == null)
			indexedColumns = new ArrayList<>();
		this.tableName = tableName;
		this.indexedColumns = Collections.unmodifiableSet(new LinkedHashSet<>(indexedColumns));
//		this.idColumnNames = idColumnNames == null ? 
//								this.indexedColumns : // not specified
//								Collections.unmodifiableSet(new LinkedHashSet<>(idColumnNames));
	}
	
	public MondrianTableDescription withInternationalizedColumns(ObjectSource<I18nViewDescription> translations) {
		this.translations = translations;
		return this;
	}
	
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
	
	@Override public String toString() {
		return this.tableName;
	}
}
