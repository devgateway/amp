package org.digijava.kernel.ampapi.endpoints.scorecard;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.StreamingOutput;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.digijava.kernel.ampapi.endpoints.scorecard.model.Quarter;
import org.digijava.kernel.ampapi.endpoints.scorecard.service.ScorecardExcelExporter;
import org.digijava.kernel.ampapi.endpoints.scorecard.service.ScorecardService;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;

/**
 * This class should have all endpoints related to the Donor Scorecard -
 * AMP-20002
 * 
 * @author Emanuel Perez
 * 
 */

@Path("scorecard")
public class DonorScorecard {

	@GET
	@Path("/export")
	@Produces("application/vnd.ms-excel")
	@ApiMethod(ui = false, id = "DonorScorecar")
	public StreamingOutput getDonorScorecard(@Context HttpServletResponse webResponse) {

		webResponse.setHeader("Content-Disposition", "attachment; filename=donorScorecard.xls");

		StreamingOutput streamOutput = new StreamingOutput() {
			public void write(OutputStream output) throws IOException, WebApplicationException {
				try {
					ScorecardService service = new ScorecardService ();
					List<Quarter> quarters = service.getQuarters ();
					ScorecardExcelExporter exporter = new ScorecardExcelExporter();
					HSSFWorkbook wb = exporter.generateExcel(service.getDonors(), quarters,
							service.getOrderedScorecardCells(service.getDonorActivityUpdates()));
					wb.write(output);
				} catch (Exception e) {
					throw new WebApplicationException(e);
				}
			}
		};
		return streamOutput;

	}

}
