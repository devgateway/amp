package org.digijava.module.gis.util;

import java.util.Date;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.log4j.Logger;
import org.apache.struts.tiles.ComponentContext;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.gis.dbentity.AmpDaWidgetPlace;
import org.digijava.module.gis.dbentity.AmpWidget;

/**
 * Widgets utilities.
 * @author Irakli Kobiashvili
 *
 */
public class WidgetUtil {

    private static Logger logger = Logger.getLogger(WidgetUtil.class);
	public static final String WIDGET_TEASER_PARAM = "widget-teaser-param";
	
	public static AmpWidget getWidget(Long widgetId){
		AmpWidget result = null;
		return result;
	}
	
	/**
	 * Retrieves place data from contexts and updates place object.
	 * @param context
	 * @return
	 * @throws DgException
	 */
	public static AmpDaWidgetPlace saveOrUpdatePlace(ComponentContext context) throws DgException{
		Object paramObj = context.getAttribute(WIDGET_TEASER_PARAM);
		if (paramObj == null || !(paramObj instanceof String)) {
			logger.error("No place param specified for teaser in layout!");
			return null;
		}
		String code=(String) paramObj;
		AmpDaWidgetPlace place = getPlace(code);
		if (place == null){
			place = new AmpDaWidgetPlace();
			place.setName(code);
		}
		place.setModule((String)context.getAttribute("org.digijava.kernel.module_name"));
		place.setModuleInstance((String)context.getAttribute("org.digijava.kernel.module_instance"));
		place.setCode(code);
		place.setLastRendered(new Date());
		savePlace(place);
		return place;
	}

	@SuppressWarnings("unchecked")
	public static List<AmpDaWidgetPlace> getAllPlaces() throws DgException{
		List<AmpDaWidgetPlace> result=null;
		Session session = PersistenceManager.getRequestDBSession();
		String oql="from "+AmpDaWidgetPlace.class.getName()+" as p ";
		try {
			Query q=session.createQuery(oql);
			result = q.list();
		} catch (Exception e) {
			logger.error(e);
			throw new DgException("Cannot load all widget place object from db",e);
		}
		return result;
	}
	
	public static AmpDaWidgetPlace getPlace(String code) throws DgException{
		AmpDaWidgetPlace result=null;
		Session session = PersistenceManager.getRequestDBSession();
		String oql="from "+AmpDaWidgetPlace.class.getName()+" as p ";
		oql += " where p.code like :placeCode";
		try {
			Query q=session.createQuery(oql);
			q.setString("placeCode", code);
			result = (AmpDaWidgetPlace)q.uniqueResult();
		} catch (Exception e) {
			logger.error(e);
			throw new DgException("Cannot load widget place object from db",e);
		}
		return result;
	}

	public static void savePlace(AmpDaWidgetPlace place) throws DgException{
		Session session=PersistenceManager.getRequestDBSession();
		Transaction tx=null;
		try {
			tx=session.beginTransaction();
			session.saveOrUpdate(place);
			tx.commit();
		} catch (Exception e) {
			if (tx!=null){
				try {
					tx.rollback();
				} catch (Exception e1) {
					throw new DgException("Cannot rallback Widget place save or update!",e1);
				}
			}
			throw new DgException("Cannot save or update Widget place!",e);
		}
		
	}
	
	//TODO correct this!
	public static AmpWidget getWidgetOnPlace(Long placeId)throws DgException{
		AmpWidget result=null;
		Session session = PersistenceManager.getRequestDBSession();
		String oql=" select w from "+AmpWidget.class.getName()+" w ";
		oql+=" where w.place.id= :placeId";
		try {
			Query query = session.createQuery(oql);
			query.setLong("placeId", placeId);
			result = (AmpWidget)query.uniqueResult();
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new DgException("Cannot search widget for place",e);
		}
		return result;
	}
	
	
}
