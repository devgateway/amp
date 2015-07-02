package org.digijava.kernel.ampapi.endpoints.documents;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.digijava.module.aim.util.DesktopDocumentsUtil;
import org.digijava.module.contentrepository.helper.DocumentData;

@Path("documents")
public class Documents {

	protected static final Logger logger = Logger.getLogger(Documents.class);
    private static final int MAX_NUMBER_OF_DOCS = 5;

	@Context
	private HttpServletRequest httpRequest;
	@Context
	private HttpServletResponse httpResponse;

	@GET
	@Path("/getTopDocuments")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public final String getDocuments() throws IOException {
        long start = System.currentTimeMillis();
		DesktopDocumentsUtil desktopDocumentsUtil = new DesktopDocumentsUtil();
		Collection<DocumentData> documents = desktopDocumentsUtil.getLatestDesktopLinks(httpRequest, MAX_NUMBER_OF_DOCS);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);

        // do not serialize transient fields
        // if fields is marked as transient, but has public method, we need to add this config
        mapper.setVisibilityChecker(
                mapper.getSerializationConfig().
                        getDefaultVisibilityChecker().
                        withFieldVisibility(JsonAutoDetect.Visibility.ANY).
                        withGetterVisibility(JsonAutoDetect.Visibility.NONE));

        long end = System.currentTimeMillis();
        logger.info("getTopDocuments. execution time is: " + (end - start));

		return mapper.writeValueAsString(documents);
	}

}
