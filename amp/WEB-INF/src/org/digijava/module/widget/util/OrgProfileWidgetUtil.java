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
     * Lads table widget by ID.
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
			throw new DgException("Cannot get Table Widget!",e);
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
	public static void delete(AmpWidgetOrgProfile widget) throws DgException{
		Session session = PersistenceManager.getRequestDBSession();
		Transaction tx = null;
		try {
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
