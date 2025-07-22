/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.models;

import org.apache.log4j.Logger;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.util.ActivityUtil;
import org.digijava.kernel.ampapi.endpoints.datafreeze.DataFreezeService;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.startup.AmpSessionListener;
import org.digijava.module.aim.dbentity.AmpActivityFrozen;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpContentTranslation;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.FlushModeType;
import java.util.HashMap;

/**
 * @author mpostelnicu@dgateway.org since Sep 24, 2010
 */
public class AmpActivityModel extends LoadableDetachableModel<AmpActivityVersion> implements IModel<AmpActivityVersion>  {
    private static Logger logger = Logger.getLogger(AmpActivityModel.class);
    
    
    protected transient AmpActivityVersion a;
    protected transient HashMap<String, AmpContentTranslation> translationHashMap = new HashMap<String, AmpContentTranslation>();
    protected Long id;
    protected String editingKey;

    private static final long serialVersionUID = 3915904820384659360L;

    
    
    public AmpActivityModel() {
        beginConversation(true);
        a = new AmpActivityVersion();
    }   
    
    public AmpActivityModel(Long id, String editingKey) {
        beginConversation(true);
        
        this.id = id;
        this.a = null;
        this.editingKey = editingKey;
    }
    
    /**
     * Start a multi-request dialogue known as a conversation.
     * This creates a hibernate session that is kept in the HttpSession (thus one per each user session)
     * @see AmpSessionListener - the hibernate session is guaranteed to be closed on session death
     */
    public void beginConversation(boolean reset) {
        if(reset){
            AmpAuthWebSession s =  (AmpAuthWebSession) org.apache.wicket.Session.get();
            s.setMetaData(OnePagerConst.RESOURCES_NEW_ITEMS, null);
            s.setMetaData(OnePagerConst.RESOURCES_DELETED_ITEMS, null);
            s.setMetaData(OnePagerConst.GPI_RESOURCES_NEW_ITEMS, null);
            s.setMetaData(OnePagerConst.GPI_RESOURCES_DELETED_ITEMS, null);
            s.setMetaData(OnePagerConst.EDITOR_ITEMS, null);
            s.setMetaData(OnePagerConst.FUNDING_FREEZING_CONFIGURATION, null);
            s.setMetaData(OnePagerConst.ACTIVITY_FREEZING_CONFIGURATION, null);
            s.setMetaData(OnePagerConst.AGREEMENT_ITEMS, null);
            s.setMetaData(OnePagerConst.COMMENTS_ITEMS, null);
            s.setMetaData(OnePagerConst.COMMENTS_DELETED_ITEMS, null);
    
            translationHashMap = new HashMap<String, AmpContentTranslation>();
        }
        Session ses = getHibernateSession(reset);
        ses.clear();
    }
    
    
    /**
     * We keep hibernate session in the http session. This will ensure there is one session per wicket conversation (pager).
     * When its null, a new session is created and a transaction started
     * Always set {@link FlushMode#MANUAL} so that changes are committed only by {@link ActivityUtil#saveActivity(AmpActivityModel, boolean)}
     * No {@link Transaction} nor {@link Session} references are kept in the model!
     * @return
     */
    public static Session getHibernateSession(){
        return getHibernateSession(false);
    }

    private static Session getHibernateSession(boolean reset) {
        AmpAuthWebSession s =  (AmpAuthWebSession) org.apache.wicket.Session.get();
        Session hibernateSession = (Session) s.getHttpSession().getAttribute(OnePagerConst.ONE_PAGER_HIBERNATE_SESSION_KEY);
        if (reset && hibernateSession != null){
            hibernateSession.clear();
            hibernateSession.close();
            hibernateSession = null;
        }
        if(hibernateSession==null || !hibernateSession.isOpen())  {
            try {
                hibernateSession = PersistenceManager.openNewSession();
                hibernateSession.setFlushMode(FlushModeType.COMMIT);
                hibernateSession.beginTransaction();
                s.getHttpSession().setAttribute(OnePagerConst.ONE_PAGER_HIBERNATE_SESSION_KEY, hibernateSession);
            } catch (HibernateException e) {
                throw new RuntimeException(e);
            }
        }
        return hibernateSession;
    }

