package org.digijava.module.mondrian.query;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mondrian.olap.Util.PropertyList;
import mondrian.rolap.CacheControlImpl;
import mondrian.spi.DynamicSchemaProcessor;
import mondrian.spi.impl.FilterDynamicSchemaProcessor;

import org.apache.commons.vfs.FileContent;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.VFS;
import org.apache.log4j.Logger;

/**
 * @author Diego Dimunzio
 * @since January 09,2009
 * 
 */


public class SchemaManager extends FilterDynamicSchemaProcessor implements
		DynamicSchemaProcessor {
	protected static Logger logger = Logger.getLogger(SchemaManager.class);
	

	static String AMP_ACTIVITY_TABLE = "(\\bamp_activity\\b)";
	static String CACHED_ACTIVITY_TABLE = "cached_amp_activity";
	
	@Override
	public String processSchema(String arg0, PropertyList arg1)
			throws Exception {
		FileSystemManager fsManager = VFS.getManager();
		File userDir = new File("").getAbsoluteFile();
		FileObject file = fsManager.resolveFile(userDir, arg0);
		FileContent fileContent = ((org.apache.commons.vfs.FileObject) file)
				.getContent();
		String schema = super.filter(arg0, arg1, fileContent.getInputStream());
	 	CacheControlImpl cache = new  mondrian.rolap.CacheControlImpl();
	 	cache.flushSchemaCache();
	 	return setTeamQuery(schema); 
	}
	
	public String setTeamQuery(String schema) {
		String newschema = "";
		newschema = schema.replaceAll("(?:@donorquery)+", getQueryText());
		logger.info("Query = " + getQueryText());
		return newschema;
	}

	public String getQueryText() {
		String result = MondrianQuery.createQuery(); 
		Pattern p = Pattern.compile(AMP_ACTIVITY_TABLE);
		Matcher m = p.matcher(result);
		result = m.replaceAll(CACHED_ACTIVITY_TABLE);
		return result;
	}
}
