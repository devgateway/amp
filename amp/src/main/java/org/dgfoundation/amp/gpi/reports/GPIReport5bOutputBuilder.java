package org.dgfoundation.amp.gpi.reports;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.AmountCell;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.TextCell;
import org.dgfoundation.amp.nireports.formulas.NiFormula;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A utility class to transform a GeneratedReport to GPI Report 5b
 * 
 * @author Viorel Chihai
 *
 */
public class GPIReport5bOutputBuilder extends GPIReportOutputBuilder {

    private static final String MTEF_FUNDINGS_YES = "1";
    private static final String MTEF_FUNDINGS_NO = "0";
    private static final int MTEF_COLUMN_1 = 1;
    private static final int MTEF_COLUMN_2 = 2;
    private static final int MTEF_COLUMN_3 = 3;

    private static final String MTEF_NAME = "MTEF";

    private List<GPIIndicator5bItem> gpiItems = new ArrayList<>();
    private Integer pivotyear;
    public GPIReport5bOutputBuilder() {
        addColumn(new GPIReportOutputColumn(GPIReportConstants.COLUMN_INDICATOR_5B,
                GPIReportConstants.REPORT_5B_TOOLTIP.get(GPIReportConstants.COLUMN_INDICATOR_5B)));
        addColumn(new GPIReportOutputColumn(ColumnConstants.DONOR_AGENCY));
        addColumn(new GPIReportOutputColumn(ColumnConstants.DONOR_GROUP));
    }

    public final static Set<String> SUMMARY_NUMBERS = Collections
            .unmodifiableSet(new HashSet<>(Arrays.asList(GPIReportConstants.COLUMN_INDICATOR_5B)));

    /**
     * build the headers of the report
     * 
     * @param generatedReport
     * @return
     */
    @Override
    protected List<GPIReportOutputColumn> buildHeaders(GeneratedReport generatedReport) {
        List<GPIReportOutputColumn> headers = new ArrayList<>();

        String donorColumnName = isDonorAgency ? ColumnConstants.DONOR_AGENCY : ColumnConstants.DONOR_GROUP;
        // at this point we need to take the pivot year from formparams. the proper fix is to provide the correct start
        // and dates in /report/years endpoint. for instance in a leap year in eth calendar year does not start in
        // 1/1 it starts 9/1 and in leap year it starts in 9/12 AMP-27540 is the followup ticket
        pivotyear = GPIReportUtils.getPivotYearFromFormParams(this.getOriginalFormParams());
        if (pivotyear == null) {
            //this shouldn't be null at this point since it has already been checked but we are
            //defensive until the proper fix is introduced in AMP-27540
            pivotyear = GPIReportUtils.getPivoteYear(generatedReport.spec);
        }
        headers.add(getColumns().get(donorColumnName));
        headers.add(new GPIReportOutputColumn(String.valueOf(pivotyear + MTEF_COLUMN_1), String.valueOf(pivotyear
                + MTEF_COLUMN_1), GPIReportConstants.REPORT_5B_TOOLTIP.get(GPIReportConstants.YEAR_1)));
        headers.add(new GPIReportOutputColumn(String.valueOf(pivotyear + MTEF_COLUMN_2), String.valueOf(pivotyear
                + MTEF_COLUMN_2), GPIReportConstants.REPORT_5B_TOOLTIP.get(GPIReportConstants.YEAR_2)));
        headers.add(new GPIReportOutputColumn(String.valueOf(pivotyear + MTEF_COLUMN_3), String.valueOf(pivotyear
                + MTEF_COLUMN_3),  GPIReportConstants.REPORT_5B_TOOLTIP.get(GPIReportConstants.YEAR_3)));
        headers.add(getColumns().get(GPIReportConstants.COLUMN_INDICATOR_5B));

        return headers;
    }



    /**
     * build the contents of the report
     * 
     * @param generatedReport
     * @return
     */
    @Override
    protected List<Map<GPIReportOutputColumn, String>> getReportContents(GeneratedReport generatedReport) {
        String donorColumnName = isDonorAgency ? ColumnConstants.DONOR_AGENCY : ColumnConstants.DONOR_GROUP;

        List<Map<GPIReportOutputColumn, String>> contents = new ArrayList<>();
        gpiItems = fetchGPIItemsFromReport(generatedReport, donorColumnName);

        gpiItems.forEach(gpiItem -> {
            Map<GPIReportOutputColumn, String> columns = new HashMap<>();
            columns.put(getColumns().get(donorColumnName), gpiItem.getDonorAgency());
            columns.put(new GPIReportOutputColumn(String.valueOf(gpiItem.getYear() + 1)),
                    gpiItem.hasYear1() ? MTEF_FUNDINGS_YES : MTEF_FUNDINGS_NO);
            columns.put(new GPIReportOutputColumn(String.valueOf(gpiItem.getYear() + 2)),
                    gpiItem.hasYear2() ? MTEF_FUNDINGS_YES : MTEF_FUNDINGS_NO);
            columns.put(new GPIReportOutputColumn(String.valueOf(gpiItem.getYear() + 3)),
                    gpiItem.hasYear3() ? MTEF_FUNDINGS_YES : MTEF_FUNDINGS_NO);
            columns.put(getColumns().get(GPIReportConstants.COLUMN_INDICATOR_5B), gpiItem.getPercentage() + "%");

            contents.add(columns);
        });

        return contents;
    }

