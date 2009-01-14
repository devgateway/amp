package org.digijava.module.widget.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts.tiles.ComponentContext;
import org.dgfoundation.amp.utils.AmpCollectionUtils.KeyWorker;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.IndicatorSector;
import org.digijava.module.widget.dbentity.AmpDaWidgetPlace;
import org.digijava.module.widget.dbentity.AmpWidget;
import org.digijava.module.widget.helper.WidgetPlaceHelper;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Widgets utilities.
 * @author Irakli Kobiashvili
 *
 */
public class WidgetUtil {

    private static Logger logger = Logger.getLogger(WidgetUtil.class);
	public static final String WIDGET_TEASER_PARAM = "widget-teaser-param";

	public static final int NO_PLACE_PARAM = 0;
	public static final int EMPTY = 1;
	public static final int EMBEDED = 2;
	public static final int TABLE = 3;
	public static final int CHART_INDICATOR = 4;
        
        public static final int ORG_PROFILE = 5;
        
        // org profile pages
        public static final int ORG_PROFILE_SUMMARY = 1;
        public static final int ORG_PROFILE_TYPE_OF_AID = 2;
        public static final int ORG_PROFILE_PLEDGES_COMM_DISB = 3;
        public static final int ORG_PROFILE_ODA_PROFILE = 4;
        public static final int ORG_PROFILE_SECTOR_BREAKDOWN = 5;
        public static final int ORG_PROFILE_REGIONAL_BREAKDOWN = 6;
        public static final int ORG_PROFILE_PARIS_DECLARATION = 7;
              
	
	
