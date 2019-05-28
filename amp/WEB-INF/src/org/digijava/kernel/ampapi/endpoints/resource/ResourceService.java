package org.digijava.kernel.ampapi.endpoints.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;

import com.google.common.cache.CacheBuilder;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.ampapi.endpoints.activity.TranslationSettings;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponseService;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.contentrepository.helper.CrConstants;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;
import org.digijava.module.translation.util.ContentTranslationUtil;

/**
 * @author Viorel Chihai
 */
public class ResourceService {

    /** the maximum number of minutes to keep a list of public documents in memory after generation */
    public static final int ENTRY_EXPIRATION_MINUTES = 10;

    /** the maximum number of sessions to keep in cache */
    public static final int MAXIMUM_CACHE_SIZE = 10;

    protected static final Map<String, List<String>> PUBLIC_RESOURCES_MAP = (Map) CacheBuilder.newBuilder().softValues()
            .maximumSize(MAXIMUM_CACHE_SIZE)
            .expireAfterWrite(ENTRY_EXPIRATION_MINUTES, TimeUnit.MINUTES)
            .build().asMap();

    public static final String PRIVATE_PATH_ITEM = "private";

    public JsonBean getImportResult(AmpResource resource, JsonBean json, List<ApiErrorMessage> errors,
                                    Collection<ApiErrorMessage> warnings) {
        JsonBean result = new JsonBean();
        if (errors.size() == 0 && resource == null) {
            result.set(EPConstants.ERROR, ApiError.toError(ApiError.UNKOWN_ERROR).getErrors());
        } else if (errors.size() > 0) {
            result.set(EPConstants.ERROR, ApiError.toError(errors).getErrors());
            result.set(ResourceEPConstants.RESOURCE, json);
        } else {
            result = new JsonBean();
            result.set(ResourceEPConstants.UUID, resource.getUuid());
            if (TranslationSettings.getCurrent().isMultilingual()) {
                result.set(ResourceEPConstants.TITLE, resource.getTranslatedTitles());
                result.set(ResourceEPConstants.DESCRIPTION, resource.getTranslatedDescriptions());
                result.set(ResourceEPConstants.NOTE, resource.getTranslatedNotes());
            } else {
                result.set(ResourceEPConstants.TITLE, resource.getTitle());
                result.set(ResourceEPConstants.DESCRIPTION, resource.getDescription());
                result.set(ResourceEPConstants.NOTE, resource.getNote());
            }

            if (resource.getType() != null) {
                result.set(ResourceEPConstants.TYPE, resource.getType().getId());
            }
            if (ResourceType.LINK.equals(resource.getResourceType())) {
                result.set(ResourceEPConstants.WEB_LINK, resource.getWebLink());
            } else {
                result.set(ResourceEPConstants.FILE_NAME, resource.getFileName());
            }
            result.set(ResourceEPConstants.RESOURCE_TYPE, resource.getResourceType().getId());
            result.set(ResourceEPConstants.ADDING_DATE,
                    DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.format(resource.getAddingDate()));
            result.set(ResourceEPConstants.TEAM, resource.getTeam());
        }
        if (!warnings.isEmpty()) {
            result.set(EPConstants.WARNINGS, ApiError.formatNoWrap(warnings));
        }
        return result;
    }

