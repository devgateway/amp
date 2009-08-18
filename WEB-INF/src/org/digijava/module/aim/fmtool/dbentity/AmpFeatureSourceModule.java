package org.digijava.module.aim.fmtool.dbentity;

import java.io.Serializable;

import org.digijava.module.aim.dbentity.AmpModulesVisibility;

public class AmpFeatureSourceModule extends AmpFeatureSource implements Serializable {

	private static final long serialVersionUID = 1L;

	
	public AmpFeatureSourceModule(){
		
	}
	
	public AmpFeatureSourceModule(String name){
		super(name);
	}
	
/*	
	private AmpModulesVisibility feature = null;

	public AmpModulesVisibility getFeature() {
		return feature;
	}

	public void setFeature(AmpModulesVisibility feature) {
		this.feature = feature;
	}
*/	
}
