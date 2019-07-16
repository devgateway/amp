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

    /**
     * Retrieve a list of documents order by document's date.
     * </br>
     * <dl>
     * This EP needs a logged in user and will return a list of as maximum 5 documents, this documents can be privates or team's documents.
     * If no user is logged in, return to an empty list.
     * The JSON object holds information regarding:
     * <dt><b>name</b><dd> - the name of document
     * <dt><b>uuid</b><dd> - the uuid of document
     * <dt><b>title</b><dd> - the title of document
     * <dt><b>description</b><dd> - the description of document
     * <dt><b>notes</b><dd> - the notes of document
     * <dt><b>calendar</b><dd> - the creation date of document
     * <dt><b>contentType</b><dd> - the contentType of document
     * <dt><b>webLink</b><dd> - the webLink of document
     * <dt><b>generalLink</b><dd> - the generalLink of document
     * <dt><b>cmDocType</b><dd> - the DocType of document
     * <dt><b>category</b><dd> - the category of document
     * <dt><b>organisations</b><dd> - the  of document
     * <dt><b>fileSize</b><dd> - the file size of document
     * <dt><b>date</b><dd> - the date of document
     * <dt><b>yearofPublication</b><dd> - the year of publication of document
     * <dt><b>labels</b><dd> - the labels of document
     * <dt><b>creatorEmail</b><dd> - the email of the creator of document
     * <dt><b>versionNumber</b><dd> - the version of document
     * <dt><b>translatedTitles</b><dd> - the translated titles of document
     * <dt><b>translatedDescriptions</b><dd> - the translated descriptions of document
     * <dt><b>translatedNotes</b><dd> - the translated notes of document
     * </dl></br></br>
     *
     * </br>
     * <h3>Sample Output:</h3><pre>
     * [
     *   {
     *      "name": "atl user ratio coutnry.xls",
     *      "uuid": "7d345859-a49e-4d52-b26c-5ce1af1141d4",
     *      "title": "knkjn",
     *      "description": "",
     *      "notes": "",
     *      "calendar": "12/27/2016",
     *      "contentType": "application/vnd.ms-excel",
     *      "webLink": null,
     *      "generalLink": "/contentrepository/downloadFile.do?uuid=7d345859-a49e-4d52-b26c-5ce1af1141d4",
     *      "cmDocType": "Budget",
     *      "category": null,
     *      "index": null,
     *      "organisations": null,
     *      "fileSize": 0.014,
     *      "date": 1482850002475,
     *      "iconPath": "images/icons/xls.gif",
     *      "yearofPublication": "2016",
     *      "labels": [],
     *      "cmDocTypeId": 50,
     *      "creatorTeamId": 2,
     *      "creatorEmail": "atl@amp.org",
     *      "versionNumber": 1,
     *      "hasDeleteRights": true,
     *      "hasViewRights": false,
     *      "hasShowVersionsRights": true,
     *      "hasVersioningRights": true,
     *      "hasMakePublicRights": true,
     *      "hasDeleteRightsOnPublicVersion": true,
     *      "hasAddParticipatingOrgRights": false,
     *      "isPublic": false,
     *      "lastVersionIsPublic": false,
     *      "showVersionHistory": true,
     *      "hasShareRights": false,
     *      "hasUnshareRights": false,
     *      "hasApproveVersionRights": false,
     *      "shareWith": null,
     *      "needsApproval": false,
     *      "isShared": false,
     *      "lastVersionIsShared": false,
     *      "currentVersionNeedsApproval": false,
     *      "hasAnyVersionPendingApproval": false,
     *      "baseNodeUUID": null,
     *      "nodeVersionUUID": null,
     *      "translatedTitles": null,
     *      "translatedDescriptions": null,
     *      "translatedNotes": null
     *   {,
     *   ....
     * ]</pre>
     *
     * @return a list of JSON objects with the documents
     */
    @GET
    @Path("/getTopDocuments")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public final String getDocuments() throws IOException {
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

        return mapper.writeValueAsString(documents);
    }

}
