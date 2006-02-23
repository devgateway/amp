/*
 * TeamMemberUtil.java
 * Created : 17-Feb-2006
 */
package org.digijava.module.aim.util;


import java.util.Iterator;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpTeamMember;

public class TeamMemberUtil {
	
	private static Logger logger = Logger.getLogger(TeamMemberUtil.class);
	
	public static Long getFundOrgOfUser(Long id) {
		Long orgId = null;
		Session session = null;
		
		try {
			session = PersistenceManager.getSession();
			AmpTeamMember tm = (AmpTeamMember) session.load(AmpTeamMember.class,
					id);
			User user = tm.getUser();
			
			String qryStr = "select o.ampOrgId from " + AmpOrganisation.class.getName() + " o " +
					"where (o.name=:name)";
			Query qry = session.createQuery(qryStr);
			qry.setParameter("name",user.getOrganizationName(),Hibernate.STRING);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				orgId = (Long) itr.next();
			}
		} catch (Exception e) {
			logger.error("Exception from getFundOrgOfUser()");
			e.printStackTrace(System.out);
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed");
				}
			}
		}
		
		logger.info("OrgId is " + orgId);
		return orgId;
	}
}