    public HashMap<String, AmpContentTranslation> getTranslationHashMap() {
        return translationHashMap;
    }

    protected AmpActivityVersion load() {
        beginConversation(false);
        AmpActivityVersion ret = ActivityUtil.load(this, id);
        if (ret.getActivityType() == null) //set default type for previously saved activities
            ret.setActivityType(ActivityUtil.ACTIVITY_TYPE_PROJECT);
        if (id != null) {
            loadFreezingConfiguration(ret);
        }
        return ret;
    }

    private void loadFreezingConfiguration(AmpActivityVersion a) {

        AmpAuthWebSession s = (AmpAuthWebSession) org.apache.wicket.Session.get();
        s.setMetaData(OnePagerConst.FUNDING_FREEZING_CONFIGURATION, null);
        s.setMetaData(OnePagerConst.ACTIVITY_FREEZING_CONFIGURATION, null);
        s.setMetaData(OnePagerConst.ACTIVITY_IS_AFFECTED_BY_FREEZING, null);
        // we get activity freezing configuration
        AmpActivityFrozen ampActivityFrozen = DataFreezeService.getActivityFrozenForActivity(a.getAmpActivityId());
        // even tough we can compute this further since its done once per field
        // we store it already computed
        Boolean isActivityEditable = DataFreezeService.isEditable(ampActivityFrozen, s.getAmpCurrentMember());

        Boolean isAffectedByFunding = DataFreezeService.isActivityAffectedByFreezing(ampActivityFrozen,
                s.getAmpCurrentMember());
        org.apache.wicket.Session.get().setMetaData(OnePagerConst.ACTIVITY_IS_AFFECTED_BY_FREEZING,
                isAffectedByFunding);
        org.apache.wicket.Session.get().setMetaData(OnePagerConst.ACTIVITY_FREEZING_CONFIGURATION, isActivityEditable);
        org.apache.wicket.Session.get().setMetaData(OnePagerConst.FUNDING_FREEZING_CONFIGURATION, ampActivityFrozen);
    }
    
    @Override   
    public void setObject(AmpActivityVersion arg0) {
        a = arg0;
        super.setObject(a);
    }

    @Override
    public void detach() {
    }

    /**
     * @see #beginConversation()
     * Ends a conversation (multi step/request dialogue). Ensures to retrieve the Hibernate session and to commit the current transaction + flush it
     * Then in a finally block we close the session.
     */
    public static void endConversation()
    {       
            AmpAuthWebSession session =  (AmpAuthWebSession) org.apache.wicket.Session.get();
            Session hibernateSession = (Session) session.getHttpSession().getAttribute(OnePagerConst.ONE_PAGER_HIBERNATE_SESSION_KEY);
            session.getHttpSession().setAttribute(OnePagerConst.ONE_PAGER_HIBERNATE_SESSION_KEY,null);
            if(hibernateSession==null) throw new RuntimeException("Attempted to close an unexisting Hibernate session!");
            if(!hibernateSession.isOpen()) throw new RuntimeException("Attempted to close an already closed session!");
            Throwable exceptionToThrow = null;
            try {
                hibernateSession.flush();        
                hibernateSession.getTransaction().commit();
                hibernateSession.setFlushMode(FlushModeType.COMMIT);
            }
            catch (Throwable t)
            {
                exceptionToThrow = t;
                try
                {
                    logger.error("Error while flushing session:", t);
                    if (hibernateSession.getTransaction().isActive()) {
                        logger.info("Trying to rollback database transaction after exception");
                        hibernateSession.getTransaction().rollback();
                    }
                    else
                        logger.error("Can't rollback transaction because transaction not active");
                }
                catch (Throwable rbEx)
                {
                    logger.error("Could not rollback transaction after exception!", rbEx);
                }
            }
            finally {
                hibernateSession.clear();
                hibernateSession.close(); 
            }
            if (exceptionToThrow != null)
                throw new RuntimeException("error while ending database conversation", exceptionToThrow);
    }

    public String getEditingKey() {
        return editingKey;
    }

    public Long getId() {
        return id;
    }
}
