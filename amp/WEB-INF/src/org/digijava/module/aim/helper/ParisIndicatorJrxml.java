package org.digijava.module.aim.helper;

import java.io.IOException;
import java.util.List;

import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;

public abstract class ParisIndicatorJrxml {

	private Site site;

	private String langCode;

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

	public void createJrxml(String filePath, String reportName, String selCurr,
			int cols, int rows, String type) throws IOException {

	}

	public void createSubJrxml(String filePath, String reportName, List data)
			throws IOException, WorkerException {

	}
}
