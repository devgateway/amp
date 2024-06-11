package org.dgfoundation.amp.onepager.util;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.wicket.request.cycle.RequestCycle;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionUtil {

    private static final Logger logger = LoggerFactory.getLogger(SessionUtil.class);

    /**
     * only available from within a Wicket cycle!
     * @return
     */
    public static HttpServletRequest getCurrentServletRequest()
    {
        return (HttpServletRequest) RequestCycle.get().getRequest().getContainerRequest();
    }

    public static void extendSessionIfNeeded(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // false to prevent creating a new session if it doesn't exist
        if (session != null) {
            // Get the maximum inactive interval (session timeout) in seconds
            int maxInactiveInterval = session.getMaxInactiveInterval();

            // Get the creation time of the session
            long creationTime = session.getCreationTime();

            // Calculate the expiration time of the session
            long expirationTime = creationTime + (maxInactiveInterval * 1000L); // Convert seconds to milliseconds

            // Get the current time
            long currentTime = System.currentTimeMillis();

            // Calculate the remaining time in milliseconds
            long remainingTime = expirationTime - currentTime;

            // Extend session timeout if remaining time is 30 seconds or less
            if (remainingTime <= 30000) { // 30 seconds in milliseconds
                // Extend session timeout to 130 minutes
                session.setMaxInactiveInterval(1800); // 130 minutes * 60 seconds
                logger.info("Session timeout extended to "+session.getMaxInactiveInterval());
            }
        }
    }
}