	public static AmpWidget getWidget(Long widgetId) throws DgException{
		AmpWidget result = null;
		Session session = PersistenceManager.getRequestDBSession();
		try {
			result = (AmpWidget)session.load(AmpWidget.class, widgetId);
		} catch (Exception e) {
			logger.error(e);
			throw new DgException("Cannot load widget from db with id="+widgetId,e);
		}
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
		String oql="from "+AmpDaWidgetPlace.class.getName()+" as p order by p.lastRendered desc";
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
	 * Saves place object to db.
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

	/**
	 * Saves multiple places together in one transaction.
	 * @param places
	 * @throws DgException
	 */
	public static void savePlaces(List<AmpDaWidgetPlace> places) throws DgException{
		Session session=PersistenceManager.getRequestDBSession();
		Transaction tx=null;
		try {
			tx=session.beginTransaction();
			for (AmpDaWidgetPlace place : places) {
				session.saveOrUpdate(place);
			}
			tx.commit();
		} catch (Exception e) {
			if (tx!=null){
				try {
					tx.rollback();
				} catch (Exception e1) {
					throw new DgException("Cannot rallback Widget places save or update!",e1);
				}
			}
			throw new DgException("Cannot save or update Widget places!",e);
		}
		
	}
	
	/**
	 * Retrieves widget assigned to particular place.
	 * @param placeId
	 * @return
	 * @throws DgException
	 */
	public static AmpWidget getWidgetOnPlace(Long placeId)throws DgException{
		AmpDaWidgetPlace place = getPlace(placeId);
		if (place!=null){
			return place.getAssignedWidget();
		}
		return null;
	}
	
	/**
	 * Retrieves all places to which the widget is assigned.
	 * widget is specified by id.
	 * @param widgetId widget for which to search places.
	 * @return
	 * @throws DgException
	 */
	@SuppressWarnings("unchecked")
	public static List<AmpDaWidgetPlace> getWidgetPlaces(Long widgetId) throws DgException{
		List<AmpDaWidgetPlace> places = new ArrayList<AmpDaWidgetPlace>();
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

	/**
	 * Retrieves places with IDs specified.
	 * @param pids id's of places.
	 * @return
	 * @throws DgException
	 */
	@SuppressWarnings("unchecked")
	public static List<AmpDaWidgetPlace> getPlacesWithIDs(Long[] pids) throws DgException{
		List<AmpDaWidgetPlace> places = new ArrayList<AmpDaWidgetPlace>(pids.length);
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

	/**
	 * Removes assignment to this widget from all widget place objects.
	 * Used when deleting widget.
	 * @param widgetId
	 * @throws DgException
	 */
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
	
	/**
	 * Deletes widget place from db.
	 * @param placeId
	 * @throws DgException
	 */
	public static void deleteWidgetPlace(Long placeId) throws DgException{
		AmpDaWidgetPlace place = getPlace(placeId);
		deleteWidgetPlace(place);
	}

	/**
	 * Deletes widget place from db.
	 * @param place
	 * @throws DgException
	 */
	public static void deleteWidgetPlace(AmpDaWidgetPlace place) throws DgException{
		Session session = PersistenceManager.getRequestDBSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.delete(place);
			tx.commit();
		} catch (Exception e) {
			if (tx!=null){
				try {
					tx.rollback();
				} catch (Exception e1) {
					logger.error(e1);
					throw new DgException("Cannot rallback place removal",e1);
				}
			}
			logger.error(e);
			throw new DgException("Cannot remove place",e);
		}
	}
	
	/**
	 * Assigns widget to all places in collection.
	 * widget parameter can be null, in this case places will have no widget assigned. 
	 * @param places
	 * @param widget can be null.
	 * @throws DgException
	 */
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

	/**
	 * Returns all widgets from db.
	 * This will include chart and table widgets.
	 * @return
	 * @throws DgException
	 */
	@SuppressWarnings("unchecked")
	public static List<AmpWidget> getAllWidgets() throws DgException{
		Session session = PersistenceManager.getRequestDBSession();
		String oql = "from "+AmpWidget.class.getName() + " as w order by w.name";
		List<AmpWidget> result = null;
		try {
			Query query = session.createQuery(oql);
			result = query.list();
		} catch (Exception e) {
			logger.error(e);
			throw new DgException("Cannot clear places",e);
		}
		return result;
	}

	/**
	 * Return all widgets converted to {@link WidgetPlaceHelper} beans.
	 * @return
	 * @throws DgException
	 */
	@SuppressWarnings("unchecked")
	public static List<WidgetPlaceHelper> getAllWidgetsHelpers() throws DgException{
		List<WidgetPlaceHelper> result = null;
		Session session = PersistenceManager.getRequestDBSession();
		String oql = "from "+AmpWidget.class.getName() + " as w order by w.name";
		try {
			Query query = session.createQuery(oql);
			List list = query.list();
			if (list==null || list.size()==0) return null;
			result = new ArrayList<WidgetPlaceHelper>(list.size());
			for (Object row : list) {
				AmpWidget widget = (AmpWidget)row;
				WidgetPlaceHelper helper = new WidgetPlaceHelper(widget);
				result.add(helper);
			}
		} catch (Exception e) {
			logger.error(e);
			throw new DgException("Cannot clear places",e);
		}
		return result;
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
        
        /**
	 * Returns true if IndicatorSector exists .
         * @param sectorId id of sector 
         * @param locationId id of location
         * @param indicatorId id of indicator {@link AmpIndicator}
         * @param indSectId id of IndicatorSector {@link IndicatorSector}
	 * @return true if IndicatorSector exists otherwise false
	 * @throws DgException
	 */
        public static boolean indicarorSectorExist(Long sectorId,Long locationId,Long indicatorId,Long indSectId) throws DgException{
            boolean exists=false;
            Session session = PersistenceManager.getRequestDBSession();
            String oql = "from "+IndicatorSector.class.getName() +
                    " inds where inds.sector=:sectorId and inds.location=:locationId and inds.indicator=:indicatorId";
            if(indSectId!=null){
                oql+=" and inds.id!=:indSectId";
            }
            try {
			Query query = session.createQuery(oql);
			query.setLong("sectorId", sectorId);
                        query.setLong("locationId",locationId);
                        query.setLong("indicatorId", indicatorId);
                        if (indSectId != null) {
                            query.setLong("indSectId", indSectId);
                        }
			if(query.list().size()>0){
                            exists=true;
                        }
		} catch (Exception e) {
			logger.error(e);
			throw new DgException("cannot load SectorIndicators!",e);
		}
            return exists;
            
        }
}
