package org.digijava.module.widget.util;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.widget.dbentity.AmpSectorTableWidget;
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
            tx = session.beginTransaction();
            if(isNew){
                  session.save(widget);
            }
            else{
                 session.merge(widget);
            }
            tx.commit();
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
            tx = session.beginTransaction();
            session.delete(widget);
            tx.commit();
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
     * Loads sector table widget by id.
     * @param id
     * @return
     * @throws DgException
     */
    public static Long calculateFunding(Long[] donorId, Long[] sectorId,Date startDate,Date endDate) throws DgException {
        double result = 0;
        Double[] allFundingWrapper = {new Double(0)};// to hold whole funding value
        Collection<DonorSectorFundingHelper> fundings = ChartWidgetUtil.getDonorSectorFunding(donorId, startDate, endDate, allFundingWrapper, sectorId);
        for (DonorSectorFundingHelper funding : fundings) {
        result+=funding.getFounding();
        }
		if("true".equals(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS))){
			result/=1000;
        }
		else{
            result/=1000000;
        }
        return Math.round(result);
    }

}
