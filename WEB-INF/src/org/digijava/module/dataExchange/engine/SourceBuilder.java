package org.digijava.module.dataExchange.engine;

import java.io.InputStream;

import org.digijava.module.dataExchange.dbentity.AmpSourceSetting;

public abstract class SourceBuilder {
	
	protected AmpSourceSetting ampSourceSetting;
	protected InputStream inputStream;


	
	public SourceBuilder(AmpSourceSetting ampSourceSetting,
			InputStream inputStream) {
		super();
		this.ampSourceSetting = ampSourceSetting;
		this.inputStream = inputStream;
	}
	
	public SourceBuilder(AmpSourceSetting ampSourceSetting) {
		super();
		this.ampSourceSetting = ampSourceSetting;
	}

	public abstract void process();
	/**
	 * @return the ampSourceSetting
	 */
	public AmpSourceSetting getAmpSourceSetting() {
		return ampSourceSetting;
	}
	/**
	 * @param ampSourceSetting the ampSourceSetting to set
	 */
	public void setAmpSourceSetting(AmpSourceSetting ampSourceSetting) {
		this.ampSourceSetting = ampSourceSetting;
	}
	/**
	 * @return the inputStream
	 */
	public InputStream getInputStream() {
		return inputStream;
	}
	/**
	 * @param inputStream the inputStream to set
	 */
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	
}
