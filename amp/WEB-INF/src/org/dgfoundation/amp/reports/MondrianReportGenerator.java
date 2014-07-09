/**
 * 
 */
package org.dgfoundation.amp.reports;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.error.AMPException;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportExecutor;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.digijava.kernel.ampapi.exception.AmpApiException;
import org.digijava.kernel.ampapi.mondrian.queries.MDXGenerator;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXAttribute;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXConfig;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXMeasure;
import org.digijava.kernel.ampapi.mondrian.util.MoConstants;
import org.digijava.kernel.ampapi.mondrian.util.MondrianMaping;
import org.olap4j.Axis;
import org.olap4j.CellSet;
import org.olap4j.CellSetAxis;

/**
 * Generates a report via Mondrian
 * @author Nadejda Mandrescu
 *
 */
public class MondrianReportGenerator implements ReportExecutor {
	protected static final Logger logger = Logger.getLogger(MondrianReportGenerator.class);
	
	public MondrianReportGenerator() {
	}
	
	@Override
	public GeneratedReport executeReport(ReportSpecification spec) throws AMPException {
		MDXConfig config = new MDXConfig();
		config.setCubeName(MoConstants.DEFAULT_CUBE_NAME);
		config.setMdxName(spec.getReportName());
		//add requested columns
		for (ReportColumn col:spec.getColumns()) {
			MDXAttribute elem = (MDXAttribute)MondrianMaping.toMDXElement(col);
			config.addRowAttribute(elem);
		}
		//add requested measures
		for (ReportMeasure measure: spec.getMeasures()) {
			MDXMeasure elem = (MDXMeasure)MondrianMaping.toMDXElement(measure);
			config.addColumnMeasure(elem);
		}
		//TODO: configure filters
		//TODO: add dates
		//TODO: sorting - do it afterwards?
		
		CellSet cellSet = null;
		long startTime = 0;
		try {
			MDXGenerator mdxGen = new MDXGenerator();
			startTime = System.currentTimeMillis();
			String mdxQuery = mdxGen.getAdvancedOlapQuery(config);
			cellSet = mdxGen.runQuery(mdxQuery);
		} catch (AmpApiException e) {
			throw new AMPException("Cannot generate Mondrian Report");
		}
		return toGeneratedReport(spec, cellSet, (int)(System.currentTimeMillis() - startTime));
	}
	
	private GeneratedReport toGeneratedReport(ReportSpecification spec, CellSet cellSet, int duration) {
		ReportArea reportArea;//TODO: update with implementing class
		CellSetAxis rows = cellSet.getAxes().get(Axis.ROWS.axisOrdinal());
		CellSetAxis columns = cellSet.getAxes().get(Axis.COLUMNS.axisOrdinal());
		//for (res.)
		
		//we should have requesting user already configure in spec - spec must have all required data
		//GeneratedReport genRep = new GeneratedReport(spec, duration, reportArea); 
		//return genRep;
		return null;
	}
}

	