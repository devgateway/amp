package org.dgfoundation.amp.ar.workers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import org.dgfoundation.amp.ar.GroupColumn;
import org.dgfoundation.amp.ar.MetaInfo;
import org.dgfoundation.amp.ar.ReportGenerator;
import org.dgfoundation.amp.ar.cell.CategAmountCell;
import org.digijava.kernel.translator.TranslatorWorker;


public abstract class MetaCellColumnWorker extends ColumnWorker {

	protected HashMap<MetaInfo, MetaInfo> metaInfoCache = new HashMap<MetaInfo, MetaInfo>();

	protected MetaInfo<?> getCachedMetaInfo(String category, Object value) 
	{		
		MetaInfo mi = new MetaInfo(category, value);
		MetaInfo cachedMi = metaInfoCache.get(mi);
		if (cachedMi == null) // no cached canonical instance, put it into database
		{
			cachedMi = mi;
			metaInfoCache.put(cachedMi, cachedMi);
		}
		return cachedMi;
	}

	protected MetaCellColumnWorker(String condition, String viewName, String columnName, ReportGenerator generator) 
	{
		super(condition, viewName, columnName,generator);
		//this.metaInfoCache = new HashMap<String, Map<Comparable, MetaInfo>>();	
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
		addMetaIfExists(rs, acc, columnName, metaKeyName, defaultValue, retrieveDirectly, false);
	}
	
	protected void addMetaIfExists(ResultSet rs, CategAmountCell acc, String columnName, String metaKeyName, String defaultValue, boolean retrieveDirectly, boolean translate) throws SQLException
	{
		if (columnsMetaData.containsKey(columnName)) {
			
			String fundingStatus = retrieveDirectly ? rs.getString(columnsMetaData.get(columnName) ) :
													retrieveValueFromRS(rs, columnsMetaData.get(columnName));
			
			if (fundingStatus == null && defaultValue != null)
				fundingStatus = defaultValue;
			
			if (fundingStatus != null && translate)
				fundingStatus = TranslatorWorker.translateText(fundingStatus);
			
			if (fundingStatus != null) {
				MetaInfo termsAssistMeta = this.getCachedMetaInfo(metaKeyName, fundingStatus);
				acc.getMetaData().add(termsAssistMeta);
			}
				
		}	
	}

}
