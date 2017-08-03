package org.digijava.module.aim.helper;

import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.util.ActivityUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.hibernate.Session;

/**
 * @author Aldo Picca
 */
public class SummaryChangesService {

    private static final Logger LOGGER = Logger.getLogger(SummaryChangesService.class);
    public static final String NEW = "New";
    public static final String EDITED = "Edited";
    public static final String DELETED = "Deleted";

    /**
     * Return a list of activities that were modified in the 24 hours prior to the date.
     *
     * @param fromDate filter by date
     * @return list of activities.
     */
    public static List<AmpActivityVersion> getActivitiesChanged(Date fromDate) {
        return ActivityUtil.getActivitiesChanged(fromDate);
    }

    public static String buildActivitiesChanged(Date fromDate) {
        StringBuffer results = new StringBuffer();

        LinkedHashMap<String, Object> activityList = SummaryChangesService.getSummaryChanges(SummaryChangesService
                .getActivitiesChanged(fromDate));

        for (String activity : activityList.keySet()) {
            Session session = PersistenceManager.getRequestDBSession();
            AmpActivityVersion activityVersion = (AmpActivityVersion) session.load(AmpActivityVersion.class,
                    Long.parseLong(activity));

            LinkedHashMap<String, Object> changesList = (LinkedHashMap) activityList.get(activity);
            SummaryChangeHtmlRenderer renderer = new SummaryChangeHtmlRenderer(activityVersion, changesList, null);
            LOGGER.info(renderer.render());

        }
        return results.toString();
    }

    /**
     * Return a list of approvers whit the activities that changed.
     *
     * @param activities activities list.
     * @return list of approvers and activities.
     */
    public static Map<String, Collection<AmpActivityVersion>> getValidators(List<AmpActivityVersion> activities) {

        Map<String, Collection<AmpActivityVersion>> results = new LinkedHashMap<>();

        for (AmpActivityVersion currentActivity : activities) {
            List<AmpTeamMember> teamHeadAndAndApprovers = TeamMemberUtil.getTeamHeadAndApprovers(currentActivity
                    .getTeam().getAmpTeamId());

            for (AmpTeamMember approver : teamHeadAndAndApprovers) {
                String key = approver.getUser().getEmail();
                if (results.get(key) == null) {
                    results.put(key, new ArrayList<AmpActivityVersion>());
                }
                results.get(key).add(currentActivity);
            }
        }
        return results;
    }

    /**
     * Return a list of activities whit every funding change.
     *
     * @param activities activities list.
     * @return list of activities and changes.
     */
    public static LinkedHashMap<String, Object> getSummaryChanges(List<AmpActivityVersion> activities) {

        LinkedHashMap<String, Object> activitiesChanges = new LinkedHashMap<>();
        for (AmpActivityVersion currentActivity : activities) {
            activitiesChanges.putAll(processActivity(currentActivity));
        }
        return activitiesChanges;
    }

    public static LinkedHashMap<String, Object> processActivity(AmpActivityVersion currentActivity) {

        LinkedHashMap<String, Object> activitiesChanges = new LinkedHashMap<>();

        AmpActivityVersion previousActivity = ActivityUtil.getPreviousVersion(currentActivity);

        Map<String, Collection<SummaryChange>> differences = new LinkedHashMap<String, Collection<SummaryChange>>();

        if ((currentActivity.getFunding() == null && previousActivity.getFunding() == null)
                || (currentActivity.getFunding() == null && previousActivity.getFunding().size() == 0)
                || (currentActivity.getFunding().size() == 0 && previousActivity.getFunding() == null)
                || (currentActivity.getFunding().size() == 0 && previousActivity.getFunding().size() == 0)) {
            // Collections are equal.
            return null;
        }

        for (AmpFunding currentFunding : currentActivity.getFunding()) {
            for (AmpFunding previousFunding : previousActivity.getFunding()) {
                if (currentFunding.equalsForVersioning(previousFunding)) {
                    Object auxValue1 = currentFunding.getValue() != null ? currentFunding.getValue() : "";
                    Object auxValue2 = previousFunding.getValue() != null ? previousFunding.getValue() : "";
                    if (!auxValue1.equals(auxValue2)) {

                        for (AmpFundingDetail currentFundingDetail : currentFunding.getFundingDetails()) {
                            boolean fundingDetailFound = checkChanges(differences, previousFunding,
                                    currentFundingDetail);
                            if (!fundingDetailFound) {
                                setNewChange(differences, currentFundingDetail);
                            }
                        }

                        for (AmpFundingDetail previousFundingDetail : previousFunding.getFundingDetails()) {
                            boolean fundingDetailFound = checkChanges(differences, currentFunding,
                                    previousFundingDetail);
                            if (!fundingDetailFound) {
                                setDeletedChange(differences, previousFundingDetail);
                            }
                        }

                    }
                }
            }

            activitiesChanges.put(currentActivity.getAmpActivityId().toString(), differences);
        }
        return activitiesChanges;
    }

