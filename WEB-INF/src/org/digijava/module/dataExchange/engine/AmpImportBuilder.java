/**
 * 
 */
package org.digijava.module.dataExchange.engine;

import org.digijava.module.dataExchange.pojo.AmpImportItem;

/**
 * @author dan
 *
 */
public class AmpImportBuilder {
	private AmpImportItem ampImportItem;

	public AmpImportBuilder() {
		// TODO Auto-generated constructor stub
	}
	
	public AmpImportBuilder(AmpImportItem ampImportItem) {
		super();
		this.ampImportItem = ampImportItem;
	}

	public AmpImportItem getAmpImportItem() {
		return ampImportItem;
	}

	public void setAmpImportItem(AmpImportItem ampImportItem) {
		this.ampImportItem = ampImportItem;
	}
	
	public void checkForErrors(){
		
	}
}
