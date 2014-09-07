package org.digijava.kernel.ampapi.mondrian.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.startup.AMPStartupListener;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.categorymanager.util.CategoryConstants;

import mondrian.olap.Util.PropertyList;
import mondrian.spi.DynamicSchemaProcessor;

public class AmpMondrianSchemaProcessor implements DynamicSchemaProcessor {

	@Override
	public String processSchema(String schemaURL, PropertyList connectInfo) throws Exception {
		String rootDir = AMPStartupListener.SERVLET_CONTEXT_ROOT_REAL_PATH;
		String url2 = schemaURL.replaceAll("res:\\.\\.", rootDir + File.separator + "WEB-INF");
		String url = schemaURL.replaceAll("res:\\.\\.", "WEB-INF");
		String contents = null;
		//try(InputStreamReader isr = new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(url), "utf-8")) { 
		try(InputStreamReader isr = new InputStreamReader(new FileInputStream(url2), "utf-8")) {
			try(Scanner scanner = new Scanner(isr)) {
				contents = scanner.useDelimiter("\\Z").next();
			}}
		if (contents == null)
			throw new RuntimeException("could not read schema");
		if (needToFlushSchemaCache())
			new mondrian.rolap.CacheControlImpl(null).flushSchemaCache();
		return processContents(contents);
	};
	
	public String processContents(String contents) {
		contents = contents.replaceAll("@@actual@@", Long.toString(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getIdInDatabase()));
		contents = contents.replaceAll("@@planned@@", Long.toString(CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getIdInDatabase()));
		contents = contents.replaceAll("@@currency@@", Long.toString(getReportCurrency().getAmpCurrencyId()));
		String locale = getReportLocale();
		contents = contents.replaceAll("@@locale@@", locale);
		int pos = contents.indexOf("@@");
		if (pos >= 0)
			throw new RuntimeException("your schema contains unrecognized tag: " + contents.substring(pos, contents.indexOf("@@", pos + 2) + 2));
		return contents;
	}
	
	protected String getReportLocale() {
		//return "_ro";
		return "";
	}
	
	protected AmpCurrency getReportCurrency() {
		return CurrencyUtil.getCurrencyByCode("EUR"); 
	}
	
	protected boolean needToFlushSchemaCache() {
		return true;
	}
	
}
