package org.digijava.module.esrigis.helpers;

import org.digijava.module.aim.helper.ActivitySector;

import java.util.ArrayList;
import java.util.List;

public class PointContent {

private static final long serialVersionUID = 1L;
    
    private String activityname;
    private String commitments;
    private String disbursements;
    private String expenditures;
    private String mtef;
    private List<ActivitySector> sectors;
    private List<SimpleDonor> donors;
    private String currecycode; 
    private String currencyname;
    private String id; 
    private String commitmentsforlocation;
    private String disbursementsforlocation;
    private String expendituresforlocation;
    private String mtefforlocation;
    
    public PointContent() {
        super();
        // TODO Auto-generated constructor stub
    }
    

    public PointContent(String commitments,
            String disbursements, ArrayList<SimpleLocation> locations) {
        super();
        this.commitments = commitments;
        this.disbursements = disbursements;
    }
    
    public String getCommitmentsforlocation() {
        return fixZero(commitmentsforlocation);
    }

    public void setCommitmentsforlocation(String commitmentsforlocation) {
        this.commitmentsforlocation = commitmentsforlocation;
    }

    public String getDisbursementsforlocation() {
        return fixZero(disbursementsforlocation);
    }

    public void setDisbursementsforlocation(String disbursementsforlocation) {
        this.disbursementsforlocation = disbursementsforlocation;
    }

    public String getExpendituresforlocation() {
        return fixZero(expendituresforlocation);
    }

    public void setExpendituresforlocation(String expendituresforlocation) {
        this.expendituresforlocation = expendituresforlocation;
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

    public void setActivityname(String activityname) {
        this.activityname = activityname;
    }

    public String getCommitments() {
        return fixZero(commitments);
    }

    public void setCommitments(String commitments) {
        this.commitments = commitments;
    }

    public String getDisbursements() {
        return fixZero(disbursements);
    }

    public void setDisbursements(String disbursements) {
        this.disbursements = disbursements;
    }
    
    public String getExpenditures() {
        return fixZero(expenditures);
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

    public String getMtef()
    {
        return fixZero(mtef);
    }
    
    public void setMtef(String mtef)
    {
        this.mtef = mtef;
    }
    
    public String getMtefforlocation()
    {
        return fixZero(mtefforlocation);
    }
    
    public void setMtefforlocation(String mtefforlocation)
    {
        this.mtefforlocation = mtefforlocation;
    }
    
    public final static String fixZero(String input)
    {
        if ((input == null || input.isEmpty()))
            return "0";
        return input;
    }


    public String getCurrencyname() {
        return currencyname;
    }


    public void setCurrencyname(String currencyname) {
        this.currencyname = currencyname;
    }
}
