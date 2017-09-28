package org.digijava.module.gpi.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivityGroup;
import org.digijava.module.aim.dbentity.AmpActivityInternalId;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpAhsurveyIndicator;
import org.digijava.module.aim.dbentity.AmpGPINiIndicator;
import org.digijava.module.aim.dbentity.AmpGPINiQuestion;
import org.digijava.module.aim.dbentity.AmpGPISurveyQuestion;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.helper.fiscalcalendar.EthiopianCalendar;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.common.util.DateTimeUtil;
import org.hibernate.Query;
import org.hibernate.Session;

public class GPIUtils {
    
    private static Logger logger = Logger.getLogger(GPIUtils.class);

    /**
     * Converts a textual representation of boolean to numeric 1 for yes/true, 0
     * for rest
     * 
     * @param text
     * @return
     */
    public static int convTxtBolToNum(String text) {
        if ("Yes".equalsIgnoreCase(text) || "true".equalsIgnoreCase(text))
            return 1;
        return 0;
    }

    public final static boolean containSectors(Collection<AmpSector> sectors1, Collection<AmpActivitySector> sectors2) throws Exception {
        boolean ret = false;
        Iterator<AmpSector> iter1 = sectors1.iterator();
        Iterator<AmpActivitySector> iter2 = sectors2.iterator();
        while (iter1.hasNext()) {
            AmpSector aux1 = iter1.next();
            while (iter2.hasNext()) {
                AmpActivitySector aux2 = iter2.next();
                if (aux1.getAmpSectorId().equals(aux2.getSectorId().getAmpSectorId())) {
                    ret = true;
                    break;
                }
            }
        }
        return ret;
    }

    public final static boolean containStatus(Collection<AmpCategoryValue> statuses, AmpCategoryValue status) throws Exception {
        boolean ret = false;
        Iterator<AmpCategoryValue> iterStatus = statuses.iterator();
        while (iterStatus.hasNext()) {
            AmpCategoryValue aux = iterStatus.next();
            if (aux.getId().equals(status.getId())) {
                ret = true;
                break;
            }
        }
        return ret;
    }

    public final static boolean containFinancingInstrument(AmpCategoryValue financing1, Collection<AmpCategoryValue> financing2) throws Exception {
        boolean ret = false;
        if (financing1 != null) {
            Iterator<AmpCategoryValue> iter2 = financing2.iterator();
            while (iter2.hasNext()) {
                AmpCategoryValue aux1 = iter2.next();
                if (aux1.getId().equals(financing1.getId())) {
                    ret = true;
                    break;
                }
            }
        }
        return ret;
    }

    public final static boolean containOrganisations(Collection<AmpOrganisation> orgs1, AmpOrganisation org2) throws Exception {
        boolean ret = false;
        if (org2 != null) {
            Iterator<AmpOrganisation> iter1 = orgs1.iterator();
            while (iter1.hasNext()) {
                AmpOrganisation aux1 = iter1.next();
                if (aux1.getAmpOrgId().equals(org2.getAmpOrgId())) {
                    ret = true;
                    break;
                }
            }
        }
        return ret;
    }

    public final static boolean containOrgGrps(Collection<AmpOrgGroup> orgs1, AmpOrgGroup org2) throws Exception {
        boolean ret = false;
        Iterator<AmpOrgGroup> iter1 = orgs1.iterator();
        while (iter1.hasNext()) {
            AmpOrgGroup aux1 = iter1.next();
            if (aux1.getAmpOrgGrpId().equals(org2.getAmpOrgGrpId())) {
                ret = true;
                break;
            }
        }
        return ret;
    }
    
    public final static boolean containOrgTypes(Collection<AmpOrgType> orgs1, AmpOrgType org2) throws Exception {
        boolean ret = false;
        Iterator<AmpOrgType> iter1 = orgs1.iterator();
        while (iter1.hasNext()) {
            AmpOrgType aux1 = iter1.next();
            if (aux1.getAmpOrgTypeId().equals(org2.getAmpOrgTypeId())) {
                ret = true;
                break;
            }
        }
        return ret;
    }

    public final static Collection<AmpOrganisation> getDonorsCollection(String[] donors) throws Exception {
        Collection<AmpOrganisation> retDonors = null;
        if (donors != null && donors.length > 0) {
            retDonors = new ArrayList<AmpOrganisation>();
            for (int i = 0; i < donors.length; i++) {
                String donorId = donors[i];
                retDonors.add(DbUtil.getOrganisation(new Long(donorId)));
            }
        }
        return retDonors;
    }

