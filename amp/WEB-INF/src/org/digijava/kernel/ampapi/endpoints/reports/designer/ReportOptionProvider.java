package org.digijava.kernel.ampapi.endpoints.reports.designer;

import com.google.common.collect.ImmutableList;
import org.digijava.kernel.ampapi.endpoints.common.TranslatorService;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.TeamUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.digijava.kernel.ampapi.endpoints.reports.designer.ReportManager.PUBLIC_REPORT_GENERATOR_MODULE_NAME;

/**
 *  Enumerate all options of report designer depending on report profile (tab/report), report type
 *  and team member access type
 *
 * @author Viorel Chihai
 */
public class ReportOptionProvider {

    public static final List<ReportOptionConfiguration> REPORT_PROFILE_OPTIONS = ImmutableList.of(
        new ReportOptionConfiguration("funding-donor", "Donor Report (Donor Funding)", null,
                "Donor Report", "Report Types"),
        new ReportOptionConfiguration("funding-component", "Regional Report (Regional Funding)", null,
                "Regional Report", "Report Types"),
        new ReportOptionConfiguration("funding-contribution", "Component Report (Component Funding)", null,
                "Component Report", "Report Types"),
        new ReportOptionConfiguration("funding-component", "Contribution Report (Activity Contributions)", null,
                "Contribution Report", "Report Types"),
        new ReportOptionConfiguration("funding-pledges", "Pledges Report"),
        new ReportOptionConfiguration("summary-report", "Summary Report"),
        new ReportOptionConfiguration("annual-report", "Annual Report"),
        new ReportOptionConfiguration("quarterly-report", "Quarterly Report"),
        new ReportOptionConfiguration("monthly-report", "Monthly Report"),
        new ReportOptionConfiguration("totals-only", "Totals Only"),
        new ReportOptionConfiguration("show-pledges", "Also show pledges",
            "Checking this box will lead to pledges being included in this report, with their commitment gap "
                    + "being displayed as Actual Commitments",
            "Also show pledges checkbox", "Report and Tab Options"),
        new ReportOptionConfiguration("empty-funding-columns",
                "Allow empty funding columns for year, quarter and month"),
            new ReportOptionConfiguration("reportCategory", "Please select a category from below", null,
                    "Reports classification", "Report Generator")
    );

    public static final List<ReportOptionConfiguration> TAB_PROFILE_OPTIONS = ImmutableList.of(
            new ReportOptionConfiguration("funding-donor", "Donor Tab (Donor Funding)", null,
                    "Donor Report", "Report Types"),
            new ReportOptionConfiguration("funding-component", "Regional Tab (Regional Funding)", null,
                    "Regional Report", "Report Types"),
            new ReportOptionConfiguration("funding-contribution", "Component Tab (Component Funding)", null,
                    "Component Report", "Report Types"),
            new ReportOptionConfiguration("funding-component", "Contribution Tab (Activity Contributions)", null,
                    "Contribution Report", "Report Types"),
            new ReportOptionConfiguration("totals-only", "Totals Only")
    );

    public static final List<ReportOptionConfiguration> MANAGEMENT_OR_REPORT_PROFILE_OPTIONS = ImmutableList.of(
        new ReportOptionConfiguration("split-by-funding", "Split by funding"),
        new ReportOptionConfiguration("show-original-currency", "Show Original reporting currencies",
                "This feature will show each transaction in the currency originally reported in the AMP.")
    );

    public static final List<ReportOptionConfiguration> MANAGEMENT_OPTIONS = ImmutableList.of(
       new ReportOptionConfiguration("public-view", "Make public", null,
               "Public View Checkbox", "Report and Tab Options"),
       new ReportOptionConfiguration("workspace-linked", "Link public view with creating workspace",
                "Leaving unchecked means that this report will render ALL activities from ALL management workspaces in"
                        + " the database. Putting the checkbox ON will only render the activities "
                        + "from the creating workspace",
                "Public View Checkbox", "Report and Tab Options")
    );

    public static final List<ReportOptionConfiguration> ALL_PROFILE_OPTIONS = ImmutableList.of(
            new ReportOptionConfiguration("use-filters", "Use Above Filters")
    );

    public static final List<String> PLEDGES_OPTION_NAMES = ImmutableList.of("funding-pledges", "summary-report",
            "annual-report", "totals-only", "empty-funding-columns");

    public static final List<String> REPORT_OPTION_NAMES = ImmutableList.of("funding-donor", "funding-component",
            "funding-contribution", "funding-component", "summary-report", "annual-report", "quarterly-report",
            "monthly-report", "totals-only", "show-pledges", "empty-funding-columns");

