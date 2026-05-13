package org.dgfoundation.amp.nireports.amp;

import org.dgfoundation.amp.newreports.*;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.output.CellFormatter;
import org.dgfoundation.amp.nireports.output.NiReportOutputBuilder;
import org.dgfoundation.amp.nireports.output.NiReportRunResult;
import org.dgfoundation.amp.nireports.output.NiReportsFormatter;
import org.dgfoundation.amp.nireports.runtime.CellColumn;
import org.dgfoundation.amp.nireports.runtime.Column;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.common.util.DateTimeUtil;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * part of the (NiReportsCore, AmpReportsSchema, Reports API) intersection - formats the output according to the AMP-specific rules.
 * Implements translations and GlobalSettings compliance on top of the extensibility offered by {@link NiReportsFormatter}
 * @author Dolghier Constantin
 *
 */
public class AmpNiReportsFormatter extends NiReportsFormatter {
    
    public AmpNiReportsFormatter(ReportSpecification spec, NiReportRunResult runResult, OutputSettings outputSettings) {
        super(spec, runResult, 
            new CellFormatter(spec.getSettings(), FormatHelper.getDefaultFormat(), DateTimeUtil.getGlobalPattern(), 
            TranslatorWorker::translateText,
            outputSettings == null ? defaultOutputSettings(spec) : outputSettings, runResult.calendar));
    }

    /**
     * builds an {@link OutputSettings} instance to use in case the user hasn't supplied one    
     * @param spec
     * @return
     */
    protected static OutputSettings defaultOutputSettings(ReportSpecification spec) {
        LinkedHashSet<ReportColumn> columns = new LinkedHashSet<>(spec.getColumns());
        columns.removeAll(spec.getHierarchies());
        columns.removeAll(spec.getInvisibleHierarchies());
        Set<String> idsValuesColumns = columns.stream().map(ReportColumn::getColumnName).collect(Collectors.toSet());
        
        return new OutputSettings(idsValuesColumns);
    }
    
    public static NiReportOutputBuilder<GeneratedReport> asOutputBuilder(OutputSettings outputSettings) {
        return (ReportSpecification spec, NiReportRunResult runResult) -> new AmpNiReportsFormatter(spec, runResult, outputSettings).format();
    }

    @Override
    protected ReportCell buildTabsUndefinedCell(CellColumn cc) {
        return new TextCell(String.format("(%s %s)", TranslatorWorker.translateText(cc.name), TranslatorWorker.translateText("Unspecified")));
    }
    
    @Override
    protected ReportOutputColumn buildReportOutputColumn(Column niCol) {
        String trnName = getNameTranslation(niCol);
        String trnDescription = getDescriptionTranslation(niCol);
        //logger.error(String.format("transforming [%s] to [%s, %s]", niCol.getHierName(), trnName, trnDescription));
        return new ReportOutputColumn(trnName, niColumnToROC.get(niCol.getParent()), niCol.name, trnDescription, buildEmptyCell(niCol), null);
    }
    
    protected String getDescriptionTranslation(Column niCol) {
        String description = null;
        if (niCol.splitCell != null) {
            if (niCol.splitCell.entityType.equals(NiReportsEngine.PSEUDOCOLUMN_MEASURE))
                description = AmpReportsSchema.measureDescriptions.get(niCol.splitCell.info.getValue());
            
            if (niCol.splitCell.entityType.equals(NiReportsEngine.PSEUDOCOLUMN_COLUMN))
                    description = AmpReportsSchema.columnDescriptions.get(niCol.splitCell.info.getValue());
        }
        return description == null ? null : TranslatorWorker.translateText(description);
    }
    
    protected String getNameTranslation(Column niCol) {
        if (niCol.splitCell != null && NiReportsEngine.PSEUDOCOLUMN_YEAR.equals(niCol.splitCell.entityType)) {
            return niCol.name;
        }
        return niCol.label.toString();
    }
}