    public final static Collection<AmpCategoryValue> getStatuses(String[] statuses) throws Exception {
        Collection<AmpCategoryValue> retStatus = null;
        if (statuses != null && statuses.length > 0) {
            retStatus = new ArrayList<AmpCategoryValue>();
            for (int i = 0; i < statuses.length; i++) {
                String statusId = statuses[i];
                retStatus.add(CategoryManagerUtil.getAmpCategoryValueFromDb(new Long(statusId)));
            }
        }
        return retStatus;
    }

    public final static Collection<AmpCategoryValue> getFinancingInstruments(String[] instruments) throws Exception {
        Collection<AmpCategoryValue> retInstrument = null;
        if (instruments != null && instruments.length > 0) {
            retInstrument = new ArrayList<AmpCategoryValue>();
            for (int i = 0; i < instruments.length; i++) {
                String instrId = instruments[i];
                retInstrument.add(CategoryManagerUtil.getAmpCategoryValueFromDb(new Long(instrId)));
            }
        }
        return retInstrument;
    }

    public final static Collection<AmpOrgGroup> getDonorGroups(String[] groups) throws Exception {
        Collection<AmpOrgGroup> retGrp = null;
        if (groups != null && groups.length > 0) {
            retGrp = new ArrayList<AmpOrgGroup>();
            for (int i = 0; i < groups.length; i++) {
                String grpId = groups[i];
                retGrp.add(DbUtil.getAmpOrgGroup(new Long(grpId)));
            }
        }
        return retGrp;
    }
    
