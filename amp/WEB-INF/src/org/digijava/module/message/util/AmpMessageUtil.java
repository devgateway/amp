package org.digijava.module.message.util;

import java.util.List;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.message.dbentity.AmpMessage;
import org.digijava.module.message.dbentity.AmpMessageSettings;
import org.digijava.module.message.dbentity.AmpMessageState;
import org.digijava.module.message.helper.MessageConstants;

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
	
	public static void saveOrUpdateMessage(AmpMessage message) throws AimException {
		Session session= null;
		Transaction tx=null;
		try {
			session=PersistenceManager.getRequestDBSession();
			tx=session.beginTransaction();
			session.saveOrUpdate(message);
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
	/**
	 * removes AmpMessage from db
	 * @param id
	 * @throws Exception
	 */
	public static void removeMessage(Long id) throws Exception{
		Session session=null;
		Transaction trans=null;
		try {
			session=PersistenceManager.getRequestDBSession();
			trans=session.beginTransaction();
			AmpMessage message=getMessage(id);
			session.delete(message);
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
	
	//***********************************************Message State functions************************************************
	
	/**
	 * loads AmpMessageState with the given state id
	 * @param stateId
	 * @return
	 * @throws Exception
	 */
	public static AmpMessageState getMessageState(Long stateId) throws Exception{
		Session session=null;
		String queryString =null;
		Query query=null;
		AmpMessageState returnValue=null;
		try {
			session=PersistenceManager.getRequestDBSession();
			queryString= "select st from " + AmpMessageState.class.getName()+ " st where st.id=:id";
			query=session.createQuery(queryString);
			query.setParameter("id", stateId);
			returnValue=(AmpMessageState)query.uniqueResult();
		}catch(Exception ex) {
			logger.error("couldn't load Message State" + ex.getMessage());	
			ex.printStackTrace();
		}
		return returnValue;
	}
	
	public static void removeMessageState(Long stateId) throws AimException{
		Session session=null;
		Transaction trans=null;
		try {
			session=PersistenceManager.getRequestDBSession();
			trans=session.beginTransaction();
			AmpMessageState state=getMessageState(stateId);
			session.delete(state);
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
	
	
	public static void saveOrUpdateMessageState(AmpMessageState messageState) throws AimException {
		Session session= null;
		Transaction tx=null;
		try {
			session=PersistenceManager.getRequestDBSession();
			tx=session.beginTransaction();
			session.saveOrUpdate(messageState);
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
	
	public static List<AmpMessageState> loadMessageStates(Long messageId) throws AimException{
		Session session=null;
		String queryString =null;
		Query query=null;
		List<AmpMessageState> returnValue=null;
		try {
			session=PersistenceManager.getRequestDBSession();
			queryString= "select a from " + AmpMessageState.class.getName()+ " a where a.message.id=:id";
			query=session.createQuery(queryString);
			query.setParameter("id", messageId);
			returnValue=(List<AmpMessageState>)query.list();
		}catch(Exception ex) {
			logger.error("couldn't load States" + ex.getMessage());	
			ex.printStackTrace();
		}
		return returnValue;
	}
	
	
	public static <E extends AmpMessage> int getUnreadMessagesAmountPerMsgType(Class<E> clazz,Long tmId) throws Exception{
		int retValue=0;
		Session session=null;
		String queryString =null;
		Query query=null;
		try {
			session=PersistenceManager.getRequestDBSession();	
			queryString="select count(*) from "+AmpMessageState.class.getName()+" state, msg from "+clazz.getName()+" msg where"+
			" msg.id=state.message.id and state.memberId=:tmId and msg.draft="+false+" and state.read="+false+" order by msg.creationDate desc";
			query=session.createQuery(queryString);			 				
			query.setParameter("tmId", tmId);			
			retValue=((Integer)query.uniqueResult()).intValue();			
		}catch(Exception ex) {
			logger.error("couldn't load Messages" + ex.getMessage());	
			ex.printStackTrace();
			throw new AimException("Unable to Load Messages", ex);
			
		}
		return retValue;
	}
	
	public static <E extends AmpMessage> int getInboxMessagesCount(Class<E> clazz,Long tmId) throws Exception {
		int retValue=0;
		Session session=null;
		String queryString =null;
		Query query=null;
		try {
			session=PersistenceManager.getRequestDBSession();	
			queryString="select count(*) from "+AmpMessageState.class.getName()+" state, msg from "+clazz.getName()+" msg where"+
			" msg.id=state.message.id and state.memberId=:tmId and msg.draft="+false+" order by msg.creationDate desc";
			query=session.createQuery(queryString);			 				
			query.setParameter("tmId", tmId);			
			retValue=((Integer)query.uniqueResult()).intValue();			
		}catch(Exception ex) {			
			ex.printStackTrace();
			throw new AimException("Unable to Load Messages", ex);
			
		}
		return retValue;
	}
	
	public static <E extends AmpMessage> int getSentOrDraftMessagesCount(Class<E> clazz,Long tmId,Boolean draft) throws Exception {
		int retValue=0;
		Session session=null;
		String queryString =null;
		Query query=null;
		try {
			session=PersistenceManager.getRequestDBSession();	
			queryString="select count(*) from "+AmpMessageState.class.getName()+" state, msg from "+clazz.getName()+" msg where"+
			" msg.id=state.message.id and state.senderId=:tmId and msg.draft="+draft+" order by msg.creationDate desc";
			query=session.createQuery(queryString);			 				
			query.setParameter("tmId", tmId);			
			retValue=((Integer)query.uniqueResult()).intValue();			
		}catch(Exception ex) {			
			ex.printStackTrace();
			throw new AimException("Unable to Load Messages", ex);
			
		}
		return retValue;
	}
	
	public static <E extends AmpMessage> List<AmpMessageState> loadAllInboxMessagesStates(Class<E> clazz,Long teamMemberId,int maxStorage,int page) throws Exception{
		Session session=null;
		String queryString =null;
		Query query=null;
		List<AmpMessageState> returnValue=null;	
		int messagesAmount=0;
		try {
			messagesAmount=getInboxMessagesCount(clazz,teamMemberId);
			session=PersistenceManager.getRequestDBSession();	
			queryString="select state from "+AmpMessageState.class.getName()+" state, msg from "+clazz.getName()+" msg where"+
			" msg.id=state.message.id and state.memberId=:tmId and msg.draft="+false;
//			if(maxStorage!=-1){
//				query=session.createQuery(queryString).setMaxResults(maxStorage);
//			}else{
//				query=session.createQuery(queryString);
//			}	
			query=session.createQuery(queryString);
			int fromIndex=messagesAmount-page*MessageConstants.MESSAGES_PER_PAGE;
			int toIndex=0;
			if(fromIndex<0){
				fromIndex=0;
				toIndex=fromIndex+MessageConstants.MESSAGES_PER_PAGE;
				if(fromIndex+MessageConstants.MESSAGES_PER_PAGE> messagesAmount-(page-1)*MessageConstants.MESSAGES_PER_PAGE){
					toIndex=messagesAmount-(page-1)*MessageConstants.MESSAGES_PER_PAGE;
				}else{
					toIndex=fromIndex+MessageConstants.MESSAGES_PER_PAGE;
				}
				if(toIndex>messagesAmount){
					toIndex=messagesAmount;
				}
				query.setFirstResult(fromIndex);
				query.setMaxResults(toIndex);
			}else{
				toIndex=fromIndex+MessageConstants.MESSAGES_PER_PAGE;
				query.setFirstResult(fromIndex);
				query.setMaxResults(toIndex);
			}
			query.setParameter("tmId", teamMemberId);			
			returnValue=query.list();			
		}catch(Exception ex) {
			logger.error("couldn't load Messages" + ex.getMessage());	
			ex.printStackTrace();
			throw new AimException("Unable to Load Messages", ex);
			
		}
		return returnValue;
	}
	
	public static <E extends AmpMessage> List<AmpMessageState> loadAllSentOrDraftMessagesStates(Class<E> clazz,Long teamMemberId,int maxStorage,Boolean draft,int page) throws Exception{
		Session session=null;
		String queryString =null;
		Query query=null;
		List<AmpMessageState> returnValue=null;	
		int messagesAmount=0;
		try {
			messagesAmount=getSentOrDraftMessagesCount(clazz,teamMemberId,draft);
			session=PersistenceManager.getRequestDBSession();	
			queryString="select state from "+AmpMessageState.class.getName()+" state, msg from "+clazz.getName()+" msg where"+
			" msg.id=state.message.id and state.senderId=:tmId and msg.draft="+draft+" ";
			query=session.createQuery(queryString);
			int fromIndex=messagesAmount-page*MessageConstants.MESSAGES_PER_PAGE;
			int toIndex=0;
			if(fromIndex<0){
				fromIndex=0;
				toIndex=fromIndex+MessageConstants.MESSAGES_PER_PAGE;
				if(fromIndex+MessageConstants.MESSAGES_PER_PAGE> messagesAmount-(page-1)*MessageConstants.MESSAGES_PER_PAGE){
					toIndex=messagesAmount-(page-1)*MessageConstants.MESSAGES_PER_PAGE;
				}else{
					toIndex=fromIndex+MessageConstants.MESSAGES_PER_PAGE;
				}
				if(toIndex>messagesAmount){
					toIndex=messagesAmount;
				}
				query.setFirstResult(fromIndex);
				query.setMaxResults(toIndex);
			}else{
				toIndex=fromIndex+MessageConstants.MESSAGES_PER_PAGE;
				query.setFirstResult(fromIndex);
				query.setMaxResults(toIndex);
			}
			query.setParameter("tmId", teamMemberId);			
			returnValue=query.list();			
		}catch(Exception ex) {
			logger.error("couldn't load Messages" + ex.getMessage());	
			ex.printStackTrace();
			throw new AimException("Unable to Load Messages", ex);
			
		}
		return returnValue;
	}

	
	//***************************************************Message Settings***********************************************
	
	public static AmpMessageSettings getMessageSettings() throws Exception{ //for this moment(according to requirements)for whole AMP there is one setting,So it's one record in table;
		Session session=null;
		String queryString =null;
		Query query=null;
		AmpMessageSettings returnValue=null;
		try {
			session=PersistenceManager.getRequestDBSession();			
			queryString= "select a from " + AmpMessageSettings.class.getName()+ " a";
			query=session.createQuery(queryString);
			returnValue=(AmpMessageSettings)query.uniqueResult();
		}catch(Exception ex) {
			logger.error("couldn't load Settings" + ex.getMessage());			
		}
		return returnValue;
	}
	
	public static void saveOrUpdateSettings(AmpMessageSettings setting) throws Exception{
		Session session= null;
		Transaction tx=null;
		try {
			session=PersistenceManager.getRequestDBSession();
			tx=session.beginTransaction();
			session.saveOrUpdate(setting);
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