    private TranslatorService translatorService;

    public ReportOptionProvider(final TranslatorService translatorService) {
        this.translatorService = translatorService;
    }

    public List<ReportOption> getOptions(final ReportProfile reportProfile, final ReportType reportType) {
        List<ReportOption> reportOptions = reportProfile.isReport() ? getReportOptions(reportType) : getTabOptions();

        Boolean publicReportEnabled = FeaturesUtil.isVisibleModule(PUBLIC_REPORT_GENERATOR_MODULE_NAME);
        ReportOption option = new ReportOption("fm-is-public-report-enabled", "", null);
        option.setVisible(publicReportEnabled);
        reportOptions.add(option);
        return reportOptions;
    }

    private List<ReportOption> getReportOptions(final ReportType reportType) {
        List<ReportOption> options = new ArrayList<>();

        options.addAll(REPORT_PROFILE_OPTIONS.stream()
                .filter(r -> isOptionVisibleForReportType(r, reportType))
                .map(r -> getOptionFromConfiguration(r))
                .collect(Collectors.toList()));

        options.addAll(getManagementOrReportProfileOptions());

        if (isCurrentMemberManager()) {
            options.addAll(getManagementOptions());
        }
        options.addAll(getAllProfileOptions());

        return options;
    }

    private List<ReportOption> getTabOptions() {
        List<ReportOption> options = new ArrayList<>();

        options.addAll(TAB_PROFILE_OPTIONS.stream()
                .map(r -> getOptionFromConfiguration(r))
                .collect(Collectors.toList()));

        if (isCurrentMemberManager()) {
            options.addAll(getManagementOptions());
            options.addAll(getManagementOrReportProfileOptions());
        }
        options.addAll(getAllProfileOptions());

        return options;
    }

    private List<ReportOption> getManagementOrReportProfileOptions() {
        return MANAGEMENT_OR_REPORT_PROFILE_OPTIONS.stream()
                .map(r -> getOptionFromConfiguration(r))
                .collect(Collectors.toList());
    }

    private List<ReportOption> getManagementOptions() {
        return MANAGEMENT_OPTIONS.stream()
                .map(r -> getOptionFromConfiguration(r))
                .collect(Collectors.toList());
    }

    private List<ReportOption> getAllProfileOptions() {
        return ALL_PROFILE_OPTIONS.stream()
                .map(r -> getOptionFromConfiguration(r))
                .collect(Collectors.toList());
    }

    private boolean isOptionVisibleForReportType(final ReportOptionConfiguration option, final ReportType reportType) {
        if (reportType.isPledge()) {
            return PLEDGES_OPTION_NAMES.contains(option.getName());
        }

        return REPORT_OPTION_NAMES.contains(option.getName());
    }

    private ReportOption getOptionFromConfiguration(final ReportOptionConfiguration option) {
        ReportOption reportOption = new ReportOption(option.getName(),
                translatorService.translateText(option.getLabel()),
                translatorService.translateText(option.getDescription()));

        if (option.getModuleFmPath() != null && option.getFeatureFmPath() != null) {
            reportOption.setVisible(FeaturesUtil.isVisibleFeature(option.getModuleFmPath(), option.getFeatureFmPath()));
        }

        return reportOption;
    }

    private boolean isCurrentMemberManager() {
        TeamMember currentMember = TeamUtil.getCurrentMember();
        if (currentMember != null) {
            return currentMember.getTeamHead() && currentMember.getTeamAccessType().equals("Management");
        }

        return false;
    }

    private static class ReportOptionConfiguration {

        private final String name;
        private final String label;
        private final String description;
        private final String featureFmPath;
        private final String moduleFmPath;

        ReportOptionConfiguration(final String name, final String label, final String description,
                                         final String featureFmPath, final String moduleFmPath) {
            this.name = name;
            this.label = label;
            this.description = description;
            this.featureFmPath = featureFmPath;
            this.moduleFmPath = moduleFmPath;
        }

        ReportOptionConfiguration(final String name, final String label) {
            this(name, label, null, null, null);
        }

        ReportOptionConfiguration(final String name, final String label, final String description) {
            this(name, label, description, null, null);
        }

        public String getName() {
            return name;
        }

        public String getLabel() {
            return label;
        }

        public String getDescription() {
            return description;
        }

        public String getModuleFmPath() {
            return moduleFmPath;
        }

        public String getFeatureFmPath() {
            return featureFmPath;
        }
    }
}
