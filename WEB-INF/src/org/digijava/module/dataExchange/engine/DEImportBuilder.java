/**
 * 
 */
package org.digijava.module.dataExchange.engine;

import org.digijava.module.dataExchange.pojo.DEImportItem;

/**
 * @author dan
 *
 */
public class DEImportBuilder {
	private DEImportItem ampImportItem;

	public DEImportBuilder() {
		// TODO Auto-generated constructor stub
	}
	
	public DEImportBuilder(DEImportItem ampImportItem) {
		super();
		this.ampImportItem = ampImportItem;
	}

	public DEImportItem getAmpImportItem() {
		return ampImportItem;
	}

	public void setAmpImportItem(DEImportItem ampImportItem) {
		this.ampImportItem = ampImportItem;
	}
	
	public void checkForErrors(){
		
	}
}
