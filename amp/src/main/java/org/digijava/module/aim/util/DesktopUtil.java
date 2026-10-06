package org.digijava.module.aim.util;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.type.DateType;
import org.hibernate.type.LongType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DesktopUtil {

    private static Logger logger = Logger.getLogger(DesktopUtil.class);


    public static List<Long> getAllChildrenIds(Long teamId) {
        Session session = null;
        List<Long> childTeamId = new ArrayList<>();
        try {
            session = PersistenceManager.getSession();
            String qryStr = "select t.ampTeamId from " + AmpTeam.class.getName() + " t where" +
                    " (t.parentTeamId=:id)";
            Query qry = session.createQuery(qryStr);
            qry.setParameter("id",teamId,LongType.INSTANCE);
            List<Long> col = qry.list();            
            while (col.size() > 0) {
                String qryParam = "";
                for(long tId:col){
                    childTeamId.add(tId);
                    if (qryParam.length() != 0)
                        qryParam += ",";
                    qryParam += tId;
                }
                qryStr = "select t.ampTeamId from " + AmpTeam.class.getName() + " t where" +
                    " t.parentTeamId in (" + qryParam + ")";
                qry = session.createQuery(qryStr);
                col = qry.list();
            }
        } catch (Exception e) {
            logger.error("Exception from getAllChildrenIds " + e.getMessage());
            e.printStackTrace(System.out);
        }
        return childTeamId;
    }


    public static List<AmpActivity> getActivitiesTobeClosed(Long ampTeamId)
    {
        Date currentDate = new Date();
        String queryString = "select act from " + AmpActivity.class.getName()
                                + " act where (act.team=:ampTeamId)" 
                                + " and (act.actualCompletionDate is not null)"  
                                + " and (act.actualCompletionDate>=:currentDate)";
        Query q = PersistenceManager.getSession().createQuery(queryString);
        q.setParameter("ampTeamId", ampTeamId, LongType.INSTANCE);
        q.setParameter("currentDate", currentDate, DateType.INSTANCE);
        return q.list();
    }   
}
