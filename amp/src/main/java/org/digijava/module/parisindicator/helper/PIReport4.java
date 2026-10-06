package org.digijava.module.parisindicator.helper;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.helper.fiscalcalendar.BaseCalendar;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.parisindicator.helper.row.PIReport4Row;
import org.digijava.module.parisindicator.helper.row.PIReportAbstractRow;
import org.digijava.module.parisindicator.util.PIConstants;
import org.digijava.module.parisindicator.util.PIUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class PIReport4 extends PIAbstractReport {

    private static Logger logger = Logger.getLogger(PIReport4.class);
    private final String reportCode = PIConstants.PARIS_INDICATOR_REPORT_4;

    public String getReportCode() {
        return this.reportCode;
    }

    @Override
    public Collection<PIReportAbstractRow> generateReport(Collection<AmpAhsurvey> commonData, int startYear,
            int endYear, AmpFiscalCalendar calendar, AmpCurrency currency, Collection<AmpSector> sectorsFilter,
            Collection<AmpCategoryValue> statusFilter, Collection<AmpCategoryValue> financingInstrumentFilter) {

        Collection<PIReportAbstractRow> list = new ArrayList<PIReportAbstractRow>();
        PIReport4Row auxRow = null;
        int yearRange = endYear - startYear + 1;
        Date[] startDates = new Date[yearRange];
        Date[] endDates = new Date[yearRange];
        double fromExchangeRate;
        double toExchangeRate;

        try {
            // Setup year ranges according the selected calendar.
            AmpFiscalCalendar fCalendar = FiscalCalendarUtil.getAmpFiscalCalendar(calendar.getAmpFiscalCalId());
            if (!(fCalendar.getBaseCal().equalsIgnoreCase(BaseCalendar.BASE_ETHIOPIAN.getValue()))) {
                for (int i = 0; i < yearRange; i++) {
                    startDates[i] = FiscalCalendarUtil
                            .getCalendarStartDate(calendar.getAmpFiscalCalId(), startYear + i);
                    endDates[i] = FiscalCalendarUtil.getCalendarEndDate(calendar.getAmpFiscalCalId(), startYear + i);
                }
            }

            // Iterate the filtered collection of AmpAhSurveys.
            Iterator<AmpAhsurvey> iterCommonData = commonData.iterator();
            while (iterCommonData.hasNext()) {
                AmpAhsurvey auxAmpAhsurvey = iterCommonData.next();
                AmpActivityVersion auxActivity = auxAmpAhsurvey.getAmpActivityId();
                AmpOrganisation auxOrganisation = auxAmpAhsurvey.getAmpDonorOrgId();
                AmpOrganisation auxPoDD = auxAmpAhsurvey.getPointOfDeliveryDonor();

                // Filter by sectors. If sectorsFilter is not null then filter
                // by the sectors in that list.
                if (sectorsFilter != null && !PIUtils.containSectors(sectorsFilter, auxActivity.getSectors())) {
                    // Ignore this AmpAhsurvey and continue with the next.
                    continue;
                }

                // Filter by status. If statusFilter is not null then filter by
                // the statuses in that list.
                if (statusFilter != null
                        && !PIUtils.containStatus(statusFilter, CategoryManagerUtil.getAmpCategoryValueFromListByKey(
                                CategoryConstants.ACTIVITY_STATUS_KEY, auxActivity.getCategories()))) {
                    // Ignore this AmpAhsurvey and continue with the next.
                    continue;
                }

                // Iterate the collection of fundings.
                Iterator<AmpFunding> iterFundings = auxActivity.getFunding().iterator();
                while (iterFundings.hasNext()) {
                    AmpFunding auxFunding = iterFundings.next();

                    // Check if the funding belongs to the original
                    // organization not the PoDD.
                    if (auxAmpAhsurvey.getAmpDonorOrgId().getAmpOrgId().equals(
                            auxFunding.getAmpDonorOrgId().getAmpOrgId())) {

                        // Filter by Financing Instrument. If
                        // financingInstrumentFilter is not null then filter
                        // by the financing instruments in that list.
                        if (financingInstrumentFilter != null) {
                            if (!PIUtils.containFinancingInstrument(auxFunding.getFinancingInstrument(),
                                    financingInstrumentFilter)) {
                                // Ignore this funding.
                                continue;
                            }
                        }

                        // Iterate the collection of funding details.
                        Iterator<AmpFundingDetail> iterFundingDetails = auxFunding.getFundingDetails().iterator();
                        while (iterFundingDetails.hasNext()) {
                            AmpFundingDetail auxFundingDetail = iterFundingDetails.next();

                            // Filter by years. Check if the transaction date
                            // falls into one of the date ranges.
                            int transactionYear = PIUtils.getTransactionYear(auxFundingDetail.getTransactionDate(),
                                    startDates, endDates, startYear, endYear);
                            if (transactionYear == 0) {
                                // Ignore this funding detail.
                                continue;
                            }

                            // Check the funding details type.
                            if (auxFundingDetail.getTransactionType().intValue() == Constants.DISBURSEMENT) {
                                if (auxFundingDetail.getAdjustmentType().getValue().equals(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey())) {
                                    auxRow = new PIReport4Row();

                                    // Check survey answers for this
                                    // AmpAhsurvey.
                                    boolean[] showColumn = PIUtils.getSurveyAnswers(
                                            PIConstants.PARIS_INDICATOR_REPORT_4, auxAmpAhsurvey);

                                    // Get percentage from Q4.
                                    double percentage = this.getResposePercentage(auxAmpAhsurvey);
                                    percentage /= 100;

                                    // Calculate exchange rates.
                                    fromExchangeRate = Util.getExchange(auxFundingDetail.getAmpCurrencyId()
                                            .getCurrencyCode(), new java.sql.Date(auxFundingDetail.getTransactionDate()
                                            .getTime()));
                                    toExchangeRate = 0;
                                    if (currency != null) {
                                        toExchangeRate = Util.getExchange(currency.getCurrencyCode(),
                                                new java.sql.Date(auxFundingDetail.getTransactionDate().getTime()));
                                    }
                                    BigDecimal amount = new BigDecimal(CurrencyWorker.convert1(auxFundingDetail
                                            .getTransactionAmount(), fromExchangeRate, toExchangeRate));

                                    // Setup row.
                                    if (showColumn[0]) {
                                        auxRow.setColumn1(amount.multiply(new BigDecimal(percentage)));
                                    } else {
                                        auxRow.setColumn1(new BigDecimal(0));
                                    }
                                    auxRow.setColumn2(amount.multiply(new BigDecimal(percentage)));
                                    auxRow.setColumn3(0);
                                    auxRow.setDonorGroup(auxPoDD.getOrgGrpId());
                                    auxRow.setYear(transactionYear);

                                    if (auxRow.getColumn1().doubleValue() != 0
                                            || auxRow.getColumn2().doubleValue() != 0) {
                                        list.add(auxRow);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return list;
    }

    private double getResposePercentage(AmpAhsurvey survey) throws Exception {
        double ret = 0;
        Iterator<AmpAhsurveyResponse> iter = survey.getResponses().iterator();
        while (iter.hasNext()) {
            AmpAhsurveyResponse auxResponse = iter.next();
            if (auxResponse.getAmpQuestionId().getQuestionNumber().intValue() == 4) {
                if (auxResponse.getResponse() != null && !auxResponse.getResponse().equals("")) {
                    // TODO: catch for numericexceptions, in that case save
                    // the error to show it in the report page as a warning for
                    // the user, also could be a JUnit test.
                    ret = Integer.valueOf(auxResponse.getResponse()).doubleValue();
                }
            }
        }

        return ret;
    }

    @Override
    public Collection<PIReportAbstractRow> reportPostProcess(Collection<PIReportAbstractRow> baseReport, int startYear,
            int endYear) throws Exception {

        Collection<PIReportAbstractRow> list = new ArrayList<PIReportAbstractRow>(baseReport);

        // TODO: make a general comparator???
        Comparator compareRows = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                PIReport4Row aux1 = (PIReport4Row) o1;
                PIReport4Row aux2 = (PIReport4Row) o2;
                if (aux1 == null && aux2 == null) {
                    return 0;
                } else if (aux1 == null) {
                    return 1;
                } else if (aux2 == null) {
                    return -1;
                } else {
                    return (aux1.getDonorGroup().getOrgGrpName() + aux1.getYear()).compareTo(aux2.getDonorGroup()
                            .getOrgGrpName()
                            + aux2.getYear());
                }
            }
        };

        Collection<PIReportAbstractRow> newList = new ArrayList<PIReportAbstractRow>();
        Collections.sort((ArrayList) list, compareRows);

        // Format the list grouping by donor grp and year.
        AmpOrgGroup auxGroup = null;
        Iterator iter = list.iterator();
        BigDecimal auxColumn1 = new BigDecimal(0);
        BigDecimal auxColumn2 = new BigDecimal(0);
        int currentYear = 0;
        while (iter.hasNext()) {
            PIReport4Row row = (PIReport4Row) iter.next();

            // Valid for the first row.
            if (auxGroup == null) {
                auxGroup = row.getDonorGroup();
                currentYear = row.getYear();
            }
            // True if the actual and previous donor are the same.
            if (auxGroup.getAmpOrgGrpId().equals(row.getDonorGroup().getAmpOrgGrpId())) {
                // If is the same year than the previous record then add the
                // amounts, otherwise save the amounts added to this point and
                // then update the current year and save the amounts in the
                // auxiliary variables.
                if (row.getYear() == currentYear) {
                    auxColumn1 = auxColumn1.add(row.getColumn1());
                    auxColumn2 = auxColumn2.add(row.getColumn2());
                } else {
                    PIReport4Row newRow = new PIReport4Row();
                    newRow.setColumn1(auxColumn1);
                    newRow.setColumn2(auxColumn2);
                    newRow.setDonorGroup(row.getDonorGroup());
                    newRow.setYear(currentYear);
                    newList.add(newRow);

                    currentYear = row.getYear();
                    auxColumn1 = row.getColumn1();
                    auxColumn2 = row.getColumn2();
                }
            } else {
                PIReport4Row newRow = new PIReport4Row();
                newRow.setColumn1(auxColumn1);
                newRow.setColumn2(auxColumn2);
                newRow.setDonorGroup(auxGroup);
                newRow.setYear(currentYear);
                newList.add(newRow);

                auxColumn1 = row.getColumn1();
                auxColumn2 = row.getColumn2();
                auxGroup = row.getDonorGroup();
                currentYear = row.getYear();
            }
            // If this is the last record then save it.
            if (!iter.hasNext()) {
                PIReport4Row newRow = new PIReport4Row();
                newRow.setColumn1(auxColumn1);
                newRow.setColumn2(auxColumn2);
                newRow.setDonorGroup(auxGroup);
                newRow.setYear(currentYear);
                newList.add(newRow);
            }
        }

        // Complete the list with the same number of years on each donor group.
        newList = this.addMissingYears(newList, startYear, endYear);

        // Calculate final percentages and add 'All Donors' row.
        if (!newList.isEmpty()) {
            newList = this.calculatePercentages(newList, startYear, endYear);
        }

        return newList;
    }

    private Collection<PIReportAbstractRow> calculatePercentages(Collection<PIReportAbstractRow> coll, int startYear,
            int endYear) throws Exception {

        int range = endYear + 1 - startYear;
        BigDecimal[] sumCol1 = new BigDecimal[range];
        BigDecimal[] sumCol2 = new BigDecimal[range];
        Iterator<PIReportAbstractRow> iterColl = coll.iterator();
        while (iterColl.hasNext()) {
            // Calculate percentages.
            PIReport4Row auxRow = (PIReport4Row) iterColl.next();
            if (auxRow.getColumn2().doubleValue() > 0) {
                auxRow.setColumn3(auxRow.getColumn1().multiply(new BigDecimal(100)).divide(auxRow.getColumn2(),
                        RoundingMode.HALF_UP).floatValue());
            } else {
                auxRow.setColumn3(0);
            }

            // Accumulate totals for each year.
            if (sumCol1[auxRow.getYear() - startYear] == null) {
                sumCol1[auxRow.getYear() - startYear] = new BigDecimal(0);
            }
            if (sumCol2[auxRow.getYear() - startYear] == null) {
                sumCol2[auxRow.getYear() - startYear] = new BigDecimal(0);
            }
            sumCol1[auxRow.getYear() - startYear] = sumCol1[auxRow.getYear() - startYear].add(auxRow.getColumn1());
            sumCol2[auxRow.getYear() - startYear] = sumCol2[auxRow.getYear() - startYear].add(auxRow.getColumn2());
        }

        // Add "All Donors" record at the beginning with the total amounts.
        int currentYear = startYear;
        ArrayList auxList = new ArrayList(coll);
        for (int i = 0; i < endYear + 1 - startYear; i++) {
            PIReport4Row auxRow = new PIReport4Row();
            AmpOrgGroup auxDonorGroup = new AmpOrgGroup();
            auxDonorGroup.setOrgGrpName(PIConstants.ALL_DONORS);
            auxDonorGroup.setAmpOrgGrpId(new Long(0));
            auxRow.setDonorGroup(auxDonorGroup);
            auxRow.setColumn1(sumCol1[i]);
            auxRow.setColumn2(sumCol2[i]);
            auxRow.setYear(startYear + i);
            if (auxRow.getColumn2().doubleValue() > 0) {
                auxRow.setColumn3(auxRow.getColumn1().multiply(new BigDecimal(100)).divide(auxRow.getColumn2(),
                        RoundingMode.HALF_UP).floatValue());
            } else {
                auxRow.setColumn3(0);
            }
            auxList.add(i, auxRow);
        }
        return auxList;
    }

    private Collection<PIReportAbstractRow> addMissingYears(Collection<PIReportAbstractRow> coll, int startYear,
            int endYear) throws Exception {
        Collection ret = new ArrayList();
        AmpOrgGroup auxGroup = null;
        int j = 0;
        Iterator iter = coll.iterator();
        while (iter.hasNext()) {
            PIReport4Row row = (PIReport4Row) iter.next();
            if (auxGroup == null) {
                auxGroup = row.getDonorGroup();
            }
            if (j == 0 || !auxGroup.getAmpOrgGrpId().equals(row.getDonorGroup().getAmpOrgGrpId())) {
                auxGroup = row.getDonorGroup();
                for (int i = startYear; i < endYear + 1; i++) {
                    PIReport4Row newRow = new PIReport4Row();
                    newRow.setColumn1(new BigDecimal(0));
                    newRow.setColumn2(new BigDecimal(0));
                    newRow.setDonorGroup(auxGroup);
                    newRow.setYear(i);
                    ret.add(newRow);
                }
            }
            j++;
        }

        Iterator iterRet = ret.iterator();
        while (iterRet.hasNext()) {
            PIReport4Row rowRet = (PIReport4Row) iterRet.next();
            Iterator iterOrigen = coll.iterator();
            while (iterOrigen.hasNext()) {
                PIReport4Row rowOrigen = (PIReport4Row) iterOrigen.next();
                if (rowRet.getDonorGroup().getAmpOrgGrpId().equals(rowOrigen.getDonorGroup().getAmpOrgGrpId())
                        && rowRet.getYear() == rowOrigen.getYear()) {
                    rowRet.setColumn1(rowOrigen.getColumn1());
                    rowRet.setColumn2(rowOrigen.getColumn2());
                    rowRet.setDonorGroup(rowOrigen.getDonorGroup());
                }
            }
        }

        return ret;
    }

    @Override
    public Collection<PIReportAbstractRow> generateReport10a(Collection<AmpOrganisation> commonData, int startYear,
            int endYear, AmpFiscalCalendar calendar, AmpCurrency currency, Collection<AmpSector> sectorsFilter,
            Collection<AmpCategoryValue> statusFilter, Collection<AmpCategoryValue> financingInstrumentFilter) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<PIReportAbstractRow> generateReport10b(Collection<NodeWrapper> commonData, int startYear,
            int endYear, AmpFiscalCalendar calendar, AmpCurrency currency, Collection<AmpSector> sectorsFilter,
            Collection<AmpCategoryValue> statusFilter, Collection<AmpCategoryValue> financingInstrumentFilter) {
        // TODO Auto-generated method stub
        return null;
    }
}
