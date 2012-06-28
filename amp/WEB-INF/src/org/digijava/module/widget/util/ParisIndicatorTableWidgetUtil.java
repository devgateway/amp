

package org.digijava.module.widget.util;
import java.util.List;

import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.widget.dbentity.AmpParisIndicatorTableWidget;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;



public class ParisIndicatorTableWidgetUtil {

      private static Logger logger = Logger.getLogger( ParisIndicatorTableWidgetUtil.class);
      /**
     * Returns ALL sector tables widgets.
     * @return
     * @throws DgException
     */
    @SuppressWarnings("unchecked")
    public static List<AmpParisIndicatorTableWidget> getAllSectorTableWidgets() throws DgException {
        Session session = PersistenceManager.getRequestDBSession();
        List<AmpParisIndicatorTableWidget> result = null;
        try {
            Query query = session.createQuery("from " + AmpParisIndicatorTableWidget.class.getName());
            List list = query.list();
            result = list;
        } catch (Exception e) {
            logger.error(e);
            throw new DgException(e);
        }
        return result;
    }

    /**
     * Creates new widget or  Updates already existing in db.
     * @param widget
     * @throws DgException
     */
    public static void saveWidget(AmpParisIndicatorTableWidget widget,boolean isNew) throws DgException {
        Session session = PersistenceManager.getRequestDBSession();
        Transaction tx = null;
        try {
//beginTransaction();
            if(isNew){
                  session.save(widget);
            }
            else{
                 session.merge(widget);
            }
            //tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                try {
                    tx.rollback();
                } catch (Exception e1) {
                    throw new DgException("Cannot rallback Widget creation!", e1);
                }
            }
            throw new DgException("Cannot create/update Widget !", e);
        }
    }

    /**
     * delete AmpSectorTableWidget widget from db.
     * @param widget
     * @throws DgException
     */
    public static void delete(AmpParisIndicatorTableWidget widget) throws DgException {
        Session session = PersistenceManager.getRequestDBSession();
        Transaction tx = null;
        try {
//beginTransaction();
            session.delete(widget);
            //tx.commit();
        } catch (Exception e) {
            logger.error(e);
            if (tx != null) {
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
       /**
     * Loads sector table widget by id.
     * @param id
     * @return
     * @throws DgException
     */
    public static AmpParisIndicatorTableWidget getAmpParisIndicatorTableWidget(Long id) throws DgException {
        Session session = PersistenceManager.getRequestDBSession();
        AmpParisIndicatorTableWidget result = null;
        try {
            result = (AmpParisIndicatorTableWidget) session.load(AmpParisIndicatorTableWidget.class, id);
        } catch (Exception e) {
            throw new DgException("Cannot get Sector Table Widget!", e);
        }
        return result;
    }

}
