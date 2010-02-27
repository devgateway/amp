package org.digijava.module.aim.fmtool.dbentity;

import java.io.Serializable;

import org.digijava.module.aim.dbentity.AmpFeaturesVisibility;

public class AmpFeatureSourceFeature extends AmpFeatureSource implements Serializable {

	private static final long serialVersionUID = 1L;
	

	public AmpFeatureSourceFeature(){
	}
	
	public AmpFeatureSourceFeature(String name){
		super(name);
	}

/*	
	private AmpFeaturesVisibility feature = null;
	
	public AmpFeaturesVisibility getFeature() {
		return feature;
	}

	public void setFeature(AmpFeaturesVisibility feature) {
		this.feature = feature;
	}
*/
}
