package org.digijava.module.widget.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.widget.dbentity.AmpSectorTableWidget;
import org.digijava.module.widget.dbentity.AmpSectorTableYear;
import org.digijava.module.widget.helper.DonorSectorFundingHelper;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class SectorTableWidgetUtil {

    private static Logger logger = Logger.getLogger(SectorTableWidgetUtil.class);

    /**
     * Returns ALL sector tables widgets.
     * @return
     * @throws DgException
     */
    @SuppressWarnings("unchecked")
    public static List<AmpSectorTableWidget> getAllSectorTableWidgets() throws DgException {
        Session session = PersistenceManager.getRequestDBSession();
        List<AmpSectorTableWidget> result = null;
        try {
            Query query = session.createQuery("from " + AmpSectorTableWidget.class.getName());
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
    public static void saveWidget(AmpSectorTableWidget widget,boolean isNew) throws DgException {
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
                    throw new DgException("Cannot rallback Widget OrgProfile creation!", e1);
                }
            }
            throw new DgException("Cannot create/update Widget OrgProfile!", e);
        }
    }

    /**
     * delete AmpSectorTableWidget widget from db.
     * @param widget
     * @throws DgException
     */
    public static void delete(AmpSectorTableWidget widget) throws DgException {
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
    public static AmpSectorTableWidget getAmpSectorTableWidget(Long id) throws DgException {
        Session session = PersistenceManager.getRequestDBSession();
        AmpSectorTableWidget result = null;
        try {
            result = (AmpSectorTableWidget) session.load(AmpSectorTableWidget.class, id);
        } catch (Exception e) {
            throw new DgException("Cannot get Sector Table Widget!", e);
        }
        return result;
    }

     /**
     * Returns amounts for the selected sectors.
     * @param id
     * @return
     * @throws DgException
     */
    public static Double calculateFunding(Long[] donorId, Long[] sectorId,Date startDate,Date endDate) throws DgException {
        Double result = ChartWidgetUtil.getDonorSectorFunding(donorId, startDate, endDate, sectorId,false);
        Double  divideByMillionDenominator=1000000d;
        if(result!=null){
            result/=divideByMillionDenominator;
        }
        return result;
    }

      /**
     * Loads Sector Table Years by table widget id.
     * @param id
     * @return
     * @throws DgException
     */
    @SuppressWarnings("unchecked")
	public static List<AmpSectorTableYear> getAmpSectorTableYears(Long id,Long columnType) throws DgException {
        Session session = PersistenceManager.getRequestDBSession();
        List<AmpSectorTableYear> result = new ArrayList<AmpSectorTableYear>();
        try {
            String oql = "select year  from " + AmpSectorTableYear.class.getName() + " year  where  year.widget.id=:id and year.type=:type order by year.order";
            Query query = session.createQuery(oql);
            query.setLong("type", columnType);
            query.setLong("id", id);
            result = query.list();
        } catch (Exception e) {
            throw new DgException("Cannot get Sector Table Widget!", e);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
	public static List<Long> getAmpSectorIds(Long id) throws DgException {
        List<Long> sectorIds=new ArrayList<Long>();
        Session session = PersistenceManager.getRequestDBSession();
      
        try {
            String oql = "select sec.ampSectorId  from " + AmpSectorTableWidget.class.getName() + " secTable " +
                    " inner join secTable.sectorsColumns secOrder inner join secOrder.sector sec "+
                    " where  secTable.id=:id order by secOrder.order";
            Query query = session.createQuery(oql);
            query.setLong("id", id);
            sectorIds = query.list();
        } catch (Exception e) {
            throw new DgException("Cannot get Sector Table Widget!", e);
        }
        return sectorIds;
    }

}
