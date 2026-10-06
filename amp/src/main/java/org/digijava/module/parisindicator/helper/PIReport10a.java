package org.digijava.module.parisindicator.helper;

import org.apache.log4j.Logger;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.calendar.dbentity.AmpCalendar;
import org.digijava.module.calendar.dbentity.Calendar;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.parisindicator.helper.row.PIReport10aRow;
import org.digijava.module.parisindicator.helper.row.PIReportAbstractRow;
import org.digijava.module.parisindicator.util.PIConstants;
import org.digijava.module.parisindicator.util.PIUtils;

import java.text.SimpleDateFormat;
import java.util.*;

public class PIReport10a extends PIAbstractReport {

    private static Logger logger = Logger.getLogger(PIReport10a.class);
    private final String reportCode = PIConstants.PARIS_INDICATOR_REPORT_10a;

    public String getReportCode() {
        return this.reportCode;
    }

    @Override
    public Collection<PIReportAbstractRow> reportPostProcess(Collection<PIReportAbstractRow> baseReport, int startYear,
            int endYear) throws Exception {

        Collection<PIReportAbstractRow> list = new ArrayList<PIReportAbstractRow>(baseReport);

        // TODO: make a general comparator???
        Comparator compareRows = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                PIReport10aRow aux1 = (PIReport10aRow) o1;
                PIReport10aRow aux2 = (PIReport10aRow) o2;
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
        int auxColumn1 = 0;
        int auxColumn2 = 0;
        int currentYear = 0;
        while (iter.hasNext()) {
            PIReport10aRow row = (PIReport10aRow) iter.next();

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
                    auxColumn1 = auxColumn1 + row.getColumn1();
                    auxColumn2 = auxColumn2 + row.getColumn2();
                } else {
                    PIReport10aRow newRow = new PIReport10aRow();
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
                PIReport10aRow newRow = new PIReport10aRow();
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
                PIReport10aRow newRow = new PIReport10aRow();
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
        int[] sumCol1 = new int[range];
        int[] sumCol2 = new int[range];
        Iterator<PIReportAbstractRow> iterColl = coll.iterator();
        while (iterColl.hasNext()) {
            // Calculate percentages.
            PIReport10aRow auxRow = (PIReport10aRow) iterColl.next();
            if (auxRow.getColumn2() > 0) {
                auxRow.setColumn3(auxRow.getColumn1() * 100 / auxRow.getColumn2());
            } else {
                auxRow.setColumn3(0);
            }

            // Accumulate totals for each year.
            sumCol1[auxRow.getYear() - startYear] = sumCol1[auxRow.getYear() - startYear] + auxRow.getColumn1();
            sumCol2[auxRow.getYear() - startYear] = sumCol2[auxRow.getYear() - startYear] + auxRow.getColumn2();
        }

        // Add "All Donors" record at the beginning with the total amounts.
        int currentYear = startYear;
        ArrayList auxList = new ArrayList(coll);
        for (int i = 0; i < endYear + 1 - startYear; i++) {
            PIReport10aRow auxRow = new PIReport10aRow();
            AmpOrgGroup auxDonorGroup = new AmpOrgGroup();
            auxDonorGroup.setOrgGrpName(PIConstants.ALL_DONORS);
            auxDonorGroup.setAmpOrgGrpId(new Long(0));
            auxRow.setDonorGroup(auxDonorGroup);
            auxRow.setColumn1(sumCol1[i]);
            auxRow.setColumn2(sumCol2[i]);
            auxRow.setYear(startYear + i);
            if (auxRow.getColumn2() > 0) {
                auxRow.setColumn3(auxRow.getColumn1() * 100 / auxRow.getColumn2());
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
            PIReport10aRow row = (PIReport10aRow) iter.next();
            if (auxGroup == null) {
                auxGroup = row.getDonorGroup();
            }
            if (j == 0 || !auxGroup.getAmpOrgGrpId().equals(row.getDonorGroup().getAmpOrgGrpId())) {
                auxGroup = row.getDonorGroup();
                for (int i = startYear; i < endYear + 1; i++) {
                    PIReport10aRow newRow = new PIReport10aRow();
                    newRow.setDonorGroup(auxGroup);
                    newRow.setYear(i);
                    ret.add(newRow);
                }
            }
            j++;
        }

        Iterator iterRet = ret.iterator();
        while (iterRet.hasNext()) {
            PIReport10aRow rowRet = (PIReport10aRow) iterRet.next();
            Iterator iterOrigen = coll.iterator();
            while (iterOrigen.hasNext()) {
                PIReport10aRow rowOrigen = (PIReport10aRow) iterOrigen.next();
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

        Collection<PIReportAbstractRow> list = new ArrayList<PIReportAbstractRow>();
        PIReport10aRow auxRow = null;
        int yearRange = endYear - startYear + 1;
        double fromExchangeRate;
        double toExchangeRate;
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");

        try {
            Iterator<AmpOrganisation> iterCommonData = commonData.iterator();
            while (iterCommonData.hasNext()) {
                AmpOrganisation auxOrganisation = iterCommonData.next();
                Iterator<AmpCalendar> iterCal = auxOrganisation.getCalendar().iterator();
                while (iterCal.hasNext()) {
                    auxRow = new PIReport10aRow();
                    AmpCalendar auxCalendar = iterCal.next();
                    String eventTypeName="";
                    if(auxCalendar.getEventsType()!=null){
                        AmpCategoryValue ampCategoryValue = CategoryManagerUtil.getAmpCategoryValueFromDb(auxCalendar.getEventsType().getId());
                        if (ampCategoryValue != null){
                            eventTypeName=ampCategoryValue.getValue();
                        }
                    }
                    if (PIConstants.MISSION.equalsIgnoreCase(eventTypeName)) {
                        Calendar cal = (Calendar) auxCalendar.getCalendarPK().getCalendar();
                        int year = new Integer(yearFormat.format(cal.getStartDate())).intValue();

                        // Checking if the Mission is 'JOINT' with other MUL/BIL
                        // donor, which can be accomplished checking against
                        // commonData.
                        int count = 0;
                        Iterator<AmpOrganisation> iterOrganisationsFromCalendar = auxCalendar.getOrganisations()
                                .iterator();
                        while (iterOrganisationsFromCalendar.hasNext()) {
                            AmpOrganisation auxOrgFromEvent = iterOrganisationsFromCalendar.next();
                            if (PIUtils.containOrganisations(commonData, auxOrgFromEvent)) {
                                count++;
                            }
                        }
                        if (count > 1) {
                            auxRow.setColumn1(1);
                        }
                        auxRow.setYear(year);
                        auxRow.setDonorGroupName(auxOrganisation.getOrgGrpId().getOrgGrpName());
                        auxRow.setDonorGroup(auxOrganisation.getOrgGrpId());
                        auxRow.setColumn2(1);
                        list.add(auxRow);
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return list;
    }

    @Override
    public Collection<PIReportAbstractRow> generateReport(Collection<AmpAhsurvey> commonData, int startYear,
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