    public JsonBean getResource(String uuid) {
        
        AmpResource resource = new AmpResource();
        Node readNode = DocumentManagerUtil.getReadNode(uuid, TLSUtils.getRequest());

        if (readNode == null) {
            ApiErrorResponseService.reportResourceNotFound(ResourceErrors.RESOURCE_NOT_FOUND);
        }

        NodeWrapper nodeWrapper = new NodeWrapper(readNode);
        resource.setUuid(nodeWrapper.getUuid());
        resource.setTitle(nodeWrapper.getTitle());
        resource.setDescription(nodeWrapper.getDescription());
        resource.setNote(nodeWrapper.getNotes());
        resource.setType(CategoryManagerUtil.getAmpCategoryValueFromDb(nodeWrapper.getCmDocTypeId()));
        resource.setAddingDate(nodeWrapper.getCalendarDate() == null ? null : nodeWrapper.getCalendarDate().getTime());
        resource.setUrl("/contentrepository/downloadFile.do?uuid=" + uuid);
        resource.setCreatorEmail(nodeWrapper.getCreator());
        resource.setYearOfPublication(nodeWrapper.getYearOfPublication());
        resource.setPublic(getPublicResources().contains(uuid));

        if (StringUtils.isNotBlank(nodeWrapper.getWebLink())) {
            resource.setWebLink(nodeWrapper.getWebLink());
            resource.setResourceType(ResourceType.LINK);
        } else {
            resource.setFileName(nodeWrapper.getName());
            resource.setFileSize(nodeWrapper.getFileSizeInMegabytes());
            resource.setResourceType(ResourceType.FILE);
        }

        if (ContentTranslationUtil.multilingualIsEnabled()) {
            resource.setTranslatedTitles(nodeWrapper.getTranslatedTitle());
            resource.setTranslatedDescriptions(nodeWrapper.getTranslatedDescription());
            resource.setTranslatedNotes(nodeWrapper.getTranslatedNote());
        }

        try {
            Node folderNode = nodeWrapper.getNode().getParent();
            if (isPrivate(nodeWrapper)) {
                resource.setPrivate(true);
                resource.setCreatorEmail(folderNode.getName());
                resource.setTeam(Long.valueOf(folderNode.getParent().getName()));
            } else {
                resource.setPrivate(false);
                resource.setTeam(Long.valueOf(folderNode.getName()));
            }
        } catch (RepositoryException e) {
            JsonBean result = new JsonBean();
            result.set(EPConstants.ERROR, ApiError.toError(ResourceErrors.RESOURCE_ERROR, e).getErrors());
            return result;
        }

        DocumentManagerUtil.logoutJcrSessions(TLSUtils.getRequest());

        return new ResourceExporter().export(resource);
    }

    private boolean isPrivate(NodeWrapper nodeWrapper) throws RepositoryException {
        return nodeWrapper.getNode().getPath().contains(PRIVATE_PATH_ITEM);
    }

    /**
     * Get all resources from AMP
     *
     * @return
     */
    public List<JsonBean> getAllResources() {
        List<JsonBean> resources = new ArrayList<>();
        List<String> resourceUuids = getAllNodeUuids();

        for (String uuid : resourceUuids) {
            resources.add(getResource(uuid));
        }

        return resources;
    }

    /**
     * Get resources
     *
     * @param uuids the list of uuids
     * @return
     */
    public List<JsonBean> getAllResources(List<String> uuids) {
        List<JsonBean> resources = new ArrayList<>();

        for (String uuid : uuids) {
            resources.add(getResource(uuid));
        }

        return resources;
    }

    /**
     * Retrieve all uuids
     *
     * @return list of node uuids
     */
    public List<String> getAllNodeUuids() {
        List<String> nodeUuids = new ArrayList<>();
        nodeUuids.addAll(getPrivateUuids());
        nodeUuids.addAll(getTeamUuids());

        return nodeUuids;
    }

    public List<String> getPrivateUuids() {
        return getUuidsFromPath("private");
    }

    private List<String> getTeamUuids() {
        return getUuidsFromPath("team");
    }

    private List<String> getUuidsFromPath(String path) {
        Session session = DocumentManagerUtil.getReadSession(TLSUtils.getRequest());
        List<String> uuids = new ArrayList<>();
        try {
            QueryManager queryManager = session.getWorkspace().getQueryManager();
            Query query = queryManager.createQuery(String.format("SELECT * FROM nt:base WHERE %s "
                    + "IS NOT NULL AND jcr:path LIKE '/%s/%%/'", CrConstants.PROPERTY_CREATOR, path), Query.SQL);
            NodeIterator nodes = query.execute().getNodes();
            while (nodes.hasNext()) {
                uuids.add(nodes.nextNode().getIdentifier());
            }
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }

        return uuids;
    }

    private List<String> getPublicResources() {
        String sessionId = TLSUtils.getRequest().getSession().getId();
        PUBLIC_RESOURCES_MAP.putIfAbsent(sessionId, getPublicResourcesFromDb());

        return PUBLIC_RESOURCES_MAP.get(sessionId);
    }

    private List<String> getPublicResourcesFromDb() {
        List<String> publicDocs = new ArrayList<>();
        String query = "SELECT uuid FROM cr_document_node_attributes";

        PersistenceManager.doWorkInTransaction(connection -> {
            publicDocs.addAll(SQLUtils.fetchStrings(connection, query));
        });

        return publicDocs;
    }

}
