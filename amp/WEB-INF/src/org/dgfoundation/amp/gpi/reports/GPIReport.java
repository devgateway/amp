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
	 * Report Output 
	 */
	protected GPIReportOutput output;

	protected Settings settings;
	
	protected boolean isEmpty;

	public GPIReportOutput getOutput() {
		return output;
	}

	public void setOutput(GPIReportOutput output) {
		this.output = output;
	}

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	public boolean isEmpty() {
		if (this.output != null) {
			return this.output.getContents().isEmpty();
		}
		
		return true;
	}
}
