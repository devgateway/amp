package org.digijava.kernel.ampapi.endpoints.gis.services;

import java.io.InputStream;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.util.DocumentUtil;
import org.digijava.module.contentrepository.dbentity.CrDocumentNodeAttributes;
import org.digijava.module.contentrepository.helper.CrConstants;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;

/**
 * Service clas used to download map tiles
 * 
 * @author Viorel Chihai
 *
 */
public final class MapTilesService {
    
    private static final Logger logger = Logger.getLogger(MapTilesService.class);
    
    public static final String FILE_NAME = "map-tiles.zip";
    
    private static MapTilesService mapTilesService = null;
    
    private MapTilesService() {
    }
     
    public static MapTilesService getInstance() {
        if (mapTilesService == null) {
            mapTilesService = new MapTilesService();
        }
        
        return mapTilesService;
    }

    public Response getArchivedMapTiles() {
        
        List<CrDocumentNodeAttributes> publicDocuments = DocumentUtil.getAllPublicDocuments();
        NodeWrapper mapTilesNodeWrapper = null;
        for (CrDocumentNodeAttributes publicDocument : publicDocuments) {
            Node node = DocumentManagerUtil.getReadNode(publicDocument.getUuid(), TLSUtils.getRequest());
            NodeWrapper nodeWrapper = new NodeWrapper(node);
            if (nodeWrapper.getName().equals(FILE_NAME)) {
                mapTilesNodeWrapper = nodeWrapper;
                break;
            }
        }
        
        if (mapTilesNodeWrapper == null) {
            logger.error("Map tiles file not found in repository as public document: " + FILE_NAME);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        try {
            InputStream fileBinary = mapTilesNodeWrapper.getNode().getProperty(CrConstants.PROPERTY_DATA).getStream();
            Double fileSize = FileUtils.ONE_MB * mapTilesNodeWrapper.getFileSizeInMegabytes();
            
            return Response.ok(fileBinary, MediaType.APPLICATION_OCTET_STREAM)
                    .header("content-disposition", "attachment; filename = " + FILE_NAME)
                    .header("content-length", fileSize).build();
        } catch (RepositoryException e) {
            logger.error(e.getMessage(), e);
            return Response.serverError().build();
        }
    }

}
