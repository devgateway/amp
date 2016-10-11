/**
 * 
 */
package org.digijava.module.dataExchange.engine;

import java.io.InputStream;

import org.digijava.module.dataExchange.dbentity.DESourceSetting;

/**
 * @author dan
 *
 */
public class UrlSourceBuilder extends SourceBuilder {
	
	
	public UrlSourceBuilder(DESourceSetting ampSourceSetting) {
		super(ampSourceSetting);
		// TODO Auto-generated constructor stub
	}

	public UrlSourceBuilder(DESourceSetting ampSourceSetting,
			String inputStream) {
		super(ampSourceSetting, inputStream);
		// TODO Auto-generated constructor stub
	}

	
	/* (non-Javadoc)
	 * @see org.digijava.module.dataExchange.engine.SourceBuilder#process()
	 */
	/**
	 * This will initialize the private property inputStream.
	 * This must be implemented by all implementers (URLSourceBuilder and WSSourceBuilder).
	 * For FileSourceBuilder the InputStream will be given as a parameter in the constructor.
	 */
	@Override
	public void process() {
		// TODO Auto-generated method stub

	}

}
