/**
 * 
 */
package org.digijava.kernel.ampapi.saiku;

import java.util.List;

import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.digijava.module.aim.helper.TeamMember;
import org.saiku.olap.dto.resultset.CellDataSet;

/**
 * Stores generated report with additional information required by Saiku 
 * @author Nadejda Mandrescu
 */
public class SaikuGeneratedReport extends GeneratedReport{
	/** Saiku CellDataSet result that can be passed to Saiku to redere the report */
	public final CellDataSet cellDataSet; 

	public SaikuGeneratedReport(ReportSpecification spec, int generationTime,
			TeamMember requestingUser, SaikuReportArea reportContents,
			CellDataSet cellDataSet,
			List<ReportOutputColumn> rootHeaders,
			List<ReportOutputColumn> leafHeaders) {
		super(spec, generationTime, requestingUser, reportContents, rootHeaders,
				leafHeaders);
		this.cellDataSet = cellDataSet;
	}
}
