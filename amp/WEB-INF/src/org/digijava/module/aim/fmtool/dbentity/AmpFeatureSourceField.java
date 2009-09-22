package org.digijava.module.aim.fmtool.dbentity;

import java.io.Serializable;

import org.digijava.module.aim.dbentity.AmpFieldsVisibility;

public class AmpFeatureSourceField extends AmpFeatureSource implements Serializable {

	private static final long serialVersionUID = 1L;

	public AmpFeatureSourceField(){
		
	}
	
	public AmpFeatureSourceField(String name){
		super(name);
	}
/* 	
	private AmpFieldsVisibility feature = null;
	
	public AmpFieldsVisibility getFeature() {
		return feature;
	}

	public void setFeature(AmpFieldsVisibility feature) {
		this.feature = feature;
	}
*/
}
