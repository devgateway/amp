package org.dgfoundation.amp.nireports;

public interface SchemaSpecificScratchpad extends AutoCloseable {
	
	/**
	 * the precision with which to run the internal calculations. Should be at least 2 orders of magnitude better than the format used for displaying
	 * @return
	 */
	public NiPrecisionSetting getPrecisionSetting();
}