    private static boolean checkChanges(Map<String, Collection<SummaryChange>> differences, AmpFunding funding,
                                        AmpFundingDetail ampFundingDetail) {
        boolean fundingDetailFound = false;
        for (AmpFundingDetail previousFundingDetail : funding.getFundingDetails()) {
            if (isSameFundingDetail(ampFundingDetail, previousFundingDetail)) {
                fundingDetailFound = true;
                if (isAmountChanged(ampFundingDetail, previousFundingDetail)) {
                    setEditedChange(differences, ampFundingDetail, previousFundingDetail);
                }
                break;
            }
        }
        return fundingDetailFound;
    }

    private static boolean isSameFundingDetail(AmpFundingDetail currentFundingDetail, AmpFundingDetail
            previousFundingDetail) {
        return ((currentFundingDetail.getReportingDate() != null &&
                currentFundingDetail.getReportingDate().equals(previousFundingDetail.getReportingDate()))
                ||
                (currentFundingDetail.getReportingDate() == null &&
                        previousFundingDetail.getReportingDate() == null &&
                        currentFundingDetail.getTransactionType().equals(previousFundingDetail.getTransactionType()) &&
                        currentFundingDetail.getTransactionDate().equals(previousFundingDetail.getTransactionDate()) &&
                        currentFundingDetail.getTransactionType().equals(previousFundingDetail.getTransactionType()) &&
                        currentFundingDetail.getAmpCurrencyId().equals(previousFundingDetail.getAmpCurrencyId()) &&
                        currentFundingDetail.getTransactionAmount().equals(previousFundingDetail.getTransactionAmount())
                )
        );
    }

    private static boolean isAmountChanged(AmpFundingDetail current, AmpFundingDetail previous) {
        return current.getTransactionAmount().compareTo(previous.getTransactionAmount()) != 0
                || current.getAmpCurrencyId() != previous.getAmpCurrencyId();
    }

    private static boolean isDateChanged(AmpFundingDetail current, AmpFundingDetail previous) {
        return current.getTransactionDate() != previous.getTransactionDate()
                || current.getAdjustmentType() != previous.getAdjustmentType();
    }

    private static void setNewChange(Map<String, Collection<SummaryChange>> objDiff, AmpFundingDetail fundingDetail) {

        SummaryChange summaryChange = new SummaryChange(fundingDetail.getTransactionType(),
                fundingDetail.getAdjustmentType(), NEW, null, null,
                fundingDetail.getTransactionAmount(), fundingDetail.getAmpCurrencyId(),
                fundingDetail.getTransactionDate());

        addChange(objDiff, summaryChange);
    }

    private static void addChange(Map<String, Collection<SummaryChange>> objDiff, SummaryChange summaryChange) {
        String key = summaryChange.getQuarter().getQuarterNumber().toString();

        if (objDiff.get(key) == null) {
            objDiff.put(key, new ArrayList<SummaryChange>());
        }

        objDiff.get(key).add(summaryChange);
    }

    private static void setDeletedChange(Map<String, Collection<SummaryChange>> objDiff, AmpFundingDetail
            fundingDetail) {

        SummaryChange summaryChange = new SummaryChange(fundingDetail.getTransactionType(),
                fundingDetail.getAdjustmentType(), DELETED, fundingDetail.getTransactionAmount(),
                fundingDetail.getAmpCurrencyId(),  null, null, fundingDetail.getTransactionDate());

        addChange(objDiff, summaryChange);
    }

    private static void setEditedChange(Map<String, Collection<SummaryChange>> objDiff, AmpFundingDetail
            curr, AmpFundingDetail previus) {

        SummaryChange summaryChange = new SummaryChange(curr.getTransactionType(),
                curr.getAdjustmentType(), EDITED, previus.getTransactionAmount(), previus.getAmpCurrencyId(),
                curr.getTransactionAmount(), curr.getAmpCurrencyId(), curr.getTransactionDate());

        addChange(objDiff, summaryChange);
    }
}
