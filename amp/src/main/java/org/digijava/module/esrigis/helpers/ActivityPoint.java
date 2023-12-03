package org.digijava.module.esrigis.helpers;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 * @author Diego Dimunzio
 * 
 */
public class ActivityPoint {

    public String getDonorcode() {
        return donorcode;
    }

    public void setDonorcode(String donorcode) {
        this.donorcode = donorcode;
    }

    private static final long serialVersionUID = 1L;
    
    private String id;
    private String ampactivityid;
    private String activityname;
    private String commitments;
    private String disbursements;
    //private String expenditures;
    private ArrayList<SimpleLocation> locations;
    private ArrayList<Structure> structures;
    private String implementationlevel;
    //private List<ActivitySector> sectors;
    private List<SimpleDonor> donors;
    //private String currecycode; 
    private String donorcode; 
    
    public ActivityPoint() {
        super();
        // TODO Auto-generated constructor stub
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

    public String getActivityname() {
        return activityname;
    }

    public ActivityPoint(String activityname, String commitments,
            String disbursements, ArrayList<SimpleLocation> locations) {
        super();
        this.activityname = activityname;
        //this.commitments = commitments;
        //this.disbursements = disbursements;
        this.setLocations(locations);
    }

    public void setActivityname(String activityname) {
        this.activityname = activityname;
    }
/*
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
    */
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
/*
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
*/
    public String getAmpactivityid() {
        return ampactivityid;
    }

    public void setAmpactivityid(String ampactivityid) {
        this.ampactivityid = ampactivityid;
    }

    
    public void setStructures(ArrayList<Structure> structures) {
        this.structures = structures;
    }

    public ArrayList<Structure> getStructures() {
        return structures;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<SimpleDonor> getDonors() {
        return donors;
    }

    public void setDonors(List<SimpleDonor> donors) {
        this.donors = donors;
    }
/*
    public String getCurrecycode() {
        return currecycode;
    }

    public void setCurrecycode(String currecycode) {
        this.currecycode = currecycode;
    }
*/
}
    
