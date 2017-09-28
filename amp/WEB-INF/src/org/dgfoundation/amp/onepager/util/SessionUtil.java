package org.dgfoundation.amp.onepager.util;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.apache.wicket.request.cycle.RequestCycle;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.hibernate.HibernateException;
import org.hibernate.Session;

public class SessionUtil {
    
//  /**
//   * Gets current user from session
//   * @return
//   */
//  public static AmpTeamMember getCurrentUser(){
//      AmpTeamMember m = null;
//      try {
//          Session session = PersistenceManager.getSession();
//          m = (AmpTeamMember) session.load(AmpTeamMember.class, (long)61);
//      } catch (HibernateException e) {
//          e.printStackTrace();
//      }
//      return m;
//  }
    
    /**
     * only available from within a Wicket cycle!
     * @return
     */
    public static HttpServletRequest getCurrentServletRequest()
    {
        return (HttpServletRequest) RequestCycle.get().getRequest().getContainerRequest();
    }
}
