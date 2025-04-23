package org.digijava.module.parisindicator.util;

import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.helper.fiscalcalendar.EthiopianCalendar;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.common.util.DateTimeUtil;

import java.util.*;

public class PIUtils {
    
    /**
     * Converts a textual representation of boolean to numeric 1 for yes/true, 0 for rest
     * @param text
     * @return
     */
    public static int convTxtBolToNum(String text) {
        if("Yes".equalsIgnoreCase(text) || "true".equalsIgnoreCase(text)) return 1;
        return 0;
    }

    public final static boolean containSectors(Collection<AmpSector> sectors1, Collection<AmpActivitySector> sectors2)
            throws Exception {
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

    public final static boolean containStatus(Collection<AmpCategoryValue> statuses, AmpCategoryValue status)
            throws Exception {
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

    public final static boolean containFinancingInstrument(AmpCategoryValue financing1,
            Collection<AmpCategoryValue> financing2) throws Exception {
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

    public final static boolean containOrganisations(Collection<AmpOrganisation> orgs1, AmpOrganisation org2)
            throws Exception {
        boolean ret = false;
        Iterator<AmpOrganisation> iter1 = orgs1.iterator();
        while (iter1.hasNext()) {
            AmpOrganisation aux1 = iter1.next();
            if (aux1.getAmpOrgId().equals(org2.getAmpOrgId())) {
                ret = true;
                break;
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
     * Given a transaction date and the date ranges, return the year for that
     * transaction or 0 if it doesn't belong to any range.
     * 
     * @param transactionDate
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
    public final static int getTransactionYear(Date transactionDate, Date[] startDates, Date[] endDates, int startYear,
            int endYear) throws Exception {
        int ret = 0;
        if (startDates[0] == null || endDates[0] == null) {
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(transactionDate);
            EthiopianCalendar ethCal = new EthiopianCalendar().getEthiopianDate(gc);
            if (ethCal.ethFiscalYear >= startYear && ethCal.ethFiscalYear <= endYear) {
                ret = ethCal.ethFiscalYear;
            }
        } else {
            int auxYear = startYear - 1;
            for (int i = 0; i < startDates.length; i++) {
                auxYear++;
                if ((transactionDate.after(startDates[i]) || chkEqualDates(transactionDate, startDates[i]))
                        && (transactionDate.before(endDates[i]) || chkEqualDates(transactionDate, endDates[i]))) {
                    ret = auxYear;
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

    public static boolean[] getSurveyAnswers(String reportCode, AmpAhsurvey survey) throws Exception {
        // Set the number of columns for each report.
        boolean[] columns = null;
        if (PIConstants.PARIS_INDICATOR_REPORT_3.equals(reportCode)) {
            columns = new boolean[2];
        } else if (PIConstants.PARIS_INDICATOR_REPORT_4.equals(reportCode)) {
            columns = new boolean[1];
        } else if (PIConstants.PARIS_INDICATOR_REPORT_5a.equals(reportCode)) {
            columns = new boolean[6];
        } else if (PIConstants.PARIS_INDICATOR_REPORT_5b.equals(reportCode)) {
            columns = new boolean[2];
        } else if (PIConstants.PARIS_INDICATOR_REPORT_6.equals(reportCode)) {
            columns = new boolean[1];
        } else if (PIConstants.PARIS_INDICATOR_REPORT_7.equals(reportCode)) {
            columns = new boolean[2];
        } else if (PIConstants.PARIS_INDICATOR_REPORT_9.equals(reportCode)) {
            // I added one more column to avoid showing in the report surveys
            // with all null values (created by ActivityForm but never taken).
            columns = new boolean[3];
        }

        // Prepare an array with all the responses (no problem if its not
        // sorted).
        String[] answers = new String[PIConstants.NUMBER_OF_SURVEY_QUESTIONS];
        Collection<AmpAhsurveyResponse> responses = survey.getResponses();
        Iterator<AmpAhsurveyResponse> iter = responses.iterator();
        while (iter.hasNext()) {
            AmpAhsurveyResponse auxResponse = iter.next();
            int quesNum = auxResponse.getAmpQuestionId().getQuestionNumber().intValue() - 1;
            String auxString = (auxResponse.getResponse() == null) ? "" : auxResponse.getResponse();
            answers[quesNum] = new String(auxString);
        }

        // Evaluate the report.
        // Remember: columns[0] is the first column :)
        // Remember: answers[0] is the first question :D
        if (PIConstants.PARIS_INDICATOR_REPORT_3.equals(reportCode)) {
            columns[1] = ("Yes".equalsIgnoreCase(answers[0]));
            columns[0] = ("Yes".equalsIgnoreCase(answers[0]) && "Yes".equalsIgnoreCase(answers[1]));
        } else if (PIConstants.PARIS_INDICATOR_REPORT_4.equals(reportCode)) {
            columns[0] = ("Yes".equalsIgnoreCase(answers[2]));
        } else if (PIConstants.PARIS_INDICATOR_REPORT_5a.equals(reportCode)) {
            
            //if we have questions above no 13 that have non-null answers it means we use the new calculated type of questions for 5a and 5b
            if(answers[13]!=null) {
                //for questions inside indicator 5a
                // IF((SUM(siblings)>=3);"Y";"N")
                int sum4=convTxtBolToNum(answers[13])+convTxtBolToNum(answers[14])+
                        convTxtBolToNum(answers[15])+convTxtBolToNum(answers[16]);
                if(sum4>=3) answers[4]="Yes"; else answers[4]="No";
                
                //IF((SUM(siblings)=2);"Y";"N")
                int sum5=convTxtBolToNum(answers[17])+convTxtBolToNum(answers[18]);
                if(sum5==2) answers[5]="Yes"; else answers[5]="No";
                
                //IF(  ((C21+C22)=2);   IF( ((C23+C24)>=1);"Y";"N")  ;"N")
                int sum6_1=convTxtBolToNum(answers[19])+convTxtBolToNum(answers[20]);
                if(sum6_1==2) {
                    int sum6_2=convTxtBolToNum(answers[21])+convTxtBolToNum(answers[22]);
                    if(sum6_2>=1) answers[6]="Yes"; else answers[6]="No";
                } else answers[6]="No";
                
                //for the question inside 5b
                //=IF(((C28+C29)=2);"Y";"N")
                int sum7=convTxtBolToNum(answers[23])+convTxtBolToNum(answers[24]);
                if(sum7==2) answers[7]="Yes"; else answers[7]="No";
            }
            
            columns[0] = ("Yes".equalsIgnoreCase(answers[0]) && "Yes".equalsIgnoreCase(answers[4]));
            columns[1] = ("Yes".equalsIgnoreCase(answers[0]) && "Yes".equalsIgnoreCase(answers[5]));
            columns[2] = ("Yes".equalsIgnoreCase(answers[0]) && "Yes".equalsIgnoreCase(answers[6]));
            columns[3] = ("Yes".equalsIgnoreCase(answers[0]) && "Yes".equalsIgnoreCase(answers[4])
                    && "Yes".equalsIgnoreCase(answers[5]) && "Yes".equalsIgnoreCase(answers[6]));
            columns[4] = "Yes".equalsIgnoreCase(answers[0]);
            columns[5] = (("Yes".equalsIgnoreCase(answers[4]) || "Yes".equalsIgnoreCase(answers[5]) || "Yes"
                    .equalsIgnoreCase(answers[6])) && "Yes".equalsIgnoreCase(answers[0]));
        } else if (PIConstants.PARIS_INDICATOR_REPORT_5b.equals(reportCode)) {
            columns[0] = ("Yes".equalsIgnoreCase(answers[0]) && "Yes".equalsIgnoreCase(answers[7]));
            columns[1] = ("Yes".equalsIgnoreCase(answers[0]));
        } else if (PIConstants.PARIS_INDICATOR_REPORT_6.equals(reportCode)) {
            columns[0] = ("Yes".equalsIgnoreCase(answers[8]));
        } else if (PIConstants.PARIS_INDICATOR_REPORT_7.equals(reportCode)) {
            columns[0] = ("Yes".equalsIgnoreCase(answers[0]));
            columns[1] = ("Yes".equalsIgnoreCase(answers[0]));
        } else if (PIConstants.PARIS_INDICATOR_REPORT_9.equals(reportCode)) {
            columns[0] = ("Yes".equalsIgnoreCase(answers[10]));
            columns[1] = ("Yes".equalsIgnoreCase(answers[10]));
            // Check if this survey has been filled in, if all answers are null
            // then ignore the survey. This is not needed on other PI because
            // only PI 9 has a column without answers (YES/NO) validation.
            boolean addValue = false;
            for (int i = 0; i < PIConstants.NUMBER_OF_SURVEY_QUESTIONS - 1; i++) {
                if (answers[i] != null && !answers[i].equalsIgnoreCase("")) {
                    addValue = true;
                    break;
                }
            }
            columns[2] = addValue;
        }
        return columns;
    }
}
