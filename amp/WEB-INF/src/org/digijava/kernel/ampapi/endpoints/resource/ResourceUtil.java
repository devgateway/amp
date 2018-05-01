package org.digijava.kernel.ampapi.endpoints.resource;

import static java.util.Collections.singletonList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;

import com.google.common.cache.CacheBuilder;

/**
 * @author Viorel Chihai
 */
public final class ResourceUtil {
    
    /** the maximum number of minutes to keep a list of public documents in memory after generation */
    public final static int ENTRY_EXPIRATION_MINUTES = 10;
    
    /** the maximum number of sessions to keep in cache */
    public final static int MAXIMUM_CACHE_SIZE = 10;
    
    protected final static Map<String, List<String>> PUBLIC_RESOURCES_MAP = (Map) CacheBuilder.newBuilder().softValues()
            .maximumSize(MAXIMUM_CACHE_SIZE)
            .expireAfterWrite(ENTRY_EXPIRATION_MINUTES, TimeUnit.MINUTES)
            .build().asMap();
    
    public static final String PRIVATE_PATH_ITEM = "private";

    private ResourceUtil() {
    }

    public static JsonBean getImportResult(AmpResource resource, JsonBean json, List<ApiErrorMessage> errors) {
        JsonBean result;
        if (errors.size() == 0 && resource == null) {
            result = ApiError.toError(ApiError.UNKOWN_ERROR);
        } else if (errors.size() > 0) {
            result = ApiError.toError(errors);
            result.set(ResourceEPConstants.RESOURCE, json);
        } else {
            result = new JsonBean();
            result.set(ResourceEPConstants.UUID, resource.getUuid());
            result.set(ResourceEPConstants.TITLE, resource.getTitle());
            result.set(ResourceEPConstants.DESCRIPTION, resource.getDescription());
            result.set(ResourceEPConstants.TYPE, resource.getType().getId());
            result.set(ResourceEPConstants.NOTE, resource.getNote());
            result.set(ResourceEPConstants.WEB_LINK, resource.getWebLink());
            result.set(ResourceEPConstants.ADDING_DATE, resource.getAddingDate());
        }
        
        return result;
    }

    public static JsonBean getResource(HttpServletRequest request, String uuid) {
        
        AmpResource resource = new AmpResource();
        Node readNode = DocumentManagerUtil.getReadNode(uuid, request);
        
        if (readNode == null) {
            return ApiError.toError(ResourceErrors.RESOURCE_NOT_FOUND);
        }
        
        try {
            if(!readNode.isNodeType("mix:referenceable")) {
                return null;
            }
        } catch (RepositoryException e) {
            return ApiError.toError(ResourceErrors.RESOURCE_ERROR, e);
        }
        
        NodeWrapper nodeWrapper = new NodeWrapper(readNode);
        if (isNodeEmpty(nodeWrapper)) {
            return null;
        }
        
        resource.setUuid(nodeWrapper.getUuid());
        resource.setTitle(nodeWrapper.getTitle());
        resource.setDescription(nodeWrapper.getDescription());
        resource.setNote(nodeWrapper.getNotes());
        resource.setFileName(nodeWrapper.getName());
        resource.setType(CategoryManagerUtil.getAmpCategoryValueFromDb(nodeWrapper.getCmDocTypeId()));
        resource.setWebLink(nodeWrapper.getWebLink());
        resource.setAddingDate(nodeWrapper.getCalendarDate() == null ? null : nodeWrapper.getCalendarDate().getTime());
        resource.setUrl("/contentrepository/downloadFile.do?uuid=" + uuid);
        resource.setFileSize(nodeWrapper.getFileSizeInMegabytes());
        resource.setCreatorEmail(nodeWrapper.getCreator());
        resource.setYearOfPublication(nodeWrapper.getYearOfPublication());
        resource.setPublic(getPublicResources(request).contains(uuid));
        
        try {
            if (isPrivate(nodeWrapper)) {
                resource.setPrivate(true);
                resource.setTeamMember(Long.valueOf(nodeWrapper.getNode().getParent().getParent().getName()));
            } else {
                resource.setPrivate(false);
                resource.setTeam(Long.valueOf(nodeWrapper.getNode().getParent().getName()));
            }
        } catch (RepositoryException e) {
            return ApiError.toError(ResourceErrors.RESOURCE_ERROR, e);
        }

        DocumentManagerUtil.logoutJcrSessions(request);
        
        return new ResourceExporter().export(resource);
    }
    
    private static boolean isPrivate(NodeWrapper nodeWrapper) throws RepositoryException {
        return nodeWrapper.getNode().getPath().contains(PRIVATE_PATH_ITEM);
    }

    private static boolean isNodeEmpty(NodeWrapper nodeWrapper) {
        return StringUtils.isBlank(nodeWrapper.getTitle()) && StringUtils.isBlank(nodeWrapper.getCreator());
    }

    /**
     * Get all resources from AMP
     * 
     * @param request
     * @return
     */
    public static List<JsonBean> getAllResources(HttpServletRequest request) {
        List<JsonBean> resources = new ArrayList<>();
        List<String> resourceUuids = getAllNodeUuids();
        
        for(String uuid : resourceUuids) {
            resources.add(getResource(request, uuid));
        }
        
        resources.removeAll(singletonList(null));
        
        return resources;
    }

    /**
     * Retrieve all uuids from database
     * 
     * @return list of node uuids
     */
    private static List<String> getAllNodeUuids() {
        List<String> nodeUuids = new ArrayList<>();
        String query = "SELECT node_id FROM reppm_node";
        
        PersistenceManager.doWorkInTransaction(connection -> {
            nodeUuids.addAll(SQLUtils.fetchStrings(connection, query));
        });
        
        return nodeUuids;
    }
    
    private static List<String> getPublicResources(HttpServletRequest request) {
        return PUBLIC_RESOURCES_MAP.putIfAbsent(request.getSession().getId(), getPublicResources());
    }
    
    private static List<String> getPublicResources() {
        List<String> publicDocs = new ArrayList<>();
        String query = "SELECT uuid FROM cr_document_node_attributes";
        
        PersistenceManager.doWorkInTransaction(connection -> {
            publicDocs.addAll(SQLUtils.fetchStrings(connection, query));
        });
        
        return publicDocs;
    }
    
}
