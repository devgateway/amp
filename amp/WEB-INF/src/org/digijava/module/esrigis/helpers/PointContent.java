package org.digijava.module.esrigis.helpers;

import java.util.ArrayList;
import java.util.List;

import org.digijava.module.aim.helper.ActivitySector;

public class PointContent {

private static final long serialVersionUID = 1L;
	
	private String activityname;
	private String commitments;
	private String disbursements;
	private String expenditures;
	private List<ActivitySector> sectors;
    private List<SimpleDonor> donors;
    private String currecycode; 
    private String id; 
    
    
    public PointContent() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
    
	public String getActivityname() {
		return activityname;
	}

	public PointContent(String commitments,
			String disbursements, ArrayList<SimpleLocation> locations) {
		super();
		this.commitments = commitments;
		this.disbursements = disbursements;
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
	
	public String getExpenditures() {
		return expenditures;
	}

	public void setExpenditures(String expenditures) {
		this.expenditures = expenditures;
	}

	public List<ActivitySector> getSectors() {
		return sectors;
	}

	public void setSectors(List<ActivitySector> sectors) {
		this.sectors = sectors;
	}

	public List<SimpleDonor> getDonors() {
		return donors;
	}

	public void setDonors(List<SimpleDonor> donors) {
		this.donors = donors;
	}

	public String getCurrecycode() {
		return currecycode;
	}

	public void setCurrecycode(String currecycode) {
		this.currecycode = currecycode;
	}

}
