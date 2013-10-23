package org.dgfoundation.amp.ar.workers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import org.dgfoundation.amp.ar.GroupColumn;
import org.dgfoundation.amp.ar.MetaInfo;
import org.dgfoundation.amp.ar.ReportGenerator;
import org.dgfoundation.amp.ar.cell.CategAmountCell;


public abstract class MetaCellColumnWorker extends ColumnWorker {

	protected Map<String, Map<Comparable, MetaInfo>> metaInfoCache;

	protected MetaInfo<?> getCachedMetaInfo(String category, Comparable<?> value) {
		Map<Comparable, MetaInfo> valuesMap = metaInfoCache.get(category);
		if (valuesMap == null) {
			valuesMap = new HashMap();
			metaInfoCache.put(category, valuesMap);
		}
		MetaInfo mi = valuesMap.get(value);
		if (mi != null)
			return mi;
		mi = new MetaInfo(category, value);
		valuesMap.put(value, mi);
		return mi;
	}

	protected MetaCellColumnWorker(String condition, String viewName, String columnName, ReportGenerator generator) 
	{
		super(condition, viewName, columnName,generator);
		this.metaInfoCache = new HashMap<String, Map<Comparable, MetaInfo>>();	
	}

	protected MetaCellColumnWorker(String destName, GroupColumn source, ReportGenerator generator) 
	{
		super(destName, source, generator);
	}
	
	protected String retrieveValueFromRS ( ResultSet rs, String columnName ) throws SQLException {
		return rs.getString(columnName);
	}
	
	protected void addMetaIfExists(ResultSet rs, CategAmountCell acc, String columnName, String metaKeyName, String defaultValue, boolean retrieveDirectly) throws SQLException
	{
		if (columnsMetaData.containsKey(columnName)) {
			
			String fundingStatus = retrieveDirectly ? rs.getString(columnsMetaData.get(columnName) ) :
													retrieveValueFromRS(rs, columnsMetaData.get(columnName));
			
			if (fundingStatus == null && defaultValue != null)
				fundingStatus = defaultValue;
			
			if (fundingStatus != null) {
				MetaInfo termsAssistMeta = this.getCachedMetaInfo(metaKeyName, fundingStatus);
				acc.getMetaData().add(termsAssistMeta);
			}
				
		}	
	}

}
