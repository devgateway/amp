package org.digijava.kernel.ampapi.endpoints.reports.designer;

import org.digijava.kernel.ampapi.endpoints.common.AMPTranslatorService;
import org.digijava.kernel.ampapi.endpoints.reports.designer.builder.ReportDesignerBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

import static org.digijava.kernel.ampapi.endpoints.reports.designer.ReportProfile.TAB;
import static org.digijava.kernel.ampapi.endpoints.reports.designer.ReportType.DONOR;

/**
 * Reports service to
 *
 * @author Viorel Chihai
 */
public class ReportDesignerService {

    private final ReportOptionProvider reportOptionProvider;

    public ReportDesignerService() {
        this.reportOptionProvider = new ReportOptionProvider(AMPTranslatorService.INSTANCE);
    }

    public ReportDesigner getReportDesigner(final ReportProfile profile, final ReportType type) {
        return new ReportDesignerBuilder()
                .withProfile(profile)
                .withType(type)
                .withColumns(getColumns(profile, type))
                .withMeasures(getMeasures(profile, type))
                .withOptions(reportOptionProvider.getAvailableOptions(profile, type))
                .build();
    }

    private List<ReportColumn> getColumns(final ReportProfile profile, final ReportType type) {
        return new ArrayList<>();
    }

    private List<ReportMeasure> getMeasures(final ReportProfile profile, final ReportType type) {
        return new ArrayList<>();
    }

    private List<ReportOption> getOptions(final ReportProfile profile, final ReportType type) {
        List<ReportOption> options = new ArrayList<>();

        BiPredicate<ReportProfile, ReportType> isVisible =
                (p, t) -> p.equals(TAB) && t.equals(DONOR);

        options.add(new ReportOption("summary", "Summary Report", "Description 1"));
        options.add(new ReportOption("funding-donor", "Summary Report", "Description 1"));
        options.add(new ReportOption("funding-regional", "Summary Report", "Description 1"));
        options.add(new ReportOption("funding-component", "Summary Report", "Description 1"));
        options.add(new ReportOption("funding-contribution", "Summary Report", "Description 1"));
        options.add(new ReportOption("public-view", "Summary Report", "Description 1"));
        options.add(new ReportOption("workspace-linked", "Summary Report", "Description 1"));
        options.add(new ReportOption("show-pledges", "Summary Report", "Description 1"));
        options.add(new ReportOption("use-filters", "Summary Report", "Description 1"));
        options.add(new ReportOption("empty-funding-columns", "Summary Report", "Description 1"));
        options.add(new ReportOption("split-by-funding", "Summary Report", "Description 1"));
        options.add(new ReportOption("show-original-currency", "Summary Report", "Description 1"));

        return options;
    }

}
