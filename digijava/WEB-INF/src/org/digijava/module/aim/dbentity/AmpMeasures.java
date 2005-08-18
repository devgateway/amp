package org.digijava.module.aim.dbentity ;

import java.io.Serializable;
import java.util.Set;


public class AmpMeasures  implements Serializable
{

	private Long measureId ;
	private String measureName ;
	private String aliasName;

	public String getAliasName() {
		return aliasName;
	}
	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}
	
	public Long getMeasureId() {
		return measureId;
	}
	public void setMeasureId(Long measureId) {
		this.measureId = measureId;
	}
	public String getMeasureName() {
		return measureName;
	}
	public void setMeasureName(String measureName) {
		this.measureName = measureName;
	}
}
