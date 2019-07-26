package org.digijava.module.esrigis.helpers;

/**
 * @author Diego Dimunzio
 */

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.admin.exception.AdminException;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpFundingMTEFProjection;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpStructure;
import org.digijava.module.aim.dbentity.AmpStructureType;
import org.digijava.module.aim.dbentity.FundingInformationItem;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.logic.FundingCalculationsHelper;
import org.digijava.module.aim.util.DecimalWraper;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryConstants.HardCodedCategoryValue;
import org.digijava.module.esrigis.dbentity.AmpMapConfig;
import org.hibernate.HibernateException;
import org.hibernate.JDBCException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.jdbc.Work;

public class DbHelper {
    private static Logger logger = Logger.getLogger(DbHelper.class);

    public static void delete(AmpMapConfig map) {
        Session sess = null;
        Transaction tx = null;

        try {
            sess = PersistenceManager.getRequestDBSession();
            // beginTransaction();
            sess.delete(map);
            // tx.commit();
        } catch (Exception e) {
            if (e instanceof JDBCException)
                throw (JDBCException) e;
            logger.error("Exception " + e.toString());
            try {
                tx.rollback();
            } catch (HibernateException ex) {
                logger.error("rollback() failed");
                logger.error(ex.toString());
            }
        }
    }

