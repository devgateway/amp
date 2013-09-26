package org.digijava.module.widget.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.tiles.ComponentContext;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.utils.AmpCollectionUtils.KeyWorker;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivityGroup;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.IndicatorSector;
import org.digijava.module.aim.exception.NoCategoryClassException;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.logic.FundingCalculationsHelper;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryClass;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.gis.util.DbUtil;
import org.digijava.module.gis.util.GenericIdGetter;
import org.digijava.module.widget.dbentity.AmpDaWidgetPlace;
import org.digijava.module.widget.dbentity.AmpWidget;
import org.digijava.module.widget.helper.ActivitySectorDonorFunding;
import org.digijava.module.widget.helper.TopDonorGroupHelper;
import org.digijava.module.widget.helper.WidgetPlaceHelper;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.mathcs.backport.java.util.Arrays;

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
        

    public static final int SECTOR_TABLE = 6;
    public static final int PARIS_INDICAROR_TABLE = 7;
    public static final int TOP_TEN_DONORS = 8;
        

              
	
	
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
	 * Return all Org Profile page places objects from db.
	 * @return
	 * @throws DgException
	 */
	@SuppressWarnings("unchecked")
	public static List<AmpDaWidgetPlace> getAllOrgProfilePlaces() throws DgException{
		List<AmpDaWidgetPlace> result=null;
		Session session = PersistenceManager.getRequestDBSession();
        // org profile places are starting with orgprof_chart_place prefix
		String oql="from "+AmpDaWidgetPlace.class.getName()+" as p where p.name like 'orgprof_chart_place%'  order by p.name ";
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
//beginTransaction();
			session.saveOrUpdate(place);
			//tx.commit();
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
//beginTransaction();
			for (AmpDaWidgetPlace place : places) {
				session.saveOrUpdate(place);
			}
			//tx.commit();
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
        public static void clearPlacesForWidget(Long widgetId) throws DgException {
            Session session = PersistenceManager.getRequestDBSession();
            clearPlacesForWidget(widgetId, session);
         }

         /**
         * Removes assignment to this widget from all widget place objects.
         * Used when deleting widget.
         * @param widgetId
         * @throws DgException
         */
        public static void clearPlacesForWidget(Long widgetId, Session session) throws DgException {
            List<AmpDaWidgetPlace> places = getWidgetPlaces(widgetId);
            if (places != null && places.size() > 0) {

                Transaction tx = null;
                try {
                    if (session == null) {
                        /* we will use this in the Junit tests mainly because
                        getRequestDBSession() don't work there*/
                        session = PersistenceManager.getSession();
                    }
//beginTransaction();
                    for (AmpDaWidgetPlace place : places) {
                        place.setAssignedWidget(null);
                        session.update(place);
                    }
                    //tx.commit();
                } catch (Exception e) {
                    if (tx != null) {
                        try {
                            tx.rollback();
                        } catch (Exception e1) {
                            logger.error(e1);
                            throw new DgException("Cannot rallback places clearing", e1);
                        }
                    }
                    logger.error(e);
                    throw new DgException("Cannot clear rallbacks", e);
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
         * @param widget
         * @throws DgException
         */
        public static void deleteWidgetPlace(AmpDaWidgetPlace place) throws DgException {
            try {
                Session session = PersistenceManager.getRequestDBSession();
                deleteWidgetPlace(place, session);
            } catch (Exception ex) {
                throw new DgException("Cannot delete Widget!", ex);
            }

        }
	/**
	 * Deletes widget place from db.
	 * @param place
	 * @throws DgException
	 */
	public static void deleteWidgetPlace(AmpDaWidgetPlace place,Session session) throws DgException{
		Transaction tx = null;
		try {
           if (session == null) {
                /* we will use this in the Junit tests mainly because
                getRequestDBSession() don't work there*/
                session = PersistenceManager.getSession();
            }
//beginTransaction();
			session.delete(place);
			//tx.commit();
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
//beginTransaction();
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
			//tx.commit();
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
	public static List<WidgetPlaceHelper> getAllWidgetsHelpers(HttpServletRequest request) throws DgException{
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
				WidgetPlaceHelper helper = new WidgetPlaceHelper(widget,request);
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

        
    public static Collection<ActivitySectorDonorFunding> getDonorSectorFunding(Long[] donorIDs, Date fromDate, Date toDate, Long[] sectorIds, HttpSession httpSession) throws DgException 
    {
        Map<Long, ActivitySectorDonorFunding> globalActivityFundingInfos = new HashMap<Long, ActivitySectorDonorFunding>();
        
        if (sectorIds != null) {
        	
            Map<Long, ActivitySectorDonorFunding> activityFundingInfos = new HashMap<Long, ActivitySectorDonorFunding>();            
            Set<Long> allActivityIdsSet = DbUtil.getAllLegalAmpActivityIds();
            
            for (Long sectId : sectorIds) {
                Set<Long> ids = new HashSet<Long>();
                ids.add(sectId);
                ids = SectorUtil.getRecursiveChildrenOfSectors(ids);
                Long[] sectIds = new Long[ids.size()];
                ids.toArray(sectIds);
                // get all fundings...
                List result = getFunding(donorIDs, fromDate, toDate, sectIds, httpSession, allActivityIdsSet);
                //Process grouped data
                if (result != null) {

                    for (Object row : result) {
                        Object[] rowData = (Object[]) row;
                        AmpActivitySector activitySector = (AmpActivitySector) rowData[0];
                        AmpSector sector = activitySector.getSectorId();
                        AmpActivityVersion activity = activitySector.getActivityId();
                        Long activityId = activitySector.getActivityId().getAmpActivityId();
                        AmpOrganisation org = (AmpOrganisation) rowData[1];
                        /*we are not interested in all sectors of activity, only selected ones,
                        same with donors*/
                        ActivitySectorDonorFunding activityFundngObj = activityFundingInfos.get(activityId);
                        //if not create and add to map
                        if (activityFundngObj == null) {
                            activityFundngObj = new ActivitySectorDonorFunding();
                            //Init name property. Quick fix.
                            activity.getName();
                            activityFundngObj.setActivity(activity);
                            activityFundngObj.setSectors(new ArrayList<AmpSector>());
                            activityFundngObj.setDonorOrgs(new ArrayList<AmpOrganisation>());
                            activityFundingInfos.put(activityId, activityFundngObj);

                        }

                        if (!activityFundngObj.getSectors().contains(sector)) {
                            activityFundngObj.getSectors().add(sector);
                        }
                        if (!activityFundngObj.getDonorOrgs().contains(org)) {
                            activityFundngObj.getDonorOrgs().add(org);
                        }
                    }
                    System.out.format("\t-->for sector %d, I have %d activities\n", sectId, activityFundingInfos.values().size());
                    getFunding(activityFundingInfos.values(), fromDate, toDate, sectIds);                    
//                    for (Iterator<ActivitySectorDonorFunding> it = activityFundingInfos.values().iterator(); it.hasNext();) {
//                        ActivitySectorDonorFunding actSectDonFund = it.next();
//                        //System.out.format("\t->activity %d has %d donorIds\n", actSectDonFund.getActivity().getAmpActivityId(), actSectDonFund.getDonorOrgs().size());
//                        // get funding for particular activity
//                        //getFunding(actSectDonFund, fromDate, toDate); // BOZO
//                    }
                }
                globalActivityFundingInfos.putAll(activityFundingInfos);
            }
        }

        return globalActivityFundingInfos.values();
    }
    
      
    public static List getFunding(Long[] donorIDs, Date fromDate, 
    		Date toDate, Long[] sectorIds, 
    		HttpSession httpSession, Set<Long> allActivityIdsSet) throws DgException 
    {
    	long aaa = System.currentTimeMillis();
    	
    	AmpCategoryClass catClass = null;
    	Long actualCommitmentCatValId = null;
        String activityWhereclause = DbUtil.generateWhereclause(allActivityIdsSet, new GenericIdGetter());

          try {
              catClass = CategoryManagerUtil.loadAmpCategoryClassByKey(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getCategoryKey());
              for (AmpCategoryValue val : catClass.getPossibleValues()) {
                  if (val.getValue().equals(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey())) {
                      actualCommitmentCatValId = val.getId();
                      break;
                  }
              }
          } catch (NoCategoryClassException e) {
              e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
          }

          Session session = PersistenceManager.getRequestDBSession();
//          StringBuilder lastVersionsQry = new StringBuilder("select actGroup.ampActivityLastVersion.ampActivityId from ");
//          lastVersionsQry.append(AmpActivityGroup.class.getName());
//          lastVersionsQry.append(" as actGroup");
          //Query actGrpupQuery = session.createQuery(lastVersionsQry.toString());
          //List lastVersions = actGrpupQuery.list();
          //String lasVersionsWhereclause = org.digijava.module.gis.util.DbUtil.generateWhereclause(lastVersions, new DbUtil.GenericIdGetter());
          
                    
        String oql = "select  actSec, f.ampDonorOrgId ";
        oql += " from ";
        oql += AmpFundingDetail.class.getName() + " as fd inner join fd.ampFundingId f ";
        oql += "   inner join f.ampActivityId act " + " inner join act.sectors actSec ";
        oql += " inner join actSec.sectorId sec ";
        oql += " inner join actSec.classificationConfig config ";
        if (donorIDs != null && donorIDs.length > 0) {
            oql += "  inner join f.ampDonorOrgId org ";
        }
        oql += " where  fd.adjustmentType = " + actualCommitmentCatValId.toString();
        if (donorIDs != null && donorIDs.length > 0) {
            oql += " and (org.ampOrgId in (:donors) ) ";
        }
        oql += " and (fd.transactionDate between :fDate and  :eDate ) ";
        oql += " and sec.ampSectorId in (:sectors) ";
        oql += " and config.name='Primary' ";
        oql += "  and act.ampActivityId in" + activityWhereclause;

        oql += " order by actSec";

        @SuppressWarnings(value = "unchecked")
        List result = null;
        try {
            Query query = session.createQuery(oql);
            if (fromDate != null && toDate != null) {
                query.setDate("fDate", fromDate);
                query.setDate("eDate", toDate);
            }
            if (donorIDs != null && donorIDs.length > 0) {
            	query.setParameterList("donors", donorIDs);
            }
            query.setParameterList("sectors", sectorIds);
            result = query.list();
        } catch (Exception e) {
            throw new DgException("Cannot load sector fundings by donors from db", e);
        }
        long timeSpent = System.currentTimeMillis() - aaa;
        System.out.format("\t%d milliseconds spent while fetching funding for sectors %s\n", timeSpent, Util.toCSStringForIN(Arrays.asList(sectorIds)));
        return result;
    }

//    @SuppressWarnings("unchecked")
//    public static void getFunding(ActivitySectorDonorFunding activityFundngObj, Date fromDate, Date toDate) throws DgException 
//    {
//        AmpCategoryClass catClass = null;
//        Long actualCommitmentCatValId = null;
//        try {
//            catClass = CategoryManagerUtil.loadAmpCategoryClassByKey(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getCategoryKey());
//            for (AmpCategoryValue val : catClass.getPossibleValues()) {
//                if (val.getValue().equals(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey())) {
//                    actualCommitmentCatValId = val.getId();
//                    break;
//                }
//            }
//        } catch (NoCategoryClassException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
//
//        AmpActivityVersion activity = activityFundngObj.getActivity();
//        List<AmpSector> sectors = activityFundngObj.getSectors();
//        List<AmpOrganisation> donors = activityFundngObj.getDonorOrgs();
//        Long[] sectorIDs = new Long[sectors.size()];
//        Long[] donorIDs = new Long[donors.size()];
//        int index = 0;
//        for (AmpSector sector : sectors) {
//            sectorIDs[index] = sector.getAmpSectorId();
//            index++;
//        }
//        index = 0;
//        for (AmpOrganisation organisation : donors) {
//            donorIDs[index] = organisation.getAmpOrgId();
//            index++;
//        }
//
//        long aaa = System.currentTimeMillis();
//        String oql = "select new AmpFundingDetail(fd.transactionType,fd.adjustmentType,fd.transactionAmount,fd.transactionDate,fd.ampCurrencyId,actSec.sectorPercentage,fd.fixedExchangeRate) ";
//        oql += " from ";
//        oql += AmpFundingDetail.class.getName() + " as fd inner join fd.ampFundingId f ";
//        oql += "   inner join f.ampActivityId act " + " inner join act.sectors actSec ";
//        oql += " inner join actSec.sectorId sec ";
//        oql += " inner join actSec.activityId act ";
//        oql += " inner join actSec.classificationConfig config ";
//        oql += " where  fd.adjustmentType = " + actualCommitmentCatValId.toString();
//        oql += " and (f.ampDonorOrgId in (" + ChartWidgetUtil.getInStatment(donorIDs) + ") ) ";
//        oql += " and (fd.transactionDate between :fDate and  :eDate ) ";
//        oql += " and actSec.sectorId in (" + ChartWidgetUtil.getInStatment(sectorIDs) + ") ";
//        oql += " and act=:actId ";
//        oql += " and config.name='Primary' ";
//        oql += " order by actSec";
//        Session session = PersistenceManager.getRequestDBSession();
//        Query query = session.createQuery(oql);
//        if (fromDate != null && toDate != null) {
//            query.setDate("fDate", fromDate);
//            query.setDate("eDate", toDate);
//            query.setLong("actId", activity.getAmpActivityId());
//        }
//
//        List<AmpFundingDetail> fundingDets = query.list();
//        FundingCalculationsHelper cal = new FundingCalculationsHelper();
//        String baseCurr = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.BASE_CURRENCY);
//        if (baseCurr == null) {
//            baseCurr = "USD";
//        }
//        cal.doCalculations(fundingDets, baseCurr);
//        long delta = System.currentTimeMillis() - aaa;
//        System.out.format("\tgetFunding() for activity %d took %d milliseconds\n", activity.getAmpActivityId(), delta);
//        activityFundngObj.setCommitment(cal.getTotActualComm());
//        activityFundngObj.setDisbursement(cal.getTotActualDisb());
//        activityFundngObj.setExpenditure(cal.getTotActualExp());
//    }

    @SuppressWarnings("unchecked")
    public static void getFunding(Collection<ActivitySectorDonorFunding> activityFundngObjs, Date fromDate, Date toDate, Long[] sectorIDs) throws DgException 
    {
        AmpCategoryClass catClass = null;
        Long actualCommitmentCatValId = null;
        try {
            catClass = CategoryManagerUtil.loadAmpCategoryClassByKey(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getCategoryKey());
            for (AmpCategoryValue val : catClass.getPossibleValues()) {
                if (val.getValue().equals(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey())) {
                    actualCommitmentCatValId = val.getId();
                    break;
                }
            }
        } catch (NoCategoryClassException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        //Set<Long> activityIds = new HashSet<Long>();
        Map<Long, ActivitySectorDonorFunding> fundingsItems = new HashMap<Long, ActivitySectorDonorFunding>(); // Map<activityId, funding_item>
        //Set<Long> donorIds = new HashSet<Long>();
        
        // preprocess activity list so that we don't have to make a fetch for each activity
        Map<Long, Set<Long>> acceptableDonorIds = new HashMap<Long, Set<Long>>(); // Map<activity_id,Set<DonorIds>> 
        for(ActivitySectorDonorFunding asdf:activityFundngObjs)
        {
        	long activityId = asdf.getActivity().getAmpActivityId();
        	fundingsItems.put(activityId, asdf);
        	if (!acceptableDonorIds.containsKey(activityId))
        		acceptableDonorIds.put(activityId, new HashSet<Long>());

        	for(AmpOrganisation donor:asdf.getDonorOrgs())
        		acceptableDonorIds.get(activityId).add(donor.getAmpOrgId());
        }
        
        Long[] allActivityIds = new Long[fundingsItems.keySet().size()];
        int index = 0;
        for(Long aid:fundingsItems.keySet())
        {
        	allActivityIds[index] = aid;
        	index ++;
        }
        //AmpActivityVersion activity = activityFundngObj.getActivity();
        //List<AmpOrganisation> donors = activityFundngObj.getDonorOrgs();
        //Long[] donorIDs = new Long[donors.size()];
        //int index = 0;
        //for (AmpOrganisation organisation : donors) {
        //    donorIDs[index] = organisation.getAmpOrgId();
        //    index++;
        //}

        long aaa = System.currentTimeMillis();
        //String oql = "select new AmpFundingDetail(fd.transactionType,fd.adjustmentType,fd.transactionAmount,fd.transactionDate,fd.ampCurrencyId,actSec.sectorPercentage,fd.fixedExchangeRate), f.ampDonorOrgId, act ";
        String oql = "select fd.transactionType, fd.adjustmentType, fd.transactionAmount, fd.transactionDate, fd.ampCurrencyId, actSec.sectorPercentage, fd.fixedExchangeRate, f.ampDonorOrgId.ampOrgId, act.ampActivityId ";
        oql += " from ";
        oql += AmpFundingDetail.class.getName() + " as fd inner join fd.ampFundingId f ";
        oql += "   inner join f.ampActivityId act " + " inner join act.sectors actSec ";
        oql += " inner join actSec.sectorId sec ";
        oql += " inner join actSec.activityId act ";
        oql += " inner join actSec.classificationConfig config ";
        oql += " where  fd.adjustmentType = " + actualCommitmentCatValId.toString();
        // oql += " and (f.ampDonorOrgId in (" + ChartWidgetUtil.getInStatment(donorIDs) + ") ) ";
        oql += " and (fd.transactionDate between :fDate and  :eDate ) ";
        oql += " and actSec.sectorId in (" + ChartWidgetUtil.getInStatment(sectorIDs) + ") ";
        oql += " and act in (" + ChartWidgetUtil.getInStatment(allActivityIds) + ") ";
        oql += " and config.name='Primary' ";
        oql += " order by actSec";
        Session session = PersistenceManager.getRequestDBSession();
        Query query = session.createQuery(oql);
        if (fromDate != null && toDate != null) {
            query.setDate("fDate", fromDate);
            query.setDate("eDate", toDate);
            //query.setLong("actId", activity.getAmpActivityId());
        }

        List<Object[]> allData = query.list();
        Map<Long, List<AmpFundingDetail>> filteredFundingDets = new HashMap<Long, List<AmpFundingDetail>>(); //Map<ampActivityId>
        
        for(Object[] fundingData:allData)
        {
        	AmpFundingDetail detail = new AmpFundingDetail(
        			(Integer) fundingData[0],			// transactionType
        			(AmpCategoryValue) fundingData[1],	// adjustmentType
        			(Double) fundingData[2],			// transactionAmount
        			(Date) fundingData[3],				// transactionDate
        			(AmpCurrency) fundingData[4],		// ampCurrencyId
        			(Float) fundingData[5],				// percentage
        			(Double) fundingData[6]				// Double.parseDouble(fundingData[6].toString()) //fixedExchangeRate
        		);
        	Long donorOrgId = (Long) fundingData[7];
        	Long activityId = (Long) fundingData[8];
        	
        	if (acceptableDonorIds.get(activityId).contains(donorOrgId))
        	{
        		if (!filteredFundingDets.containsKey(activityId))
        			filteredFundingDets.put(activityId, new ArrayList<AmpFundingDetail>());
        		filteredFundingDets.get(activityId).add(detail);
        	}
        }
        
        String baseCurr = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.BASE_CURRENCY);
        if (baseCurr == null) {
            baseCurr = "USD";
        }
        
        long delta = System.currentTimeMillis() - aaa;
        System.out.format("\tgetFunding() for %d activities took %d milliseconds\n", activityFundngObjs.size(), delta);
        for(long ampActivityId:filteredFundingDets.keySet())
        {
        	List<AmpFundingDetail> msh = filteredFundingDets.get(ampActivityId);
            FundingCalculationsHelper cal = new FundingCalculationsHelper();
        	cal.doCalculations(msh, baseCurr);
        	ActivitySectorDonorFunding activityFundngObj = fundingsItems.get(ampActivityId);
        	activityFundngObj.setCommitment(cal.getTotActualComm());
        	activityFundngObj.setDisbursement(cal.getTotActualDisb());
        	activityFundngObj.setExpenditure(cal.getTotActualExp());        	
        }
    }

    @SuppressWarnings("unchecked")
	public static List<TopDonorGroupHelper> getTopTenDonorGroups(Integer fromYear,Integer toYear) throws DgException {
        AmpCategoryClass catClass = null;
        Long actualCommitmentCatValId = null;
        try {
            catClass = CategoryManagerUtil.loadAmpCategoryClassByKey(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getCategoryKey());
            for (AmpCategoryValue val : catClass.getPossibleValues()) {
                if (val.getValue().equals(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey())) {
                    actualCommitmentCatValId = val.getId();
                    break;
                }
            }
        } catch (NoCategoryClassException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        List<TopDonorGroupHelper> donorGroups = new ArrayList<TopDonorGroupHelper>();
        Date fromDate = null;
		Date toDate = null;
		if (fromYear!=null && toYear!=null){
			/**
			 * we should get default calendar and from/to dates should be taken according to it.
			 * So for example, if some calendar's startMonth is 1st of July, we should take an interval from
			 * year's(parameter that is passed to function) 1st of July to the next year's 1st of July
			 */
			Long defaultCalendarId=new Long(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_CALENDAR));
			AmpFiscalCalendar calendar=FiscalCalendarUtil.getAmpFiscalCalendar(defaultCalendarId);
			fromDate=ChartWidgetUtil.getStartOfYear(fromYear.intValue(),calendar.getStartMonthNum()-1,calendar.getStartDayNum());
			//we need data including the last day of toYear,this is till the first day of toYear+1
			int MILLISECONDS_IN_DAY = 1000 * 60 * 60 * 24;
			toDate =new Date(ChartWidgetUtil.getStartOfYear(toYear.intValue()+1,calendar.getStartMonthNum()-1,calendar.getStartDayNum()).getTime()-MILLISECONDS_IN_DAY);
		}		
        Session session = PersistenceManager.getRequestDBSession();
        String queryString = " select new   org.digijava.module.widget.helper.TopDonorGroupHelper(orgGrp.orgGrpName, sum(fd.transactionAmountInBaseCurrency)) ";
        queryString += " from ";
        queryString += AmpFundingDetail.class.getName() +
                " as fd  inner join fd.ampFundingId f ";
        queryString += " inner join f.ampDonorOrgId org  ";
        queryString += " inner join org.orgGrpId orgGrp ";
        queryString += "  inner join f.ampActivityId act ";
        queryString += " where  fd.transactionType = 0 and fd.adjustmentType = " + actualCommitmentCatValId.toString();
        queryString += " and act.team is not null ";
        queryString += " and  (fd.transactionDate>=:startDate and fd.transactionDate<:endDate)  ";
        queryString += " group by orgGrp.ampOrgGrpId, orgGrp.orgGrpName ";
        queryString += " order by sum(fd.transactionAmountInBaseCurrency) desc ";
        try {
            Query query = session.createQuery(queryString);
            query.setDate("startDate", fromDate);
            query.setDate("endDate", toDate);
            query.setMaxResults(10);
            donorGroups = query.list();
        } catch (Exception e) {
            logger.error(e);
            throw new DgException("cannot load top donors!", e);
        }
        return donorGroups;
    }
}
