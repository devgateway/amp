package org.digijava.module.gpi.helper;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpGPISurvey;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpGPISurveyQuestion;
import org.digijava.module.aim.dbentity.AmpGPISurveyResponse;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.helper.fiscalcalendar.BaseCalendar;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.gpi.helper.row.GPIReport1Row;
import org.digijava.module.gpi.helper.row.GPIReportAbstractRow;
import org.digijava.module.gpi.model.GPIFilter;
import org.digijava.module.gpi.util.GPIConstants;
import org.digijava.module.gpi.util.GPIUtils;
import org.hibernate.query.Query;
import org.hibernate.Session;

import java.util.Collections;

public class GPIReport1 extends GPIAbstractReport {

    private static Logger logger = Logger.getLogger(GPIReport1.class);
    private final String reportCode = GPIConstants.GPI_REPORT_1;
    private final Session session = PersistenceManager.getSession();

    public String getReportCode() {
        return this.reportCode;
    }

    @Override
    public Collection<GPIReportAbstractRow> generateReport(Collection commonData, GPIFilter filter) {

        long time = Calendar.getInstance().getTimeInMillis();
        Collection<GPIReportAbstractRow> list = new ArrayList<GPIReportAbstractRow>();

        GPIReport1Row auxRow = null;
        int yearRange = filter.getEndYer() - filter.getStartYear() + 1;
        Date[] startDates = new Date[yearRange];
        Date[] endDates = new Date[yearRange];
        BigInteger currentActivityId = null;

        try {
            // Setup year ranges according the selected calendar.
            AmpFiscalCalendar fCalendar = FiscalCalendarUtil.getAmpFiscalCalendar(filter.getCalendar().getAmpFiscalCalId());
            if (!(fCalendar.getBaseCal().equalsIgnoreCase(BaseCalendar.BASE_ETHIOPIAN.getValue()))) {
                for (int i = 0; i < yearRange; i++) {
                    startDates[i] = FiscalCalendarUtil.getCalendarStartDate(filter.getCalendar().getAmpFiscalCalId(), filter.getStartYear() + i);
                    endDates[i] = FiscalCalendarUtil.getCalendarEndDate(filter.getCalendar().getAmpFiscalCalId(), filter.getStartYear() + i);
                }
            }

            // Create a set of years (no duplicates) that will be used to
            // populate the report.
            // ie: if an activity has 2 funding with some funding details:
            // F1 - FD1 - 2010
            // F1 - FD2 - 2010
            // F1 - FD3 - 2014
            // F2 - FD1 - 2012
            // F2 - FD2 - 2013
            // F2 - FD3 - 2014
            // Then we will count ONLY 1 project for 2010, 2012, 2013 and 2014.
            Set<Integer> yearsFromFunding = new HashSet<Integer>();

            //After refactoring we iterate over a sorted list of funding details ([activity_id, funding_id, funding_detail_id, transaction_date])
            Iterator<Object[]> iter = commonData.iterator();
            while (iter.hasNext()) {                
                Object[] data = iter.next();
                Date transactionDate = (Date) data[3];

                // Detect when we change activity to reset the years collection.
                if (currentActivityId == null) {
                    currentActivityId = (BigInteger) data[0];
                } else {
                    if (!currentActivityId.equals((BigInteger) data[0])) {
                        currentActivityId = (BigInteger) data[0];
                        yearsFromFunding = new HashSet<Integer>();
                    }
                }

                // Filter by years. Check if the funding detail date
                // falls into one of the date ranges.
                if (GPIUtils.getYear(transactionDate, startDates, endDates, filter.getStartYear(), filter.getEndYer()) == 0) {
                    // Ignore this project.
                    continue;
                }

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(transactionDate);
                if (!yearsFromFunding.contains(calendar.get(Calendar.YEAR))) {
                    Integer auxYear = calendar.get(Calendar.YEAR);
                    yearsFromFunding.add(auxYear);

                    auxRow = new GPIReport1Row();
                    // Check survey answers for thisAmpGPISurvey.
                    // Big time saved by only looking for valid responses.
                    if (!data[12].equals("")) {                 
                        String[] arrayResponses = ((String) data[12]).split(",");                   
                        boolean[] showColumn = GPIUtils.getSurveyAnswers(GPIConstants.GPI_REPORT_1, arrayResponses);
                        // If there was an answer (yes or no).
                        if (arrayResponses.length > 1 && showColumn != null) {
                            if (showColumn[0]) {
                                auxRow.setColumn1(new Integer(1));
                            } else {
                                auxRow.setColumn1(new Integer(0));
                            }
                            auxRow.setColumn2(new Integer(1));
                        }
                    }
                    auxRow.setColumn3(0);
                    auxRow.setYear(auxYear);
                    
                    AmpOrgGroup auxOrgGroup = new AmpOrgGroup();
                    auxOrgGroup.setAmpOrgGrpId(((BigInteger) data[4]).longValue());
                    auxOrgGroup.setOrgGrpName(data[5].toString());
                    auxRow.setDonorGroup(auxOrgGroup);
                                        
                    list.add(auxRow);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        logger.warn("generate report: " + ((Calendar.getInstance().getTimeInMillis() - time) / 1000) + "s");
        return list;
    }

    @Override
    public Collection<GPIReportAbstractRow> reportPostProcess(Collection<GPIReportAbstractRow> baseReport, int startYear, int endYear) throws Exception {

        Collection<GPIReportAbstractRow> list = new ArrayList<GPIReportAbstractRow>(baseReport);

        // TODO: make a general comparator???
        Comparator compareRows = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                GPIReport1Row aux1 = (GPIReport1Row) o1;
                GPIReport1Row aux2 = (GPIReport1Row) o2;
                if (aux1 == null && aux2 == null) {
                    return 0;
                } else if (aux1 == null) {
                    return 1;
                } else if (aux2 == null) {
                    return -1;
                } else {
                    return (aux1.getDonorGroup().getOrgGrpName() + aux1.getYear()).compareTo(aux2.getDonorGroup().getOrgGrpName() + aux2.getYear());
                }
            }
        };

        Collection<GPIReportAbstractRow> newList = new ArrayList<GPIReportAbstractRow>();
        Collections.sort((ArrayList) list, compareRows);

        // Format the list grouping by donor grp and year.
        AmpOrgGroup auxGroup = null;
        Iterator iter = list.iterator();
        Integer auxColumn1 = null;
        Integer auxColumn2 = null;
        int currentYear = 0;
        while (iter.hasNext()) {
            GPIReport1Row row = (GPIReport1Row) iter.next();

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
                    if(row.getColumn1() != null) {
                        if(auxColumn1 == null) {
                            auxColumn1 = 0;
                        }
                        auxColumn1 = auxColumn1 + row.getColumn1();
                    }
                    if(row.getColumn2() != null) {
                        if(auxColumn2 == null) {
                            auxColumn2 = 0;
                        }
                        auxColumn2 = auxColumn2 + row.getColumn2();
                    }
                } else {
                    GPIReport1Row newRow = new GPIReport1Row();
                    if(auxColumn1 != null) {
                        newRow.setColumn1(auxColumn1);
                    }
                    if(auxColumn2 != null) {
                        newRow.setColumn2(auxColumn2);
                    }
                    newRow.setDonorGroup(row.getDonorGroup());
                    newRow.setYear(currentYear);
                    newList.add(newRow);

                    currentYear = row.getYear();
                    auxColumn1 = row.getColumn1();
                    auxColumn2 = row.getColumn2();
                }
            } else {
                GPIReport1Row newRow = new GPIReport1Row();
                if(auxColumn1 != null) {
                    newRow.setColumn1(auxColumn1);
                }
                if(auxColumn2 != null) {
                    newRow.setColumn2(auxColumn2);
                }
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
                GPIReport1Row newRow = new GPIReport1Row();
                if(auxColumn1 != null) {
                    newRow.setColumn1(auxColumn1);
                }
                if(auxColumn2 != null) {
                    newRow.setColumn2(auxColumn2);
                }
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
        
        // Now we have the complete list with all donors then check the "kosovo" condition (AMP-17226)
        if (!FeaturesUtil.isVisibleModule("/Activity Form/Program")) {
            Iterator<GPIReportAbstractRow> i = newList.iterator();
            while (i.hasNext()) {
                GPIReport1Row row = (GPIReport1Row) i.next();
                row.setColumn1(new Integer(0));
                row.setColumn2(new Integer(0));
                row.setColumn3(new Integer(0));
            }
        }

        return newList;
    }

    private Collection<GPIReportAbstractRow> calculatePercentages(Collection<GPIReportAbstractRow> coll, int startYear, int endYear) throws Exception {

        int range = endYear + 1 - startYear;
        Integer[] sumCol1 = new Integer[range];
        Integer[] sumCol2 = new Integer[range];
        Iterator<GPIReportAbstractRow> iterColl = coll.iterator();
        while (iterColl.hasNext()) {
            // Calculate percentages.
            GPIReport1Row auxRow = (GPIReport1Row) iterColl.next();
            if(auxRow.getColumn2() != null) {
                if (auxRow.getColumn2().doubleValue() > 0) {
                    if (auxRow.getColumn2().doubleValue() > 0) {
                        auxRow.setColumn3(new BigDecimal(auxRow.getColumn1()).multiply(new BigDecimal(100)).divide(new BigDecimal(auxRow.getColumn2()), RoundingMode.HALF_UP).floatValue());
                    } else {
                        auxRow.setColumn3(0);
                    }
                }
            }

            // Accumulate totals for each year.
            if (sumCol1[auxRow.getYear() - startYear] == null) {
                sumCol1[auxRow.getYear() - startYear] = new Integer(0);
            }
            if (sumCol2[auxRow.getYear() - startYear] == null) {
                sumCol2[auxRow.getYear() - startYear] = new Integer(0);
            }
            if(auxRow.getColumn1() != null) {
                sumCol1[auxRow.getYear() - startYear] = sumCol1[auxRow.getYear() - startYear] + (auxRow.getColumn1());
            }
            if(auxRow.getColumn2() != null) {
                sumCol2[auxRow.getYear() - startYear] = sumCol2[auxRow.getYear() - startYear] + (auxRow.getColumn2());
            }
        }

        // Add "All Donors" record at the beginning with the total amounts.
        int currentYear = startYear;
        ArrayList auxList = new ArrayList(coll);
        for (int i = 0; i < endYear + 1 - startYear; i++) {
            GPIReport1Row auxRow = new GPIReport1Row();
            AmpOrgGroup auxDonorGroup = new AmpOrgGroup();
            auxDonorGroup.setOrgGrpName(TranslatorWorker.translateText(GPIConstants.ALL_DONORS));
            auxDonorGroup.setAmpOrgGrpId(new Long(0));
            auxRow.setDonorGroup(auxDonorGroup);
            auxRow.setColumn1(sumCol1[i]);
            auxRow.setColumn2(sumCol2[i]);
            auxRow.setYear(startYear + i);
            if(auxRow.getColumn2() != null) {
                if (auxRow.getColumn2().doubleValue() > 0) {
                    auxRow.setColumn3(new BigDecimal(auxRow.getColumn1()).multiply(new BigDecimal(100)).divide(new BigDecimal(auxRow.getColumn2()), RoundingMode.HALF_UP).floatValue());
                } else {
                    auxRow.setColumn3(0);
                }
            }
            //AMP-17181
            //auxList.add(i, auxRow);
        }
        return auxList;
    }

    private Collection<GPIReportAbstractRow> addMissingYears(Collection<GPIReportAbstractRow> coll, int startYear, int endYear) throws Exception {
        Collection ret = new ArrayList();
        AmpOrgGroup auxGroup = null;
        int j = 0;
        Iterator iter = coll.iterator();
        while (iter.hasNext()) {
            GPIReport1Row row = (GPIReport1Row) iter.next();
            if (auxGroup == null) {
                auxGroup = row.getDonorGroup();
            }
            if (j == 0 || !auxGroup.getAmpOrgGrpId().equals(row.getDonorGroup().getAmpOrgGrpId())) {
                auxGroup = row.getDonorGroup();
                for (int i = startYear; i < endYear + 1; i++) {
                    GPIReport1Row newRow = new GPIReport1Row();
                    newRow.setColumn1(null);
                    newRow.setColumn2(null);
                    newRow.setDonorGroup(auxGroup);
                    newRow.setYear(i);
                    ret.add(newRow);
                }
            }
            j++;
        }

        Iterator iterRet = ret.iterator();
        while (iterRet.hasNext()) {
            GPIReport1Row rowRet = (GPIReport1Row) iterRet.next();
            Iterator iterOrigen = coll.iterator();
            while (iterOrigen.hasNext()) {
                GPIReport1Row rowOrigen = (GPIReport1Row) iterOrigen.next();
                if (rowRet.getDonorGroup().getAmpOrgGrpId().equals(rowOrigen.getDonorGroup().getAmpOrgGrpId()) && rowRet.getYear() == rowOrigen.getYear()) {
                    rowRet.setColumn1(rowOrigen.getColumn1());
                    rowRet.setColumn2(rowOrigen.getColumn2());
                    rowRet.setDonorGroup(rowOrigen.getDonorGroup());
                }
            }
        }

        return ret;
    }
}
