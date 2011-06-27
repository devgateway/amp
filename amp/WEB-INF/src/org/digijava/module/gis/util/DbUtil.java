package org.digijava.module.gis.util;

import java.sql.Timestamp;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.ecs.xml.XML;
import org.apache.ecs.xml.XMLDocument;
import org.apache.log4j.Logger;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.collections.CollectionUtils;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpActivityProgram;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpRegionalFunding;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpSectorScheme;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.IndicatorConnection;
import org.digijava.module.aim.dbentity.IndicatorSector;
import org.digijava.module.aim.helper.TreeItem;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesDetails;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesLocation;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesProgram;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesSector;
import org.digijava.module.gis.dbentity.GisMap;
import org.digijava.module.gis.dbentity.GisSettings;
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
    public static final int SELECT_DEFAULT = 0;
    public static final int SELECT_SECTOR_SCHEME = 1;
    public static final int SELECT_SECTOR = 2;
    public static final int SELECT_PROGRAM = 3;

    public  static final int SECTORS_FOR_ACTIVITIES = 0;
    public  static final int SECTORS_FOR_PLEDGES = 1;

    public  static final int MAP_MODE_ACTIVITIES = 0;
    public  static final int MAP_MODE_PLEDGES = 1;



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
            q.setString("mapCode", code);
            q.setInteger("mapLevel", level);
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
            retVal = SectorUtil.getFundingLocationSectorLevel1(new Integer(SectorUtil.getPrimaryConfigClassificationId().intValue()));
        } catch (DgException ex) {
            ex.printStackTrace();
        }
        return retVal;
    }

    public static List getSectorFoundingDonors(Long sectorId) {

        List subSectorIds = null;
        if (sectorId > -1) {
            subSectorIds = getSubSectorIdsWhereclause(sectorId);
        }
        List retVal = null;
        Session session = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            Query q = null;
            if (sectorId > -1) {

                StringBuffer whereCaluse = new StringBuffer();
                Iterator parentIt = subSectorIds.iterator();
                while (parentIt.hasNext()) {
                    Long parSecId = (Long) parentIt.next();
                    whereCaluse.append(parSecId.longValue());
                    if (parentIt.hasNext()) {
                        whereCaluse.append(",");
                    }
                }
                StringBuffer qs = new StringBuffer("select fnd.ampDonorOrgId.name, fnd.ampDonorOrgId.ampOrgId from ");
                qs.append(AmpFunding.class.getName());
                qs.append(" fnd, ");
                qs.append(AmpActivitySector.class.getName());
                qs.append(" asmap ");
                qs.append("where fnd.ampActivityId.ampActivityId=asmap.activityId.ampActivityId and asmap.sectorId.ampSectorId in (");
                qs.append(whereCaluse);
                qs.append(")");
                qs.append(" group by fnd.ampDonorOrgId.ampOrgId");
                q = session.createQuery(qs.toString());
           }
            
            retVal = q.list();
        } catch (Exception ex) {
            logger.debug("Unable to get sector funding donors from DB", ex);
        }
        return retVal;
    }
    
    public static List getAllDonors() {
        List retVal = null;
        Session session = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            Query q = null;
                StringBuffer qs = new StringBuffer("select org.name, org.ampOrgId from ");
                qs.append(AmpOrganisation.class.getName());
                qs.append(" org");
                q = session.createQuery(qs.toString());
            
            retVal = q.list();
        } catch (Exception ex) {
            logger.debug("Unable to get sector funding donors from DB", ex);
        }
        return retVal;
    }
    
    public static List getFundingDonors() {
        List retVal = null;
        Session session = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            Query q = null;
            String queryString = null;    
            queryString="select o.name, o.ampOrgId from " 
            	+ AmpOrganisation.class.getName()+ " o "
            	+" where o.ampOrgId in (select f.ampDonorOrgId from "+ AmpFunding.class.getName() 
            	+" f where f.ampActivityId in (select al.activity from "
            	+ AmpActivityLocation.class.getName()+ " al)" 
            	+" and f.ampFundingId in (select afd.ampFundingId from "
            	+ AmpFundingDetail.class.getName()+ " afd)) order by o.name";
                q = session.createQuery(queryString);
            
            retVal = q.list();
        } catch (Exception ex) {
            logger.debug("Unable to get sector funding donors from DB", ex);
        }
        return retVal;
    }

    public static List getSectorFoundings(Long sectorId) {
        return getSectorFoundings(sectorId, SELECT_DEFAULT, null);
    }
    
        public static Long getLocationIdByName (String locationName, int mapLevel) {
        Long retVal = null;
        Session sess = null;
        try {
            sess = PersistenceManager.getRequestDBSession();
            StringBuilder queryStr = new StringBuilder("select reg.id from ");
            queryStr.append(AmpCategoryValueLocations.class.getName());
            queryStr.append(" as reg where reg.name = :REG_NAME ");
            queryStr.append(" and reg.parentCategoryValue.value = :MAP_LEVEL");

            Query q = sess.createQuery(queryStr.toString());


            String mapLevelParam = null;
            if (mapLevel == 2) {
                mapLevelParam = "Region";
            } else if (mapLevel == 3) {
                mapLevelParam = "District";
            } else {
                throw new DgException("Incorrect map level parameter");
            }
            q.setString("REG_NAME", locationName);
            q.setString("MAP_LEVEL", mapLevelParam);
            retVal = (Long) q.uniqueResult();
        } catch (Exception ex) {
            logger.debug("Unable to get region ID from DB", ex);
        }


        return retVal;
    }

    public static String getLocationNameById (Long locId) {
        String retVal = null;
        Session sess = null;
        try {
            sess = PersistenceManager.getRequestDBSession();
            StringBuilder queryStr = new StringBuilder("select reg.name from ");
            queryStr.append(AmpCategoryValueLocations.class.getName());
            queryStr.append(" as reg where reg.id = :REG_ID");
            Query q = sess.createQuery(queryStr.toString());
            q.setLong("REG_ID", locId);
            retVal = (String) q.uniqueResult();
        } catch (Exception ex) {
            logger.debug("Unable to get region name from DB", ex);
        }
        return retVal;
    }
    
    public static List getSectorFoundings(Long sectorId, int sectorQueryType, Long teamId) {
        List subSectorIds = null;
        if (sectorId > -1) {
            if (sectorQueryType == SELECT_SECTOR_SCHEME) {
                //In this case we have sector scheme id in sectorId parameter
                List <AmpSector> schemeSecs = SectorUtil.getAllSectorsFromScheme(sectorId);
                subSectorIds = new ArrayList();
                for (AmpSector iterSec: schemeSecs) {
                    subSectorIds.add(iterSec.getAmpSectorId());
                }
            } else if (sectorQueryType == SELECT_SECTOR || sectorQueryType == SELECT_DEFAULT) {
                subSectorIds = getSubSectorIdsWhereclause(sectorId);
            }
        }
        List retVal = null;
        Session session = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            Query q = null;
            if (sectorId > -1) {

                StringBuffer whereCaluse = new StringBuffer();
                Iterator parentIt = subSectorIds.iterator();
                while (parentIt.hasNext()) {
                    Long parSecId = (Long) parentIt.next();
                    whereCaluse.append(parSecId.longValue());
                    if (parentIt.hasNext()) {
                        whereCaluse.append(",");
                    }
                }


                //Get only activities with fundings
                /*
                List actsToGet = getActIdsHavingFundings();
                StringBuffer actWhereClause = new StringBuffer(" sec.activityId.ampActivityId in (");
                Iterator <Long> it = actsToGet.iterator();
                while (it.hasNext()) {
                    Long actId = it.next();
                    actWhereClause.append(actId);
                    if (it.hasNext()) {
                        actWhereClause.append(",");
                    }
                }
                actWhereClause.append(")");  */


                //Get only activities with regional and district locations
                List actsToGet = getActIdsHavingRegionalAndDistrictLevels();
                StringBuffer actWhereClause = new StringBuffer(" and sec.activityId.ampActivityId in (");
                Iterator <Long> it = actsToGet.iterator();
                while (it.hasNext()) {
                    Long actId = it.next();
                    actWhereClause.append(actId);
                    if (it.hasNext()) {
                        actWhereClause.append(",");
                    }
                }
                actWhereClause.append(")");


                StringBuffer qs = new StringBuffer("select distinct sec.activityId, sec.sectorPercentage from ");
                qs.append(AmpActivitySector.class.getName());
                qs.append(" sec where sec.activityId.draft=false and sec.sectorId in (");
                qs.append(whereCaluse);
                qs.append(")");
                qs.append(" and sec.activityId.team is not null ");
                if (sectorQueryType == SELECT_SECTOR_SCHEME) {
                    qs.append(actWhereClause);
                }

                if (teamId != null) {
                    qs.append(" and sec.activityId.team.ampTeamId = :TEAM_ID");
                }

                q = session.createQuery(qs.toString());
                if (teamId != null) {
                    q.setLong("TEAM_ID", teamId);
                }
           } else {
                //Get only activities with regional and district locations
                List actsToGet = getActIdsHavingRegionalAndDistrictLevels();
                StringBuffer whereClause = new StringBuffer(" and sec.activityId.ampActivityId in (");
                Iterator <Long> it = actsToGet.iterator();
                while (it.hasNext()) {
                    Long actId = it.next();
                    whereClause.append(actId);
                    if (it.hasNext()) {
                        whereClause.append(",");
                    }
                }
                whereClause.append(")");
                //q = session.createQuery("select distinct sec.activityId, sec.sectorPercentage from " +AmpActivitySector.class.getName() + " sec where sec.activityId.");
                StringBuffer qs = new StringBuffer("select distinct sec.activityId, sec.sectorPercentage from ");
                qs.append(AmpActivitySector.class.getName());
                qs.append(" sec where sec.activityId.draft=false");
                qs.append(whereClause);

                if (teamId != null) {
                    qs.append(" and sec.activityId.team.ampTeamId = :TEAM_ID");
                }

                q = session.createQuery(qs.toString());
                if (teamId != null) {
                    q.setLong("TEAM_ID", teamId);
                }

                q = session.createQuery(qs.toString());
            }
            retVal = q.list();
        } catch (Exception ex) {
            logger.debug("Unable to get sector fundings from DB", ex);
        }
        return retVal;
    }


    
	public static List getProgramFoundings(Long prgId) {
        List retVal = null;
        Session session = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            Query q = null;
            StringBuffer qs = new StringBuffer("select prg.activity, prg.programPercentage from ");
            qs.append(AmpActivityProgram.class.getName());
            qs.append(" prg where prg.program.ampThemeId = :PRG_ID or prg.program.parentThemeId.ampThemeId = :PRG_ID ");
            qs.append(" or prg.program.parentThemeId.parentThemeId.ampThemeId = :PRG_ID ");
            qs.append(" and prg.activity.team is not null");
            q = session.createQuery(qs.toString());
            q.setLong("PRG_ID", prgId);
            retVal = q.list();

            //Convert double percentages to floats
            for (Object o : retVal) {
                Object[] o1 = (Object[]) o;
                o1[1] = new Float(((Long)o1[1]).floatValue());
            }

        } catch (Exception ex) {
            logger.debug("Unable to get program fundings from DB", ex);
        }
        return retVal;
    }

    public static List getActIdsHavingRegionalAndDistrictLevels() {
        List<Long> retVal = null;
        Session session = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            Query q = null;
            StringBuffer qs = new StringBuffer();
            qs.append("select distinct actLoc.activity.ampActivityId from ");
            qs.append(AmpActivityLocation.class.getName());
            qs.append(" actLoc where actLoc.location.regionLocation != null and (actLoc.location.regionLocation.parentCategoryValue.value = 'Region' or actLoc.location.regionLocation.parentCategoryValue.value = 'Zone')");
            q = session.createQuery(qs.toString());
            retVal = q.list();
        } catch (Exception ex) {
            logger.debug("Unable to get activity IDs from DB", ex);
        }
        return retVal;
    }

    public static List getActIdsHavingFundings() {
        List<Long> retVal = null;
        Session session = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            Query q = null;
            StringBuffer qs = new StringBuffer();
            qs.append("select distinct actFundDet.ampFundingId.ampActivityId.ampActivityId from ");
            qs.append(AmpFundingDetail.class.getName());
            qs.append(" as actFundDet");
            q = session.createQuery(qs.toString());
            retVal = q.list();
        } catch (Exception ex) {
            logger.debug("Unable to get activity IDs from DB", ex);
        }
        return retVal;
    }

    
    public static List getSectorFoundingsPublic(Long sectorId, int sectorQueryType) {

        List subSectorIds = null;
        if (sectorId > -1) {
            if (sectorQueryType == SELECT_SECTOR_SCHEME) {
                //In this case we have sector scheme id in sectorId parameter
                List <AmpSector> schemeSecs = SectorUtil.getAllSectorsFromScheme(sectorId);
                subSectorIds = new ArrayList();
                for (AmpSector iterSec: schemeSecs) {
                    subSectorIds.add(iterSec.getAmpSectorId());
                }
            } else if (sectorQueryType == SELECT_SECTOR || sectorQueryType == SELECT_DEFAULT) {
                subSectorIds = getSubSectorIdsWhereclause(sectorId);
            }
        }
        List retVal = null;
        Session session = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            Query q = null;
            StringBuffer publicwhere= new StringBuffer("select distinct aa.ampActivityId from "
            		+ AmpActivity.class.getName()
            		+ " aa where aa.team in (select at.ampTeamId from "
            		+ AmpTeam.class.getName() + " at where parentTeamId is not null)");

            if (sectorId > -1) {
            	StringBuffer whereCaluse = getLongIdsWhereclause(subSectorIds);
                StringBuffer qs = new StringBuffer("select sec.activityId, sec.sectorPercentage from ");
                qs.append(AmpActivitySector.class.getName());
                qs.append(" sec where sec.sectorId in (");
                qs.append(whereCaluse);
                qs.append(") and sec.activityId in (" + publicwhere);
                qs.append(") and sec.activityId.team is not null");
                qs.append(" and sec.sectorId in (");
                qs.append(whereCaluse);
                qs.append(")");
                q = session.createQuery(qs.toString());
           } else {

                List actsToGet = getActIdsHavingRegionalAndDistrictLevels();
                StringBuffer whereClause = new StringBuffer(" and sec.activityId.ampActivityId in (");
                Iterator <Long> it = actsToGet.iterator();
                while (it.hasNext()) {
                    Long actId = it.next();
                    whereClause.append(actId);
                    if (it.hasNext()) {
                        whereClause.append(",");
                    }
                }
                whereClause.append(")");


                q = session.createQuery("select distinct sec.activityId, sec.sectorPercentage from " 
                		+AmpActivitySector.class.getName() 
                		+ " sec where sec.activityId in ("+publicwhere+")" + whereClause);
            }
            retVal = q.list();
        } catch (Exception ex) {
            logger.debug("Unable to get sector fundings from DB", ex);
        }
        return retVal;
    }

    public static List getProgramFoundingsByDonor(Long prgId, Long donorid) {
        List retVal = null;
        Session session = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            Query q = null;
            StringBuffer donorwhere = new StringBuffer("select f.ampActivityId from "+ AmpFunding.class.getName()
            		+" f where f.ampDonorOrgId ="+donorid);

            StringBuffer qs = new StringBuffer("select prg.activity, prg.programPercentage from ");
            qs.append(AmpActivityProgram.class.getName());
            qs.append(" prg where prg.program.ampThemeId = :PRG_ID ");
            qs.append(" and prg.activity.team is not null and prg.activity in ("+donorwhere+")");
            q = session.createQuery(qs.toString());
            q.setLong("PRG_ID", prgId);

            retVal = q.list();
        } catch (Exception ex) {
            logger.debug("Unable to get sector fundings from DB", ex);
        }
        return retVal;
    }
    
    public static List getSectorFoundingsByDonor(Long sectorId, Long donorid, int sectorQueryType) {
        List subSectorIds = null;
        if (sectorId > -1) {
            if (sectorQueryType == SELECT_SECTOR_SCHEME) {
                //In this case we have sector scheme id in sectorId parameter
                List <AmpSector> schemeSecs = SectorUtil.getAllSectorsFromScheme(sectorId);
                subSectorIds = new ArrayList();
                for (AmpSector iterSec: schemeSecs) {
                    subSectorIds.add(iterSec.getAmpSectorId());
                }
            } else if (sectorQueryType == SELECT_SECTOR || sectorQueryType == SELECT_DEFAULT) {
                subSectorIds = getSubSectorIdsWhereclause(sectorId);
            }
        }
        /*
    	List subSectorIds = null;
        if (sectorId > -1) {
            if (sectorQueryType == SELECT_SECTOR_SCHEME) {
                //In this case we have sector scheme id in sectorId parameter
                List <AmpSector> schemeSecs = SectorUtil.getAllSectorsFromScheme(sectorId);
                subSectorIds = new ArrayList();
                for (AmpSector iterSec: schemeSecs) {
                    subSectorIds.add(iterSec.getAmpSectorId());
                }
            } else if (sectorQueryType == SELECT_SECTOR || sectorQueryType == SELECT_DEFAULT) {
            subSectorIds = getSubSectorIdsWhereclause(sectorId);
        }
        */
        List retVal = null;
        Session session = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            Query q = null;
            StringBuffer donorwhere = new StringBuffer("select f.ampActivityId from "+ AmpFunding.class.getName()
            		+" f where f.ampDonorOrgId ="+donorid);
            if (sectorId > -1) {
                /*
            	StringBuffer whereCaluse = new StringBuffer();
            	Iterator parentIt = subSectorIds.iterator();
                while (parentIt.hasNext()) {
                    Long parSecId = (Long) parentIt.next();
                    whereCaluse.append(parSecId.longValue());
                    if (parentIt.hasNext()) {
                        whereCaluse.append(",");
                    }
                }     */
                StringBuffer qs = new StringBuffer("select sec.activityId, sec.sectorPercentage from ");
                qs.append(AmpActivitySector.class.getName());
                qs.append(" sec where sec.sectorId in (");
                qs.append(getLongIdsWhereclause(subSectorIds));
                qs.append(")");
                qs.append(" and sec.activityId.team is not null and sec.activityId in ("+donorwhere+")");
                q = session.createQuery(qs.toString());
           } else {
                q = session.createQuery("select distinct sec.activityId, sec.sectorPercentage from " +AmpActivitySector.class.getName()
                + " sec where sec.activityId in ("+donorwhere+")");
            }
            retVal = q.list();
        } catch (Exception ex) {
            logger.debug("Unable to get sector fundings from DB", ex);
        }
        return retVal;
    }
    
    
    public static List getSubSectorIdsWhereclause(Long sectorId) {
        List param = new ArrayList();
        param.add(sectorId);
        List childIds = new ArrayList();
        getSubSectorIds(param, childIds);
        childIds.add(sectorId);
        return childIds;
    }

    public static void getSubSectorIds(List sectorIds, List childIds) {
            Session session = null;
            StringBuffer whereCaluse = new StringBuffer();
            Iterator parentIt = sectorIds.iterator();
            while (parentIt.hasNext()) {
                Long parSecId = (Long) parentIt.next();
                whereCaluse.append(parSecId.longValue());
                if (parentIt.hasNext()) {
                    whereCaluse.append(",");
                }
            }
            List local = null;
            try {
                session = PersistenceManager.getRequestDBSession();
                Query q = null;
                StringBuffer qs = new StringBuffer("select sec.ampSectorId from ");
                qs.append(AmpSector.class.getName());
                qs.append(" sec where sec.parentSectorId.ampSectorId in ");
                qs.append("(");
                qs.append(whereCaluse);
                qs.append(")");
                q = session.createQuery(qs.toString());
                local = q.list();
            } catch (Exception ex) {
                logger.debug("Unable to get sector IDs from DB", ex);
            }

            if (!local.isEmpty()) {
                childIds.addAll(local);
                getSubSectorIds(local, childIds);
            }
    }

    public static List getIndicatorsForSector(Long sectorId, int mapLevel) {
       List retVal = null;
       Session session = null;
       try {
           session = PersistenceManager.getRequestDBSession();

           StringBuffer querySrc = new StringBuffer();
           querySrc.append("select distinct ds.indicator.indicatorId, ds.indicator.name from ");
           querySrc.append(IndicatorConnection.class.getName());
           querySrc.append(" ds");
           querySrc.append(" where ds.sector.ampSectorId=:sectorId and");
           querySrc.append(" (ds.location.regionLocation.parentCategoryValue.value='Region' or");
           querySrc.append(" ds.location.regionLocation.parentCategoryValue.value='District')");
           querySrc.append(" group by ds.indicator.indicatorId order by ds.indicator.name");
           Query q = session.createQuery(querySrc.toString());
           q.setLong("sectorId", sectorId);
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
           q1.setLong("sectorId", sectorId);
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

              q.setLong("sectorId", sectorId);
              q.setLong("indicatorId", indicatorId);
              q.setLong("subgroupId", subgroupId);

              if (year.intValue() > 0) {
                  q.setInteger("indValDate", year);
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

              q.setLong("sectorId", sectorId);
              q.setLong("indicatorId", indicatorId);
              q.setLong("subgroupId", subgroupId);

              if (interval != null) {
                  q.setDate("intervalStart", interval.getStart());
                  q.setDate("intervalEnd", interval.getEnd());
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
                  Object[] actualObj = new Object[4];
                  actualObj[0] = indValObj.getValue();
                  actualObj[1] = locCode;
                  actualObj[2] = indValObj.getSource();
                  actualObj[3] = indValObj.getIndicatorSource().getValue();

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
            q.setLong("activityId", activityId);
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

              q.setLong("sectorId", sectorId);
              q.setLong("indicatorId", indicatorId);
              q.setLong("subgroupId", subgroupId);

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

              q.setLong("sectorId", sectorId);
              q.setLong("indicatorId", indicatorId);
              q.setLong("subgroupId", subgroupId);

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

              q.setLong("sectorId", sectorId);
              q.setLong("indicatorId", indicatorId);

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

    public static XMLDocument getAllSectorsAsHierarchyXML(boolean showSecondarySectorSchemes) {
        return getAllSectorsAsHierarchyXML(SECTORS_FOR_ACTIVITIES, showSecondarySectorSchemes);

    }

    public static XMLDocument getAllSectorsAsHierarchyXML(int sectorMode, boolean showSecondarySectorSchemes) {
        Collection <AmpSectorScheme> schemes = SectorUtil.getAllSectorSchemes();
        Long primarySchemeId = null;
        List<AmpClassificationConfiguration> allClassificationConfigs  = null;
        try {
            primarySchemeId = SectorUtil.getPrimaryConfigClassificationId();
            allClassificationConfigs  = SectorUtil.getAllClassificationConfigs();
        } catch (DgException ex) {

        }

        Set multySectorSchemIDs = new HashSet();
        for (AmpClassificationConfiguration ampccs : allClassificationConfigs) {
            if (ampccs.isMultisector()) {
                multySectorSchemIDs.add(ampccs.getClassification().getAmpSecSchemeId());
            }
        }

        XMLDocument xmlDoc = new XMLDocument();
        XML root = new XML("sector-tree");
        xmlDoc.addElement(root);


        for (AmpSectorScheme scheme : schemes) {
            boolean primary = (primarySchemeId != null && primarySchemeId.equals(scheme.getAmpSecSchemeId()));
           if (primary || (multySectorSchemIDs.contains(scheme.getAmpSecSchemeId()) && showSecondarySectorSchemes)) {

            XML schemeNode = new XML("scheme");
            schemeNode.addAttribute("name", scheme.getSecSchemeName());
            schemeNode.addAttribute("code", scheme.getSecSchemeCode());
            schemeNode.addAttribute("id", scheme.getAmpSecSchemeId());



            schemeNode.addAttribute("primary", primary);

            Collection <AmpSector> sectors =
                    SectorUtil.getSectorLevel1(Integer.valueOf(scheme.getAmpSecSchemeId().intValue()));
            if (sectors != null && !sectors.isEmpty()) {
                Set subSet = null;
                if (sectorMode == SECTORS_FOR_ACTIVITIES) {
                    subSet = getSectorsInActivitiesSublist (sectors);
                } else if (sectorMode == SECTORS_FOR_PLEDGES) {
                    subSet = getSectorsInPledgesSublist(sectors);
                }
                populateSectorTree(sectors, schemeNode, subSet, sectorMode);
            }

            root.addElement(schemeNode);
           }
        }
        return xmlDoc;
    }

  
    
   

    public static XMLDocument getAllSectorsAsHierarchyXML(int sectorMode, boolean showSecondarySectorSchemes, int sectorFilterMode) {
        Collection <AmpSectorScheme> schemes = SectorUtil.getAllSectorSchemes();
        Long primarySchemeId = null;
        List<AmpClassificationConfiguration> allClassificationConfigs  = null;
        try {
            primarySchemeId = SectorUtil.getPrimaryConfigClassificationId();
            allClassificationConfigs  = SectorUtil.getAllClassificationConfigs();
        } catch (DgException ex) {

        }

        Set multySectorSchemIDs = new HashSet();
        for (AmpClassificationConfiguration ampccs : allClassificationConfigs) {
            if (ampccs.isMultisector()) {
                multySectorSchemIDs.add(ampccs.getClassification().getAmpSecSchemeId());
            }
        }

        XMLDocument xmlDoc = new XMLDocument();
        XML root = new XML("data");
        xmlDoc.addElement(root);


        XML secRoot = new XML("sector-tree");
        root.addElement(secRoot);


        for (AmpSectorScheme scheme : schemes) {
            boolean primary = (primarySchemeId != null && primarySchemeId.equals(scheme.getAmpSecSchemeId()));
           if ( (sectorFilterMode == GisSettings.SECTOR_SCHEME_AUTOMATIC &&
                   (primary || (multySectorSchemIDs.contains(scheme.getAmpSecSchemeId()) && showSecondarySectorSchemes)))
              || (sectorFilterMode == GisSettings.SECTOR_SCHEME_CONFIGURABLE && scheme.getShowInRMFilters())) {

            XML schemeNode = new XML("scheme");
            schemeNode.addAttribute("name", scheme.getSecSchemeName());
            schemeNode.addAttribute("code", scheme.getSecSchemeCode());
            schemeNode.addAttribute("id", scheme.getAmpSecSchemeId());



            schemeNode.addAttribute("primary", primary);

            Collection <AmpSector> sectors =
                    SectorUtil.getSectorLevel1(Integer.valueOf(scheme.getAmpSecSchemeId().intValue()));
            if (sectors != null && !sectors.isEmpty()) {
                Set subSet = null;
                if (sectorMode == SECTORS_FOR_ACTIVITIES) {
                    subSet = getSectorsInActivitiesSublist (sectors);
                } else if (sectorMode == SECTORS_FOR_PLEDGES) {
                    subSet = getSectorsInPledgesSublist(sectors);
                }
                populateSectorTree(sectors, schemeNode, subSet, sectorMode);
            }

            secRoot.addElement(schemeNode);
           }
        }

        //Programs
        Collection<AmpTheme> allPrograms = null;
        try {
            allPrograms = ProgramUtil.getAllThemes(true);
        } catch (DgException ex) {

        }
        Collection programsFlatTree = CollectionUtils.getHierarchy(
                allPrograms,
                new ProgramUtil.ProgramHierarchyDefinition(),
                new ProgramUtil.XMLtreeItemFactory());
        XML prgRoot = new XML("programs-tree");
        root.addElement(prgRoot);
        populateProgramsTree (programsFlatTree, prgRoot, getUsedPrograms());

        return xmlDoc;
    }

    private static boolean populateSectorTree (Collection <AmpSector> sectors, XML parentNode,
                                            Set usedSectorSublist) {
        return populateSectorTree (sectors, parentNode, usedSectorSublist, SECTORS_FOR_ACTIVITIES);

    }

    private static boolean populateProgramsTree (Collection<TreeItem> programs, XML parentNode, Set usedProgramSublist) {
        boolean oneOfPrgsHasFounding = false;
        for (TreeItem prgTreeItem: programs) {
            AmpTheme theme = (AmpTheme) prgTreeItem.getMember();
            if (theme.getParentThemeId() != null || (theme.getShowInRMFilters() != null && theme.getShowInRMFilters().booleanValue())) {
                XML sectorTag = new XML("program");
                sectorTag.addAttribute("name", theme.getName());
                sectorTag.addAttribute("id", theme.getAmpThemeId());

                boolean curentPrjHasFunding = usedProgramSublist.contains(theme.getAmpThemeId());
                boolean oneOfChildrenHasFounding = false;
                if (!oneOfPrgsHasFounding && oneOfPrgsHasFounding) {
                    oneOfPrgsHasFounding = true;
                }

                Collection<TreeItem> childPrgs = prgTreeItem.getChildren();
                if (childPrgs != null && !childPrgs.isEmpty()) {
                    oneOfChildrenHasFounding = populateProgramsTree (childPrgs, sectorTag,usedProgramSublist);;
                    if (!oneOfPrgsHasFounding && oneOfChildrenHasFounding) {
                        oneOfPrgsHasFounding = oneOfChildrenHasFounding;
                    }
                }

                sectorTag.addAttribute("hasFoundings", curentPrjHasFunding || oneOfChildrenHasFounding);
                parentNode.addElement(sectorTag);
            }

        }

        return oneOfPrgsHasFounding;
    }

    private static boolean populateSectorTree (Collection <AmpSector> sectors, XML parentNode,
                                            Set usedSectorSublist, int sectorMode) {
        boolean oneOfSecsHasFounding = false;
        for (AmpSector sector : sectors) {
            XML sectorTag = new XML("sector");
            sectorTag.addAttribute("name", sector.getName());
            sectorTag.addAttribute("id", sector.getAmpSectorId());

            boolean curentSectorHasFunding = usedSectorSublist.contains(sector.getAmpSectorId());
            boolean oneOfChildrenHasFounding = false;
            if (!oneOfSecsHasFounding && curentSectorHasFunding) {
                oneOfSecsHasFounding = true;
            }


            Collection<AmpSector> childSecs = SectorUtil.getAllChildSectors(sector.getAmpSectorId());
            if (childSecs != null && !childSecs.isEmpty()) {
                Set actSec = null;
                if (sectorMode == SECTORS_FOR_ACTIVITIES) {
                    actSec = getSectorsInActivitiesSublist (childSecs);
                } else if (sectorMode == SECTORS_FOR_PLEDGES) {
                    actSec = getSectorsInPledgesSublist(childSecs);
                }
                oneOfChildrenHasFounding = populateSectorTree(childSecs, sectorTag, actSec, sectorMode);
                if (!oneOfSecsHasFounding && oneOfChildrenHasFounding) {
                    oneOfSecsHasFounding = oneOfChildrenHasFounding;
                }
            }

            sectorTag.addAttribute("hasFoundings", curentSectorHasFunding || oneOfChildrenHasFounding);
            parentNode.addElement(sectorTag);
        }
        return oneOfSecsHasFounding;
    }

    private static Set getUsedPrograms() {
        Set retVal = null;
        Session sess = null;

        try {
            sess = PersistenceManager.getRequestDBSession();
        } catch (DgException ex) {

        }

        StringBuffer queryStr = new StringBuffer("select distinct prj.ampActivityProgramId from ");
        queryStr.append(AmpActivityProgram.class.getName());
        queryStr.append(" as prj");

        Query q = sess.createQuery(queryStr.toString());
        retVal = new HashSet(q.list());

        return retVal;
    }

    public static Set getSectorsInActivitiesSublist (Collection <AmpSector> listToFilter) {
        Set retVal = null;
        List queryResults = null;
        Session session = null;
		Collection col = null;

        StringBuffer sectorsWhereclause = new StringBuffer();
        Iterator <AmpSector> it = listToFilter.iterator();
        while (it.hasNext()) {
            Long secId = it.next().getAmpSectorId();
            sectorsWhereclause.append(secId);
            if (it.hasNext()) {
                sectorsWhereclause.append(",");
            }
        }

        try {
			session = PersistenceManager.getSession();
            StringBuffer queryStr = new StringBuffer("select distinct actSec.sectorId.ampSectorId from ");
            queryStr.append(AmpActivitySector.class.getName());
            queryStr.append(" as actSec where actSec.sectorId.ampSectorId in (");
            queryStr.append(sectorsWhereclause);
            queryStr.append(") and actSec.sectorPercentage > 0");

            Query qry = session.createQuery(queryStr.toString());
			queryResults = qry.list();
            retVal = new HashSet(queryResults);

		} catch (Exception e) {
			logger.error("Error filtering used sectors, " + e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
			}
		}

        return retVal;
    }

    public static Set getSectorsInPledgesSublist (Collection <AmpSector> listToFilter) {
        Set retVal = null;
        List queryResults = null;
        Session session = null;
		Collection col = null;

        StringBuffer sectorsWhereclause = new StringBuffer();
        Iterator <AmpSector> it = listToFilter.iterator();
        while (it.hasNext()) {
            Long secId = it.next().getAmpSectorId();
            sectorsWhereclause.append(secId);
            if (it.hasNext()) {
                sectorsWhereclause.append(",");
            }
        }

        try {
			session = PersistenceManager.getSession();
            StringBuffer queryStr = new StringBuffer("select distinct pldgSec.sector.ampSectorId from ");
            queryStr.append(FundingPledgesSector.class.getName());
            queryStr.append(" as pldgSec where pldgSec.sector.ampSectorId in (");
            queryStr.append(sectorsWhereclause);
            queryStr.append(") and pldgSec.sectorpercentage > 0");

            Query qry = session.createQuery(queryStr.toString());
			queryResults = qry.list();
            retVal = new HashSet(queryResults);

		} catch (Exception e) {
			logger.error("Error filtering used sectors, " + e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
			}
		}

        return retVal;
    }

    public static List <FundingPledgesSector> getPledgesBySector (Long sectorId, int sectorQueryType) {
        List retVal = null;
        Session session = null;

        List subSectorIds = null;
        if (sectorId > -1) {
            if (sectorQueryType == SELECT_SECTOR_SCHEME) {
                //In this case we have sector scheme id in sectorId parameter
                List <AmpSector> schemeSecs = SectorUtil.getAllSectorsFromScheme(sectorId);
                subSectorIds = new ArrayList();
                for (AmpSector iterSec: schemeSecs) {
                    subSectorIds.add(iterSec.getAmpSectorId());
                }
            } else if (sectorQueryType == SELECT_SECTOR || sectorQueryType == SELECT_DEFAULT) {
                subSectorIds = getSubSectorIdsWhereclause(sectorId);
            }
        }

        try {
			session = PersistenceManager.getRequestDBSession();
            StringBuffer qStr = new StringBuffer("select distinct ps from ");
            qStr.append(FundingPledgesSector.class.getName());
            qStr.append(" as ps ");
            qStr.append(" where ps.sector.ampSectorId in (");
            qStr.append(getLongIdsWhereclause(subSectorIds));
            qStr.append(")");

            Query q = session.createQuery(qStr.toString());

            retVal = q.list();
            /*
            Iterator <FundingPledgesSector> psi = retVal.iterator();
            while (psi.hasNext()) {
                FundingPledgesSector fps = psi.next();
                fps.getPledgeid().getOrganization();
            }
            */

		} catch (Exception e) {
			logger.error("Error getting pledges, " + e);
		}
        /*
        finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
			}
		}
        */

        return retVal;
    }

    private static StringBuffer getLongIdsWhereclause (List <Long> idList) {
        StringBuffer retVal = new StringBuffer();
        Iterator <Long> it = idList.iterator();
        while (it.hasNext()) {
            retVal.append(it.next());
            if (it.hasNext()) {
                retVal.append(",");
            }
        }
        return retVal;
    }

    public static GisSettings getGisSettings (HttpServletRequest request) {
        Site site = RequestUtils.getSite(request);
        ModuleInstance modInst = RequestUtils.getModuleInstance(request);
        return getGisSettings (site.getSiteId(), modInst.getInstanceName());
    }

    public static GisSettings getGisSettings (String siteId, String instanceId) {
        GisSettings retVal = null;
        Session sess = null;
        try {
            sess = PersistenceManager.getRequestDBSession();
            StringBuffer queryStr = new StringBuffer("from ");
            queryStr.append(GisSettings.class.getName());
            queryStr.append(" as gs where gs.siteId = :SITE_ID and gs.instanceId = :INSTANCE_ID ");

            Query q = sess.createQuery(queryStr.toString());
            q.setString("SITE_ID", siteId);
            q.setString("INSTANCE_ID", instanceId);

            retVal = (GisSettings)q.uniqueResult();

            //If settings do not exist
            if (retVal == null) {
                GisSettings newOne = new GisSettings(siteId, instanceId);
                createGisSettings (newOne);
                retVal = newOne;
            }

        } catch (DgException ex) {
          logger.error("Error getting GIS settings from database " + ex);
        }


        return retVal;
    }

    private static void createGisSettings (GisSettings sets) throws DgException {
        Session sess = PersistenceManager.getRequestDBSession();
        sess.save(sets);
    }
    
    
    // NEW PART
    
    public static Object[] getActivityFundings (Collection<Long> sectorIds,
                                                Collection<Long> programIds,
                                                Collection<Long> donorIds,
                                                Collection<Long> donorGroupIds,
                                                Collection<Long> donorTypeIds,
                                                Collection<AmpCategoryValueLocations> locations,
                                                List <AmpTeam> workspaces,
                                                java.util.Date startDate,
                                                java.util.Date endDate) {
        List queryResults = null;
        Object[] retVal = null;

        Map <Long, Map> sectorPercentageMap = (sectorIds != null && !sectorIds.isEmpty()) ? getActivitySectorPercentages(sectorIds) : null;
        Map <Long, Map> programPercentageMap = (programIds != null && !programIds.isEmpty()) ? getActivityProgramPercentages(programIds) : null;
        Map <Long, Map> locationPercentageMap = (locations != null && !locations.isEmpty()) ? getActivityLocationPercentages(locations) : null;

        String donorIdsWhereclause = generateWhereclause(donorIds, new GenericIdGetter());
        String donorGroupIdsWhereclause = generateWhereclause(donorGroupIds, new GenericIdGetter());
        String donorTypeIdsWhereclause = generateWhereclause(donorTypeIds, new GenericIdGetter());

        Set<Long> allActivityIdsSet = new HashSet<Long>();

        //If no locations selected no data will be returned
        if (locationPercentageMap != null && !locationPercentageMap.isEmpty()) {

            Set<Long> secActIds = null;
            if (sectorPercentageMap != null) {
                secActIds = sectorPercentageMap.keySet();
            }

            Set<Long> prgActIds = null;
            if (programPercentageMap != null) {
                prgActIds = programPercentageMap.keySet();
            }

            Set<Long> actIds = locationPercentageMap.keySet();
            for (Long actId : actIds) {
                if ((secActIds != null && secActIds.contains(actId)) ||  (prgActIds != null && prgActIds.contains(actId))) {
                    allActivityIdsSet.add(actId);
                }
            }
        }

        cleanUpMap(sectorPercentageMap, allActivityIdsSet);
        cleanUpMap(programPercentageMap, allActivityIdsSet);
        cleanUpMap(locationPercentageMap, allActivityIdsSet);


        //If any activity in selected filter
        if (!allActivityIdsSet.isEmpty()) {
            String activityWhereclause = generateWhereclause(allActivityIdsSet, new GenericIdGetter());

            Session sess = null;
            try {
                sess = PersistenceManager.getRequestDBSession();
                StringBuilder queryStr = new StringBuilder("select fd.transactionAmount, fd.transactionType, fd.transactionDate, fd.ampCurrencyId.currencyCode, fd.ampFundingId.ampActivityId.ampActivityId from ");
                queryStr.append(AmpFundingDetail.class.getName());
                queryStr.append(" as fd where fd.transactionDate >= :START_DATE and fd.transactionDate <= :END_DATE");

                queryStr.append(" and fd.ampFundingId.ampActivityId.ampActivityId in ");
                queryStr.append(activityWhereclause);

                if (donorIdsWhereclause != null) {
                    queryStr.append(" and fd.ampFundingId.ampDonorOrgId.ampOrgId in ");
                    queryStr.append(donorIdsWhereclause);
                }

                if (donorGroupIdsWhereclause != null) {
                    queryStr.append(" and fd.ampFundingId.ampDonorOrgId.orgGrpId.ampOrgGrpId in ");
                    queryStr.append(donorGroupIdsWhereclause);
                }

                if (donorTypeIdsWhereclause != null) {
                    queryStr.append(" and fd.ampFundingId.ampDonorOrgId.orgGrpId.orgType.ampOrgTypeId in ");
                    queryStr.append(donorTypeIdsWhereclause);
                }

                Query q = sess.createQuery(queryStr.toString());
                q.setDate("START_DATE", startDate);
                q.setDate("END_DATE", endDate);

                queryResults = q.list();

            } catch (DgException ex) {
              logger.error("Error getting activity fundings from database " + ex);
            }

            retVal = new Object[] {queryResults, sectorPercentageMap, programPercentageMap, locationPercentageMap};
        } else {
            //return dummy result
            retVal = new Object[]{};
        }


        return retVal;
    }


    //Remove act. IDs that didn't match filter criteria
    private static void cleanUpMap (Map<Long, Map> dataMap, Set<Long> allActivityIdsSet) {
        if (dataMap != null) {
            Set<Long> keySet = dataMap.keySet();
            Iterator <Long> it = keySet.iterator();
            Set<Long> removeSet = new HashSet();
            while (it.hasNext()) {
                Long actId = it.next();
                if (!allActivityIdsSet.contains(actId)) {
                    removeSet.add(actId);
                }
            }

            for (Long remId: removeSet) {
                dataMap.remove(remId);
            }
        }

    }

    public static Object[] getActivityRegionalFundings (Collection<Long> sectorIds,
                                                        Collection<Long> programIds,
                                                        Collection<Long> donorIds,
                                                        Collection<Long> donorGroupIds,
                                                        Collection<Long> donorTypeIds,
                                                        Collection<AmpCategoryValueLocations> locations,
                                                        List <AmpTeam> workspaces,
                                                        java.util.Date startDate,
                                                        java.util.Date endDate) {
        List queryResults = null;
        Object[] retVal = null;

        Map <Long, Map> sectorPercentageMap = (sectorIds != null && !sectorIds.isEmpty()) ? getActivitySectorPercentages(sectorIds) : null;
        Map <Long, Map> programPercentageMap = (programIds != null && !programIds.isEmpty()) ? getActivityProgramPercentages(programIds) : null;

        String donorIdsWhereclause = generateWhereclause(donorIds, new GenericIdGetter());
        String donorGroupIdsWhereclause = generateWhereclause(donorGroupIds, new GenericIdGetter());
        String donorTypeIdsWhereclause = generateWhereclause(donorTypeIds, new GenericIdGetter());

        Set<Long> allActivityIdsSet = null;
        String locationWhereclause = null;
        //If no locations selected no data will be returned
        if (locations != null && !locations.isEmpty()) {
            allActivityIdsSet = sectorPercentageMap != null ? sectorPercentageMap.keySet() : new HashSet<Long>();
            Set<Long> prgActIds = programPercentageMap != null ? programPercentageMap.keySet() : new HashSet<Long>();
            allActivityIdsSet.addAll(prgActIds);
            locationWhereclause = generateWhereclause(locations, new LocationIdGetter());
        }



        //If any activity in selected filter
        if (!allActivityIdsSet.isEmpty()) {
            String activityWhereclause = generateWhereclause(allActivityIdsSet, new GenericIdGetter());

            Session sess = null;
            try {
                sess = PersistenceManager.getRequestDBSession();
                StringBuilder queryStr = new StringBuilder("select rf.transactionAmount, rf.transactionType, rf.transactionDate, rf.currency.currencyCode, rf.activity.ampActivityId, rf.regionLocation.id from ");
                queryStr.append(AmpRegionalFunding.class.getName());
                queryStr.append(" as rf where rf.transactionDate >= :START_DATE and rf.transactionDate <= :END_DATE");
                queryStr.append(" and rf.activity.ampActivityId in ");
                queryStr.append(activityWhereclause);
                queryStr.append(" and rf.regionLocation.id in ");
                queryStr.append(locationWhereclause);


                if (donorIdsWhereclause != null) {
                    queryStr.append(" and rf.reportingOrganization.ampOrgId in ");
                    queryStr.append(donorIdsWhereclause);
                }

                if (donorGroupIdsWhereclause != null) {
                    queryStr.append(" and rf.reportingOrganization.orgGrpId.ampOrgGrpId in ");
                    queryStr.append(donorGroupIdsWhereclause);
                }

                if (donorTypeIdsWhereclause != null) {
                    queryStr.append(" and rf.reportingOrganization.orgGrpId.orgType.ampOrgTypeId in ");
                    queryStr.append(donorTypeIdsWhereclause);
                }


                Query q = sess.createQuery(queryStr.toString());
                q.setDate("START_DATE", startDate);
                q.setDate("END_DATE", endDate);

                queryResults = q.list();

            } catch (DgException ex) {
              logger.error("Error getting activity regional fundings from database " + ex);
            }

            retVal = new Object[] {queryResults, sectorPercentageMap, programPercentageMap};
        } else {
            //return dummy result
            retVal = new Object[]{};
        }
        return retVal;
    }

    private static Map <Long, Map> getActivitySectorPercentages (Collection<Long> sectorsIds) {
        String sectorWhereclause = generateWhereclause(sectorsIds, new GenericIdGetter());
        Map <Long, Map> retVal = null;
        Session sess = null;
        try {
            sess = PersistenceManager.getRequestDBSession();
            StringBuilder queryStr = new StringBuilder("select actSec.activityId.ampActivityId, actSec.sectorId.ampSectorId, actSec.sectorPercentage from ");
            queryStr.append(AmpActivitySector.class.getName());
            queryStr.append(" as actSec where actSec.sectorPercentage is not null and actSec.sectorPercentage > 0");
            if (sectorWhereclause != null) {
                queryStr.append(" and actSec.sectorId.ampSectorId in ");
                queryStr.append(sectorWhereclause);
            }
            Query q = sess.createQuery(queryStr.toString());
            List results = q.list();

            if (results != null) {
                retVal = new HashMap <Long, Map>();
                for (Object resultObj: results) {
                    Object[] resArray = (Object[]) resultObj;
                    Long actId = (Long)resArray[0];
                    Long secId = (Long)resArray[1];
                    Float secPercentage = (Float)resArray[2];

                    if (retVal.containsKey(actId)) {
                        ((Map<Long, Float>) retVal.get(actId)).put(secId, secPercentage);
                    } else {
                        Map <Long, Float> secPercentMap = new HashMap <Long, Float>();
                        secPercentMap.put(secId, secPercentage);
                        retVal.put(actId, secPercentMap);
                    }
                }
            }
        } catch (DgException ex) {
          logger.error("Error getting activity sectors from database " + ex);
        }
        return retVal;
    }

    private static Map <Long, Map> getActivityProgramPercentages (Collection<Long> programIds) {
        String programWhereclause = generateWhereclause(programIds, new GenericIdGetter());

        Map <Long, Map> retVal = null;
        Session sess = null;
        try {
            sess = PersistenceManager.getRequestDBSession();
            StringBuilder queryStr = new StringBuilder("select actPrg.activity.ampActivityId, actPrg.program.ampThemeId, actPrg.programPercentage from ");
            queryStr.append(AmpActivityProgram.class.getName());
            queryStr.append(" as actPrg where actPrg.programPercentage is not null and actPrg.programPercentage > 0");
            if (programWhereclause != null) {
                queryStr.append(" and actPrg.program.ampThemeId in ");
                queryStr.append(programWhereclause);
            }
            Query q = sess.createQuery(queryStr.toString());
            List results = q.list();

            if (results != null) {
                retVal = new HashMap <Long, Map>();
                for (Object resultObj: results) {
                    Object[] resArray = (Object[]) resultObj;
                    Long actId = (Long)resArray[0];
                    Long prgId = (Long)resArray[1];
                    Float prgPercentage = new Float(((Long)resArray[2]).floatValue());

                    if (retVal.containsKey(actId)) {
                        ((Map<Long, Float>) retVal.get(actId)).put(prgId, prgPercentage);
                    } else {
                        Map <Long, Float> secPercentMap = new HashMap <Long, Float>();
                        secPercentMap.put(prgId, prgPercentage);
                        retVal.put(actId, secPercentMap);
                    }
                }
            }
        } catch (DgException ex) {
          logger.error("Error getting activity programs from database " + ex);
        }
        return retVal;
    }



    private static Map <Long, Map> getActivityLocationPercentages (Collection<AmpCategoryValueLocations> locations) {
        String locationWhereclause = generateWhereclause(locations, new LocationIdGetter());
        Map <Long, Map> retVal = null;
        Session sess = null;
        try {
            sess = PersistenceManager.getRequestDBSession();
            StringBuilder queryStr = new StringBuilder("select actLoc.activity.ampActivityId, actLoc.location.regionLocation.id, actLoc.locationPercentage from ");
            queryStr.append(AmpActivityLocation.class.getName());
            queryStr.append(" as actLoc where actLoc.locationPercentage is not null and actLoc.locationPercentage > 0");
            if (locationWhereclause != null) {
                queryStr.append(" and actLoc.location.regionLocation.id in ");
                queryStr.append(locationWhereclause);
            }
            Query q = sess.createQuery(queryStr.toString());
            List results = q.list();

            if (results != null) {
                retVal = new HashMap <Long, Map>();
                for (Object resultObj: results) {
                    Object[] resArray = (Object[]) resultObj;
                    Long actId = (Long)resArray[0];
                    Long locId = (Long)resArray[1];
                    Float locPercentage = (Float)resArray[2];

                    if (retVal.containsKey(actId)) {
                        ((Map<Long, Float>) retVal.get(actId)).put(locId, locPercentage);
                    } else {
                        Map <Long, Float> locPercentMap = new HashMap <Long, Float>();
                        locPercentMap.put(locId, locPercentage);
                        retVal.put(actId, locPercentMap);
                    }
                }
            }
        } catch (DgException ex) {
          logger.error("Error getting activity locations from database " + ex);
        }
        return retVal;
    }





    //For pledges
    public static Object[] getPledgeFundings (Collection<Long> sectorIds,
                                              Collection<Long> programIds,
                                              Collection<Long> donorIds,
                                              Collection<AmpCategoryValueLocations> locations,
                                              List <AmpTeam> workspaces,
                                              java.util.Date startDate,
                                              java.util.Date endDate) {
        List queryResults = null;
        Object[] retVal = null;

        Map <Long, Map> sectorPercentageMap = (sectorIds != null && !sectorIds.isEmpty()) ? getPledgeSectorPercentages(sectorIds) : null;
        Map <Long, Map> programPercentageMap = (programIds != null && !programIds.isEmpty()) ? getPledgeProgramPercentages(programIds) : null;
        Map <Long, Map> locationPercentageMap = (locations != null && !locations.isEmpty()) ? getPledgeLocationPercentages(locations) : null;

        Set allPledgeIdsSet = new HashSet<Long>();

        //If no locations selected no data will be returned
        if (locationPercentageMap != null && !locationPercentageMap.isEmpty()) {

            Set<Long> secPledgeIds = null;
            if (sectorPercentageMap != null) {
                secPledgeIds = sectorPercentageMap.keySet();
            }

            Set<Long> prgPledgeIds = null;
            if (programPercentageMap != null) {
                prgPledgeIds = programPercentageMap.keySet();
            }

            Set<Long> actIds = locationPercentageMap.keySet();
            for (Long actId : actIds) {
                if ((secPledgeIds != null && secPledgeIds.contains(actId)) ||  (prgPledgeIds != null && prgPledgeIds.contains(actId))) {
                    allPledgeIdsSet.add(actId);
                }
            }
        }

        //If any activity in selected filter
        if (!allPledgeIdsSet.isEmpty()) {
            String pledgeWhereclause = generateWhereclause(allPledgeIdsSet, new GenericIdGetter());

            Session sess = null;
            try {
                sess = PersistenceManager.getRequestDBSession();
                StringBuilder queryStr = new StringBuilder("select fpd.amount, 0, fpd.fundingYear, fpd.currency, fpd.pledgeid.id from ");
                queryStr.append(FundingPledgesDetails.class.getName());
                queryStr.append(" as fpd where cast(fpd.fundingYear, Integer) >= :START_DATE and cast(fpd.fundingYear, Integer) <= :END_DATE");

                queryStr.append(" and fpd.pledgeid.id in ");
                queryStr.append(pledgeWhereclause);

                Query q = sess.createQuery(queryStr.toString());
                q.setInteger("START_DATE", startDate.getYear() + 1900);
                q.setInteger("END_DATE", endDate.getYear() + 1900);

                queryResults = q.list();

            } catch (Exception ex) {
              logger.error("Error getting oledge fundings from database " + ex);
            }

            retVal = new Object[] {queryResults, sectorPercentageMap, programPercentageMap, locationPercentageMap};
        } else {
            //return dummy result
            retVal = new Object[]{};
        }


        return retVal;
    }

    private static Map <Long, Map> getPledgeSectorPercentages (Collection<Long> sectorIds) {
        String sectorWhereclause = generateWhereclause(sectorIds, new GenericIdGetter());
        Map <Long, Map> retVal = null;
        Session sess = null;
        try {
            sess = PersistenceManager.getRequestDBSession();
            StringBuilder queryStr = new StringBuilder("select pldSec.pledgeid.id, pldSec.sector.ampSectorId, pldSec.sectorpercentage from ");
            queryStr.append(FundingPledgesSector.class.getName());
            queryStr.append(" as pldSec where pldSec.sectorpercentage is not null and pldSec.sectorpercentage > 0");
            if (sectorWhereclause != null) {
                queryStr.append(" and pldSec.sector.ampSectorId in ");
                queryStr.append(sectorWhereclause);
            }
            Query q = sess.createQuery(queryStr.toString());
            List results = q.list();

            if (results != null) {
                retVal = new HashMap <Long, Map>();
                for (Object resultObj: results) {
                    Object[] resArray = (Object[]) resultObj;
                    Long pldId = (Long)resArray[0];
                    Long secId = (Long)resArray[1];
                    Float secPercentage = (Float)resArray[2];

                    if (retVal.containsKey(pldId)) {
                        ((Map<Long, Float>) retVal.get(pldId)).put(secId, secPercentage);
                    } else {
                        Map <Long, Float> secPercentMap = new HashMap <Long, Float>();
                        secPercentMap.put(secId, secPercentage);
                        retVal.put(pldId, secPercentMap);
                    }
                }
            }
        } catch (Exception ex) {
          logger.error("Error getting pledge sectors from database " + ex);
        }
        return retVal;
    }

    private static Map <Long, Map> getPledgeProgramPercentages (Collection<Long> programIds) {
        String programWhereclause = generateWhereclause(programIds, new GenericIdGetter());

        Map <Long, Map> retVal = null;
        Session sess = null;
        try {
            sess = PersistenceManager.getRequestDBSession();
            StringBuilder queryStr = new StringBuilder("select pldSec.pledgeid.id, pldSec.program.ampThemeId, pldSec.programpercentage from ");
            queryStr.append(FundingPledgesProgram.class.getName());
            queryStr.append(" as pldPrg where pldPrg.programpercentage is not null and pldPrg.programpercentage > 0");
            if (programWhereclause != null) {
                queryStr.append(" and pldSec.program.ampThemeId in ");
                queryStr.append(programWhereclause);
            }
            Query q = sess.createQuery(queryStr.toString());
            List results = q.list();

            if (results != null) {
                retVal = new HashMap <Long, Map>();
                for (Object resultObj: results) {
                    Object[] resArray = (Object[]) resultObj;
                    Long pldId = (Long)resArray[0];
                    Long prgId = (Long)resArray[1];
                    Float prgPercentage = new Float(((Long)resArray[2]).floatValue());

                    if (retVal.containsKey(pldId)) {
                        ((Map<Long, Float>) retVal.get(pldId)).put(prgId, prgPercentage);
                    } else {
                        Map <Long, Float> secPercentMap = new HashMap <Long, Float>();
                        secPercentMap.put(prgId, prgPercentage);
                        retVal.put(pldId, secPercentMap);
                    }
                }
            }
        } catch (DgException ex) {
          logger.error("Error getting pledge programs from database " + ex);
        }
        return retVal;
    }



    private static Map <Long, Map> getPledgeLocationPercentages (Collection<AmpCategoryValueLocations> locations) {
        String locationWhereclause = generateWhereclause(locations, new LocationIdGetter());
        Map <Long, Map> retVal = null;
        Session sess = null;
        try {
            sess = PersistenceManager.getRequestDBSession();
            StringBuilder queryStr = new StringBuilder("select pldLoc.pledgeid.id, pldLoc.location.id, pldLoc.locationpercentage from ");
            queryStr.append(FundingPledgesLocation.class.getName());
            queryStr.append(" as pldLoc where pldLoc.locationpercentage is not null and pldLoc.locationpercentage > 0");
            if (locationWhereclause != null) {
                queryStr.append(" and pldLoc.location.id in ");
                queryStr.append(locationWhereclause);
            }
            Query q = sess.createQuery(queryStr.toString());
            List results = q.list();

            if (results != null) {
                retVal = new HashMap <Long, Map>();
                for (Object resultObj: results) {
                    Object[] resArray = (Object[]) resultObj;
                    Long pldId = (Long)resArray[0];
                    Long locId = (Long)resArray[1];
                    Float locPercentage = (Float)resArray[2];

                    if (retVal.containsKey(pldId)) {
                        ((Map<Long, Float>) retVal.get(pldId)).put(locId, locPercentage);
                    } else {
                        Map <Long, Float> locPercentMap = new HashMap <Long, Float>();
                        locPercentMap.put(locId, locPercentage);
                        retVal.put(pldId, locPercentMap);
                    }
                }
            }
        } catch (DgException ex) {
          logger.error("Error getting activity locations from database " + ex);
        }
        return retVal;
    }




    public static class SectorIdGetter implements IdGetter {
        public Long getId (Object obj) {
            Long retVal = null;
            if (obj != null && obj instanceof AmpSector) {
                retVal = ((AmpSector) obj).getAmpSectorId();
            }
            return retVal;
        }
    }

    public static class ProgramIdGetter implements IdGetter {
        public Long getId (Object obj) {
            Long retVal = null;
            if (obj != null && obj instanceof AmpTheme) {
                retVal = ((AmpTheme) obj).getAmpThemeId();
            }
            return retVal;
        }
    }

    public static class DonorIdGetter implements IdGetter {
        public Long getId (Object obj) {
            Long retVal = null;
            if (obj != null && obj instanceof AmpOrganisation) {
                retVal = ((AmpOrganisation) obj).getAmpOrgId();
            }
            return retVal;
        }
    }

    public static class LocationIdGetter implements IdGetter {
        public Long getId (Object obj) {
            Long retVal = null;
            if (obj != null && obj instanceof AmpCategoryValueLocations) {
                retVal = ((AmpCategoryValueLocations) obj).getId();
            }
            return retVal;
        }
    }

    public static class GenericIdGetter implements IdGetter {
        public Long getId (Object obj) {
            Long retVal = null;
            if (obj != null) {
                retVal = (Long) obj;
            }
            return retVal;
        }
    }

    private static String generateWhereclause (Collection objects, IdGetter getter) {
        String retValStr = null;
        if (objects != null) {
            StringBuilder retVal = new StringBuilder("(");
            Iterator it = objects.iterator();
            while (it.hasNext()) {
                Object obj = it.next();
                Long id = getter.getId(obj);
                retVal.append(id);
                if (it.hasNext()) {
                    retVal.append(",");
                }
            }
            retVal.append(")");
            retValStr = retVal.toString();
        }
        return retValStr;
    }

    public static Collection <AmpSector> getSelectedSectors (Long id, int sectorQueryType) {
        Collection <AmpSector> retVal = null;
        if (id > -1) {
            if (sectorQueryType == SELECT_SECTOR_SCHEME) {
                List <AmpSector> schemeSecs = SectorUtil.getAllSectorsFromScheme(id);
                retVal = new ArrayList();
                for (AmpSector iterSec: schemeSecs) {
                    retVal.add(iterSec);
                }
            } else if (sectorQueryType == SELECT_SECTOR || sectorQueryType == SELECT_DEFAULT) {
                retVal = SectorUtil.getAllChildSectors(id);
                retVal.add(SectorUtil.getAmpSector(id));
            }
        } else {
            retVal = SectorUtil.getAllSectors();
        }
        return retVal;
    }

    public static Collection <AmpTheme> getSelectedPrograms (Long id) throws DgException {
        Collection <AmpTheme> retVal = ProgramUtil.getAllSubThemesFor(id);
        retVal.add(ProgramUtil.getThemeById(id));
        return retVal;
    }

    public static Collection <AmpCategoryValueLocations> getSelectedLocations (String countryISO, int mapLevel) {
        List retVal = null;
        try {
            Session sess = PersistenceManager.getRequestDBSession();
            StringBuilder queryStr = new StringBuilder("from ");
            queryStr.append(AmpCategoryValueLocations.class.getName());
            if (mapLevel == GisMap.MAP_LEVEL_REGION) {
                queryStr.append(" as loc where loc.parentCategoryValue.value='Region' and  loc.parentLocation.iso = :COUNTRY_ISO");
            } else if (mapLevel == GisMap.MAP_LEVEL_DISTRICT) {
                queryStr.append(" as loc where loc.parentCategoryValue.value='District' and parentLocation.parentLocation.iso = :COUNTRY_ISO");
            }

            Query q = sess.createQuery(queryStr.toString());
            q.setString("COUNTRY_ISO", countryISO);
            retVal = q.list();
        } catch (Exception ex) {
          logger.error("Error getting locations from database " + ex);
        }
        return retVal;
    }

    public static Map <Long, String> getActivityNames (Set<Long> actIds) {
        Map <Long, String> retVal = new HashMap <Long, String> ();
        try {
            String actIdWhereclause = generateWhereclause(actIds, new GenericIdGetter());
            Session sess = PersistenceManager.getRequestDBSession();
            StringBuilder queryStr = new StringBuilder("select act.ampActivityId, act.name from ");
            queryStr.append(AmpActivity.class.getName());
            queryStr.append(" as act where act.ampActivityId in ");
            queryStr.append(actIdWhereclause);
            Query q = sess.createQuery(queryStr.toString());
            List <Object[]> qRes = q.list();

            if (qRes != null) {
                for (Object[] res : qRes) {
                    retVal.put((Long)res[0], (String)res[1]);
                }
            }


        } catch (Exception ex) {
          logger.error("Error getting activity names from database " + ex);
        }
        return retVal;
    }

    public static Map <Long, Set> getActivitySectorNames (Set<Long> actIds) {
        Map <Long, Set> retVal = new HashMap <Long, Set> ();
        try {
            String actIdWhereclause = generateWhereclause(actIds, new GenericIdGetter());
            Session sess = PersistenceManager.getRequestDBSession();
            StringBuilder queryStr = new StringBuilder("select actSec.activityId.ampActivityId, actSec.sectorId.name from ");
            queryStr.append(AmpActivitySector.class.getName());
            queryStr.append(" as actSec where actSec.activityId.ampActivityId in ");
            queryStr.append(actIdWhereclause);
            Query q = sess.createQuery(queryStr.toString());
            List <Object[]> qRes = q.list();

            if (qRes != null) {
                for (Object[] res : qRes) {
                    Long actId = (Long)res[0];
                    if (!retVal.containsKey(actId)) {
                        retVal.put(actId, new HashSet());
                    }
                    retVal.get(actId).add((String)res[1]);

                }
            }


        } catch (Exception ex) {
          logger.error("Error getting activity sector names from database " + ex);
        }
        return retVal;
    }

    public static Map <Long, Set> getActivityDonorNames (Set<Long> actIds) {
        Map <Long, Set> retVal = new HashMap <Long, Set> ();
        try {
            String actIdWhereclause = generateWhereclause(actIds, new GenericIdGetter());
            Session sess = PersistenceManager.getRequestDBSession();
            StringBuilder queryStr = new StringBuilder("select fnd.ampActivityId.ampActivityId, fnd.ampDonorOrgId.name from ");
            queryStr.append(AmpFunding.class.getName());
            queryStr.append(" as fnd where fnd.ampActivityId.ampActivityId in ");
            queryStr.append(actIdWhereclause);
            Query q = sess.createQuery(queryStr.toString());
            List <Object[]> qRes = q.list();

            if (qRes != null) {
                for (Object[] res : qRes) {
                    Long actId = (Long)res[0];
                    if (!retVal.containsKey(actId)) {
                        retVal.put(actId, new HashSet());
                    }
                    retVal.get(actId).add((String)res[1]);

                }
            }


        } catch (Exception ex) {
          logger.error("Error getting activity donor names from database " + ex);
        }
        return retVal;
    }

    public static Map <Long, Set> getActivityLocationNames (Set<Long> actIds) {
        Map <Long, Set> retVal = new HashMap <Long, Set> ();
        try {
            String actIdWhereclause = generateWhereclause(actIds, new GenericIdGetter());
            Session sess = PersistenceManager.getRequestDBSession();
            StringBuilder queryStr = new StringBuilder("select loc.activity.ampActivityId, loc.location.regionLocation.name from ");
            queryStr.append(AmpActivityLocation.class.getName());
            queryStr.append(" as loc where loc.activity.ampActivityId in ");
            queryStr.append(actIdWhereclause);
            Query q = sess.createQuery(queryStr.toString());
            List <Object[]> qRes = q.list();

            if (qRes != null) {
                for (Object[] res : qRes) {
                    Long actId = (Long)res[0];
                    if (!retVal.containsKey(actId)) {
                        retVal.put(actId, new HashSet());
                    }
                    retVal.get(actId).add((String)res[1]);

                }
            }


        } catch (Exception ex) {
          logger.error("Error getting activity location names from database " + ex);
        }
        return retVal;
    }
}
