package org.digijava.kernel.ampapi.endpoints.ndd;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.*;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpTheme;

import java.util.Set;

public class DashboardService {

    // TODO: add params for filter widget.
    public static GeneratedReport generateDirectIndirectReport() {
        ReportSpecificationImpl spec = new ReportSpecificationImpl("DirectIndirect", ArConstants.DONOR_TYPE);
        spec.setSummaryReport(true);
        spec.setGroupingCriteria(GroupingCriteria.GROUPING_TOTALS_ONLY);
        spec.setEmptyOutputForUnspecifiedData(false);

        AmpTheme directProgram = NDDService.getSrcProgramRoot();
        Set<AmpActivityProgramSettings> programSettings = directProgram.getProgramSettings();
        if (programSettings == null || programSettings.size() != 1) {
            throw new RuntimeException("Cant determine the first column of the report.");
        }
        AmpActivityProgramSettings singleProgramSetting = ((AmpActivityProgramSettings) programSettings.toArray()[0]);
        if (singleProgramSetting.getName().equalsIgnoreCase(ColumnConstants.PRIMARY_PROGRAM)) {
            spec.addColumn(new ReportColumn(ColumnConstants.PRIMARY_PROGRAM_LEVEL_3));
        } else if (singleProgramSetting.getName().equalsIgnoreCase(ColumnConstants.SECONDARY_PROGRAM)) {
            spec.addColumn(new ReportColumn(ColumnConstants.SECONDARY_PROGRAM_LEVEL_3));
        } else if (singleProgramSetting.getName().equalsIgnoreCase(ColumnConstants.TERTIARY_PROGRAM)) {
            spec.addColumn(new ReportColumn(ColumnConstants.TERTIARY_PROGRAM_LEVEL_3));
        } else if (singleProgramSetting.getName().equalsIgnoreCase(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES)) {
            spec.addColumn(new ReportColumn(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_3));
        }
        spec.setHierarchies(spec.getColumns());

        // TODO: add param for the amount (commitment, disbursement, etc).
        spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_COMMITMENTS));
        GeneratedReport report = EndpointUtils.runReport(spec);
        return report;
    }
}
