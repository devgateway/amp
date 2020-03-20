package org.digijava.kernel.content;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.jcr.NamespaceException;
import javax.jcr.NamespaceRegistry;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Workspace;
import javax.jcr.observation.Event;
import javax.jcr.observation.ObservationManager;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.jackrabbit.core.RepositoryImpl;
import org.apache.jackrabbit.core.config.RepositoryConfig;
import org.apache.log4j.Logger;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.util.ExpiringMemoizer;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.contentrepository.helper.CrConstants;
import org.digijava.module.contentrepository.listeners.NodeRemovalListener;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;

/**
 * @author Viorel Chihai
 *
 */
public final class ContentRepositoryManager {
    private static Logger logger = Logger.getLogger(ContentRepositoryManager.class);
    
    private static final String JACKRABBIT_DIR_PATH = "/jackrabbit";
    private static final String REPOSITORY_CONFIG_FILE_PATH = "/repository.xml";

    private static final String AMP_LABLE_NAMESPACE = "http://amp-demo.code.ro/label";
    private static final String AMP_DOC_NAMESPACE = "http://amp-demo.code.ro/ampdoc";

    private static final String DEFAULT_WRITE_USER_EMAIL = "admin@amp.org";
    
    private static final String JCR_WRITE_SESSION = "jcr-write-session";
    private static final String JCR_READ_SESSION = "jcr-read-session";

    private static final int EVENT_ALL_TYPES = 0x7f;

    private static RepositoryImpl repository;

    private static ExpiringMemoizer<Set<String>> privateResources =
            new ExpiringMemoizer<>(() -> getUuidsFromPath("private"));

    private static ExpiringMemoizer<Set<String>> teamResourcesCache =
            new ExpiringMemoizer<>(() -> getUuidsFromPath("team"));

    private ContentRepositoryManager() { }

    /**
     * Get the jackrabbit repository instance.
     * 
     * @return content repository
     */
    public static RepositoryImpl getRepositoryInstance() {
        if (repository == null) {
            initialize();
        }
        
        return repository;
    }
    
    /**
     * Initiate the jackrabbit repository.
     */
    public static void initialize() {
        String appPath = DocumentManagerUtil.getApplicationPath();
        String dir = appPath + JACKRABBIT_DIR_PATH;
        String configFilePath = dir + REPOSITORY_CONFIG_FILE_PATH;
        
        try {
            repository = RepositoryImpl.create(RepositoryConfig.install(new File(configFilePath), new File(dir)));
        } catch (RepositoryException | IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    /**
     * Shuts down the repository.
     */
    public static void shutdown() {
        getRepositoryInstance().shutdown();
    }
    
    /**
     * Gets a read session from current request. If it doens't exist, it will be created.
     * 
     * @param request
     * @return logged in session
     */
    public static Session getReadSession(HttpServletRequest request) {
        Session readSession = (Session) request.getAttribute(JCR_READ_SESSION);
        
        try {
            readSession = getOrCloseIfNotLive(readSession);
            if (readSession == null) {
                readSession = getRepositoryInstance().login();
            }
        } catch (RepositoryException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        
        request.setAttribute(JCR_READ_SESSION, readSession);
        
        return readSession;
    }
    
    /**
     * Gets a write session from current request. If it doens't exist, it will be created.
     * 
     * @param request
     * @return
     */
    public static Session getWriteSession(HttpServletRequest request) {
        Session writeSession = (Session) request.getAttribute(JCR_WRITE_SESSION);
        try {
            writeSession = getOrCloseIfNotLive(writeSession);
            if (writeSession == null) {
                writeSession = getRepositoryInstance().login(getCredentials(request));
                request.setAttribute(JCR_WRITE_SESSION, writeSession);
            } else {
                writeSession.save();
                registerNamespace(writeSession, "ampdoc", AMP_DOC_NAMESPACE);
                registerNamespace(writeSession, "amplabel", AMP_LABLE_NAMESPACE);
            }
            registerObservers(writeSession);
        } catch (RepositoryException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        
        request.setAttribute(JCR_WRITE_SESSION, writeSession);
        
        return writeSession;
    }

    /**
     * Created the credentials used for fetching a write session.
     * 
     * @param request
     * @return credentials 
     */
    private static SimpleCredentials getCredentials(HttpServletRequest request) {
        String userName = DEFAULT_WRITE_USER_EMAIL;
        TeamMember teamMember = (TeamMember) request.getSession().getAttribute(Constants.CURRENT_MEMBER);
        
        if (teamMember != null && teamMember.getEmail() != null) {
            userName = teamMember.getEmail();
        }

        SimpleCredentials creden = new SimpleCredentials(userName, userName.toCharArray());
        
        return creden;
    }
    
    private static void registerNamespace(Session session, String namespace, String uri) {
        Workspace workspace = session.getWorkspace();
        NamespaceRegistry namespaceRegistry = null;
        try {
            namespaceRegistry = workspace.getNamespaceRegistry();
            namespaceRegistry.getURI(namespace);
        } catch (NamespaceException e) {
            try {
                namespaceRegistry.registerNamespace(namespace, uri);
            } catch (RepositoryException e1) {
                logger.error(e1.getMessage(), e1);
            }
        } catch (RepositoryException e) {
            logger.error(e.getMessage(), e);
        }
    }
    
    /**
     * Close sessions related to a request.
     * 
     * @param request
     */
    public static void closeSessions(HttpServletRequest request) {
        closeSession((Session) request.getAttribute(JCR_WRITE_SESSION));
        request.removeAttribute(JCR_WRITE_SESSION);
        
        closeSession((Session) request.getAttribute(JCR_READ_SESSION));
        request.removeAttribute(JCR_READ_SESSION);
    }
    
    /**
     * Closes a <code>Session</code>. This method should be called when a <code>Session</code> is no longer needed.
     * 
     * @param session
     */
    public static void closeSession(Session session) {
        if (session != null) {
            session.logout();
        }
    }
    
    /**
     * Return session if is usable. Close if it is unusable and return null.
     * 
     * @param session
     * @return
     */
    private static Session getOrCloseIfNotLive(Session session) {
        if (session != null && !session.isLive()) {
            closeSession(session);
            return null;
        }
        
        return session;
    }
    
    private static void registerObservers(Session session) {
        try {
            ObservationManager observationManager = session.getWorkspace().getObservationManager();

            NodeRemovalListener teamListener =
                    new NodeRemovalListener(NodeRemovalListener.TEAM_RESOURCE_PATH_DEPTH);
            observationManager.addEventListener(teamListener, Event.NODE_REMOVED, "/team",
                    true, null, null, false);

            NodeRemovalListener privateListener =
                    new NodeRemovalListener(NodeRemovalListener.PRIVATE_RESOURCE_PATH_DEPTH);
            observationManager.addEventListener(privateListener, Event.NODE_REMOVED, "/private",
                    true, null, null, false);

            observationManager.addEventListener(e -> privateResources.invalidate(), EVENT_ALL_TYPES,
                    "/private", true, null, null, false);

            observationManager.addEventListener(e -> teamResourcesCache.invalidate(), EVENT_ALL_TYPES,
                    "/team", true, null, null, false);
        } catch (RepositoryException e) {
            throw new RuntimeException("Failed to register observers.", e);
        }
    }

    public static Set<String> getPrivateUuids() {
        return privateResources.get();
    }

    public static Set<String> getTeamUuids() {
        return privateResources.get();
    }

    private static Set<String> getUuidsFromPath(String path) {
        Session session = DocumentManagerUtil.getReadSession(TLSUtils.getRequest());
        Set<String> uuids = new HashSet<>();
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
}
