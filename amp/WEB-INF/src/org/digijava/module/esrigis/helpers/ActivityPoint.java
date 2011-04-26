package org.digijava.module.esrigis.helpers;

import java.io.Writer;
import java.util.ArrayList;

import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;

import net.sf.json.JSON;

/**
 * 
 * @author Diego Dimunzio
 * 
 */
public class ActivityPoint {

	private static final long serialVersionUID = 1L;

	private String activityname;
	private String commitments;
	private String disbursements;
	private ArrayList<SimpleLocation> locations;
    private String implementationlevel;
	
	public ActivityPoint() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getActivityname() {
		return activityname;
	}

	public ActivityPoint(String activityname, String commitments,
			String disbursements, ArrayList<SimpleLocation> locations) {
		super();
		this.activityname = activityname;
		this.commitments = commitments;
		this.disbursements = disbursements;
		this.setLocations(locations);
	}

	public void setActivityname(String activityname) {
		this.activityname = activityname;
	}

	public String getCommitments() {
		return commitments;
	}

	public void setCommitments(String commitments) {
		this.commitments = commitments;
	}

	public String getDisbursements() {
		return disbursements;
	}

	public void setDisbursements(String disbursements) {
		this.disbursements = disbursements;
	}
	
	public void setLocations(ArrayList<SimpleLocation> locations) {
		this.locations = locations;
	}

	public ArrayList<SimpleLocation> getLocations() {
		return locations;
	}

	public void setImplementationlevel(String implementationlevel) {
		this.implementationlevel = implementationlevel;
	}

	public String getImplementationlevel() {
		return implementationlevel;
	}

}
	
