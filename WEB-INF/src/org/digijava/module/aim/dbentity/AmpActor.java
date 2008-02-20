package org.digijava.module.aim.dbentity ;

import java.io.Serializable;

public class AmpActor  implements Serializable
{

	private Long ampActorId ;
	private String name;
	private String nameTrimmed;
	private AmpMeasure measure;
	

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getAmpActorId() {
		return ampActorId;
	}
	public void setAmpActorId(Long ampActorId) {
		this.ampActorId = ampActorId;
	}
	
	public AmpMeasure getMeasure() {
		return measure;
	}
	public void setMeasure(AmpMeasure measure) {
		this.measure = measure;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object arg) {
		if (arg instanceof AmpActor) {
			AmpActor actor = (AmpActor) arg;
			return actor.getAmpActorId().equals(ampActorId);
		} 
		throw new ClassCastException();
	}
	public String getNameTrimmed() {
		return name.replace(" ", "");
	}
	public void setNameTrimmed(String nameTrimmed) {
		this.nameTrimmed = nameTrimmed;
	}
	

}
