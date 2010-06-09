/**
 * 
 */
package org.digijava.module.dataExchange.pojo;

import org.digijava.module.dataExchange.dbentity.AmpSourceSetting;

/**
 * @author dan
 *
 */
public class DEImportItem {
	private AmpSourceSetting ampSourceSetting;
	
	
	public DEImportItem(AmpSourceSetting ampSourceSetting) {
		super();
		this.ampSourceSetting = ampSourceSetting;
	}


	public DEImportItem() {
		// TODO Auto-generated constructor stub
	}

	public String getInputStream(){
		return this.ampSourceSetting.getSource();
	}
	
	public AmpSourceSetting getAmpSourceSetting() {
		return ampSourceSetting;
	}

	public void setAmpSourceSetting(AmpSourceSetting ampSourceSetting) {
		this.ampSourceSetting = ampSourceSetting;
	}
	
	

}