    public final static Collection<AmpOrgType> getDonorTypes(String[] groups) throws Exception {
        Collection<AmpOrgType> retType = null;
        if (groups != null && groups.length > 0) {
            retType = new ArrayList<AmpOrgType>();
            for (int i = 0; i < groups.length; i++) {
                try {
                    String grpId = groups[i];
                    retType.add(DbUtil.getAmpOrgType(new Long(grpId)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return retType;
    }

    public final static Collection<AmpSector> getSectors(String[] sectors) throws Exception {
        Collection<AmpSector> retGrp = null;
        if (sectors != null && sectors.length > 0) {
            retGrp = new ArrayList<AmpSector>();
            for (int i = 0; i < sectors.length; i++) {
                String grpId = sectors[i];
                retGrp.add(SectorUtil.getAmpSector((new Long(grpId))));
            }
        }
        return retGrp;
    }

    /**
     * Given a project date and the date ranges, return the year for that
     * transaction or 0 if it doesn't belong to any range.
     * 
     * @param currentDate
     *            The date to evaluate.
     * @param startDates
     *            Array of dates with the first day of each year.
     * @param endDates
     *            Array of dates with the last day of each year.
     * @param startYear
     *            The first year represented in the startDates and endDates
     *            ranges.
     * @return The year of the transaction for the calendar represented by the
     *         Date ranges. Or 0 if not.
     */
    public final static int getYear(Date currentDate, Date[] startDates, Date[] endDates, int startYear, int endYear) throws Exception {
        int ret = 0;
        if (currentDate != null) {
            if (startDates[0] == null || endDates[0] == null) {
                GregorianCalendar gc = new GregorianCalendar();
                gc.setTime(currentDate);
                EthiopianCalendar ethCal = new EthiopianCalendar().getEthiopianDate(gc);
                if (ethCal.ethFiscalYear >= startYear && ethCal.ethFiscalYear <= endYear) {
                    ret = ethCal.ethFiscalYear;
                }
            } else {
                int auxYear = startYear - 1;
                for (int i = 0; i < startDates.length; i++) {
                    auxYear++;
                    if ((currentDate.after(startDates[i]) || chkEqualDates(currentDate, startDates[i])) && (currentDate.before(endDates[i]) || chkEqualDates(currentDate, endDates[i]))) {
                        ret = auxYear;
                    }
                }
            }
        }
        return ret;
    }

    public static boolean chkEqualDates(Date d1, Date d2) throws Exception {
        boolean result = false;
        result = (DateTimeUtil.formatDate(d1).equalsIgnoreCase(DateTimeUtil.formatDate(d2))) ? true : false;
        return result;
    }

    // TODO: Now the questions/answers are hardcoded, lets change it by
    // selecting the right answer for the current question.
    public static boolean[] getSurveyAnswers(String reportCode, String[] arrayResponses) throws Exception {         
        // Set the number of columns for each report.
        boolean[] columns = null;
        if (GPIConstants.GPI_REPORT_1.equals(reportCode)) {
            columns = new boolean[1];
        } else if (GPIConstants.GPI_REPORT_6.equals(reportCode)) {
            columns = new boolean[1];
        } else if (GPIConstants.GPI_REPORT_9b.equals(reportCode)) {
            columns = new boolean[4];
        } else if (GPIConstants.GPI_REPORT_5a.equals(reportCode)) {
            columns = new boolean[1];
        } 

        // Prepare an array with all the responses (no problem if its not
        // sorted).
        String[] answers = new String[GPIConstants.NUMBER_OF_SURVEY_QUESTIONS];
        if(arrayResponses != null && arrayResponses.length > 1) {
            for (int i = 0; i < arrayResponses.length; i++) {
                String[] auxResponse = arrayResponses[i].split(":");
                int questNum = Integer.valueOf(auxResponse[0]) - 1;
                answers[questNum] = new String((auxResponse.length == 2 && !auxResponse[1].equals("")) ? auxResponse[1] : "");
            }
                    
            // Check if the general question (AMP-18209) is visible.
            boolean GM1Active = false;
            boolean GM1Answer = false;
            if (FeaturesUtil.isVisibleModule("/Activity Form/GPI/GPI Item/GPI Questions List/Has this project been formally agreed upon (i.e. memorandum of understanding, project agreement, etc.) with a Government entity?")) {
                GM1Active = true;
                if (("Yes".equalsIgnoreCase(answers[6]))) {
                    GM1Answer = true;
                }
            }
    
            // Evaluate the report.
            // Remember: columns[0] is the first column :)
            // Remember: answers[0] is the first question :D
            if (GPIConstants.GPI_REPORT_1.equals(reportCode)) {
                // Check if the question has a valid answer (yes/no) because the activityform saves the survey automatically even with no responses.
                if(answers[0] == null || answers[0].trim().equals("")) {
                    columns = null;
                } else {
                    columns[0] = ("Yes".equalsIgnoreCase(answers[0]));
                }
            } else if(GPIConstants.GPI_REPORT_5a.equals(reportCode)) {
                if(GM1Active && !GM1Answer) {
                    // In this case the question is active in AF and the answer is not "yes".
                    columns = null;
                } else {
                    // Indicator 5a doesnt evaluate any other question.
                    columns[0] = true;
                }
            } else if (GPIConstants.GPI_REPORT_9b.equals(reportCode)) {
                if(GM1Active && !GM1Answer) {
                    // In this case the question is active in AF and the answer is not "yes".
                    columns = null;
                } else {
                    if((answers[2] == null || answers[2].trim().equals("")) && (answers[3] == null || answers[3].trim().equals(""))
                            && (answers[4] == null || answers[4].trim().equals("")) && (answers[5] == null || answers[5].trim().equals(""))) {
                        // None of the 4 questions have been answeres (yes/no) so this funding is invalid.
                        columns = null;
                    } else {
                        columns[0] = ("Yes".equalsIgnoreCase(answers[2]));
                        columns[1] = ("Yes".equalsIgnoreCase(answers[3]));
                        columns[2] = ("Yes".equalsIgnoreCase(answers[4]));
                        columns[3] = ("Yes".equalsIgnoreCase(answers[5]));
                    }
                }
            } else if (GPIConstants.GPI_REPORT_6.equals(reportCode)) {
                // Check if the question has a valid answer (yes/no) because the activityform saves the survey automatically even with no responses.
                if(answers[1] == null || answers[1].trim().equals("")) {
                    columns = null;
                } else {
                    columns[0] = ("Yes".equalsIgnoreCase(answers[1]));
                }
            }
        }
        return columns;
    }
    
    public static List<AmpGPISurveyQuestion> getQuestionsByCode(String code) {
        Session session = PersistenceManager.getSession();
        Query query = session.createSQLQuery("SELECT * FROM amp_gpi_survey_question agsq WHERE amp_indicator_id = (SELECT amp_indicator_id FROM amp_gpi_survey_indicator agsi WHERE indicator_code like ?)")
                .addEntity(AmpGPISurveyQuestion.class).setString(0, code);
        //List<AmpGPISurveyQuestion> list = new ArrayList<AmpGPISurveyQuestion>();
        return query.list();
    }
    
    public static Collection<AmpGPINiIndicator> getActivityFormGPINiIndicators() {
        Collection<AmpGPINiIndicator> responses = new ArrayList<AmpGPINiIndicator>();
        Session session = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String qry = "SELECT indc FROM "
                    + AmpGPINiIndicator.class.getName() + " indc " 
                    + "WHERE indc.ampGPINiIndicatorId IN (" 
                    + "SELECT DISTINCT q.ampGPINiIndicator FROM "
                    + AmpGPINiQuestion.class.getName() + " q " 
                    + "WHERE q.requiresDataEntry = TRUE) "
                    + "ORDER BY indc.code ASC";
            
            responses = session.createQuery(qry).list();

        } catch (Exception ex) {
            logger.error("Unable to get gpi ni indicators : ", ex);
        }
        
        return responses;
    }
}