    private List<GPIIndicator5bItem> fetchGPIItemsFromReport(GeneratedReport generatedReport, String donorColumnName) {

        List<GPIIndicator5bItem> allGpiItems = new ArrayList<>();
        int year = pivotyear;
        String calendarPrefix = "";
        if (generatedReport.spec.getSettings().getCalendar().getIsFiscal()) {
            calendarPrefix = generatedReport.spec.getSettings().getCalendar().getDefaultFiscalYearPrefix() + " ";
        }

        if (generatedReport.reportContents.getChildren() != null) {
            for (ReportArea reportArea : generatedReport.reportContents.getChildren()) {
                GPIIndicator5bItem gpiItem = new GPIIndicator5bItem(year);
                for (ReportOutputColumn roc : generatedReport.leafHeaders) {
                    ReportCell rc = reportArea.getContents().get(roc);
                    rc = rc != null ? rc : TextCell.EMPTY;

                    if (FeaturesUtil.getGlobalSettingValueBoolean(GlobalSettingsConstants.MTEF_ANNUAL_DATE_FORMAT)) {
                        if (roc.originalColumnName.equals(String.format("%s %s", MTEF_NAME, (year + MTEF_COLUMN_1)))) {
                            if (((AmountCell) rc).extractValue().compareTo(BigDecimal.ZERO) > 0) {
                                gpiItem.setYear1(true);
                            }
                        }
    
                        if (roc.originalColumnName.equals(String.format("%s %s", MTEF_NAME, (year + MTEF_COLUMN_2)))) {
                            if (((AmountCell) rc).extractValue().compareTo(BigDecimal.ZERO) > 0) {
                                gpiItem.setYear2(true);
                            }
                        }
    
                        if (roc.originalColumnName.equals(String.format("%s %s", MTEF_NAME, (year + MTEF_COLUMN_3)))) {
                            if (((AmountCell) rc).extractValue().compareTo(BigDecimal.ZERO) > 0) {
                                gpiItem.setYear3(true);
                            }
                        }
                    } else if (roc.originalColumnName.equals(MeasureConstants.MTEF)) {
                        if (roc.parentColumn.originalColumnName.equals(String.format("%s", calendarPrefix + (year
                                + MTEF_COLUMN_1)))) {
                            if (((AmountCell) rc).extractValue().compareTo(BigDecimal.ZERO) > 0) {
                                gpiItem.setYear1(true);
                            }
                        }

                        if (roc.parentColumn.originalColumnName.equals(String.format("%s", calendarPrefix + (year
                                + MTEF_COLUMN_2)))) {
                            if (((AmountCell) rc).extractValue().compareTo(BigDecimal.ZERO) > 0) {
                                gpiItem.setYear2(true);
                            }
                        }
                        
                        if (roc.parentColumn.originalColumnName.equals(String.format("%s", calendarPrefix + (year
                                + MTEF_COLUMN_3)))) {
                            if (((AmountCell) rc).extractValue().compareTo(BigDecimal.ZERO) > 0) {
                                gpiItem.setYear3(true);
                            }
                        }
                    }
                    
                    if (roc.originalColumnName.equals(donorColumnName)) {
                        gpiItem.setDonorAgency(rc.displayedValue);
                    }
                }
                allGpiItems.add(gpiItem);
            }
        }

        // We create another report for fetching funding data
        GeneratedReport measuresReport = GPIReportUtils.getGeneratedReportForIndicator5bActDisb(originalFormParams);
        Map<String, BigDecimal> donorActDisb = getDisbursementsForDonros(measuresReport, donorColumnName);

        allGpiItems.forEach(gpiItem -> {
            if (donorActDisb.containsKey(gpiItem.getDonorAgency())) {
                gpiItem.setFundings(true);
                gpiItem.setActualDisb(donorActDisb.get(gpiItem.getDonorAgency()));
            }
        });

        allGpiItems = allGpiItems.stream().filter(gpiItem -> gpiItem.isNotEmpty()).collect(Collectors.toList());

        return allGpiItems;
    }

