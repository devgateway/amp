package org.digijava.module.widget.helper;


import java.util.List;

import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.util.DecimalWraper;

public class ActivitySectorDonorFunding {
    private DecimalWraper commitment;
    private DecimalWraper disbursement;
    private DecimalWraper expenditure;
    private AmpActivityVersion activity;
    private List<AmpSector> sectors;
    private List<AmpOrganisation> donorOrgs;

    public List<AmpOrganisation> getDonorOrgs() {
        return donorOrgs;
    }

    public void setDonorOrgs(List<AmpOrganisation> donorOrgs) {
        this.donorOrgs = donorOrgs;
    }

    public List<AmpSector> getSectors() {
        return sectors;
    }

    public void setSectors(List<AmpSector> sectors) {
        this.sectors = sectors;
    }
   

    public AmpActivityVersion getActivity() {
        return activity;
    }

    public DecimalWraper getCommitment() {
        return commitment;
    }

    public DecimalWraper getDisbursement() {
        return disbursement;
    }

    public DecimalWraper getExpenditure() {
        return expenditure;
    }

    public String getFmtCommitment() {
        String fmtCommitment=null;
        if (commitment != null&&commitment.doubleValue()!=0) {
            fmtCommitment = FormatHelper.formatNumber(commitment.doubleValue());
        } 
        return fmtCommitment;
    }

    public String getFmtDisbursement() {
        String fmtDisbursement=null;
        if (disbursement != null&&disbursement.doubleValue()!=0) {
            fmtDisbursement = FormatHelper.formatNumber(disbursement.doubleValue());
        } 

        return fmtDisbursement;
    }

    public String getFmtExpenditure() {
        String fmtExpenditure=null;
        if (expenditure != null&&expenditure.doubleValue()!=0) {
            fmtExpenditure = FormatHelper.formatNumber(expenditure.doubleValue());
        }
        return fmtExpenditure;
    }

    public void setActivity(AmpActivityVersion activity) {
        this.activity = activity;
    }

    public void setCommitment(DecimalWraper commitment) {
        this.commitment = commitment;
    }

    public void setDisbursement(DecimalWraper disbursement) {
        this.disbursement = disbursement;
    }

    public void setExpenditure(DecimalWraper expenditure) {
        this.expenditure = expenditure;
    }

   

   
}

