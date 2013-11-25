package org.digijava.module.mondrian.job;

import java.util.*;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.hibernate.Session;

public class PublicViewColumnsUtil 
{
	protected static Logger logger = Logger.getLogger(PublicViewColumnsUtil.class);
	
	public static String getPublicViewTable(String extractorView)
	{
		return ArConstants.VIEW_PUBLIC_PREFIX + extractorView;
	}
	
	/**
	 * returns Map<String extractorViewName, Boolean does_cached_view_exist>
	 * @param session
	 * @return
	 */
	public static Map<String, Boolean> getExtractorColumns(java.sql.Connection conn)
	{
		try
		{
			Map<String, Boolean> result = new TreeMap<String, Boolean>();
			List<String> views = SQLUtils.<String>fetchAsList(conn, "SELECT DISTINCT(extractorview) FROM amp_columns WHERE extractorview IS NOT NULL ORDER BY extractorview", 1);
			for(String viewName:views)
			{
				String cachedViewName = getPublicViewTable(viewName);
				Boolean publicViewExists = SQLUtils.tableExists(conn, cachedViewName);
				result.put(cachedViewName, publicViewExists);
			}
			return result;
		}
		catch(Exception e)
		{
			throw new RuntimeException("error getting extractor columns", e);
		}
	}
	
	public static void createPublicViewTable(java.sql.Connection conn, String extractorView)
	{
		try
		{
			String cachedViewName = getPublicViewTable(extractorView);
			boolean publicViewExists = SQLUtils.tableExists(conn, cachedViewName);
			if (publicViewExists)
				SQLUtils.rawRunQuery(conn, "DROP TABLE " + cachedViewName, null);
		}
		catch(Exception e)
		{
			
		}
	}
	
}
