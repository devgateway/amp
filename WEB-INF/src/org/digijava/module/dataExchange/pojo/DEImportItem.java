/**
 * 
 */
package org.digijava.module.dataExchange.pojo;

import org.digijava.module.dataExchange.dbentity.DESourceSetting;

/**
 * @author dan
 *
 */
public class DEImportItem {
	private DESourceSetting DESourceSetting;
	
	
	public DEImportItem(DESourceSetting DESourceSetting) {
		super();
		this.DESourceSetting = DESourceSetting;
	}


	public DEImportItem() {
		// TODO Auto-generated constructor stub
	}

	public String getInputStream(){
		return this.DESourceSetting.getSource();
	}
	
	public DESourceSetting getAmpSourceSetting() {
		return DESourceSetting;
	}

	public void setAmpSourceSetting(DESourceSetting DESourceSetting) {
		this.DESourceSetting = DESourceSetting;
	}
	
	

}
