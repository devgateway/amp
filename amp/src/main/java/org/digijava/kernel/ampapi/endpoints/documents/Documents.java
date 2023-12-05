package org.digijava.kernel.ampapi.endpoints.documents;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.digijava.module.aim.util.DesktopDocumentsUtil;
import org.digijava.module.contentrepository.helper.DocumentData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.Collection;

@Path("documents")
@Api("documents")
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
    @ApiOperation(
            value = "Retrieve a list of documents order by document's date.",
            notes = "This EP needs a logged in user and will return a list of as maximum 5 documents, this documents "
                    + "can be privates or team's documents.\n"
                    + "If no user is logged in, return to an empty list.\n"
                    + "\n"
                    + "The JSON object holds information regarding:\n"
                    + "\n"
                    + "Field|Description\n"
                    + "---|---\n"
                    + "name|the name of document\n"
                    + "uuid|the uuid of document\n"
                    + "title|the title of document\n"
                    + "description|the description of document\n"
                    + "notes|the notes of document\n"
                    + "calendar|the creation date of document\n"
                    + "contentType|the contentType of document\n"
                    + "webLink|the webLink of document\n"
                    + "generalLink|the generalLink of document\n"
                    + "cmDocType|the DocType of document\n"
                    + "category|the category of document\n"
                    + "organisations|the  of document\n"
                    + "fileSize|the file size of document\n"
                    + "date|the date of document\n"
                    + "yearofPublication|the year of publication of document\n"
                    + "labels|the labels of document\n"
                    + "creatorEmail|the email of the creator of document\n"
                    + "versionNumber|the version of document\n"
                    + "translatedTitles|the translated titles of document\n"
                    + "translatedDescriptions|the translated descriptions of document\n"
                    + "translatedNotes|the translated notes of document\n"
                    + "\n"
                    + "\n"
                    + "### Sample Output\n"
                    + "```\n"
                    + "[\n"
                    + "  {\n"
                    + "     \"name\": \"atl user ratio coutnry.xls\",\n"
                    + "     \"uuid\": \"7d345859-a49e-4d52-b26c-5ce1af1141d4\",\n"
                    + "     \"title\": \"knkjn\",\n"
                    + "     \"description\": \"\",\n"
                    + "     \"notes\": \"\",\n"
                    + "     \"calendar\": \"12/27/2016\",\n"
                    + "     \"contentType\": \"application/vnd.ms-excel\",\n"
                    + "     \"webLink\": null,\n"
                    + "     \"generalLink\": \"/contentrepository/downloadFile.do?"
                    + "uuid=7d345859-a49e-4d52-b26c-5ce1af1141d4\",\n"
                    + "     \"cmDocType\": \"Budget\",\n"
                    + "     \"category\": null,\n"
                    + "     \"index\": null,\n"
                    + "     \"organisations\": null,\n"
                    + "     \"fileSize\": 0.014,\n"
                    + "     \"date\": 1482850002475,\n"
                    + "     \"iconPath\": \"images/icons/xls.gif\",\n"
                    + "     \"yearofPublication\": \"2016\",\n"
                    + "     \"labels\": [],\n"
                    + "     \"cmDocTypeId\": 50,\n"
                    + "     \"creatorTeamId\": 2,\n"
                    + "     \"creatorEmail\": \"atl@amp.org\",\n"
                    + "     \"versionNumber\": 1,\n"
                    + "     \"hasDeleteRights\": true,\n"
                    + "     \"hasViewRights\": false,\n"
                    + "     \"hasShowVersionsRights\": true,\n"
                    + "     \"hasVersioningRights\": true,\n"
                    + "     \"hasMakePublicRights\": true,\n"
                    + "     \"hasDeleteRightsOnPublicVersion\": true,\n"
                    + "     \"hasAddParticipatingOrgRights\": false,\n"
                    + "     \"isPublic\": false,\n"
                    + "     \"lastVersionIsPublic\": false,\n"
                    + "     \"showVersionHistory\": true,\n"
                    + "     \"hasShareRights\": false,\n"
                    + "     \"hasUnshareRights\": false,\n"
                    + "     \"hasApproveVersionRights\": false,\n"
                    + "     \"shareWith\": null,\n"
                    + "     \"needsApproval\": false,\n"
                    + "     \"isShared\": false,\n"
                    + "     \"lastVersionIsShared\": false,\n"
                    + "     \"currentVersionNeedsApproval\": false,\n"
                    + "     \"hasAnyVersionPendingApproval\": false,\n"
                    + "     \"baseNodeUUID\": null,\n"
                    + "     \"nodeVersionUUID\": null,\n"
                    + "     \"translatedTitles\": null,\n"
                    + "     \"translatedDescriptions\": null,\n"
                    + "     \"translatedNotes\": null\n"
                    + "  {,\n"
                    + "  ....\n"
                    + "]\n"
                    + "```")
    public final String getDocuments() throws IOException {
        DesktopDocumentsUtil desktopDocumentsUtil = new DesktopDocumentsUtil();
        Collection<DocumentData> documents = desktopDocumentsUtil.getLatestDesktopLinks(httpRequest, MAX_NUMBER_OF_DOCS);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // do not serialize transient fields
        // if fields is marked as transient, but has public method, we need to add this config
        mapper.setVisibility(
                mapper.getSerializationConfig().
                        getDefaultVisibilityChecker().
                        withFieldVisibility(JsonAutoDetect.Visibility.ANY).
                        withGetterVisibility(JsonAutoDetect.Visibility.NONE));

        return mapper.writeValueAsString(documents);
    }

}
