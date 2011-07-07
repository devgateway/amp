/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.digijava.module.widget.util;


import java.util.List;

import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.widget.dbentity.AmpWidgetOrgProfile;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author medea
 */
public class OrgProfileWidgetUtil {
    
        private static Logger logger = Logger.getLogger(OrgProfileWidgetUtil.class);
    
      /**
     * Returns ALL OrgProfile widgets.
     * @return
     * @throws DgException
     */
    @SuppressWarnings("unchecked")
	public static List<AmpWidgetOrgProfile> getAllOrgProfileWidgets() throws DgException{
    	Session session=PersistenceManager.getRequestDBSession();
    	List<AmpWidgetOrgProfile> result=null;
    	try {
			Query query=session.createQuery("from "+AmpWidgetOrgProfile.class.getName());
			result=query.list();
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
    	return result;
    }

    /**
     * Returns true if such organization profile widget already exists.
     * @return
     * @throws DgException
     */
    public static boolean orgProfileWidgetExists(Long type, Long widgetId) throws DgException {
        boolean exists = false;
        Session session = PersistenceManager.getRequestDBSession();
        List<AmpWidgetOrgProfile> result = null;
        try {
            String queryString = "from " + AmpWidgetOrgProfile.class.getName() + " opw where opw.type=:type ";
            if (widgetId != null) {
                queryString += " and opw.id!=:id";
            }
            Query query = session.createQuery(queryString);
            query.setLong("type", type);
            if (widgetId != null) {
            query.setLong("id", widgetId);
            }
            result = query.list();
            if(result!=null&&result.size()>0){
                exists=true;
            }
        } catch (Exception e) {
            logger.error(e);
            throw new DgException(e);
        }
        return exists;
    }
    
      /**
     * Loads  widget by ID.
     * @param id
     * @return
     * @throws DgException
     */
    public static AmpWidgetOrgProfile getAmpWidgetOrgProfile(Long id) throws DgException{
		Session session=PersistenceManager.getRequestDBSession();
		AmpWidgetOrgProfile result=null;
		try {
			result=(AmpWidgetOrgProfile)session.load(AmpWidgetOrgProfile.class, id);
		} catch (Exception e) {
			throw new DgException("Cannot get  Widget!",e);
		}
    	return result;
    }
    
    /**
     * Creates new widget or  Updates already existing in db.
     * @param widget
     * @throws DgException
     */
	public static void saveWidget(AmpWidgetOrgProfile widget) throws DgException{
		Session session=PersistenceManager.getRequestDBSession();
		Transaction tx=null;
		try {
			tx=session.beginTransaction();
			session.saveOrUpdate(widget);
			tx.commit();
		} catch (Exception e) {
			if (tx!=null){
				try {
					tx.rollback();
				} catch (Exception e1) {
					throw new DgException("Cannot rallback Widget OrgProfile creation!",e1);
				}
			}
			throw new DgException("Cannot create/update Widget OrgProfile!",e);
		}
	}
        
     /**
     * delete OrgProfile widget from db.
     * @param widget
     * @throws DgException
     */
    public static void delete(AmpWidgetOrgProfile widget) throws DgException {
        try {
            Session session = PersistenceManager.getRequestDBSession();
            delete(widget, session);
        } catch (Exception ex) {
            throw new DgException("Cannot delete Widget OrgProfile!", ex);
        }

    }

     /**
	 * delete OrgProfile widget from db.
	 * @param widget
	 * @throws DgException
	 */
	public static void delete(AmpWidgetOrgProfile widget,Session session) throws DgException{
		Transaction tx = null;
		try {
            if (session == null) {
                /* we will use this in the Junit tests mainly because
                getRequestDBSession() don't work there*/
                session = PersistenceManager.getSession();
            }
			tx = session.beginTransaction();
			session.delete(widget);
			tx.commit();
		} catch (Exception e) {
			logger.error(e);
			if (tx !=null){
				try {
					tx.rollback();
				} catch (Exception e1) {
					logger.error(e1);
					throw new DgException("Cannot rallback  widget delete");
				}
			}
			throw new DgException("Cannot delete  widget");
		}
	}



}