    private Map<String, BigDecimal> getDisbursementsForDonros(GeneratedReport generatedReport, String donorColumnName) {
        Map<String, BigDecimal> donorDisbursements = new HashMap<>();

        if (generatedReport.reportContents.getChildren() != null) {
            for (ReportArea reportArea : generatedReport.reportContents.getChildren()) {
                String donor = "";
                BigDecimal actDisb = BigDecimal.ZERO;
                for (ReportOutputColumn roc : generatedReport.leafHeaders) {
                    ReportCell rc = reportArea.getContents().get(roc);
                    rc = rc != null ? rc : TextCell.EMPTY;

                    if (roc.originalColumnName.equals(donorColumnName)) {
                        donor = rc.displayedValue;
                    }

                    if (roc.originalColumnName.equals(MeasureConstants.ACTUAL_DISBURSEMENTS)) {
                        ReportCell disbRc = reportArea.getContents().get(roc);
                        actDisb = ((AmountCell) disbRc).extractValue();
                    }
                }

                donorDisbursements.put(donor, actDisb);
            }
        }

        return donorDisbursements;
    }

    /**
     * Calculate the summary number (indicator 5b on country level). We don't
     * use the generatedReport here since the values are fetched from gpiItems
     * generated in getReportContents() method
     * 
     * @param generatedReport
     * @return
     */
    @Override
    protected Map<GPIReportOutputColumn, String> getReportSummary(GeneratedReport generatedReport) {
        Map<GPIReportOutputColumn, String> summaryColumns = new HashMap<>();
        BigDecimal percentageIndicator5b = BigDecimal.ZERO;

        if (!gpiItems.isEmpty()) {
            BigDecimal numberOfDonors = BigDecimal.valueOf(gpiItems.size());

            BigDecimal totalDisbursements = gpiItems.stream().map(GPIIndicator5bItem::getActualDisb)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal indicator5bSum = BigDecimal.ZERO;

            for (GPIIndicator5bItem item : gpiItems) {
                indicator5bSum = indicator5bSum.add(item.getActualDisb().multiply(
                        new BigDecimal(item.getMtefYearsWithYes()).divide(numberOfDonors, NiFormula.DIVISION_MC)));
            }

            percentageIndicator5b = totalDisbursements.equals(BigDecimal.ZERO) ? BigDecimal.ZERO
                    : indicator5bSum.scaleByPowerOfTen(2)
                            .divide(totalDisbursements.multiply(BigDecimal.valueOf(3)), NiFormula.DIVISION_MC)
                            .setScale(0, RoundingMode.HALF_UP);
        }

        summaryColumns.put(new GPIReportOutputColumn(GPIReportConstants.COLUMN_INDICATOR_5B),
                percentageIndicator5b + "%");

        return summaryColumns;
    }

}

class GPIIndicator5bItem {
    
    protected static final int YEAR_DIVIDER = 3;
    
    private String donorAgency;
    private int year;
    private boolean year1 = false;
    private boolean year2 = false;
    private boolean year3 = false;
    private BigDecimal actualDisb = BigDecimal.ZERO;
    private boolean fundings = false;

    public GPIIndicator5bItem() {
    }

    public GPIIndicator5bItem(int year) {
        this.year = year;
    }

    public String getDonorAgency() {
        return donorAgency;
    }

    public void setDonorAgency(String donorAgency) {
        this.donorAgency = donorAgency;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public boolean hasYear1() {
        return year1;
    }

    public void setYear1(boolean year1) {
        this.year1 = year1;
    }

    public boolean hasYear2() {
        return year2;
    }

    public void setYear2(boolean year2) {
        this.year2 = year2;
    }

    public boolean hasYear3() {
        return year3;
    }

    public void setYear3(boolean year3) {
        this.year3 = year3;
    }

    public BigDecimal getActualDisb() {
        return actualDisb;
    }

    public void setActualDisb(BigDecimal actualDisb) {
        this.actualDisb = actualDisb;
    }

    public boolean hasMtefs() {
        return year1 || year2 || year3;
    }

    public boolean hasFundings() {
        return fundings;
    }

    public void setFundings(boolean fundings) {
        this.fundings = fundings;
    }

    public boolean isNotEmpty() {
        return hasMtefs() || hasFundings();
    }

    public int getMtefYearsWithYes() {
        int sum = 0;

        if (hasYear1()) {
            sum++;
        }

        if (hasYear2()) {
            sum++;
        }

        if (hasYear3()) {
            sum++;
        }

        return sum;
    }

    public BigDecimal getPercentage() {
        BigDecimal percentageDisb = new BigDecimal(getMtefYearsWithYes())
                .divide(new BigDecimal(YEAR_DIVIDER), NiFormula.DIVISION_MC)
                .multiply(new BigDecimal(GPIReportOutputBuilder.PERCENTAGE_MULTIPLIER))
                .setScale(0, RoundingMode.HALF_UP);

        return percentageDisb;
    }
}
