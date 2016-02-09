/**
 * 
 */
package org.digijava.kernel.ampapi.saiku;

import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportEnvironment;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.helper.TeamMember;
import org.saiku.olap.dto.resultset.AbstractBaseCell;
import org.saiku.olap.dto.resultset.CellDataSet;

/**
 * Stores generated report with additional information required by Saiku 
 * @author Nadejda Mandrescu
 */
public class SaikuGeneratedReport extends GeneratedReport{
	/** Saiku CellDataSet result that can be passed to Saiku to render the report */
	public final CellDataSet cellDataSet; 

	public SaikuGeneratedReport(ReportSpecification spec, int generationTime,
			TeamMember requestingUser, SaikuReportArea reportContents,
			CellDataSet cellDataSet,
			List<ReportOutputColumn> rootHeaders,
			List<ReportOutputColumn> leafHeaders, ReportEnvironment environment) {
		super(spec, generationTime, requestingUser, reportContents, rootHeaders, leafHeaders, null, null, null);
		this.cellDataSet = cellDataSet;
		//translateHeaders(environment);
	}
	
	/**
	 * no need to translate anything, since they come translated out of the reporting engine
	 * @param environment
	 */
	@Deprecated
	private void translateHeaders(ReportEnvironment environment) {
		if (cellDataSet == null || cellDataSet.getCellSetHeaders() == null) return;
		AbstractBaseCell[][] headers = cellDataSet.getCellSetHeaders();
		for (int i = 0; i < headers.length; i++)
			for (int j = 0; j< headers[i].length; j++) {
				String formattedValue = headers[i][j].getFormattedValue();
				if (!NumberUtils.isNumber(formattedValue)) {
					if(formattedValue != null){
						headers[i][j].setFormattedValue(
								TranslatorWorker.translateText(formattedValue, environment.locale, 3l));
					}
				}
			}
	}
	
}
