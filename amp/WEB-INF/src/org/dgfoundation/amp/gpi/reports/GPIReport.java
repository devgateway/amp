package org.dgfoundation.amp.gpi.reports;

import java.util.List;
import java.util.Map;

import org.digijava.kernel.ampapi.endpoints.settings.Settings;

/**
 * Report model used for GPI Reports
 * 
 * @author Viorel Chihai
 *
 */
public class GPIReport {

	/**
	 * Report Page 
	 */
	protected GPIReportPage page;
	
	/**
	 * A map containing the information regarding summary numbers (totals)
	 */
	protected Map<GPIReportOutputColumn, String> summary;

	protected Settings settings;
	
	protected boolean isEmpty;

	public GPIReportPage getPage() {
		return page;
	}

	public void setPage(GPIReportPage page) {
		this.page = page;
	}

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	public Map<GPIReportOutputColumn, String> getSummary() {
		return summary;
	}

	public void setSummary(Map<GPIReportOutputColumn, String> summary) {
		this.summary = summary;
	}

	public boolean isEmpty() {
		if (this.page != null) {
			return this.page.getContents().isEmpty();
		}
		
		return true;
	}
}
