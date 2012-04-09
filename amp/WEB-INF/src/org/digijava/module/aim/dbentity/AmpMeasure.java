package org.digijava.module.aim.dbentity ;

import java.io.Serializable;
import java.util.Set;

public class AmpMeasure  implements Serializable, Cloneable
{

	private Long ampMeasureId ;
	private String name ;
	private AmpIssues issue;
	private Set actors;
	

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getAmpMeasureId() {
		return ampMeasureId;
	}
	public void setAmpMeasureId(Long ampMeasureId) {
		this.ampMeasureId = ampMeasureId;
	}
	
	public AmpIssues getIssue() {
		return issue;
	}
	public void setIssue(AmpIssues issue) {
		this.issue = issue;
	}

	public Set getActors() {
		return actors;
	}
	public void setActors(Set actors) {
		this.actors = actors;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
	
}
