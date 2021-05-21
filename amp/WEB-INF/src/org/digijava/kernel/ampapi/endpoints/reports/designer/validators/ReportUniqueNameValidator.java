package org.digijava.kernel.ampapi.endpoints.reports.designer.validators;

import org.apache.commons.lang3.StringUtils;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.reports.designer.ReportRequest;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpReports;

import java.util.List;

import static org.digijava.kernel.ampapi.endpoints.reports.designer.ReportDesignerErrors.REPORT_NAME_DUPLICATED;

/**
 * Validates that report name is unique across AMP.
 *
 * @author Viorel Chihai
 */
public class ReportUniqueNameValidator implements ReportValidator {

    private String name;

    private final AmpReports report;

    public ReportUniqueNameValidator(final AmpReports report) {
        this.report = report;
    }

    public boolean isValid(Object value) {
        ReportRequest reportRequest = (ReportRequest) value;
        if (StringUtils.isNotBlank(reportRequest.getName())) {
            boolean nameIsUpdated = report.getAmpReportId() == null ? true
                    : !StringUtils.equalsIgnoreCase(report.getName(), reportRequest.getName());

            if (nameIsUpdated) {
                name = reportRequest.getName();
                String queryStr = "select r FROM " + AmpReports.class.getName() + " r where "
                        + AmpReports.hqlStringForName("r") + "=:reportName";
                List<AmpReports> conflicts = PersistenceManager.getSession()
                        .createQuery(queryStr)
                        .setString("reportName", reportRequest.getName())
                        .list();

                return conflicts.stream()
                        .filter(r -> report.getAmpReportId() == null
                                || !report.getAmpReportId().equals(r.getAmpReportId()))
                        .findAny().isPresent();
            }
        }

        return true;
    }

    public ApiErrorMessage getErrorMessage() {
        return REPORT_NAME_DUPLICATED.withDetails(name);
    }

}