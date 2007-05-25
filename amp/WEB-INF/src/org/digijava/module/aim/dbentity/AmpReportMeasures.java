package org.digijava.module.aim.dbentity ;

import java.io.Serializable;


public class AmpReportMeasures  implements Serializable
{
	private AmpMeasures measure;
	private String measureType;
	
	public AmpMeasures getMeasure() {
		return measure;
	}
	public void setMeasure(AmpMeasures measure) {
		this.measure = measure;
	}

	public String getMeasureType() {
		return measureType;
	}
	public void setMeasureType(String measureType) {
		this.measureType = measureType;
	}
}