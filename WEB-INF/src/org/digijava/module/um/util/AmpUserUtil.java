package org.digijava.module.um.util;

import net.sf.hibernate.Query;
import org.digijava.kernel.persistence.PersistenceManager;
import java.util.Collection;
import org.digijava.kernel.user.User;
import java.util.ArrayList;
import net.sf.hibernate.Session;
import org.digijava.module.aim.util.DbUtil;
import org.apache.log4j.Logger;

public class AmpUserUtil {
    private static Logger logger = Logger.getLogger(AmpUserUtil.class);

    public static Collection<User> getAllUsers() {
    Session session = null;
    Query qry = null;
    Collection<User> users = new ArrayList<User>();

    try {
        session = PersistenceManager.getRequestDBSession();
        String queryString = "select u from " + User.class.getName()
            + " u";
        qry = session.createQuery(queryString);
        users = qry.list();
    } catch(Exception e) {
        logger.error("Unable to get user");
        logger.debug("Exceptiion " + e);
    }
    return users;
}

    public AmpUserUtil() {
    }
}
