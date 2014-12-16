package org.digijava.kernel.ampapi.endpoints.documents;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.util.JSONResult;
import org.digijava.module.aim.action.GetDesktopLinks;
import org.digijava.module.aim.util.DesktopDocumentsUtil;
import org.digijava.module.contentrepository.helper.DocumentData;

@Path("documents")
public class Documents {

	protected static final Logger logger = Logger.getLogger(Documents.class);

	@Context
	private HttpServletRequest httpRequest;
	@Context
	private HttpServletResponse httpResponse;

	@GET
	@Path("/getTopDocuments")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public final Collection<DocumentData> getDocuments() {

		DesktopDocumentsUtil desktopDocumentsUtil = new DesktopDocumentsUtil();
		Collection<DocumentData> documents = desktopDocumentsUtil.getLatestDesktopLinks(httpRequest, 5);
		return documents;
	}

}
