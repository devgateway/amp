package org.dgfoundation.amp.reports.saiku.export;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;

/**
 * class containing info regarding how a report has been generated Saiku-side<br />
 * Notice the class is a mess which duplicates data, breaks encapsulation (Saiku JSON in {@link #queryModel}...) and holds the generated report in JSON, because the Saiku side is a mess
 * @author Dolghier Constantin
 *
 */
public class ReportGenerationInfo {
	
	/**
	 * the report rendered as Json (mwahahaha)
	 */
	public final JsonBean jb;
	
	/**
	 * the report type, one of AMPReportExportConstants.XXX fields
	 */
	public final String type;
	
	/**
	 * the report specification which lead to this report having been generated. Does not consistently include data from {@link #queryModel}! (I warned you it's a mess)
	 */
	public final ReportSpecification report;
	
	/**
	 * the settings applied on top of the reportSpecification (wtf, ReportSpecificationImpl should have been enough)
	 */
	public final Map<String, Object> queryModel;
	
	/**
	 * suffix to add to all created sheets
	 */
	public final String suffix;
	
	public ReportGenerationInfo(JsonBean jb, String type, ReportSpecification report, LinkedHashMap<String, Object> queryModel, String suffix) {
		this.jb = jb;
		this.type = type;
		this.report = report;
		this.queryModel = Collections.unmodifiableMap(new LinkedHashMap<String, Object>(queryModel));
		this.suffix = suffix == null ? "" : suffix;
	}
}
