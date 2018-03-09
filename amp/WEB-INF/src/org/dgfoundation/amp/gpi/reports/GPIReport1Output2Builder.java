package org.dgfoundation.amp.gpi.reports;

import static java.util.stream.Collectors.groupingBy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Predicate;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.nireports.formulas.NiFormula;

/**
 * A utility class to transform a GeneratedReport to GPI Report 1 Output 2
 * 
 * @author Viorel Chihai
 *
 */
public class GPIReport1Output2Builder extends GPIReportOutputBuilder {

    public GPIReport1Output2Builder() {
        addColumn(new GPIReportOutputColumn(GPIReportConstants.COLUMN_YEAR));
        addColumn(new GPIReportOutputColumn(ColumnConstants.ACTUAL_APPROVAL_DATE));
        addColumn(new GPIReportOutputColumn(GPIReportConstants.GPI_1_Q1));
        addColumn(new GPIReportOutputColumn(GPIReportConstants.GPI_1_Q2));
        addColumn(new GPIReportOutputColumn(GPIReportConstants.GPI_1_Q3));
        addColumn(new GPIReportOutputColumn(GPIReportConstants.GPI_1_Q4));
    }

    /**
     * build the headers of the report
     * 
     * @param generatedReport
     * @return
     */
    @Override
    protected List<GPIReportOutputColumn> buildHeaders(GeneratedReport generatedReport) {
        List<GPIReportOutputColumn> headers = new ArrayList<>();

        headers.add(getColumns().get(GPIReportConstants.COLUMN_YEAR));
        headers.add(getColumns().get(GPIReportConstants.GPI_1_Q1));
        headers.add(getColumns().get(GPIReportConstants.GPI_1_Q2));
        headers.add(getColumns().get(GPIReportConstants.GPI_1_Q3));
        headers.add(getColumns().get(GPIReportConstants.GPI_1_Q4));

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
        List<Map<GPIReportOutputColumn, String>> contents = new ArrayList<>();

        GpiReport1Output2Visitor visitor = new GpiReport1Output2Visitor();
        generatedReport.reportContents.accept(visitor);

        Map<String, List<GPIOutput2Item>> gpiElements = visitor.getGpiItems().stream()
                .collect(groupingBy(gpiItem -> String.valueOf(
                        GPIReportUtils.getYearOfCustomCalendar(generatedReport.spec, gpiItem.getApprovalDate()))));

        gpiElements.entrySet().stream().forEach(gpiElement -> contents.add(generateYearRow(gpiElement)));

        Comparator<Map<GPIReportOutputColumn, String>> byYear = (Map<GPIReportOutputColumn, String> o1,
                Map<GPIReportOutputColumn, String> o2) -> o2.get(getYearColumn()).compareTo(o1.get(getYearColumn()));

        contents.sort(byYear);

        return contents;
    }

    /**
     * @param gpiElement
     * @return
     */
    private Map<GPIReportOutputColumn, String> generateYearRow(Entry<String, List<GPIOutput2Item>> gpiElement) {
        String year = gpiElement.getKey();
        List<GPIOutput2Item> items = gpiElement.getValue();

        BigDecimal cnt = BigDecimal.valueOf(gpiElement.getValue().size());
        Map<GPIReportOutputColumn, String> row = new HashMap<>();
        row.put(getYearColumn(), year);

        BigDecimal q6Cnt = getCountOfFilteredElements(items, GPIOutput2Item::getQ6);
        
        BigDecimal q8Sum = getSumOfFields(items, 
                gpiItem -> gpiItem.getQ8().divide(gpiItem.getQ7(), NiFormula.DIVISION_MC));
        BigDecimal q9Sum = getSumOfFields(items, 
                gpiItem -> gpiItem.getQ9().divide(gpiItem.getQ7(), NiFormula.DIVISION_MC));
        
        BigDecimal q10Cnt = getCountOfFilteredElements(items, GPIOutput2Item::getQ10);

        row.put(getColumns().get(GPIReportConstants.GPI_1_Q1), getPercentage(q6Cnt, cnt) + "%");
        row.put(getColumns().get(GPIReportConstants.GPI_1_Q2), getPercentage(q8Sum, cnt) + "%");
        row.put(getColumns().get(GPIReportConstants.GPI_1_Q3), getPercentage(q9Sum, cnt) + "%");
        row.put(getColumns().get(GPIReportConstants.GPI_1_Q4), getPercentage(q10Cnt, cnt) + "%");

        return row;
    }

    /**
     * @param cnt
     * @param q6Cnt
     * @return
     */
    protected BigDecimal getPercentage(BigDecimal a, BigDecimal b) {
        return a.divide(b, NiFormula.DIVISION_MC).scaleByPowerOfTen(2).setScale(0, RoundingMode.HALF_UP);
    }

    /**
     * @param items
     * @return
     */
    private BigDecimal getCountOfFilteredElements(List<GPIOutput2Item> items, Predicate<GPIOutput2Item> p) {
        return BigDecimal.valueOf(items.stream().filter(p).count());
    }

    /**
     * @param items
     * @return
     */
    private BigDecimal getSumOfFields(List<GPIOutput2Item> items, Function<GPIOutput2Item, BigDecimal> func) {
        return items.stream().map(func).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * build the contents of the report
     * 
     * @param generatedReport
     * @return
     */
    @Override
    protected Map<GPIReportOutputColumn, String> getReportSummary(GeneratedReport generatedReport) {
        return null;
    }
}
