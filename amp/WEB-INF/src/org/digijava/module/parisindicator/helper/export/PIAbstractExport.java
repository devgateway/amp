package org.digijava.module.parisindicator.helper.export;

import java.util.Collection;
import java.util.Map;

import org.digijava.kernel.request.Site;
import org.digijava.module.parisindicator.helper.PIReportAbstractRow;

public abstract class PIAbstractExport {

	// Change this value in the constructor of each report.
	int NUMBER_OF_CELLS = 0;
	private Site site;
	private String langCode;

	public abstract Collection generateDataSource(Collection<PIReportAbstractRow> rows) throws Exception;

	public abstract Map<String, String> getParameters(int yearSeparator) throws Exception;

	public PIAbstractExport(Site site, String langcode) {
		this.site = site;
		this.langCode = langcode;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public String getLangCode() {
		return langCode;
	}

	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}
}