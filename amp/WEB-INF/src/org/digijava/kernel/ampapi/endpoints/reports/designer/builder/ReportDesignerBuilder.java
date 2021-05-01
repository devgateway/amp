package org.digijava.kernel.ampapi.endpoints.reports.designer.builder;

import org.digijava.kernel.ampapi.endpoints.reports.designer.ReportColumn;
import org.digijava.kernel.ampapi.endpoints.reports.designer.ReportDesigner;
import org.digijava.kernel.ampapi.endpoints.reports.designer.ReportMeasure;
import org.digijava.kernel.ampapi.endpoints.reports.designer.ReportOption;
import org.digijava.kernel.ampapi.endpoints.reports.designer.ReportProfile;
import org.digijava.kernel.ampapi.endpoints.reports.designer.ReportType;

import java.util.List;

/**
 * @author Viorel Chihai
 */
public class ReportDesignerBuilder {

    private final ReportDesigner reportDesigner;

    public ReportDesignerBuilder() {
        this.reportDesigner = new ReportDesigner();
    }

    public ReportDesignerBuilder withProfile(final ReportProfile profile) {
        reportDesigner.setProfile(profile);

        return this;
    }

    public ReportDesignerBuilder withType(final ReportType type) {
        reportDesigner.setType(type);

        return this;
    }

    public ReportDesignerBuilder withColumns(final List<ReportColumn> columns) {
        reportDesigner.setColumns(columns);

        return this;
    }

    public ReportDesignerBuilder withMeasures(final List<ReportMeasure> measures) {
        reportDesigner.setMeasures(measures);

        return this;
    }

    public ReportDesignerBuilder withOptions(final List<ReportOption> options) {
        reportDesigner.setOptions(options);

        return this;
    }

    public ReportDesigner build() {
        return reportDesigner;
    }
}
