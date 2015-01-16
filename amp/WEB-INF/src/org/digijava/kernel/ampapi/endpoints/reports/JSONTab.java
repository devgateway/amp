package org.digijava.kernel.ampapi.endpoints.reports;

import java.util.ArrayList;
import java.util.List;


import org.dgfoundation.amp.ar.AmpARFilter;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.translation.util.MultilingualInputFieldValues;

public class JSONTab {
	private long id;
	private String name;
	private Boolean visible;
	private AmpARFilter filter;
	private List <JsonBean> translatedNames = new ArrayList <JsonBean> ();

	public JSONTab(Long ampReportId, String name, boolean visible) {
		this.id = ampReportId;
		this.name = name;
		this.visible = visible;
	}
	
	public JSONTab(Long ampReportId, boolean visible) {
		this.id = ampReportId;
		populateNames ();
		this.visible = visible;
	}
	public JSONTab() {
		// TODO Auto-generated constructor stub
	}
	
	private void populateNames () {
		MultilingualInputFieldValues inputValues = new MultilingualInputFieldValues(AmpReports.class, id, "name", null, null);
		for (String language: inputValues.getTranslations().keySet()) {
			JsonBean translatedName = new JsonBean();
			translatedName.set(language, inputValues.getTranslations().get(language));
			if (TLSUtils.getEffectiveLangCode().equals(language)) {
				name = inputValues.getTranslations().get(language);
			}
			translatedNames.add(translatedName);
		}
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public AmpARFilter getFilter() {
		return filter;
	}
	public void setFilter(AmpARFilter filter) {
		this.filter = filter;
	}
	public Boolean getVisible() {
		return visible;
	}
	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public List<JsonBean> getTranslatedNames() {
		return translatedNames;
	}

	public void setTranslatedNames(List<JsonBean> translatedNames) {
		this.translatedNames = translatedNames;
	}

}
