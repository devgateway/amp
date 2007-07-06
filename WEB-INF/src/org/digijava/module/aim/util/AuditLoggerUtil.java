/*
 * ActivityUtil.java
 * Created: 14 Feb, 2005
 */

package org.digijava.module.aim.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpAuditLogger;
import org.digijava.module.aim.helper.TeamMember;

/**
 * ActivityUtil is the persister class for all activity related
 * entities
 *
 * @author Priyajith
 */
public class AuditLoggerUtil {

	private static Logger logger = Logger.getLogger(AuditLoggerUtil.class);

	public static void logObject(HttpSession hsession,HttpServletRequest request, LoggerIdentifiable o, String action){
		
			Session session = null;
			Transaction tx = null;
			TeamMember tm = (TeamMember) hsession.getAttribute("currentMember");
			String browser=request.getHeader("user-agent");
			try {
				session = PersistenceManager.getSession();
				tx = session.beginTransaction();
				AmpAuditLogger aal=new AmpAuditLogger();
				aal.setAction(action);
				long time=System.currentTimeMillis();
				Timestamp ts=new Timestamp(time);
				aal.setLoggedDate(ts);
				aal.setAuthorName(tm.getMemberName());
				aal.setAuthorEmail(tm.getEmail());
				aal.setBrowser(browser);
				aal.setIp(request.getRemoteAddr());
				aal.setObjectId((String) o.getIdentifier());
				aal.setObjectType((String) o.getObjectType());
				aal.setTeamName(tm.getTeamName());
				session.save(aal);
				tx.commit();
			} catch (Exception ex) {
				logger.error("Exception : " + ex.getMessage());
			} finally {
				if (session != null) {
					try {
						PersistenceManager.releaseSession(session);
					} catch (Exception rsf) {
						logger.error("Release session failed :" + rsf.getMessage());
					}
				}
			}

			return ;
		}

		

} 
