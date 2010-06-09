package org.digijava.module.dataExchange.engine;

import java.io.InputStream;

import org.digijava.module.dataExchange.dbentity.DESourceSetting;

public abstract class SourceBuilder {
	
	protected DESourceSetting DESourceSetting;
	protected InputStream inputStream;


	
	public SourceBuilder(DESourceSetting DESourceSetting,
			InputStream inputStream) {
		super();
		this.DESourceSetting = DESourceSetting;
		this.inputStream = inputStream;
	}
	
	public SourceBuilder(DESourceSetting DESourceSetting) {
		super();
		this.DESourceSetting = DESourceSetting;
	}

	/**
	 * This will initialize the private property inputStream.
	 * This must be implemented by all implementers (URLSourceBuilder and WSSourceBuilder).
	 * For FileSourceBuilder the InputStream will be given as a parameter in the constructor.
	 */
	public abstract void process();
	/**
	 * @return the DESourceSetting
	 */
	public DESourceSetting getAmpSourceSetting() {
		return DESourceSetting;
	}
	/**
	 * @param DESourceSetting the DESourceSetting to set
	 */
	public void setAmpSourceSetting(DESourceSetting DESourceSetting) {
		this.DESourceSetting = DESourceSetting;
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