    public static AmpMapConfig getMap(Long id) {
        Session session = null;
        AmpMapConfig map = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            StringBuilder qs = new StringBuilder("from ").append(AmpMapConfig.class.getName()).
                    append(" mc where mc.id=:ID");
            Query q = session.createQuery(qs.toString());
            q.setLong("ID", id);

            //map = (AmpMapConfig) session.load(AmpMapConfig.class, id);
            map = (AmpMapConfig) q.uniqueResult();
        } catch (Exception e) {
            logger.error("Unable to get object of class "
                    + AmpMapConfig.class.getName() + " width id=" + id
                    + ". Error was:" + e);
        }
        return map;
    }

    public static void save(AmpMapConfig map) {
        Session session = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            session.saveOrUpdate(map);
        } catch (Exception e) {
            logger.error("Unable to save item", e);
        }

    }
    
    
    public static ArrayList<Long> getInActivities(String query)
            throws Exception {
        Session session = PersistenceManager.getRequestDBSession();
        List<Object> qResult = session.createSQLQuery(query).list();
        ArrayList<Long> result = new ArrayList<Long>(qResult.size());
        for (Object obj : qResult) {
            result.add(PersistenceManager.getLong(obj));
        }
        return result;
    }

    
    /**
     * returns a map of all the locations with a certain id
     * @param ids
     * @return
     */
    protected static Map<Long, AmpCategoryValueLocations> getLocationsById(Set<Long> ids)
    {
        Map<Long, AmpCategoryValueLocations> locationsMap = new HashMap<Long, AmpCategoryValueLocations>();
        
        Session session = PersistenceManager.getSession();
        Query createQuery = session.createQuery("from "+AmpCategoryValueLocations.class.getName() + " acvl WHERE acvl.id IN (" + Util.toCSStringForIN(ids) + ")");
        Iterator<AmpCategoryValueLocations> it = createQuery.list().iterator();
        while (it.hasNext()) {
            AmpCategoryValueLocations loc = it.next();
            locationsMap.put(loc.getId(), loc);
        }
        return locationsMap;
    }
    
    /**
     * constructs a Map<ACVL.id, Region/Zone-ACVL.id>
     * @param allUsedAcvlIDs
     * @param impLevel
     * @return
     */
    protected static Map<Long, Long> getLocationRegions(Set<Long> allUsedAcvlIDs, String impLevel)
    {
        final String findLocationRegionsQuery = 
                String.format("SELECT acvl.id, getlocationidbyimpllocMap(acvl.id, '%s'::character varying) AS region_id FROM amp_category_value_location acvl WHERE acvl.id IN (" + Util.toCSStringForIN(allUsedAcvlIDs) + ")",
                        impLevel);      
        final Map<Long, Long> locationToRegion = new HashMap<Long, Long>();
        PersistenceManager.getSession().doWork(new Work() {
            public void execute(java.sql.Connection conn) throws java.sql.SQLException {
                try(RsInfo rsi = SQLUtils.rawRunQuery(conn, findLocationRegionsQuery, null)) {
                    while (rsi.rs.next()) {
                        Long locId = rsi.rs.getLong(1);
                        Long regionLocId = rsi.rs.getLong(2);
                        locationToRegion.put(locId, regionLocId);
                    }
                }
            }
        });
        return locationToRegion;
    }
    
    /**
     * generate a list of (Location, Funding) entries based on an unsorted input soup of funding information
     * @param fundingDets - the input soup of raw funding information:
     *  item[0] = FundingInformationItem fd
     *  item[1] = [Long id] of region
     *  item[2] = [String name] of region
     *  item[3...end] = (optional) [Float locationPercentage, sectorPercentage, xxxPercentage]
     * @param currCode - the code of the currency to use while doing the calculations
     * @param adjustmentType - the adjustment type to add the totals to
     * @param impLevel - the implementation level, used for location levels while walking up the hierarchy
     * @param regListChildren - Collection<AmpCategoryValueLocations.id>, ignored
     * @param decimalsToShow: decimals to format the output
     * @param divideByDenominator: units used for formatting the output
     * @return
     */
    protected static ArrayList<SimpleLocation> generateFundingSummaries(List<Object[]> fundingDets, String currCode, HardCodedCategoryValue adjustmentType, String impLevel, int decimalsToShow, BigDecimal divideByDenominator)
    {
        // collect all the location Ids existant in the system
        Set<Long> allUsedAcvlIDs = new HashSet<Long>();
        for(Object[] obj:fundingDets){
            allUsedAcvlIDs.add((Long) obj[1]);
        }

        Map<Long, Long> locationToRegion = getLocationRegions(allUsedAcvlIDs, impLevel); // Map<acvl.id, region.id>
        Map<Long, AmpCategoryValueLocations> regionLocations = getLocationsById(new HashSet<Long>(locationToRegion.values())); //Map<region.id, region>
                
        // group all funding items by respective region/zone/whatever
        HashMap<Long, ArrayList<FundingInformationItem>> fundingByRegion = new HashMap<Long, ArrayList<FundingInformationItem>>();      
        for(Object[] item:fundingDets)
        {
            FundingInformationItem fd = (FundingInformationItem) item[0];
            FundingInformationItem currentFd;
            if(fd.getTransactionType().equals(Constants.MTEFPROJECTION)){
                currentFd=new AmpFundingMTEFProjection(fd.getTransactionType(),fd.getAdjustmentType(),fd.getAbsoluteTransactionAmount(),fd.getTransactionDate(),fd.getAmpCurrencyId(),fd.getFixedExchangeRate());
            }else{
                currentFd = new AmpFundingDetail(fd.getTransactionType(),fd.getAdjustmentType(),fd.getAbsoluteTransactionAmount(),fd.getTransactionDate(),fd.getAmpCurrencyId(),fd.getFixedExchangeRate());
            }
            Double finalAmount = currentFd.getAbsoluteTransactionAmount();
            for(int i = 3; i < item.length; i++)
                if (item[i] != null)
                {
                    Float fl = (Float) item[i];
                    finalAmount *= (fl / 100.0);
                }
            currentFd.setTransactionAmount(finalAmount);
            Long originald = (Long) item[1];
            Long regionId = locationToRegion.get(originald);
            if (regionId == null)
                continue; // this location has no region
            
            if (!fundingByRegion.containsKey(regionId))
                fundingByRegion.put(regionId, new ArrayList<FundingInformationItem>());
            
            fundingByRegion.get(regionId).add(currentFd);
        }
        
        ArrayList<SimpleLocation> regionTotals = new ArrayList<SimpleLocation>();
        DecimalWraper totaldisbursement = null;
        DecimalWraper totalexpenditures = null;
        DecimalWraper totalcommitment = null;
        DecimalWraper totalMtef = null;
        Iterator<Long> it2 = fundingByRegion.keySet().iterator();
        while(it2.hasNext())
        {
            Long locId = it2.next();
            //Long regionId = locationToRegion.get(locId);
            Long regionId = null;
            for (Map.Entry<Long, Long> e : locationToRegion.entrySet()) {
                if (e.getValue()==locId){
                    regionId = e.getKey();
                    break;
                }
            }
            if (regionId!= 0L && regionId == null)
                continue;

            FundingCalculationsHelper cal = new FundingCalculationsHelper();

            ArrayList<FundingInformationItem> afda = fundingByRegion.get(locId);
            
            cal.doCalculations(afda, currCode, true);
           
            if (CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey().equals(adjustmentType.getValueKey())) {
                totalexpenditures = cal.getTotActualExp();
            } else {
                totalexpenditures = cal.getTotPlannedExp();
            }
            
            if (CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey().equals(adjustmentType.getValueKey())) {
                totaldisbursement = cal.getTotActualDisb();
            } else {
                totaldisbursement = cal.getTotPlanDisb();
            }
            
            if (CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey().equals(adjustmentType.getValueKey())) {
                totalcommitment = cal.getTotActualComm();
            } else {
                totalcommitment = cal.getTotPlannedComm();
            }

            totalMtef = cal.getTotalMtef(); // no adjustment for MTEF
            
            SimpleLocation sl = new SimpleLocation();
            sl.setName(regionLocations.get(locId).getName());
            sl.setGeoId(regionLocations.get(locId).getGeoCode());
            sl.setCommitments(totalcommitment.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(decimalsToShow, RoundingMode.HALF_UP).toString());
            sl.setDisbursements(totaldisbursement.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(decimalsToShow, RoundingMode.HALF_UP).toString());
            sl.setExpenditures(totalexpenditures.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(decimalsToShow, RoundingMode.HALF_UP).toString());
            sl.setAmountsCurrencyCode(currCode); // was filter.getCurrencyCode()
            sl.setMtef(totalMtef.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(decimalsToShow, RoundingMode.HALF_UP).toString());
            regionTotals.add(sl);
        }
    
        return regionTotals;
    }
    
    
    public static ArrayList<Long> getInActivitiesLong(String query)
            throws Exception {
        Session session = PersistenceManager.getRequestDBSession();
        ArrayList<Long> result = (ArrayList<Long>) session.createSQLQuery(query).list();
        return result;
    }
    
    public static Collection<AmpStructureType> getAllStructureTypes() {
        Session session = null;
        Query q = null;
        List<AmpStructureType> sts = new ArrayList<AmpStructureType>();
        StringBuilder queryString = new StringBuilder(
                "select structure_type from "
                        + AmpStructureType.class.getName() + " structure_type ");
        queryString.append("order by " + AmpStructureType.hqlStringForName("structure_type") + " asc");
        try {
            session = PersistenceManager.getRequestDBSession();
            q = session.createQuery(queryString.toString());
            sts = q.list();
        } catch (Exception ex) {
            logger.error("Unable to get Amp Structure Types from database ", ex);
        }
        return sts;
    }

    public static AmpStructureType getStructureTypesByName(String name) {
        Session session = null;
        Query q = null;
        AmpStructureType stt = null;
        ArrayList result;
        StringBuilder queryString = new StringBuilder("select st from "
                + AmpStructureType.class.getName() + " st ");
        queryString.append("where " + AmpStructureType.hqlStringForName("st") + "=:name");
        try {
            session = PersistenceManager.getRequestDBSession();
            q = session.createQuery(queryString.toString());
            q.setString("name", name.trim());
            result = (ArrayList) q.list();
            if (result.size() > 0) {
                stt = (AmpStructureType) result.get(0);
            }
        } catch (Exception ex) {
            logger.error("Unable to get Amp Structure Type from database "
                    + name + " ", ex);
        }
        return stt;
    }

    public static AmpActivity getActivityByAmpId(String id) {
        AmpActivity act = (AmpActivity) PersistenceManager.getSession()
                .createCriteria(AmpActivity.class)
                .add(Restrictions.eq("ampId", id))
                .uniqueResult();
        
        return act;
    }

    public static AmpActivityVersion getActivityById(Long id) {
        Session session = null;
        AmpActivityVersion activity = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            activity = (AmpActivityVersion) session.load(
                    AmpActivityVersion.class, id);

        } catch (Exception ex) {
            logger.error("Unable to get Amp Structure Type from database ", ex);
        }
        return activity;
    }

    public static List<AmpMapConfig> getMaps() {
        Session session = null;
        Query q = null;
        List<AmpMapConfig> maps = new ArrayList<AmpMapConfig>();
        StringBuilder queryString = new StringBuilder("select a from "
                + AmpMapConfig.class.getName() + " a");
        try {
            session = PersistenceManager.getRequestDBSession();
            q = session.createQuery(queryString.toString());
            maps = q.list();
        } catch (Exception ex) {
            logger.error("Unable to get maps from database ", ex);
        }
        return maps;
    }

    public static AmpStructureType getStructureType(Long structureTypeId) {
        Session session = null;
        AmpStructureType ampStructureType = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            ampStructureType = (AmpStructureType) session.load(
                    AmpStructureType.class, structureTypeId);
        } catch (Exception e) {
            logger.error("Unable to get object of class "
                    + AmpStructureType.class.getName() + " width id="
                    + structureTypeId + ". Error was:" + e);
        }
        return ampStructureType;
    }
    public static void deleteStructureType(AmpStructureType structureType) throws AdminException {
        Session sess = null;
        Transaction tx = null;

        try {
            sess = PersistenceManager.getRequestDBSession();
            Query q = sess.createQuery("select st from " + AmpStructure.class.getName() + " st where st.type.typeId=:typeId "  );
            q.setLong("typeId", structureType.getTypeId());
            if (!q.list().isEmpty()){
                throw new AdminException("The Structure Type is being referenced, it can not be deleted."); 
            }   
            
//beginTransaction();
            sess.delete(structureType);
            //tx.commit();
        }
        catch (Exception e) {
            if (e instanceof AdminException){
                throw (AdminException)e;
            }
            if (e instanceof JDBCException)
                throw (JDBCException) e;
            logger.error("Exception " + e.toString());
            try {
                tx.rollback();
            } catch (HibernateException ex) {
                logger.error("rollback() failed");
                logger.error(ex.toString());
            }
        }

    }

    public static void saveStructure(AmpStructure structure) {
        Session session = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            session.save(structure);

        } catch (Exception e) {
            logger.error("Unable to save structure type", e);
        }

    }

    public static void saveStructureType(AmpStructureType structureType) {
        Session session = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            session.save(structureType);

        } catch (Exception e) {
            logger.error("Unable to save structure type", e);
        }

    }

    public static void saveMapConfig(AmpMapConfig map) {
        Session session = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            session.save(map);
        } catch (Exception e) {
            logger.error("Unable to save structure type", e);
        }

    }

    private static Long[] getAllDescendants(Long[] sectorIds,
            ArrayList<AmpSector> allSectorList) {
        // Go through the list to determine the children
        List<Long> tempSectorIds = new ArrayList<Long>();
        for (AmpSector as : allSectorList) {
            if(sectorIds != null) {
                for (Long i : sectorIds) {
                    if (!tempSectorIds.contains(i))
                        tempSectorIds.add(i);
                    if (as.getParentSectorId() != null
                            && as.getParentSectorId().getAmpSectorId().equals(i)) {
                        tempSectorIds.add(as.getAmpSectorId());
                    } else if (as.getParentSectorId() != null
                            && as.getParentSectorId().getParentSectorId() != null
                            && as.getParentSectorId().getParentSectorId()
                                    .getAmpSectorId().equals(i)) {
                        tempSectorIds.add(as.getAmpSectorId());
                    }
                }
            } else {
                tempSectorIds.add(new Long(-1));
            }
        }
        return (Long[]) tempSectorIds.toArray(new Long[0]);
    }

    public static Long[] getAllDescendantsLocation(Long[] locationIds) 
    {
        Set<Long> ids = DynLocationManagerUtil.getRecursiveChildrenOfCategoryValueLocations(Arrays.asList(locationIds), false);
        return (Long[]) ids.toArray(new Long[0]);
    }

    public static AmpMapConfig getMapByType(Integer mapType) {
        Session session = null;
        Query q = null;
        AmpMapConfig map = new AmpMapConfig();
        StringBuilder queryString = new StringBuilder("select a from "
                + AmpMapConfig.class.getName() + " a where mapType = :mapType ");
        try {
            session = PersistenceManager.getRequestDBSession();
            q = session.createQuery(queryString.toString());
            q.setInteger("mapType", mapType);
            List<AmpMapConfig> maps = q.list();
            if (maps.size() > 0)
                map = maps.get(0);
            else
                map = null;
        } catch (Exception ex) {
            logger.error("Unable to get individual map from database, of type: " + mapType, ex);
        }
        return map;
    }

    public static List<AmpMapConfig> getMapsBySubType(Integer mapSubType) {
        Session session = null;
        Query q = null;
        
        List<AmpMapConfig> maps = new ArrayList<AmpMapConfig>();
        StringBuilder queryString = new StringBuilder("select a from "
                + AmpMapConfig.class.getName() + " a where mapSubType = :mapSubType ");
        try {
            session = PersistenceManager.getRequestDBSession();
            q = session.createQuery(queryString.toString());
            q.setInteger("mapSubType", mapSubType);
            maps = q.list();
        } catch (Exception ex) {
            logger.error("Unable to get individual map from database, of type: " + mapSubType, ex);
        }
        return maps;        
    }

    public static List<AmpCategoryValue> getPeacebuildingMarkers() {
        List<AmpCategoryValue> retVal = null;
        StringBuilder qs = new StringBuilder("from ").
                append(AmpCategoryValue.class.getName()).
                append(" cv where cv.ampCategoryClass.keyName='procurement_system'");
        try {
            Session session = PersistenceManager.getRequestDBSession();
            Query q = session.createQuery(qs.toString());
            retVal = q.list();

            if (retVal != null && !retVal.isEmpty()) {

                Collections.sort(retVal, new Comparator <AmpCategoryValue> (){
                    public int compare(AmpCategoryValue o1, AmpCategoryValue o2) {
                        int retVal;
                        if (o1.getIndex()<o2.getIndex()) retVal = -1;
                                else if (o1.getIndex()<o2.getIndex()) retVal = 0;
                                else retVal = 1;
                        return retVal;
                    }
                }
                );
            }

        } catch (Exception ex) {
            logger.error("Unable to get peacebuilding markers from database", ex);
        }

        return retVal;
    }
    
   
    
    

}
