package org.digijava.module.parisindicator.helper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpAhsurvey;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpOrganisationDocument;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.helper.fiscalcalendar.BaseCalendar;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.calendar.dbentity.AmpCalendar;
import org.digijava.module.calendar.dbentity.Calendar;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.contentrepository.util.DocumentOrganizationManager;
import org.digijava.module.parisindicator.helper.row.PIReport10aRow;
import org.digijava.module.parisindicator.helper.row.PIReport10bRow;
import org.digijava.module.parisindicator.helper.row.PIReportAbstractRow;
import org.digijava.module.parisindicator.model.PIUseCase;
import org.digijava.module.parisindicator.util.PIConstants;
import org.digijava.module.parisindicator.util.PIUtils;
import org.hibernate.query.Query;
import org.hibernate.Session;

import java.util.Collections;

public class PIReport10b extends PIAbstractReport {

    private static Logger logger = Logger.getLogger(PIReport10b.class);
    private final String reportCode = PIConstants.PARIS_INDICATOR_REPORT_10b;

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
                PIReport10bRow aux1 = (PIReport10bRow) o1;
                PIReport10bRow aux2 = (PIReport10bRow) o2;
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
            PIReport10bRow row = (PIReport10bRow) iter.next();

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
                    PIReport10bRow newRow = new PIReport10bRow();
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
                PIReport10bRow newRow = new PIReport10bRow();
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
                PIReport10bRow newRow = new PIReport10bRow();
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
            PIReport10bRow auxRow = (PIReport10bRow) iterColl.next();
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
            PIReport10bRow auxRow = new PIReport10bRow();
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
            PIReport10bRow row = (PIReport10bRow) iter.next();
            if (auxGroup == null) {
                auxGroup = row.getDonorGroup();
            }
            if (j == 0 || !auxGroup.getAmpOrgGrpId().equals(row.getDonorGroup().getAmpOrgGrpId())) {
                auxGroup = row.getDonorGroup();
                for (int i = startYear; i < endYear + 1; i++) {
                    PIReport10bRow newRow = new PIReport10bRow();
                    newRow.setDonorGroup(auxGroup);
                    newRow.setYear(i);
                    ret.add(newRow);
                }
            }
            j++;
        }

        Iterator iterRet = ret.iterator();
        while (iterRet.hasNext()) {
            PIReport10bRow rowRet = (PIReport10bRow) iterRet.next();
            Iterator iterOrigen = coll.iterator();
            while (iterOrigen.hasNext()) {
                PIReport10bRow rowOrigen = (PIReport10bRow) iterOrigen.next();
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
    public Collection<PIReportAbstractRow> generateReport10b(Collection<NodeWrapper> commonData, int startYear,
            int endYear, AmpFiscalCalendar calendar, AmpCurrency currency, Collection<AmpSector> sectorsFilter,
            Collection<AmpCategoryValue> statusFilter, Collection<AmpCategoryValue> financingInstrumentFilter) {

        Collection<PIReportAbstractRow> list = new ArrayList<PIReportAbstractRow>();
        try {
            Iterator<NodeWrapper> iterNodes = commonData.iterator();
            while (iterNodes.hasNext()) {
                NodeWrapper auxNodeWrapper = iterNodes.next();
                // Get organizations for this document (trying to get documents
                // from an organization didn't work).
                Collection<AmpOrganisation> orgsFromDocument = DocumentOrganizationManager.getInstance()
                        .getOrganizationsByUUID(auxNodeWrapper.getUuid());
                Collection<AmpOrganisation> auxOrganizations = new ArrayList<AmpOrganisation>();
                // Calculate number of MUL and BIL donors for this document.
                int orgs4Doc = 0;
                Iterator<AmpOrganisation> iterOrgs = orgsFromDocument.iterator();
                while (iterOrgs.hasNext()) {
                    AmpOrganisation auxOrg = iterOrgs.next();
                    if (auxOrg.getOrgGrpId().getOrgType().getOrgTypeCode().equals(PIConstants.ORG_GRP_BILATERAL)
                            || auxOrg.getOrgGrpId().getOrgType().getOrgTypeCode().equals(
                                    PIConstants.ORG_GRP_MULTILATERAL)) {
                        orgs4Doc++;
                        auxOrganizations.add(auxOrg);
                    }
                }
                Iterator<AmpOrganisation> iterOrgs2 = auxOrganizations.iterator();
                while (iterOrgs2.hasNext()) {
                    AmpOrganisation auxOrg = iterOrgs2.next();
                    PIReport10bRow auxRow = new PIReport10bRow();
                    auxRow.setYear(Integer.valueOf(
                            auxNodeWrapper.getDate().substring(auxNodeWrapper.getDate().length() - 4)).intValue());
                    auxRow.setDonorGroup(auxOrg.getOrgGrpId());
                    auxRow.setDonorGroupName(auxOrg.getOrgGrpId().getOrgGrpName());
                    auxRow.setColumn2(1);
                    if (orgs4Doc > 1) {
                        auxRow.setColumn1(1);
                    }
                    list.add(auxRow);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
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
    public Collection<PIReportAbstractRow> generateReport(Collection<AmpAhsurvey> commonData, int startYear,
            int endYear, AmpFiscalCalendar calendar, AmpCurrency currency, Collection<AmpSector> sectorsFilter,
            Collection<AmpCategoryValue> statusFilter, Collection<AmpCategoryValue> financingInstrumentFilter) {
        // TODO Auto-generated method stub
        return null;
    }
}
