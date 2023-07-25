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
import java.util.Set;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.newreports.AmountsUnits;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpAhsurvey;
import org.digijava.module.aim.dbentity.AmpGPISurvey;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.GPISetup;
import org.digijava.module.aim.exception.NoCategoryClassException;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.fiscalcalendar.BaseCalendar;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.aim.util.GPISetupUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryClass;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.gpi.helper.row.GPIReport1Row;
import org.digijava.module.gpi.helper.row.GPIReport5aRow;
import org.digijava.module.gpi.helper.row.GPIReportAbstractRow;
import org.digijava.module.gpi.model.GPIFilter;
import org.digijava.module.gpi.util.GPIConstants;
import org.digijava.module.gpi.util.GPIUtils;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.hibernate.query.Query;
import org.hibernate.Session;

import java.util.Collections;

public class GPIReport5a extends GPIAbstractReport {

    private static Logger logger = Logger.getLogger(GPIReport5a.class);
    private final String reportCode = GPIConstants.GPI_REPORT_5a;
    private final Session session = PersistenceManager.getSession();

    public String getReportCode() {
        return this.reportCode;
    }

    @Override
    public Collection<GPIReportAbstractRow> generateReport(Collection commonData, GPIFilter filter) {

        long time = Calendar.getInstance().getTimeInMillis();
        
        GPISetup setup = GPISetupUtil.getSetup();
        Collection<GPIReportAbstractRow> list = new ArrayList<GPIReportAbstractRow>();
        GPIReport5aRow auxRow = null;
        int yearRange = filter.getEndYer() - filter.getStartYear() + 1;
        Date[] startDates = new Date[yearRange];
        Date[] endDates = new Date[yearRange];
        double fromExchangeRate;
        double toExchangeRate;
        
        BigDecimal divider = BigDecimal.valueOf(AmountsUnits.getDefaultValue().divider);

        if (setup != null) {
            try {
                // Setup year ranges according the selected calendar.
                AmpFiscalCalendar fCalendar = FiscalCalendarUtil.getAmpFiscalCalendar(filter.getCalendar().getAmpFiscalCalId());
                if (!(fCalendar.getBaseCal().equalsIgnoreCase(BaseCalendar.BASE_ETHIOPIAN.getValue()))) {
                    for (int i = 0; i < yearRange; i++) {
                        startDates[i] = FiscalCalendarUtil.getCalendarStartDate(filter.getCalendar().getAmpFiscalCalId(), filter.getStartYear() + i);
                        endDates[i] = FiscalCalendarUtil.getCalendarEndDate(filter.getCalendar().getAmpFiscalCalId(), filter.getStartYear() + i);
                    }
                }

                Iterator iter = commonData.iterator();
                while (iter.hasNext()) {
                    Object[] data = (Object[]) iter.next();
                    Date transactionDate = (Date) data[3];

                    // Filter by years. Check if the funding detail date
                    // falls into one of the date ranges.
                    if (GPIUtils.getYear(transactionDate, startDates, endDates, filter.getStartYear(), filter.getEndYer()) == 0) {
                        // Ignore this project.
                        continue;
                    }

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(transactionDate);

                    auxRow = new GPIReport5aRow();

                    // Calculate exchange rates.
                    fromExchangeRate = Util.getExchange(data[9].toString(), new java.sql.Date(transactionDate.getTime()));
                    toExchangeRate = 0;
                    if (filter.getCurrency() != null) {
                        toExchangeRate = Util.getExchange(filter.getCurrency().getCurrencyCode(),
                                new java.sql.Date(transactionDate.getTime()));
                    }
                    BigDecimal amount = new BigDecimal(CurrencyWorker.convert1((Double) data[7], fromExchangeRate, toExchangeRate));
                    amount = amount.divide(divider);

                    // This is Actual or Planned for funding.
                    String auxCategoryValue = data[10].toString();
                    // Match the funding type with the config (2 different
                    // things, has to be hardcoded at some level).
                    int column = -1;
                    // To understand this part it reads this way: IF the
                    // Actual Disbursement (on indicator 5a) is the actual
                    // commitment (on fundings) then...
                    int transactionType = ((Integer) data[8]).intValue();
                    if (setup.getIndicator5aActualDisbursement().equals("ACTUAL_COMMITMENTS")) {
                        if (auxCategoryValue.equalsIgnoreCase("actual") && transactionType == Constants.COMMITMENT) {
                            column = 1;
                        }
                    } else if (setup.getIndicator5aActualDisbursement().equals("ACTUAL_DISBURSEMENTS")) {
                        if (auxCategoryValue.equalsIgnoreCase("actual") && transactionType == Constants.DISBURSEMENT) {
                            column = 1;
                        }
                    } else if (setup.getIndicator5aActualDisbursement().equals("ACTUAL_EXPENDITURES")) {
                        if (auxCategoryValue.equalsIgnoreCase("actual") && transactionType == Constants.EXPENDITURE) {
                            column = 1;
                        }
                    } else if (setup.getIndicator5aActualDisbursement().equals("PLANNED_COMMITMENTS")) {
                        if (auxCategoryValue.equalsIgnoreCase("planned") && transactionType == Constants.COMMITMENT) {
                            column = 1;
                        }
                    } else if (setup.getIndicator5aActualDisbursement().equals("PLANNED_DISBURSEMENTS")) {
                        if (auxCategoryValue.equalsIgnoreCase("planned") && transactionType == Constants.DISBURSEMENT) {
                            column = 1;
                        }
                    } else if (setup.getIndicator5aActualDisbursement().equals("PLANNED_EXPENDITURES")) {
                        if (auxCategoryValue.equalsIgnoreCase("planned") && transactionType == Constants.EXPENDITURE) {
                            column = 1;
                        }
                    }

                    if (setup.getIndicator5aPlannedDisbursement().equals("ACTUAL_COMMITMENTS")) {
                        if (auxCategoryValue.equalsIgnoreCase("actual") && transactionType == Constants.COMMITMENT) {
                            column = 2;
                        }
                    } else if (setup.getIndicator5aPlannedDisbursement().equals("ACTUAL_DISBURSEMENTS")) {
                        if (auxCategoryValue.equalsIgnoreCase("actual") && transactionType == Constants.DISBURSEMENT) {
                            column = 2;
                        }
                    } else if (setup.getIndicator5aPlannedDisbursement().equals("ACTUAL_EXPENDITURES")) {
                        if (auxCategoryValue.equalsIgnoreCase("actual") && transactionType == Constants.EXPENDITURE) {
                            column = 2;
                        }
                    } else if (setup.getIndicator5aPlannedDisbursement().equals("PLANNED_COMMITMENTS")) {
                        if (auxCategoryValue.equalsIgnoreCase("planned") && transactionType == Constants.COMMITMENT) {
                            column = 2;
                        }
                    } else if (setup.getIndicator5aPlannedDisbursement().equals("PLANNED_DISBURSEMENTS")) {
                        if (auxCategoryValue.equalsIgnoreCase("planned") && transactionType == Constants.DISBURSEMENT) {
                            column = 2;
                        }
                    } else if (setup.getIndicator5aPlannedDisbursement().equals("PLANNED_EXPENDITURES")) {
                        if (auxCategoryValue.equalsIgnoreCase("planned") && transactionType == Constants.EXPENDITURE) {
                            column = 2;
                        }
                    }

                    // Check survey answers for thisAmpGPISurvey.
                    String[] arrayResponses = ((String) data[12]).split(",");
                    boolean[] answers = GPIUtils.getSurveyAnswers(GPIConstants.GPI_REPORT_5a, arrayResponses);
                    if (answers != null && answers[0]) {
                        auxRow.setColumn1(null);
                        auxRow.setColumn2(null);
                        auxRow.setColumn3(-1);

                        switch (column) {
                        case 1:
                            auxRow.setColumn1(amount);
                            break;
                        case 2:
                            auxRow.setColumn2(amount);
                            break;
                        }
                        auxRow.setColumn3(0);

                        AmpOrgGroup auxOrgGroup = new AmpOrgGroup();
                        auxOrgGroup.setAmpOrgGrpId(((BigInteger) data[4]).longValue());
                        auxOrgGroup.setOrgGrpName(data[5].toString());
                        auxRow.setDonorGroup(auxOrgGroup);

                        auxRow.setYear(calendar.get(Calendar.YEAR));
                        list.add(auxRow);
                    } else {
                        auxRow.setColumn3(0);

                        AmpOrgGroup auxOrgGroup = new AmpOrgGroup();
                        auxOrgGroup.setAmpOrgGrpId(((BigInteger) data[4]).longValue());
                        auxOrgGroup.setOrgGrpName(data[5].toString());
                        auxRow.setDonorGroup(auxOrgGroup);

                        auxRow.setYear(calendar.get(Calendar.YEAR));
                        list.add(auxRow);
                    }
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
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
                GPIReport5aRow aux1 = (GPIReport5aRow) o1;
                GPIReport5aRow aux2 = (GPIReport5aRow) o2;
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
        BigDecimal auxColumn1 = null;
        BigDecimal auxColumn2 = null;
        int currentYear = 0;
        while (iter.hasNext()) {
            GPIReport5aRow row = (GPIReport5aRow) iter.next();

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
                            auxColumn1 = new BigDecimal(0);
                        }
                        auxColumn1 = auxColumn1.add(row.getColumn1());
                    }
                    if(row.getColumn2() != null) {
                        if(auxColumn2 == null) {
                            auxColumn2 = new BigDecimal(0);
                        }
                        auxColumn2 = auxColumn2.add(row.getColumn2());
                    }
                } else {
                    GPIReport5aRow newRow = new GPIReport5aRow();
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
                GPIReport5aRow newRow = new GPIReport5aRow();
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
                GPIReport5aRow newRow = new GPIReport5aRow();
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
        
        // Format numbers.
        for (GPIReportAbstractRow row : newList) {
            GPIReport5aRow auxRow = (GPIReport5aRow) row;
            if (auxRow.getColumn1() != null) {
                if (auxRow.getColumn1().doubleValue() != 0) {
                    auxRow.setColumn1DisplayValue(FormatHelper.formatNumber(auxRow.getColumn1().doubleValue()));
                } else {
                    auxRow.setColumn1DisplayValue("0");
                }
            } else {
                auxRow.setColumn1DisplayValue(GPIConstants.NO_DATA);
            }
            if (auxRow.getColumn2() != null) {
                if (auxRow.getColumn2().doubleValue() != 0) {
                    auxRow.setColumn2DisplayValue(FormatHelper.formatNumber(auxRow.getColumn2().doubleValue()));
                } else {
                    auxRow.setColumn2DisplayValue("0");
                }
            } else {
                auxRow.setColumn2DisplayValue(GPIConstants.NO_DATA);
            }
            auxRow.setColumn3DisplayValue("" + Math.round(auxRow.getColumn3()) + "%");
        }

        return newList;
    }

    private Collection<GPIReportAbstractRow> calculatePercentages(Collection<GPIReportAbstractRow> coll, int startYear, int endYear) throws Exception {

        int range = endYear + 1 - startYear;
        BigDecimal[] sumCol1 = new BigDecimal[range];
        BigDecimal[] sumCol2 = new BigDecimal[range];
        Iterator<GPIReportAbstractRow> iterColl = coll.iterator();
        while (iterColl.hasNext()) {
            // Calculate percentages.
            GPIReport5aRow auxRow = (GPIReport5aRow) iterColl.next();
            if(auxRow.getColumn2() != null) {
                if (auxRow.getColumn2().doubleValue() > 0) {
                    if(auxRow.getColumn1() == null) {
                        auxRow.setColumn1(new BigDecimal(0));
                    }
                    auxRow.setColumn3(auxRow.getColumn1().multiply(new BigDecimal(100)).divide(auxRow.getColumn2(), RoundingMode.HALF_UP).floatValue());
                } else {
                    auxRow.setColumn3(0);
                }
            }   

            // Accumulate totals for each year.
            if (sumCol1[auxRow.getYear() - startYear] == null) {
                sumCol1[auxRow.getYear() - startYear] = new BigDecimal(0);
            }
            if (sumCol2[auxRow.getYear() - startYear] == null) {
                sumCol2[auxRow.getYear() - startYear] = new BigDecimal(0);
            }
            if(auxRow.getColumn1() != null) {
                sumCol1[auxRow.getYear() - startYear] = sumCol1[auxRow.getYear() - startYear].add(auxRow.getColumn1());
            }
            if(auxRow.getColumn2() != null) {
                sumCol2[auxRow.getYear() - startYear] = sumCol2[auxRow.getYear() - startYear].add(auxRow.getColumn2());
            }
        }

        // Add "All Donors" record at the beginning with the total amounts.
        int currentYear = startYear;
        ArrayList auxList = new ArrayList(coll);
        for (int i = 0; i < endYear + 1 - startYear; i++) {
            GPIReport5aRow auxRow = new GPIReport5aRow();
            AmpOrgGroup auxDonorGroup = new AmpOrgGroup();
            auxDonorGroup.setOrgGrpName(TranslatorWorker.translateText(GPIConstants.ALL_DONORS));
            auxDonorGroup.setAmpOrgGrpId(new Long(0));
            auxRow.setDonorGroup(auxDonorGroup);
            auxRow.setColumn1(sumCol1[i]);
            auxRow.setColumn2(sumCol2[i]);
            auxRow.setYear(startYear + i);
            if(auxRow.getColumn2() != null) {
                if (auxRow.getColumn2().doubleValue() > 0) {
                    auxRow.setColumn3(auxRow.getColumn1().multiply(new BigDecimal(100)).divide(auxRow.getColumn2(), RoundingMode.HALF_UP).floatValue());
                } else {
                    auxRow.setColumn3(0);
                }
            }
            auxList.add(i, auxRow);
        }
        return auxList;
    }

    private Collection<GPIReportAbstractRow> addMissingYears(Collection<GPIReportAbstractRow> coll, int startYear, int endYear) throws Exception {
        //First make list of donors with surveys.
        /*Set<String> validDonors = new HashSet<String>();
        Iterator<GPIReportAbstractRow> iAll = coll.iterator();
        while(iAll.hasNext()) {
            GPIReport5aRow auxRow = (GPIReport5aRow) iAll.next();
            if(auxRow.getColumn1() != null || auxRow.getColumn2() != null) {
                validDonors.add(auxRow.getDonorGroup().getOrgGrpName());
            }
        }*/
        
        Collection ret = new ArrayList();
        AmpOrgGroup auxGroup = null;
        int j = 0;
        Iterator iter = coll.iterator();
        while (iter.hasNext()) {
            GPIReport5aRow row = (GPIReport5aRow) iter.next();
            if (auxGroup == null) {
                auxGroup = row.getDonorGroup();
            }
            if (j == 0 || !auxGroup.getAmpOrgGrpId().equals(row.getDonorGroup().getAmpOrgGrpId())) {
                auxGroup = row.getDonorGroup();
                boolean hasData = false;/*validDonors.contains(auxGroup.getOrgGrpName());*/
                for (int i = startYear; i < endYear + 1; i++) {
                    GPIReport5aRow newRow = new GPIReport5aRow();
                    newRow.setColumn1(hasData ? new BigDecimal(0) : null);
                    newRow.setColumn2(hasData ? new BigDecimal(0) : null);
                    newRow.setDonorGroup(auxGroup);
                    newRow.setYear(i);
                    ret.add(newRow);
                }
            }
            j++;
        }

        Iterator iterRet = ret.iterator();
        while (iterRet.hasNext()) {
            GPIReport5aRow rowRet = (GPIReport5aRow) iterRet.next();
            Iterator iterOrigen = coll.iterator();
            while (iterOrigen.hasNext()) {
                GPIReport5aRow rowOrigen = (GPIReport5aRow) iterOrigen.next();
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
