package org.digijava.module.dataExchange.util;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.util.TeamUtil;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;

public class ExportUtil {

    private static Logger logger = Logger.getLogger(ExportUtil.class);
	
	
    public static Collection<AmpActivity> getAllTeamAmpActivities(Long teamId) {
        Session session = null;
        Collection<AmpActivity> retValue = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "";
            Query qry = null;
            if(teamId == null) {
            	queryString = "select act from "+ AmpActivity.class.getName() + " act where act.team is null";
            	qry=session.createQuery(queryString);
            }
            else{
            	queryString = "select act from "  + AmpActivity.class.getName() + " act where (act.team=:teamId)";
            	qry=session.createQuery(queryString);
            	qry.setParameter("teamId", teamId, Hibernate.LONG);
            }
            retValue = qry.list();
        } catch(Exception e) {
            logger.debug("Exception from getAllTeamAmpActivities()");
            logger.debug(e.toString());
            throw new RuntimeException(e);
        }
        return retValue;
    }
	
}
