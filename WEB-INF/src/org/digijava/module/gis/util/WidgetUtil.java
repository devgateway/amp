package org.digijava.module.gis.util;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import net.sf.hibernate.type.Type;

import org.apache.log4j.Logger;
import org.apache.struts.tiles.ComponentContext;
import org.dgfoundation.amp.utils.AmpCollectionUtils.KeyWorker;
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

	/**
	 * Return all place objects from db.
	 * @return
	 * @throws DgException
	 */
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
	
	/**
	 * Load place object by its code.
	 * @param code
	 * @return
	 * @throws DgException
	 */
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
			throw new DgException("Cannot load widget place object from db with code="+code,e);
		}
		return result;
	}

	/**
	 * Load place object by its primary key.
	 * @param id
	 * @return
	 * @throws DgException
	 */
	public static AmpDaWidgetPlace getPlace(Long id) throws DgException{
		AmpDaWidgetPlace result=null;
		Session session = PersistenceManager.getRequestDBSession();
		try {
			result = (AmpDaWidgetPlace)session.load(AmpDaWidgetPlace.class, id);
		} catch (Exception e) {
			logger.error(e);
			throw new DgException("Cannot load widget place object from db, with id="+id,e);
		}
		return result;
	}

	/**
	 * ave place object to db.
	 * @param place
	 * @throws DgException
	 */
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
	
	public static AmpWidget getWidgetOnPlace(Long placeId)throws DgException{
		AmpDaWidgetPlace place = getPlace(placeId);
		if (place!=null){
			return place.getAssignedWidget();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static List<AmpDaWidgetPlace> getWidgetPlaces(Long widgetId) throws DgException{
		List<AmpDaWidgetPlace> places = null;
		String oql = "select p from "+AmpDaWidgetPlace.class.getName()+" as p where p.assignedWidget.id =:wid";
		Session session = PersistenceManager.getRequestDBSession();
		try {
			Query query = session.createQuery(oql);
			query.setLong("wid", widgetId);
			places = (List<AmpDaWidgetPlace>)query.list();
		} catch (Exception e) {
			logger.error(e);
			throw new DgException("cannot search widget places!",e);
		}
		return places;
	}

	@SuppressWarnings("unchecked")
	public static List<AmpDaWidgetPlace> getPlacesWithIDs(Long[] pids) throws DgException{
		List<AmpDaWidgetPlace> places = null;
		String oql = "select p from "+AmpDaWidgetPlace.class.getName()+" as p where (p.id in ( ";
		for (int i = 0; i < pids.length; i++) {
			oql+=""+pids[i];
			if (i<pids.length-1){
				oql+=",";
			}
		}
		oql += " )) ";
		Session session = PersistenceManager.getRequestDBSession();
		try {
			Query query = session.createQuery(oql);
			places = (List<AmpDaWidgetPlace>)query.list();
		} catch (Exception e) {
			logger.error(e);
			throw new DgException("cannot search widget places!",e);
		}
		return places;
	}

	public static void clearPlacesForWidget(Long widgetId) throws DgException{
		List<AmpDaWidgetPlace> places = getWidgetPlaces(widgetId);
		if (places!=null && places.size()>0){
			Session session = PersistenceManager.getRequestDBSession();
			Transaction tx = null;
			try {
				tx = session.beginTransaction();
				for (AmpDaWidgetPlace place : places) {
					place.setAssignedWidget(null);
					session.update(place);
				}
				tx.commit();
			} catch (Exception e) {
				if (tx!=null){
					try {
						tx.rollback();
					} catch (Exception e1) {
						logger.error(e1);
						throw new DgException("Cannot rallback places clearing",e1);
					}
				}
				logger.error(e);
				throw new DgException("Cannot clear rallbacks",e);
			}
		}
	}
	
	public static void updatePlacesWithWidget(Collection<AmpDaWidgetPlace> places, AmpWidget widget) throws DgException{
		if (places==null || places.size()==0) return; 
		Session session = PersistenceManager.getRequestDBSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			for (AmpDaWidgetPlace place : places) {
				if (place.getId().longValue() < 0){
					Long id = new Long( - (place.getId()));
					AmpDaWidgetPlace tmpPlace = getPlace(id);
					tmpPlace.setAssignedWidget(null);
					session.update(tmpPlace);
				}else {
					AmpDaWidgetPlace tmpPlace = getPlace(place.getId());
					tmpPlace.setAssignedWidget(widget);
					session.update(tmpPlace);
				}
			}
			tx.commit();
		} catch (Exception e) {
			if (tx!=null){
				try {
					tx.rollback();
				} catch (Exception e1) {
					logger.error(e1);
					throw new DgException("Cannot rallback places clearing",e1);
				}
			}
			logger.error(e);
			throw new DgException("Cannot clear places",e);
		}
	}
	
	public static class PlaceKeyWorker implements KeyWorker<Long, AmpDaWidgetPlace>{

		public void markKeyForRemoval(AmpDaWidgetPlace element) {
			element.setId(new Long(-(element.getId().longValue())));
		}

		public void updateKey(AmpDaWidgetPlace element, Long newKey) {
			element.setId(newKey);			
		}

		public Long resolveKey(AmpDaWidgetPlace element) {
			return element.getId();
		}
		
	}
}
