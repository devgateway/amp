package org.digijava.kernel.ampapi.endpoints.reports;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.dgfoundation.amp.error.AMPException;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.reports.ReportUtils;
import org.dgfoundation.amp.reports.mondrian.MondrianReportGenerator;
import org.digijava.kernel.ampapi.endpoints.util.JSONResult;
import org.digijava.kernel.ampapi.endpoints.util.ReportMetadata;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.util.DbUtil;

/***
 * 
 * @author
 * 
 */

@Path("data")
public class Reports {

	private static final String DEFAULT_CATALOG_NAME = "AMP";
	private static final String DEFAULT_CUBE_NAME = "[Donor Funding]";
	private static final String DEFAULT_QUERY_NAME = "XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX";
	private static final String DEFAULT_CONNECTION_NAME = "amp";
	private static final String DEFAULT_SCHEMA_NAME = "AMP";

	@GET
	@Path("/report/{report_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public final JSONResult getReport(@PathParam("report_id") Long reportId) {
		
		AmpReports ampReport = DbUtil.getAmpReport(reportId);

		ReportSpecificationImpl spec = ReportUtils.toReportSpecification(ampReport);;

		MondrianReportGenerator generator = new MondrianReportGenerator(ReportAreaImpl.class, false);

		JSONResult result = new JSONResult();
		ReportMetadata metadata = new ReportMetadata();
		metadata.setReportSpec(spec);
		metadata.setCatalog(DEFAULT_CATALOG_NAME);
		metadata.setCube(DEFAULT_CUBE_NAME);
		metadata.setQueryName(DEFAULT_QUERY_NAME);
		metadata.setName(ampReport.getName());
		metadata.setConnection(DEFAULT_CONNECTION_NAME);
		metadata.setSchema(DEFAULT_SCHEMA_NAME);

		result.setReportMetadata(metadata);

		try {
			result.setMdx(generator.getMDXQuery(spec));
		} catch (AMPException e) {
			System.err.println(e.getMessage());
		}
		return result;
	}
}
