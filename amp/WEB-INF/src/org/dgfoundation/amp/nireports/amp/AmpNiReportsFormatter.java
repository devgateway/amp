package org.dgfoundation.amp.nireports.amp;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Supplier;

import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.amp.OutputSettings;
import org.dgfoundation.amp.nireports.output.NiReportOutputBuilder;
import org.dgfoundation.amp.nireports.output.NiReportRunResult;
import org.dgfoundation.amp.nireports.output.NiReportsFormatter;
import org.dgfoundation.amp.nireports.runtime.Column;
import org.dgfoundation.amp.nireports.output.CellFormatter;
import org.digijava.kernel.ampapi.mondrian.util.MoConstants;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.helper.FormatHelper;

/**
 * part of the (NiReportsCore, AmpReportsSchema, Reports API) intersection - formats the output according to the AMP-specific rules
 * @author Dolghier Constantin
 *
 */
public class AmpNiReportsFormatter extends NiReportsFormatter {
	
	public AmpNiReportsFormatter(ReportSpecification spec, NiReportRunResult runResult, 
			Supplier<ReportAreaImpl> reportAreaSupplier, OutputSettings outputSettings) {
		super(spec, runResult, reportAreaSupplier,
			new CellFormatter(spec.getSettings(), FormatHelper.getDefaultFormat(), MoConstants.DATE_DISPLAY_FORMAT, 
				TranslatorWorker::translateText,
				outputSettings == null ? defaultOutputSettings(spec) : outputSettings));
	}

	/**
	 * builds an {@link OutputSettings} instance to use in case the user hasn't supplied one 	
	 * @param spec
	 * @return
	 */
	protected static OutputSettings defaultOutputSettings(ReportSpecification spec) {
		LinkedHashSet<ReportColumn> columns = new LinkedHashSet<>(spec.getColumns());
		columns.removeAll(spec.getHierarchies());
		Set<String> idsValuesColumns = columns.isEmpty() ? Collections.emptySet() : new HashSet<>(Arrays.asList(columns.iterator().next().getColumnName()));
		return new OutputSettings(idsValuesColumns);
	}
	
	public static NiReportOutputBuilder<GeneratedReport> asOutputBuilder(Supplier<ReportAreaImpl> reportAreaSupplier, OutputSettings outputSettings) {
		return (ReportSpecification spec, NiReportRunResult runResult) -> new AmpNiReportsFormatter(spec, runResult, reportAreaSupplier, outputSettings).format();
	}

	@Override
	protected ReportOutputColumn buildReportOutputColumn(Column niCol) {
		String trnName = getNameTranslation(niCol);
		String trnDescription = getDescriptionTranslation(niCol);
		return new ReportOutputColumn(trnName, niColumnToROC.get(niCol.getParent()), niCol.name, trnDescription, null);
	}
	
	protected String getDescriptionTranslation(Column niCol) {
		String description = niCol.getDescription();
		return description == null ? null : TranslatorWorker.translateText(description);
	}
	
	protected String getNameTranslation(Column niCol) {
		if (niCol.splitCell != null && NiReportsEngine.PSEUDOCOLUMN_YEAR.equals(niCol.splitCell.entityType)) {
			return niCol.name;
		}
		return TranslatorWorker.translateText(niCol.name);
	}
}
