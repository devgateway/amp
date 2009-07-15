package org.digijava.module.gis.util;

import java.text.Collator;
import java.util.ArrayList;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.IndicatorConnection;
import org.digijava.module.aim.dbentity.IndicatorSector;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.gis.dbentity.GisMap;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: </p>
 *
 * @author George Kvizhinadze
 * @version 1.0
 */
public class DbUtil {
    private static Logger logger = Logger.getLogger(DbUtil.class);

    public DbUtil() {
    }

    public static void saveMap(GisMap map) throws DgException {
        Session sess = null;
        Transaction tx = null;
        try {
            sess = PersistenceManager.getSession();
            tx = sess.beginTransaction();
            sess.save(map);
            tx.commit();
        } catch (Exception ex) {
            if (tx != null) {
                try {
                    tx.rollback();
                } catch (HibernateException ex1) {
                    logger.warn("rollback() failed", ex1);
                }
            }
            logger.debug("Unable to save the map", ex);
            throw new DgException(
                    "Unable to save the map", ex);
        } finally {
            if (sess != null) {
                try {
                    PersistenceManager.releaseSession(sess);
                } catch (Exception ex1) {
                    logger.warn("releaseSession() failed", ex1);
                }
            }
        }
    }

    public static GisMap getMap(long id) {
        Session session = null;
        GisMap gisMap = null;
        try {
            session = PersistenceManager.getSession();
            gisMap = (GisMap) session.load(
                    GisMap.class,
                    new Long(id));
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                PersistenceManager.releaseSession(session);
            } catch (Exception ex2) {
                ex2.printStackTrace();
            }
        }
        return gisMap;
    }

    public static GisMap getMapByCode(String code, int level) {
        Session session = null;
        GisMap map = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            Query q = session.createQuery("from " +
                                          GisMap.class.getName() +
                                          " rs where (rs.mapCode=:mapCode) and rs.mapLevel=:mapLevel");
            q.setParameter("mapCode", code, Hibernate.STRING);
            q.setParameter("mapLevel", level, Hibernate.INTEGER);
            Iterator it = q.iterate();
            if (it.hasNext()) {
                map = (GisMap) it.next();
            } else {
                logger.debug("Unable to get map from DB");
            }
        } catch (Exception ex) {
            logger.debug("Unable to get map from DB", ex);
        }
        return map;

    }

    //TODO To be moved to aid module later
    public static List getUsedSectors() {
        List retVal = null;
        Session session = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            Query q = session.createQuery("select sec.sectorId, count(*) from " +
                                          AmpActivitySector.class.getName() +
                                          " sec where exists (from " +
                                          AmpFunding.class.getName() +
                                          " fnd where sec.activityId.ampActivityId=fnd.ampActivityId.ampActivityId)" +
                                          " and exists (from " +
                                          AmpActivityLocation.class.getName() +
                                          " loc where sec.activityId.ampActivityId=loc.activity.ampActivityId and" +
                                          " loc.location.region is not null)" +
                                          " and size(sec.activityId.locations)>0" +
                                          " group by sec.sectorId");


            retVal = q.list();
        } catch (Exception ex) {
            logger.debug("Unable to used sectors from DB", ex);
        }
        return retVal;
    }

    public static Collection getPrimaryToplevelSectors(){
        Collection retVal = null;
        try {
            retVal = SectorUtil.getSectorLevel1(new Integer(SectorUtil.getPrimaryConfigClassificationId().intValue()));
        } catch (DgException ex) {
            ex.printStackTrace();
        }
        return retVal;
    }

    public static List getSectorFoundings(Long sectorId) {
        List retVal = null;
        Session session = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            Query q = null;
            if (sectorId > -1) {
                q = session.createQuery("select sec.activityId, sec.sectorPercentage from " +AmpActivitySector.class.getName() + " sec where sec.sectorId.ampSectorId=:sectorId or sec.sectorId.parentSectorId.ampSectorId=:sectorId");
                q.setParameter("sectorId", sectorId, Hibernate.LONG);
            } else {
                q = session.createQuery("select distinct sec.activityId, sec.sectorPercentage from " +AmpActivitySector.class.getName() + " sec");
            }
            retVal = q.list();
        } catch (Exception ex) {
            logger.debug("Unable to get map from DB", ex);
        }
        return retVal;
    }

    public static List getIndicatorsForSector(Long sectorId, int mapLevel) {
       List retVal = null;
       Session session = null;
       try {
           session = PersistenceManager.getRequestDBSession();

           StringBuffer querySrc = new StringBuffer();
           querySrc.append("select ds.indicator.indicatorId, ds.indicator.name from ");
           querySrc.append(IndicatorConnection.class.getName());
           querySrc.append(" ds");
           querySrc.append(" where ds.sector.ampSectorId=:sectorId and");
           querySrc.append(" (ds.location.regionLocation.parentCategoryValue.value='Region' or");
           querySrc.append(" ds.location.regionLocation.parentCategoryValue.value='District')");
           querySrc.append(" group by ds.indicator.indicatorId order by ds.indicator.name");
           Query q = session.createQuery(querySrc.toString());
           q.setParameter("sectorId", sectorId, Hibernate.LONG);
           List allIndicatorList = q.list();

           //Get used indicator list for this map level
           StringBuffer querySrc1 = new StringBuffer();
           querySrc1.append("select distinct ds.indicatorConnection.indicator.indicatorId from ");
           querySrc1.append(AmpIndicatorValue.class.getName());
           querySrc1.append(" ds where ds.indicatorConnection.sector.ampSectorId=:sectorId and");

           if (mapLevel == 2) {
               querySrc1.append(" ds.indicatorConnection.location.regionLocation.parentCategoryValue.value='Region'");
           } else if (mapLevel == 3) {
               querySrc1.append(" ds.indicatorConnection.location.regionLocation.parentCategoryValue.value='District'");
           }
           Query q1 = session.createQuery(querySrc1.toString());
           q1.setParameter("sectorId", sectorId, Hibernate.LONG);
           List usedIndicatorList = q1.list();

           retVal = new ArrayList();

           Iterator it = allIndicatorList.iterator();
           while (it.hasNext()) {
               Object [] obj = (Object[]) it.next();
               Object [] filtered = new Object[3];
               filtered[0] = obj[0];
               filtered[1] = obj[1];
               filtered[2] = new Boolean(usedIndicatorList.contains(obj[0]));

               retVal.add(filtered);
           }
       } catch (Exception ex) {
           logger.debug("Unable to get indicators from DB", ex);
       }
       return retVal;
   }

   public static List getIndicatorValuesForSectorIndicator(Long sectorId,Long indicatorId, Integer year, Long subgroupId, int areaLevel) {
          List retVal = null;
          Session session = null;
          try {
              session = PersistenceManager.getRequestDBSession();

              StringBuffer queryString = new StringBuffer();

              if (areaLevel==GisMap.MAP_LEVEL_REGION) {
                  queryString.append("select indVal.value, indConn.location.ampRegion.regionCode, indVal.source from ");
              } else if (areaLevel==GisMap.MAP_LEVEL_DISTRICT) {
                  queryString.append("select indVal.value, indConn.location.ampZone.zoneCode, indVal.source from ");
              }
              queryString.append(AmpIndicatorValue.class.getName());
              queryString.append(" indVal, ");
              queryString.append(IndicatorSector.class.getName());
              queryString.append(" indConn where indConn.sector.ampSectorId=:sectorId");
              queryString.append(" and indConn.indicator.indicatorId=:indicatorId ");
              queryString.append(" and indConn.id=indVal.indicatorConnection.id");

              queryString.append(" and indVal.subgroup.ampIndicatorSubgroupId=:subgroupId");

              if (year.intValue() > 0) {
                  queryString.append(" and year(indVal.valueDate)=:indValDate");
              }

              Query q = session.createQuery(queryString.toString());

              q.setParameter("sectorId", sectorId, Hibernate.LONG);
              q.setParameter("indicatorId", indicatorId, Hibernate.LONG);
              q.setParameter("subgroupId", subgroupId, Hibernate.LONG);

              if (year.intValue() > 0) {
                  q.setParameter("indValDate", year, Hibernate.INTEGER);
              }


              retVal = q.list();
          } catch (Exception ex) {
              logger.debug("Unable to get indicators from DB", ex);
          }
          return retVal;
   }

   public static List getIndicatorValuesForSectorIndicator(Long sectorId,Long indicatorId, DateInterval interval, Long subgroupId, int areaLevel) {
          List retVal = null;
          List qResult = null;
          Session session = null;
          try {
              session = PersistenceManager.getRequestDBSession();

              StringBuffer queryString = new StringBuffer();

//              if (areaLevel==GisMap.MAP_LEVEL_REGION) {
                  queryString.append("select indVal, indConn.location.regionLocation.name from ");
                  /*
              } else if (areaLevel==GisMap.MAP_LEVEL_DISTRICT) {
                  queryString.append("select indVal.value, indConn.location.ampZone.zoneCode, indVal.source from ");
              }*/
              queryString.append(AmpIndicatorValue.class.getName());
              queryString.append(" indVal, ");
              queryString.append(IndicatorSector.class.getName());
              queryString.append(" indConn where indConn.sector.ampSectorId=:sectorId");
              queryString.append(" and indConn.indicator.indicatorId=:indicatorId ");
              queryString.append(" and indConn.id=indVal.indicatorConnection.id");

              queryString.append(" and indVal.subgroup.ampIndicatorSubgroupId=:subgroupId");

              if (interval != null) {
                  queryString.append(" and indVal.dataIntervalStart=:intervalStart");
                  queryString.append(" and indVal.dataIntervalEnd=:intervalEnd");
              }

              Query q = session.createQuery(queryString.toString());

              q.setParameter("sectorId", sectorId, Hibernate.LONG);
              q.setParameter("indicatorId", indicatorId, Hibernate.LONG);
              q.setParameter("subgroupId", subgroupId, Hibernate.LONG);

              if (interval != null) {
                  q.setParameter("intervalStart", interval.getStart(), Hibernate.DATE);
                  q.setParameter("intervalEnd", interval.getEnd(), Hibernate.DATE);
              }


              qResult = q.list();
          } catch (Exception ex) {
              logger.debug("Unable to get indicators from DB", ex);
          }

          if (qResult != null) {
              retVal = new ArrayList();
              Iterator it = qResult.iterator();
              while (it.hasNext()) {
                  Object[] tmpObj = (Object[])it.next();
                  AmpIndicatorValue indValObj = (AmpIndicatorValue)tmpObj[0];
                  String locCode = (String)tmpObj[1];
                  Object[] actualObj = new Object[3];
                  actualObj[0] = indValObj.getValue();
                  actualObj[1] = locCode;
                  actualObj[2] = indValObj.getSource();

                  retVal.add(actualObj);
              }
          }
          return retVal;
   }


   public static List getIndicatorValuesForSectorIndicator(Long sectorId,Long indicatorId, Long subgroupId) {
       return getIndicatorValuesForSectorIndicator(sectorId, indicatorId, new Integer(-1), subgroupId, GisMap.MAP_LEVEL_REGION);
   }

    public static Double getActivityFoundings(Long activityId) {
        Double retVal = null;
        Session session = null;
        GisMap map = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            Query q = session.createQuery("select sum(fd.transactionAmount) from " + AmpFundingDetail.class.getName() + " fd where fd.ampFundingId.ampActivityId.ampActivityId=:activityId");
            q.setParameter("activityId", activityId, Hibernate.LONG);
            List tmpLst = q.list();
            if (!tmpLst.isEmpty())
            retVal = (Double)tmpLst.get(0);
        } catch (Exception ex) {
            logger.debug("Unable to get map from DB", ex);
        }
        return retVal;
    }

    public static Double getTotalActivityFoundings(String ActIdWhereclause) {
            Double retVal = null;
            Session session = null;
            try {
                session = PersistenceManager.getRequestDBSession();
                Query q = session.createQuery("select sum(fd.transactionAmount) from " +AmpFundingDetail.class.getName() + " fd where fd.ampFundingId.ampActivityId.ampActivityId in ("+ActIdWhereclause + ")");
                List tmpLst = q.list();
                if (!tmpLst.isEmpty())
                retVal = (Double)tmpLst.get(0);
            } catch (Exception ex) {
                logger.debug("Unable to get map from DB", ex);
            }
            return retVal;
    }

    /**
     * loads  all IndicatorSector ;
     * @param indicatorValueId
     * @return IndicatorSector list
     *
     */

 public static List getAllIndicatorSectors() {
       List retVal = null;
       Session session = null;
       try {
           session = PersistenceManager.getRequestDBSession();
           String query=" from " + IndicatorSector.class.getName() + " indsec ";
           Query q = session.createQuery(query);
           retVal = q.list();
       } catch (Exception ex) {
           logger.debug("Unable to get indicators from DB", ex);
       }
       return retVal;
   }

    /**
     * counts  all IndicatorSector  ;
     * @param indicatorValueId
     * @return IndicatorSector list size
     *
     */
    public static int getAllIndicatorSectorsSize() {
        int retVal = 0;
        Session session = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            String query = "select count(indsec) from " +IndicatorSector.class.getName() +" indsec ";
            Query q = session.createQuery(query);
            if (q.uniqueResult() != null) {
                retVal = (Integer) q.uniqueResult();
            }
        } catch (Exception ex) {
            logger.debug("Unable to get indicators from DB", ex);
        }
        return retVal;
    }

    public static List<IndicatorSector> searchIndicatorSectors(String sortBy,String keyword,Long sectorId , Long regionId){
    	List<IndicatorSector> retVal = null;
    	Session session = null;
    	String queryString =null;
		Query query=null;
    	try{
    		session = PersistenceManager.getRequestDBSession();
            queryString="select indsec from " + IndicatorSector.class.getName() + " indsec inner join indsec.location loc inner join loc.location reg";
            if((keyword!=null && keyword.length()>0) || sectorId!=null || regionId != null ) {
            	queryString+=" where ";
            }
            //filter
            if(keyword!=null && keyword.length()>0){
            	queryString+=" indsec.indicator.name like '%" +keyword+ "%'";
            }
            if(sectorId!=null){
            	if(keyword!=null && keyword.length()>0){
            		queryString += " and ";
            	}
            	queryString+= " indsec.sector.ampSectorId="+sectorId;
            }
            if(regionId !=null){
            	if((keyword!=null && keyword.length()>0) || sectorId!=null){
            		queryString += " and ";
            	}
            	queryString+= " indsec.location.location.id="+regionId;
            }
            //sort
            if(sortBy==null || sortBy.equals("nameAscending")){
				queryString += " order by indsec.indicator.name" ;
			}else if(sortBy.equals("nameDescending")){
				queryString += " order by indsec.indicator.name desc" ;
			}else if(sortBy.equals("sectNameAscending")){
				queryString += " order by indsec.sector.name" ;
			}else if(sortBy.equals("sectNameDescending")){
				queryString += " order by indsec.sector.name desc" ;
			}else if(sortBy.equals("regionNameAscending")){
				queryString += " order by indsec.location.location.name" ;
			}else if(sortBy.equals("regionNameDescending")){
				queryString += " order by indsec.location.location.name desc" ;
			}
            query=session.createQuery(queryString);
			retVal=query.list();
    	}catch (Exception e) {
    		logger.debug("Unable to get indicators from DB", e);
		}
    	return retVal;
    }

    public static List getIndicatorSectorsForCurrentPage(Integer[] page,int numberPerPage,String keyWord) {
       List retVal = null;
       Session session = null;
       try {
           session = PersistenceManager.getRequestDBSession();
           String query=" from " + IndicatorSector.class.getName() + " indsec ";
           if(keyWord!=null){
        	   query+=" where indsec.indicator.name like '%" +keyWord+ "%'";
           }
           Query q = session.createQuery(query);
           int selectedIndex=(page[0]-1)*numberPerPage;
           q.setFirstResult(selectedIndex);
           q.setMaxResults(numberPerPage);
           retVal = q.list();
           if(retVal!=null&&retVal.size()==0&page[0]!=1){
               page[0]=page[0]-numberPerPage;
               // If all records were deleted we need to navigate to the previous page
               retVal=getIndicatorSectorsForCurrentPage(page,numberPerPage,keyWord);
           }
       } catch (Exception ex) {
           logger.debug("Unable to get indicators from DB", ex);
       }
       return retVal;
   }

   public static List getAvailIndicatorYears() {
       List retVal = null;
       Session session = null;
        try {

        	//oracle compatibility changes
            session = PersistenceManager.getRequestDBSession();
            String query = "select distinct indval.valueDate from " +
                    AmpIndicatorValue.class.getName() +
                    " indval where indval.valueDate is not null order by indval.valueDate desc";
             Query q = session.createQuery(query);

             retVal=new ArrayList();

            Collection dates=q.list();
            for (Iterator iterator = dates.iterator(); iterator.hasNext();) {
            	Timestamp tmpDate = (Timestamp) iterator.next();
            	retVal.add(tmpDate.getYear());
			}

            return retVal;
            //retVal = q.list();

        } catch (Exception ex) {
            logger.debug("Unable to get indicators from DB", ex);
        }
       return retVal;
   }

   public static List getAvailIndicatorDateRanges() {
       List retVal = null;
       Session session = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            String query = "select distinct indval.dataIntervalStart, indval.dataIntervalEnd from " +
                    AmpIndicatorValue.class.getName() +
                    " indval where indval.dataIntervalStart is not null and indval.dataIntervalEnd is not null " +
                    "order by indval.valueDate desc";
            Query q = session.createQuery(query);
             retVal = q.list();

        } catch (Exception ex) {
            logger.debug("Unable to get indicators from DB", ex);
        }
       return retVal;
   }


   public static List getAvailYearsForSectorIndicator(Long sectorId,Long indicatorId, int mapLevel, Long subgroupId) {
          List retVal = null;
          Session session = null;
          try {
              session = PersistenceManager.getRequestDBSession();

              StringBuffer queryString = new StringBuffer("select distinct year(indVal.valueDate) from ");
              queryString.append(AmpIndicatorValue.class.getName());
              queryString.append(" indVal, ");
              queryString.append(IndicatorSector.class.getName());
              queryString.append(" indConn where indConn.sector.ampSectorId=:sectorId");

              if (mapLevel==GisMap.MAP_LEVEL_REGION) {
                  queryString.append(" and indConn.location.ampRegion is not null and indConn.location.ampZone is null");
              } else if (mapLevel==GisMap.MAP_LEVEL_DISTRICT) {
                  queryString.append(" and indConn.location.ampRegion is not null and indConn.location.ampZone is not null");
              }
              queryString.append(" and indVal.subgroup.ampIndicatorSubgroupId=:subgroupId ");
              queryString.append(" and indConn.indicator.indicatorId=:indicatorId ");
              queryString.append(" and indConn.id=indVal.indicatorConnection.id order by year(indVal.valueDate) desc");


              Query q = session.createQuery(queryString.toString());

              q.setParameter("sectorId", sectorId, Hibernate.LONG);
              q.setParameter("indicatorId", indicatorId, Hibernate.LONG);
              q.setParameter("subgroupId", subgroupId, Hibernate.LONG);

              retVal = q.list();
          } catch (Exception ex) {
              logger.debug("Unable to get indicator years from DB", ex);
          }
          return retVal;
   }

   public static List getAvailDateIntervalsForSectorIndicator(Long sectorId,Long indicatorId, int mapLevel, Long subgroupId) {
          List retVal = null;
          Session session = null;
          try {
              session = PersistenceManager.getRequestDBSession();
              StringBuffer queryString = new StringBuffer("select distinct indVal.dataIntervalStart, indVal.dataIntervalEnd from ");
              queryString.append(AmpIndicatorValue.class.getName());
              queryString.append(" indVal, ");
              queryString.append(IndicatorSector.class.getName());
              queryString.append(" indConn where indConn.sector.ampSectorId=:sectorId");

              if (mapLevel==GisMap.MAP_LEVEL_REGION) {
                  queryString.append(" and indConn.location.ampRegion is not null and indConn.location.ampZone is null");
              } else if (mapLevel==GisMap.MAP_LEVEL_DISTRICT) {
                  queryString.append(" and indConn.location.ampRegion is not null and indConn.location.ampZone is not null");
              }
              queryString.append(" and indVal.subgroup.ampIndicatorSubgroupId=:subgroupId ");
              queryString.append(" and indConn.indicator.indicatorId=:indicatorId ");
              queryString.append(" and indConn.id=indVal.indicatorConnection.id order by indVal.dataIntervalStart desc");

              Query q = session.createQuery(queryString.toString());

              q.setParameter("sectorId", sectorId, Hibernate.LONG);
              q.setParameter("indicatorId", indicatorId, Hibernate.LONG);
              q.setParameter("subgroupId", subgroupId, Hibernate.LONG);

              retVal = q.list();
          } catch (Exception ex) {
              logger.debug("Unable to get indicator years from DB", ex);
          }
          return retVal;
   }


   public static List getAvailSubgroupsForSectorIndicator(Long sectorId,Long indicatorId, int mapLevel) {
          List retVal = null;
          Session session = null;
          try {
              session = PersistenceManager.getRequestDBSession();

              StringBuffer queryString = new StringBuffer("select distinct indVal.subgroup.ampIndicatorSubgroupId, ");
              queryString.append("indVal.subgroup.subgroupName from ");
              queryString.append(AmpIndicatorValue.class.getName());
              queryString.append(" indVal, ");
              queryString.append(IndicatorSector.class.getName());
              queryString.append(" indConn where indConn.sector.ampSectorId=:sectorId");

              if (mapLevel==GisMap.MAP_LEVEL_REGION) {
                  queryString.append(" and indConn.location.ampRegion is not null and indConn.location.ampZone is null");
              } else if (mapLevel==GisMap.MAP_LEVEL_DISTRICT) {
                  queryString.append(" and indConn.location.ampRegion is not null and indConn.location.ampZone is not null");
              }

              queryString.append(" and indConn.indicator.indicatorId=:indicatorId ");
              queryString.append(" and indConn.id=indVal.indicatorConnection.id order by year(indVal.valueDate) desc");


              Query q = session.createQuery(queryString.toString());

              q.setParameter("sectorId", sectorId, Hibernate.LONG);
              q.setParameter("indicatorId", indicatorId, Hibernate.LONG);

              retVal = q.list();
          } catch (Exception ex) {
              logger.debug("Unable to get indicator years from DB", ex);
          }
          return retVal;
   }

   public static List<String> getAvailSubgroupsForSectorIndicator(Long sectorId,Long indicatorId) {
	   List<String> retVal = null;
       Session session = null;
       try {
    	   session = PersistenceManager.getRequestDBSession();
    	   String queryString="select distinct indVal.subgroup.subgroupName from " + AmpIndicatorValue.class.getName() + " indVal, " +
    	   IndicatorSector.class.getName()+ " indConn where indConn.sector.ampSectorId="+sectorId+" and indConn.indicator.indicatorId="+indicatorId
    	   +" and indConn.id=indVal.indicatorConnection.id order by year(indVal.valueDate) desc";
    	   Query q = session.createQuery(queryString);
    	   retVal=q.list();
		} catch (Exception e) {
			logger.debug("Unable to get indicator years from DB", e);
			e.printStackTrace();
		}
       return retVal;
   }

   public static class HelperIndicatorNameAscComparator implements Comparator<IndicatorSector> {
       Locale locale;
       Collator collator;

       public HelperIndicatorNameAscComparator(){
           this.locale=new Locale("en", "EN");
       }

       public HelperIndicatorNameAscComparator(String iso) {
           this.locale = new Locale(iso.toLowerCase(), iso.toUpperCase());
       }

       public int compare(IndicatorSector o1, IndicatorSector o2) {
           collator = Collator.getInstance(locale);
           collator.setStrength(Collator.TERTIARY);

           int result = collator.compare(o1.getIndicator().getName(), o2.getIndicator().getName());
           return result;
       }
   }

   public static class HelperIndicatorNameDescComparator implements Comparator<IndicatorSector> {
       Locale locale;
       Collator collator;

       public HelperIndicatorNameDescComparator(){
           this.locale=new Locale("en", "EN");
       }

       public HelperIndicatorNameDescComparator(String iso) {
           this.locale = new Locale(iso.toLowerCase(), iso.toUpperCase());
       }

       public int compare(IndicatorSector o1, IndicatorSector o2) {
           collator = Collator.getInstance(locale);
           collator.setStrength(Collator.TERTIARY);

           int result = -collator.compare(o1.getIndicator().getName(), o2.getIndicator().getName());
           return result;
       }
   }

   public static class HelperSectorNameAscComparator implements Comparator<IndicatorSector> {
       Locale locale;
       Collator collator;

       public HelperSectorNameAscComparator(){
           this.locale=new Locale("en", "EN");
       }

       public HelperSectorNameAscComparator(String iso) {
           this.locale = new Locale(iso.toLowerCase(), iso.toUpperCase());
       }

       public int compare(IndicatorSector o1, IndicatorSector o2) {
           collator = Collator.getInstance(locale);
           collator.setStrength(Collator.TERTIARY);

           int result = collator.compare(o1.getSector().getName(), o2.getSector().getName());
           return result;
       }
   }

   public static class HelperSectorNameDescComparator implements Comparator<IndicatorSector> {
       Locale locale;
       Collator collator;

       public HelperSectorNameDescComparator(){
           this.locale=new Locale("en", "EN");
       }

       public HelperSectorNameDescComparator(String iso) {
           this.locale = new Locale(iso.toLowerCase(), iso.toUpperCase());
       }

       public int compare(IndicatorSector o1, IndicatorSector o2) {
           collator = Collator.getInstance(locale);
           collator.setStrength(Collator.TERTIARY);

           int result = - collator.compare(o1.getSector().getName(), o2.getSector().getName());
           return result;
       }
   }

   public static class HelperRegionNameAscComparator implements Comparator<IndicatorSector> {
       Locale locale;
       Collator collator;

       public HelperRegionNameAscComparator(){
           this.locale=new Locale("en", "EN");
       }

       public HelperRegionNameAscComparator(String iso) {
           this.locale = new Locale(iso.toLowerCase(), iso.toUpperCase());
       }

       public int compare(IndicatorSector o1, IndicatorSector o2) {
           collator = Collator.getInstance(locale);
           collator.setStrength(Collator.TERTIARY);

           int result = collator.compare(o1.getLocation().getLocation().getName(), o2.getLocation().getLocation().getName());
           return result;
       }
   }

   public static class HelperRegionNameDescComparator implements Comparator<IndicatorSector> {
       Locale locale;
       Collator collator;

       public HelperRegionNameDescComparator(){
           this.locale=new Locale("en", "EN");
       }

       public HelperRegionNameDescComparator(String iso) {
           this.locale = new Locale(iso.toLowerCase(), iso.toUpperCase());
       }

       public int compare(IndicatorSector o1, IndicatorSector o2) {
           collator = Collator.getInstance(locale);
           collator.setStrength(Collator.TERTIARY);

           int result = - collator.compare(o1.getLocation().getLocation().getName(), o2.getLocation().getLocation().getName());
           return result;
       }
   }
}
