package org.digijava.module.aim.dbentity;

import org.digijava.module.aim.annotations.reports.Identificator;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class GPISetup implements Serializable, Comparable {

    public static final String ACTUAL_DISBURSEMENTS = "Actual Disbursements";
    public static final String PLANNED_DISBURSEMENTS = "Planned Disbursements";
    public static final String ACTUAL_COMMITMENTS = "Actual Commitments";
    public static final String PLANNED_COMMITMENTS = "Planned Commitments";
    public static final String ACTUAL_EXPENDITURES = "Actual Expenditures";
    public static final String PLANNED_EXPENDITURES = "Planned Expenditures";

    @Identificator
    private Long gpiSetupId;
    private String indicator5aActualDisbursement;
    private String indicator5aPlannedDisbursement;
    private String indicator6ScheduledDisbursements;
    private String indicator9bDisbursements;

    private transient Map<String, String> listOfValues;

    public static Map<String, String> getListOfValues() {
        Map<String, String> ret = new HashMap<String, String>();
        ret.put("ACTUAL_COMMITMENTS", ACTUAL_COMMITMENTS);
        ret.put("PLANNED_COMMITMENTS", PLANNED_COMMITMENTS);
        ret.put("ACTUAL_DISBURSEMENTS", ACTUAL_DISBURSEMENTS);
        ret.put("PLANNED_DISBURSEMENTS", PLANNED_DISBURSEMENTS);
        ret.put("ACTUAL_EXPENDITURES", ACTUAL_EXPENDITURES);
        ret.put("PLANNED_EXPENDITURES", PLANNED_EXPENDITURES);
        return ret;
    }

    public Long getGpiSetupId() {
        return gpiSetupId;
    }

    public void setGpiSetupId(Long gpiSetupId) {
        this.gpiSetupId = gpiSetupId;
    }

    @Override
    public int compareTo(Object o) {
        // TODO Auto-generated method stub
        return 0;
    }

    public String getIndicator5aActualDisbursement() {
        return indicator5aActualDisbursement;
    }

    public void setIndicator5aActualDisbursement(String indicator5aActualDisbursement) {
        this.indicator5aActualDisbursement = indicator5aActualDisbursement;
    }

    public String getIndicator5aPlannedDisbursement() {
        return indicator5aPlannedDisbursement;
    }

    public void setIndicator5aPlannedDisbursement(String indicator5aPlannedDisbursement) {
        this.indicator5aPlannedDisbursement = indicator5aPlannedDisbursement;
    }

    public String getIndicator6ScheduledDisbursements() {
        return indicator6ScheduledDisbursements;
    }

    public void setIndicator6ScheduledDisbursements(String indicator6ScheduledDisbursements) {
        this.indicator6ScheduledDisbursements = indicator6ScheduledDisbursements;
    }

    public String getIndicator9bDisbursements() {
        return indicator9bDisbursements;
    }

    public void setIndicator9bDisbursements(String indicator9bDisbursements) {
        this.indicator9bDisbursements = indicator9bDisbursements;
    }

    public static String getActualDisbursements() {
        return ACTUAL_DISBURSEMENTS;
    }

    public static String getPlannedDisbursements() {
        return PLANNED_DISBURSEMENTS;
    }

    public static String getActualCommitments() {
        return ACTUAL_COMMITMENTS;
    }

    public static String getPlannedCommitments() {
        return PLANNED_COMMITMENTS;
    }

    public static String getActualExpenditures() {
        return ACTUAL_EXPENDITURES;
    }

    public static String getPlannedExpenditures() {
        return PLANNED_EXPENDITURES;
    }

}
