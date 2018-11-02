package org.dgfoundation.amp.gpi.reports;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.nireports.formulas.NiFormula;
import org.digijava.kernel.persistence.PersistenceManager;
import org.hibernate.jdbc.Work;

/**
 * A utility class to transform a GeneratedReport to GPI Report 1 Output 1
 * 
 * @author Viorel Chihai
 *
 */
public class GPIReport1Output1Builder extends GPIReportOutputBuilder {

    public Map<Long, String> orgToGroupName = new HashMap<>();

    public GPIReport1Output1Builder() {
        addColumn(new GPIReportOutputColumn(ColumnConstants.PROJECT_TITLE));
        addColumn(new GPIReportOutputColumn(GPIReportConstants.GPI_1_Q1, GPIReportConstants.GPI_1_Q1,
                GPIReportConstants.REPORT_1_OUTPUT_1_TOOLTIP.get(GPIReportConstants.GPI_1_Q1)));
        addColumn(new GPIReportOutputColumn(GPIReportConstants.GPI_1_Q2, GPIReportConstants.GPI_1_Q2,
                GPIReportConstants.REPORT_1_OUTPUT_1_TOOLTIP.get(GPIReportConstants.GPI_1_Q2)));
        addColumn(new GPIReportOutputColumn(GPIReportConstants.GPI_1_Q3, GPIReportConstants.GPI_1_Q3,
                GPIReportConstants.REPORT_1_OUTPUT_1_TOOLTIP.get(GPIReportConstants.GPI_1_Q3)));
        addColumn(new GPIReportOutputColumn(GPIReportConstants.GPI_1_Q4, GPIReportConstants.GPI_1_Q4,
                GPIReportConstants.REPORT_1_OUTPUT_1_TOOLTIP.get(GPIReportConstants.GPI_1_Q4)));
        addColumn(new GPIReportOutputColumn(GPIReportConstants.GPI_1_Q5, GPIReportConstants.GPI_1_Q5,
                GPIReportConstants.REPORT_1_OUTPUT_1_TOOLTIP.get(GPIReportConstants.GPI_1_Q5)));
        addColumn(new GPIReportOutputColumn(ColumnConstants.GPI_1_Q6,
                GPIReportConstants.REPORT_1_OUTPUT_1_TOOLTIP.get(ColumnConstants.GPI_1_Q6)));
        addColumn(new GPIReportOutputColumn(ColumnConstants.GPI_1_Q6_DESCRIPTION));
        addColumn(new GPIReportOutputColumn(ColumnConstants.GPI_1_Q7,
                GPIReportConstants.REPORT_1_OUTPUT_1_TOOLTIP.get(ColumnConstants.GPI_1_Q7)));
        addColumn(new GPIReportOutputColumn(ColumnConstants.GPI_1_Q8,
                GPIReportConstants.REPORT_1_OUTPUT_1_TOOLTIP.get(ColumnConstants.GPI_1_Q8)));
        addColumn(new GPIReportOutputColumn(ColumnConstants.GPI_1_Q9,
                GPIReportConstants.REPORT_1_OUTPUT_1_TOOLTIP.get(ColumnConstants.GPI_1_Q9)));
        addColumn(new GPIReportOutputColumn(ColumnConstants.GPI_1_Q10,
                GPIReportConstants.REPORT_1_OUTPUT_1_TOOLTIP.get(ColumnConstants.GPI_1_Q10)));
        addColumn(new GPIReportOutputColumn(ColumnConstants.GPI_1_Q10_DESCRIPTION));

        addColumn(new GPIReportOutputColumn(GPIReportConstants.COLUMN_EXTENT_OF_USE_OF_COUNTRY_RESULT,
                GPIReportConstants.REPORT_1_OUTPUT_1_TOOLTIP
                        .get(GPIReportConstants.COLUMN_EXTENT_OF_USE_OF_COUNTRY_RESULT)));
        addColumn(new GPIReportOutputColumn(GPIReportConstants.COLUMN_EXTENT_OF_USE_OF_GOV_SOURCES,
                GPIReportConstants.REPORT_1_OUTPUT_1_TOOLTIP
                        .get(GPIReportConstants.COLUMN_EXTENT_OF_USE_OF_GOV_SOURCES)));
        addColumn(new GPIReportOutputColumn(GPIReportConstants.COLUMN_REMARK));
        addColumn(new GPIReportOutputColumn(ColumnConstants.ACTIVITY_ID));
        addColumn(new GPIReportOutputColumn(ColumnConstants.DONOR_ID));
        addColumn(new GPIReportOutputColumn(ColumnConstants.DONOR_AGENCY));
        addColumn(new GPIReportOutputColumn(GPIReportConstants.COLUMN_YEAR));

        orgToGroupName = fetchOrgToGroupName();
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

        headers.add(getColumns().get(ColumnConstants.PROJECT_TITLE));
        headers.add(getColumns().get(GPIReportConstants.GPI_1_Q1));
        headers.add(getColumns().get(GPIReportConstants.GPI_1_Q2));
        headers.add(getColumns().get(GPIReportConstants.GPI_1_Q3));
        headers.add(getColumns().get(GPIReportConstants.GPI_1_Q4));
        headers.add(getColumns().get(GPIReportConstants.GPI_1_Q5));
        headers.add(getColumns().get(ColumnConstants.GPI_1_Q6));
        headers.add(getColumns().get(ColumnConstants.GPI_1_Q6_DESCRIPTION));
        headers.add(getColumns().get(ColumnConstants.GPI_1_Q7));
        headers.add(getColumns().get(ColumnConstants.GPI_1_Q8));
        headers.add(getColumns().get(ColumnConstants.GPI_1_Q9));
        headers.add(getColumns().get(ColumnConstants.GPI_1_Q10));
        headers.add(getColumns().get(ColumnConstants.GPI_1_Q10_DESCRIPTION));
        headers.add(getColumns().get(GPIReportConstants.COLUMN_EXTENT_OF_USE_OF_COUNTRY_RESULT));
        headers.add(getColumns().get(GPIReportConstants.COLUMN_EXTENT_OF_USE_OF_GOV_SOURCES));

        return headers;
    }

