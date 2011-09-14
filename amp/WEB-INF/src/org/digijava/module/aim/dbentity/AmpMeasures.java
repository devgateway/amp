package org.digijava.module.aim.dbentity ;

import java.io.Serializable;
import java.util.Set;

import org.digijava.module.aim.annotations.reports.Identificator;

//seems that it's no longer used
@Deprecated
public class AmpMeasures  implements Serializable, Comparable
{
	@Identificator
	private Long measureId ;
	
	private String measureName ;
	private String aliasName;
	private String type;
	private Set reports;
	private String expression;
	private String description;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
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

	public Set getReports() {
		return reports;
	}
	public void setReports(Set reports) {
		this.reports = reports;
	}
	
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		if (!(o instanceof AmpMeasures))
			return -1;
		AmpMeasures auxColumn=(AmpMeasures) o;
		return this.getMeasureName().compareTo(auxColumn.getMeasureName());
	}
	

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}
}
