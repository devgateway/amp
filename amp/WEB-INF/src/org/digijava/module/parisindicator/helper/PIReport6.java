package org.digijava.module.parisindicator.helper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpAhsurvey;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.helper.fiscalcalendar.BaseCalendar;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.parisindicator.helper.row.PIReport6Row;
import org.digijava.module.parisindicator.helper.row.PIReportAbstractRow;
import org.digijava.module.parisindicator.model.PIUseCase;
import org.digijava.module.parisindicator.util.PIConstants;
import org.digijava.module.parisindicator.util.PIUtils;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.Collections;

public class PIReport6 extends PIAbstractReport {

    private static Logger logger = Logger.getLogger(PIReport6.class);
    private final String reportCode = PIConstants.PARIS_INDICATOR_REPORT_6;

    public String getReportCode() {
        return this.reportCode;
    }

    @Override
    public Collection<PIReportAbstractRow> generateReport(Collection<AmpAhsurvey> commonData, int startYear,
            int endYear, AmpFiscalCalendar calendar, AmpCurrency currency, Collection<AmpSector> sectorsFilter,
            Collection<AmpCategoryValue> statusFilter, Collection<AmpCategoryValue> financingInstrumentFilter) {

        Collection<PIReportAbstractRow> list = new ArrayList<PIReportAbstractRow>();
        PIReport6Row auxRow = null;
        int yearRange = endYear - startYear + 1;
        Date[] startDates = new Date[yearRange];
        Date[] endDates = new Date[yearRange];

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

                if (auxActivity.getActualStartDate() == null || auxActivity.getActualCompletionDate() == null) {
                    continue;
                }
                int activityStartYear = PIUtils.getTransactionYear(auxActivity.getActualStartDate(), startDates,
                        endDates, startYear, endYear);
                int activityEndYear = PIUtils.getTransactionYear(auxActivity.getActualCompletionDate(), startDates,
                        endDates, startYear, endYear);
                if (activityStartYear == 0 || activityEndYear == 0) {
                    // Ignore this activity.
                    continue;
                }

                // Check survey answers for this
                // AmpAhsurvey.
                boolean[] showColumn = PIUtils.getSurveyAnswers(PIConstants.PARIS_INDICATOR_REPORT_6, auxAmpAhsurvey);

                if (showColumn[0]) {
                    auxRow = new PIReport6Row();
                    auxRow.setDonorGroup(auxPoDD.getOrgGrpId());
                    int[] years = new int[yearRange];
                    if (activityStartYear != activityEndYear) {
                        years[activityEndYear - startYear]++;
                        years[activityStartYear - startYear]++;
                    } else {
                        years[activityEndYear - startYear]++;
                    }
                    auxRow.setYears(years);
                    list.add(auxRow);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return list;
    }

    @Override
    public Collection<PIReportAbstractRow> reportPostProcess(Collection<PIReportAbstractRow> baseReport, int startYear,
            int endYear) throws Exception {

        Collection<PIReportAbstractRow> list = new ArrayList<PIReportAbstractRow>(baseReport);

        // TODO: make a general comparator???
        Comparator compareRows = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                PIReport6Row aux1 = (PIReport6Row) o1;
                PIReport6Row aux2 = (PIReport6Row) o2;
                if (aux1 == null && aux2 == null) {
                    return 0;
                } else if (aux1 == null) {
                    return 1;
                } else if (aux2 == null) {
                    return -1;
                } else {
                    return (aux1.getDonorGroup().getOrgGrpName()).compareTo(aux2.getDonorGroup().getOrgGrpName());
                }
            }
        };

        Collection<PIReportAbstractRow> newList = new ArrayList<PIReportAbstractRow>();
        Collections.sort((ArrayList) list, compareRows);

        // Format the list grouping by donor grp and year.
        AmpOrgGroup auxGroup = null;
        Iterator iter = list.iterator();
        int[] auxYears = new int[endYear + 1 - startYear];
        while (iter.hasNext()) {
            PIReport6Row row = (PIReport6Row) iter.next();

            // Valid for the first row.
            if (auxGroup == null) {
                auxGroup = row.getDonorGroup();
            }
            // True if the actual and previous donor are the same.
            if (auxGroup.getAmpOrgGrpId().equals(row.getDonorGroup().getAmpOrgGrpId())) {
                for (int i = 0; i < endYear + 1 - startYear; i++) {
                    auxYears[i] += row.getYears()[i];
                }
            } else {
                PIReport6Row newRow = new PIReport6Row();
                newRow.setYears(auxYears);
                newRow.setDonorGroup(auxGroup);
                newList.add(newRow);

                auxYears = row.getYears();
                auxGroup = row.getDonorGroup();
            }
            // If this is the last record then save it.
            if (!iter.hasNext()) {
                PIReport6Row newRow = new PIReport6Row();
                newRow.setYears(auxYears);
                newRow.setDonorGroup(auxGroup);
                newList.add(newRow);
            }
        }

        newList = this.addAllDonor(newList, startYear, endYear);

        return newList;
    }

    private Collection<PIReportAbstractRow> addAllDonor(Collection<PIReportAbstractRow> coll, int startYear, int endYear)
            throws Exception {

        int[] auxYears = new int[endYear + 1 - startYear];
        ArrayList list = new ArrayList(coll);
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            PIReport6Row auxRow = (PIReport6Row) iter.next();
            for (int i = 0; i < endYear + 1 - startYear; i++) {
                auxYears[i] += auxRow.getYears()[i];
            }
        }
        PIReport6Row auxRow = new PIReport6Row();
        AmpOrgGroup auxDonorGroup = new AmpOrgGroup();
        auxDonorGroup.setOrgGrpName(PIConstants.ALL_DONORS);
        auxRow.setDonorGroup(auxDonorGroup);
        auxRow.setYears(auxYears);
        list.add(0, auxRow);

        return list;
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
