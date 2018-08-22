package org.digijava.module.contentrepository.listeners;

import java.util.Date;
import java.util.Objects;
import java.util.function.Consumer;

import javax.jcr.RepositoryException;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;

import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.services.sync.model.SyncConstants;
import org.digijava.module.aim.dbentity.AmpOfflineChangelog;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Remember removed nodes in order to enable resources synchronization.
 * <p>This listener works on assumption that resources are always saved at the same absolute path depth. Nodes
 * at greater or lower depth are ignored by this listener.
 */
public class NodeRemovalListener implements EventListener {

    /**
     * Absolute path for private resources is: /private/{team id}/{username}/{resource title}
     */
    public static final int PRIVATE_RESOURCE_PATH_DEPTH = 4;

    /**
     * Absolute path for team resources is: /team/{team id}/{resource title}
     */
    public static final int TEAM_RESOURCE_PATH_DEPTH = 3;

    private static final Logger logger = LoggerFactory.getLogger(NodeRemovalListener.class);

    private final int pathDepth;

    public NodeRemovalListener(int pathDepth) {
        this.pathDepth = pathDepth;
    }

    @Override
    public void onEvent(EventIterator events) {
        PersistenceManager.doInTransaction((Consumer<Session>) s -> processEvents(s, () -> events));
    }

    private void processEvents(Session session, Iterable<Event> events) {
        for (Event event : events) {
            try {
                processEvent(session, event);
            } catch (RepositoryException e) {
                logger.error("Failed to process event: " + event, e);
            }
        }
    }

    private void processEvent(Session session, Event event) throws RepositoryException {
        if (pathDepth == getPathDepth(event.getPath())) {
            AmpOfflineChangelog changelog = new AmpOfflineChangelog();
            changelog.setEntityName(SyncConstants.Entities.RESOURCE);
            changelog.setEntityId(event.getIdentifier());
            changelog.setOperationName(SyncConstants.Ops.DELETED);
            changelog.setOperationTime(new Date(event.getDate()));
            session.save(changelog);
        }
    }

    /**
     * There is no available JCR/Jackrabbit util method to compute path depth. Apparently '/' characters are
     * allowed only to separate nodes and cannot be escaped.
     */
    private int getPathDepth(String path) {
        Objects.requireNonNull(path);
        if (!path.startsWith("/")) {
            throw new IllegalArgumentException("Path is not absolute: " + path);
        }
        int depth = 0;
        for (char ch : path.toCharArray()) {
            if (ch == '/') {
                depth++;
            }
        }
        return depth;
    }
}