    private Map<Long, String> fetchOrgToGroupName() {
        String query = "SELECT o.amp_org_id as orgId, gr.org_grp_name AS groupName " + "FROM amp_organisation o "
                + "JOIN amp_org_group gr ON o.org_grp_id = gr.amp_org_grp_id";

        Map<Long, String> orgToGroupName = new HashMap<>();
        PersistenceManager.getSession().doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                try (RsInfo rs = SQLUtils.rawRunQuery(connection, query, null)) {
                    while (rs.rs.next()) {

                        Long orgId = rs.rs.getLong("orgId");
                        String orgGroup = rs.rs.getString("groupName");

                        orgToGroupName.put(orgId, orgGroup);
                    }
                }
            }
        });

        return orgToGroupName;
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

        List<GPIOutput1Item> gpiItems = getFilteredGPIItems(generatedReport);
        gpiItems.forEach(gpiItem -> contents.add(generateRow(generatedReport, gpiItem)));

        GPIReportOutputColumn approvalColumn = getColumns().get(ColumnConstants.ACTUAL_APPROVAL_DATE);
        Comparator<Map<GPIReportOutputColumn, String>> byApprovalDate = (Map<GPIReportOutputColumn, String> o1,
                Map<GPIReportOutputColumn, String> o2) -> o2.get(approvalColumn) == null ? -1
                        : o2.get(approvalColumn).compareTo(o1.get(approvalColumn));

        contents.sort(byApprovalDate);

        return contents;
    }

    /**
     * @param generatedReport
     * @return
     */
    private List<GPIOutput1Item> getFilteredGPIItems(GeneratedReport generatedReport) {
        GpiReport1Output1Visitor visitor = new GpiReport1Output1Visitor();
        generatedReport.reportContents.accept(visitor);

        FilterRule donorAgencyRule = GPIReportUtils.getFilterRule(originalFormParams, ColumnConstants.DONOR_AGENCY);
        Set<String> filteredDonors = donorAgencyRule == null ? new HashSet<>()
                : donorAgencyRule.values.stream().collect(Collectors.toSet());

        List<GPIOutput1Item> gpiItems = visitor.getGpiItems().stream()
                .filter(g -> filteredDonors.isEmpty() || filteredDonors.contains(String.valueOf(g.getDonorId())))
                .collect(Collectors.toList());
        return gpiItems;
    }

    /**
     * build the contents of the report
     * 
     * @param generatedReport
     * @return
     */
    @Override
    protected Map<GPIReportOutputColumn, String> getReportSummary(GeneratedReport generatedReport) {
        List<GPIOutput1Item> gpiItems = getFilteredGPIItems(generatedReport);

        return generateSummary(gpiItems);
    }

    /**
     * @param generatedReport
     * @param gpiElement
     * @return
     */
    private Map<GPIReportOutputColumn, String> generateRow(GeneratedReport generatedReport, GPIOutput1Item gpiElement) {

        Map<GPIReportOutputColumn, String> row = new HashMap<>();

        row.put(getColumns().get(GPIReportConstants.COLUMN_YEAR), String.valueOf(
                        GPIReportUtils.getYearOfCustomCalendar(generatedReport.spec, gpiElement.getApprovalDate())));
        row.put(getColumns().get(ColumnConstants.DONOR_AGENCY), gpiElement.getDonorAgency());
        row.put(getColumns().get(ColumnConstants.PROJECT_TITLE), gpiElement.getProjectTitle());
        row.put(getColumns().get(GPIReportConstants.GPI_1_Q1),
                formatAmount(generatedReport, gpiElement.getActCommitments(), true));
        row.put(getColumns().get(GPIReportConstants.GPI_1_Q2), gpiElement.getApprovalDateAsString());
        row.put(getColumns().get(GPIReportConstants.GPI_1_Q3),
                String.join("###", gpiElement.getFinancingInstruments().values()));
        row.put(getColumns().get(GPIReportConstants.GPI_1_Q4),
                String.join("###", getImplAgencyWithGroupName(gpiElement.getImplementingAgencies())));
        row.put(getColumns().get(GPIReportConstants.GPI_1_Q5),
                String.join("###", gpiElement.getPrimarySectors().values()));
        row.put(getColumns().get(ColumnConstants.GPI_1_Q6), gpiElement.getQ6() ? "Yes" : "No");
        row.put(getColumns().get(ColumnConstants.GPI_1_Q6_DESCRIPTION), gpiElement.getQ6Description());
        row.put(getColumns().get(ColumnConstants.GPI_1_Q7), gpiElement.getQ7().toString());
        row.put(getColumns().get(ColumnConstants.GPI_1_Q8), gpiElement.getQ8().toString());
        row.put(getColumns().get(ColumnConstants.GPI_1_Q9), gpiElement.getQ9().toString());
        row.put(getColumns().get(ColumnConstants.GPI_1_Q10), gpiElement.getQ10() ? "Yes" : "No");
        row.put(getColumns().get(ColumnConstants.GPI_1_Q10_DESCRIPTION), gpiElement.getQ10Description());
        row.put(getColumns().get(GPIReportConstants.COLUMN_EXTENT_OF_USE_OF_COUNTRY_RESULT),
                getPercentage(gpiElement.getQ8(), gpiElement.getQ7()) + "%");
        row.put(getColumns().get(GPIReportConstants.COLUMN_EXTENT_OF_USE_OF_GOV_SOURCES),
                getPercentage(gpiElement.getQ9(), gpiElement.getQ7()) + "%");
        row.put(getColumns().get(ColumnConstants.ACTIVITY_ID), String.valueOf(gpiElement.getActivityId()));
        row.put(getColumns().get(ColumnConstants.DONOR_ID), String.valueOf(gpiElement.getDonorId()));

        return row;
    }

    /**
     * @param gpiElements
     * @return
     */
    private Map<GPIReportOutputColumn, String> generateSummary(List<GPIOutput1Item> gpiElements) {
        Map<GPIReportOutputColumn, String> row = new HashMap<>();
        row.put(getColumns().get(GPIReportConstants.COLUMN_REMARK), getRemarkEndpointURL());

        if (!gpiElements.isEmpty()) {

            BigDecimal cnt = BigDecimal.valueOf(gpiElements.size());

            BigDecimal q6Cnt = getCountOfFilteredElements(gpiElements, GPIOutput1Item::getQ6);

            BigDecimal q8Sum = getSumOfFields(gpiElements,
                    gpiItem -> gpiItem.getQ8().divide(gpiItem.getQ7(), NiFormula.DIVISION_MC));
            BigDecimal q9Sum = getSumOfFields(gpiElements,
                    gpiItem -> gpiItem.getQ9().divide(gpiItem.getQ7(), NiFormula.DIVISION_MC));

            BigDecimal q10Cnt = getCountOfFilteredElements(gpiElements, GPIOutput1Item::getQ10);

            row.put(getColumns().get(GPIReportConstants.GPI_1_Q1), getPercentage(q6Cnt, cnt) + "%");
            row.put(getColumns().get(GPIReportConstants.GPI_1_Q2), getPercentage(q8Sum, cnt) + "%");
            row.put(getColumns().get(GPIReportConstants.GPI_1_Q3), getPercentage(q9Sum, cnt) + "%");
            row.put(getColumns().get(GPIReportConstants.GPI_1_Q4), getPercentage(q10Cnt, cnt) + "%");

        }

        return row;
    }

    /**
     * @param items
     * @return
     */
    private BigDecimal getCountOfFilteredElements(List<GPIOutput1Item> items, Predicate<GPIOutput1Item> p) {
        return BigDecimal.valueOf(items.stream().filter(p).count());
    }

    /**
     * @param items
     * @return
     */
    private BigDecimal getSumOfFields(List<GPIOutput1Item> items, Function<GPIOutput1Item, BigDecimal> func) {
        return items.stream().map(func).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<String> getImplAgencyWithGroupName(Map<Long, String> implementingAgencies) {
        List<String> implAgencyGroup = new ArrayList<>();

        implementingAgencies.entrySet().forEach(e -> {
            implAgencyGroup.add(String.format("%s - %s", e.getValue(), orgToGroupName.get(e.getKey())));
        });

        return implAgencyGroup;
    }

    /**
     * Generate the remark endpoint url
     * 
     * @return
     */
    private String getRemarkEndpointURL() {
        String remarkEndpoint = GPIReportConstants.GPI_REMARK_ENDPOINT
                + "?indicatorCode=%s&donorId=%s&donorType=%s&from=%s&to=%s";

        String donorType = GPIReportConstants.HIERARCHY_DONOR_AGENCY;

        FilterRule donorAgencyRule = GPIReportUtils.getFilterRule(originalFormParams, ColumnConstants.DONOR_AGENCY);
        List<String> filteredDonors = donorAgencyRule == null ? new ArrayList<>()
                : donorAgencyRule.values.stream().collect(Collectors.toList());

        String ids = String.join(",", filteredDonors);

        FilterRule approvalDateRule = GPIReportUtils.getFilterRule(originalFormParams,
                ColumnConstants.ACTUAL_APPROVAL_DATE);

        String min = approvalDateRule == null ? "0" : approvalDateRule.min != null ? approvalDateRule.min : "0";
        String max = approvalDateRule == null ? "0" : approvalDateRule.max != null ? approvalDateRule.max : "0";

        return String.format(remarkEndpoint, GPIReportConstants.REPORT_1, ids, donorType, min, max);
    }
}
