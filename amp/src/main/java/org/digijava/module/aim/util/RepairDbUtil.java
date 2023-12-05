package org.digijava.module.aim.util;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.hibernate.Session;

public class RepairDbUtil {
    private static Logger logger = Logger.getLogger(RepairDbUtil.class);
    
    public static void repairDb() {
        repairInexistentActivityCreators();
        //repairInexistentPageInPageFilters();
    }
    
    /**
     * Checks whether the AmpTeamMember that created the activity still exists. If there is a link from an activity 
     * to an inexistent AmpteamMember then the field activity_creator is set to NULL
     *
     */
    public static void repairInexistentActivityCreators() {
        Session session = null;
        String qryStr = null;
        
        try{
            session             = PersistenceManager.getSession();
            qryStr              = "UPDATE amp_activity_version SET activity_creator=NULL WHERE activity_creator NOT IN (SELECT amp_team_mem_id FROM amp_team_member)" ;
            int result          = session.createNativeQuery(qryStr).executeUpdate();
                
            if (result > 0) {
                logger.error ("There was an error with inexistent activity creator in AmpActivity --- updated " + result + "rows" );
            }
        }
    
        
        catch (Exception ex) {
            logger.error("Exception : " + ex.getMessage());
            ex.printStackTrace(System.out);
        }
    }
    
//  public static void repairInexistentPageInPageFilters() {
//      Session session = null;
//      String qryStr = null;
//      
//      try{
//          session             = PersistenceManager.getSession();
//          qryStr              = "DELETE FROM amp_team_page_filters WHERE page NOT IN (SELECT amp_page_id FROM amp_pages)" ;
//          int result          = session.createNativeQuery(qryStr).executeUpdate();
//              
//          if (result > 0) {
//              logger.error ("There was an error with inexistent amp_page in amp_team_page_filter --- deleted " + result + "rows" );
//          }
//      }
//  
//      
//      catch (Exception ex) {
//          logger.error("Exception : " + ex.getMessage());
//          ex.printStackTrace(System.out);
//      }
//  }
    
    public static void repairBannedUsersAreStillInATeam() {
        try {
            Session session = PersistenceManager.getSession();
            String qryStr = "UPDATE dg_user SET banned = false WHERE banned = TRUE "
                    + "AND dg_user.id IN (SELECT amp_team_member.user_ FROM amp_team_member WHERE deleted is not true)";

            int result = session.createNativeQuery(qryStr).executeUpdate();

            if (result > 0) {
                logger.error("There was an error with banned users still appearing in teams --- updated "
                        + result + "rows");
            }
        }
        
        catch (Exception ex) {
            logger.error("Failed to repair banned users that are still in a team : " + ex.getMessage(), ex);
        }
    }
    
    
    public static void repairDocumentNoLongerInContentRepository(String uuid, String className) {
        logger.error("JACKRABBIT asked to cleanup non-existing node with uuid=" + uuid + ", className = " + className);
        /*logger.error("TEMPORARY SHIM, NOT DELETING ANYTHING", new RuntimeException("TODO-CONSTANTIN"));
        return;*//*
        int numOfObjectsDeleted         = DocumentManagerUtil.deleteObjectsReferringDocument(uuid, className); 
        if ( numOfObjectsDeleted > 0 )
            logger.error ("There was an error with " + className + " using deleted documents. Deleting " + numOfObjectsDeleted + "rows", new RuntimeException("this should not happen"));*/
    }
}
