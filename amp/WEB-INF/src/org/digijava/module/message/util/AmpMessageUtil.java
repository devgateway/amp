package org.digijava.module.message.util;

import java.util.List;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.message.dbentity.AmpMessage;

public class AmpMessageUtil {
private static Logger logger = Logger.getLogger(AmpMessageUtil.class);
	
	public static List<AmpMessage> getAllMessages() throws AimException {
		Session session=null;
		String queryString =null;
		Query query=null;
		List<AmpMessage> returnValue=null;
		try {
			session=PersistenceManager.getRequestDBSession();			
			queryString= "select a from " + AmpMessage.class.getName()+ " a";
			query=session.createQuery(queryString);
			returnValue=query.list();
		}catch(Exception ex) {
			logger.error("couldn't load Messages" + ex.getMessage());			
		}
		return returnValue;
	}
	
	public static List<AmpMessage> getAllMessagesForCurrentUser(Long teamId, Long userId, Long teamMemberId) throws AimException{
		Session session=null;
		String queryString =null;
		Query query=null;
		List<AmpMessage> returnValue=null;		
		try {
			session=PersistenceManager.getRequestDBSession();			
			queryString= "select a from " + AmpMessage.class.getName()+ " a where a.receiverType='ALL' or (a.receiverType='TEAM' and a.receiverId=:teamId) "+
			" or (a.receiverType='USER' and a.receiverId=:userId) or (a.receiverType='TM' and a.selectedTeamId=:teamId and a.receiverId=:teamMemberId)";
			query=session.createQuery(queryString);
			query.setParameter("teamId", teamId);
			query.setParameter("userId",userId);
			query.setParameter("teamMemberId", teamMemberId);
			returnValue=query.list();
		}catch(Exception ex) {
			logger.error("couldn't load Messages" + ex.getMessage());	
			ex.printStackTrace();
			throw new AimException("Unable to Load Messages", ex);
			
		}
		return returnValue;
	}
	
	public static AmpMessage getMessage(Long messageId) throws AimException{
		Session session=null;
		String queryString =null;
		Query query=null;
		AmpMessage returnValue=null;
		try {
			session=PersistenceManager.getRequestDBSession();
			queryString= "select a from " + AmpMessage.class.getName()+ " a where a.id=:id";
			query=session.createQuery(queryString);
			query.setParameter("id", messageId);
			returnValue=(AmpMessage)query.uniqueResult();
		}catch(Exception ex) {
			logger.error("couldn't load Message" + ex.getMessage());	
			ex.printStackTrace();
		}
		return returnValue;
	}
	
	public static void removeMessage(Long id) throws Exception{
		Session session=null;
		Transaction trans=null;
		try {
			session=PersistenceManager.getRequestDBSession();
			trans=session.beginTransaction();
			AmpMessage alert=getMessage(id);
			session.delete(alert);
			trans.commit();
		} catch (Exception ex) {
			if(trans!=null) {
				try {
					trans.rollback();					
				}catch(Exception e ) {
					logger.error("...Rollback failed");
					throw new AimException("Can't rollback", e);
				}			
			}
			throw new AimException("delete failed",ex);
		}
	}
	
	public static void saveOrUpdateMessage(AmpMessage alert) throws AimException {
		Session session= null;
		Transaction tx=null;
		try {
			session=PersistenceManager.getRequestDBSession();
			tx=session.beginTransaction();
			session.saveOrUpdate(alert);
			tx.commit();
		}catch(Exception ex) {
			if(tx!=null) {
				try {
					tx.rollback();					
				}catch(Exception e ) {
					logger.error("...Rollback failed");
					throw new AimException("Can't rollback", e);
				}			
			}
			throw new AimException("update failed",ex);
		}
	}
	
	
}
