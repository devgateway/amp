package org.dgfoundation.amp.gpi.reports;

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

	public boolean isEmpty() {
		if (this.page != null) {
			return this.page.getContents().isEmpty();
		}
		
		return true;
	}
}
