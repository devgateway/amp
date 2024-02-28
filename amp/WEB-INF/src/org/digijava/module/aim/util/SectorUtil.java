package org.digijava.module.aim.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.algo.AlgoUtils;
import org.dgfoundation.amp.algo.DatabaseWaver;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.ampapi.endpoints.sectorMapping.dto.SchemaClassificationDTO;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.Sector;
import org.digijava.module.aim.util.caching.AmpCaching;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.jdbc.Work;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StandardBasicTypes;

/**
 * Utility class for persisting all Sector with Scheme related entities
 *
 * @author Govind G Dalwani
 */

public class SectorUtil {

    private static Logger logger = Logger.getLogger(SectorUtil.class);
    private static List<String> sectorsOrder = Arrays.asList(AmpClassificationConfiguration
                    .PRIMARY_CLASSIFICATION_CONFIGURATION_NAME,
            AmpClassificationConfiguration.SECONDARY_CLASSIFICATION_CONFIGURATION_NAME,
            AmpClassificationConfiguration.TERTIARY_CLASSIFICATION_CONFIGURATION_NAME,
            AmpClassificationConfiguration.QUATERNARY_CLASSIFICATION_CONFIGURATION_NAME,
            AmpClassificationConfiguration.QUINARY_CLASSIFICATION_CONFIGURATION_NAME,
            AmpClassificationConfiguration.TAG_CLASSIFICATION_CONFIGURATION_NAME);

    public static Collection searchForSector(String keyword, Long ampSecSchemeId) {
        Session session = null;
        Collection col = new ArrayList();

        logger.info("INSIDE Search Sector DBUTIL..... ");

        try {

            session = PersistenceManager.getSession();
            int tempIncr = 0;
            logger.info("searching SECTORS...............");
            /*
             * String qryStr =
             * "select country.countryId,country.countryName,region.ampRegionId,region.name "
             * + "from " + Country.class.getName() + " country , " +
             * AmpRegion.class.getName() + " " +
             * "region where region.name like '%" + keyword + "%' and " +
             * "country.iso = region.country";
             */
            String sectorNameHql = AmpSector.hqlStringForName("sector");
            String qryStr = "select sector from " + AmpSector.class.getName()
                    + " sector inner join sector.ampSecSchemeId " + " sscheme"
                    + " where lower(" + sectorNameHql + ") like '%"
                    + keyword.toLowerCase() + "%' and (sector.deleted is null or sector.deleted = false) ";
            if (ampSecSchemeId != null) {
                qryStr += " and sscheme.ampSecSchemeId=:ampSecSchemeId";
            }

            Query qry = session.createQuery(qryStr);
            // qry.setParameter("orgType", orgType, LongType.INSTANCE) ;
            if (ampSecSchemeId != null) {
                qry.setLong("ampSecSchemeId", ampSecSchemeId);
            }
            // col =qry.list();
            Iterator itr = qry.list().iterator();

            ActivitySector sectr;
            tempIncr = 0;

            while (itr.hasNext()) {
                AmpSector as = (AmpSector) itr.next();
                sectr = new ActivitySector();
                sectr.setSectorScheme(as.getAmpSecSchemeId().getSecSchemeName());
                if (as.getParentSectorId() != null) {

                    sectr.setSectorName(as.getParentSectorId().getName());
                    sectr.setSectorId(as.getParentSectorId().getAmpSectorId());
                    sectr.setSubsectorLevel1Id(as.getAmpSectorId());
                    sectr.setSubsectorLevel1Name(as.getName());

                    if (as.getParentSectorId().getParentSectorId() != null) {

                        sectr.setSectorName(as.getParentSectorId()
                                .getParentSectorId().getName());
                        sectr.setSectorId(as.getParentSectorId()
                                .getAmpSectorId());
                        sectr.setSubsectorLevel1Id(as.getParentSectorId()
                                .getAmpSectorId());
                        sectr.setSubsectorLevel1Name(as.getParentSectorId()
                                .getName());
                        sectr.setSubsectorLevel2Id(as.getAmpSectorId());
                        sectr.setSubsectorLevel2Name(as.getName());

                    }
                } else {

                    sectr.setSectorName(as.getName());
                    sectr.setSectorId(as.getAmpSectorId());

                }
                col.add(sectr);
            }

        } catch (Exception ex) {
            logger.debug("Unable to search sectors" + ex);
        }
        return col;
    }// End Search Sector.

    public static List<AmpSectorScheme> getAllSectorSchemes() {
        return getAllSectorSchemes(false);
    }

    /**
     * Get allSector Scheme
     * @param classificationConfiguration check if the sector scheme has its associated ClassificationConfiguration
     * @return
     */
    public static List<AmpSectorScheme> getAllSectorSchemes(boolean classificationConfiguration) {
        try {
            String sectorSchemeNameHql = AmpSectorScheme.hqlStringForName("ss");

            StringBuffer queryString =new StringBuffer( "select ss from " + AmpSectorScheme.class.getName() + " ss ");
            if(classificationConfiguration){
                queryString.append("where exists ( from "+ AmpClassificationConfiguration.class.getName() +" cc where cc.classification.ampSecSchemeId=ss.ampSecSchemeId )");
            }
            queryString.append(" order by " + sectorSchemeNameHql);


            Query qry = PersistenceManager.getSession().createQuery(queryString.toString());
            List<AmpSectorScheme> col = new ArrayList<>(qry.list());
            return col;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static List<SchemaClassificationDTO> getAllSectorSchemesAndClassification() {
        String queryString = null;
        Session session = null;
        Query qry = null;
        List<SchemaClassificationDTO> result = null;

        try {
            session = PersistenceManager.getSession();
            queryString = "SELECT sch.amp_sec_scheme_id id, sch.sec_scheme_name value, " +
                    " conf.classification_id classificationId, conf.name classificationName " +
                    " FROM amp_classification_config conf " +
                    " FULL OUTER JOIN amp_sector_scheme sch ON conf.classification_id = sch.amp_sec_scheme_id ";

            qry = session.createSQLQuery(queryString)
                    .addScalar("id", StandardBasicTypes.LONG)
                    .addScalar("value", StandardBasicTypes.STRING)
                    .addScalar("classificationId", StandardBasicTypes.LONG)
                    .addScalar("classificationName", StandardBasicTypes.STRING)
                    .setResultTransformer(Transformers.aliasToBean(SchemaClassificationDTO.class));

            result = qry.list();
        } catch (Exception ex) {
            logger.error("Unable to get Schemes and classification from database " + ex.getMessage());
            ex.printStackTrace(System.out);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static List<AmpSector> getAllParentSectors(Long secSchemeId){
        try
        {
            String queryString = "select s from " + AmpSector.class.getName()
                    + " s " + "where amp_sec_scheme_id = " + secSchemeId
                    + " and parent_sector_id is null and (s.deleted is null or s.deleted = false)  " + "order by " + AmpSector.hqlStringForName("s");
            List<AmpSector> sectors = new ArrayList<>(PersistenceManager.getSession().createQuery(queryString).list());
            return sectors;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<AmpSector> getAllParentSectors() {
        Session session = null;
        List<AmpSector> col = null;

        try {
            session = PersistenceManager.getSession();

            String queryString = "select cls from "
                    + AmpClassificationConfiguration.class.getName()
                    + " cls where cls.primary=true ";
            Query qry = session.createQuery(queryString);
            AmpClassificationConfiguration auxConfig = (AmpClassificationConfiguration) qry
                    .uniqueResult();

            queryString = "select s from "
                    + AmpSector.class.getName()
                    + " s "
                    + " where parent_sector_id is null and amp_sec_scheme_id = "
                    + auxConfig.getClassification().getAmpSecSchemeId()
                    + " and (s.deleted is null or s.deleted = false)  order by " + AmpSector.hqlStringForName("s");
            qry = session.createQuery(queryString);
            col = qry.list();

        } catch (Exception e) {
            logger.error("Cannot get parent sectors, " + e);
        }
        return col;
    }

    public static Collection<AmpSector> getAllSectors() {
        if (AmpCaching.getInstance().sectorsCache == null)
        {
            Session session = null;

            try {
                session = PersistenceManager.getRequestDBSession();
                String queryString = "select sc from " + AmpSector.class.getName()
                        + " sc where (sc.deleted is null or sc.deleted = false) order by " + AmpSector.hqlStringForName("sc");
                Query qry = session.createQuery(queryString);
                AmpCaching.getInstance().initSectorsCache(qry.list());
            } catch (Exception e) {
                throw new RuntimeException("Cannot get sectors, ", e);
            }
        }
        return AmpCaching.getInstance().sectorsCache.getAllSectors();
    }

    public static Collection<AmpSector> getAllChildSectors(Long parSecId) {
        if (AmpCaching.getInstance().sectorsCache == null)
            getAllSectors(); // force initialization of cache

        return AmpCaching.getInstance().sectorsCache.getChildSectors(parSecId);
    }

    public static void updateSectorOrganisation(Long sectorId,
            AmpOrganisation organisation) {
        AmpSector sector = getAmpSector(sectorId);
        sector.setAmpOrgId(organisation);
        DbUtil.update(sector);
        AmpCaching.getInstance().sectorsCache = null; //invalidate
    }

    public static void updateSubSectors(AmpSector sector,
            AmpOrganisation organisation) {

        int index = 0;
        ArrayList sectorList = new ArrayList();
        Iterator itr = null;

        try {

            itr = getSubSectors(sector.getAmpSectorId()).iterator();
            while (itr.hasNext()) {
                Sector subSec = (Sector) itr.next();
                sectorList.add(subSec);
            }
            while (index < sectorList.size()) {
                Sector sec = (Sector) sectorList.get(index++);
                itr = getSubSectors(sec.getSectorId()).iterator();

                while (itr.hasNext()) {
                    Sector subSec = (Sector) itr.next();
                    sectorList.add(subSec);
                }
            }

            for (int i = 0; i < sectorList.size(); i++) {
                Sector sec = (Sector) sectorList.get(i);
                updateSectorOrganisation(sec.getSectorId(), organisation);
            }
        } catch (Exception e) {
            logger.debug("Exception from updateSubSectors()");
            logger.debug(e.toString());
        }
    }

    // Retreives all sub-sectors within the sector with id 'parentSecId'
    public static Collection getSubSectors(Long parentSecId) {

        Session session = null;
        Query qry = null;
        Collection col = new ArrayList();
        Iterator itr = null;
        AmpSector ampSector = null;

        try {
            session = PersistenceManager.getSession();
            String queryString = new String();

            if (parentSecId.intValue() == 0) {
                queryString = "select s from " + AmpSector.class.getName()
                        + " s where s.parentSectorId is null and (s.deleted is null or s.deleted = false) order by " + AmpSector.hqlStringForName("s");

                qry = session.createQuery(queryString);
            } else {
                queryString = "select s from " + AmpSector.class.getName()
                        + " s where (s.parentSectorId=:parentSectorId) "
                        + " and (s.deleted is null or s.deleted = false) order by " + AmpSector.hqlStringForName("s");

                qry = session.createQuery(queryString);
                qry.setParameter("parentSectorId", parentSecId, LongType.INSTANCE);
            }
            itr = qry.list().iterator();

            while (itr.hasNext()) {
                ampSector = (AmpSector) itr.next();

                Sector sec = new Sector();
                if (ampSector.getAmpOrgId() != null) {
                    sec.setOrgId(ampSector.getAmpOrgId().getAmpOrgId());
                    sec.setOrgName(ampSector.getAmpOrgId().getName());
                }
                sec.setSectorId(ampSector.getAmpSectorId());
                sec.setSectorName(ampSector.getName());
                col.add(sec);
            }

        } catch (Exception ex) {
            logger.error("Unable to get subsectors");
            ex.printStackTrace(System.out);
        }

        return col;
    }

    /*
     * Retreives the sector details for the sector with the id 'sectorId'
     */
    public static Sector getSector(Long sectorId) {

        Session session = null;
        Query qry = null;
        Iterator itr = null;
        AmpSector ampSector = null;
        Sector sec = null;

        try {
            session = PersistenceManager.getSession();
            String queryString = new String();
            queryString = "select s from " + AmpSector.class.getName()
                    + " s where (s.ampSectorId=:ampSectorId) and (s.deleted is null or s.deleted = false) ";

            qry = session.createQuery(queryString);
            qry.setParameter("ampSectorId", sectorId, LongType.INSTANCE);
            itr = qry.list().iterator();

            if (itr.hasNext()) {
                ampSector = (AmpSector) itr.next();
                sec = new Sector(ampSector.getAmpSectorId(),
                        ampSector.getName(), ampSector.getAmpOrgId()
                                .getAmpOrgId(), DbUtil.getOrganisation(
                                ampSector.getAmpOrgId().getAmpOrgId())
                                .getName());

            }

        } catch (Exception ex) {
            logger.error("Unable to get sector info");
            logger.debug("Exceptiion " + ex);
        }
        return sec;
    }

    public static boolean getIndIcatorSector(Long indicatorId, Long sectorId) {

        Session session = null;
        Query qry = null;
        List<AmpIndicatorSector> indSectors = null;
        boolean exist = false;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = new String();
            queryString = "select s from "
                    + AmpIndicatorSector.class.getName()
                    + " s where (s.themeIndicatorId=:themeIndicatorId) and s.sectorId=:sectorId";

            qry = session.createQuery(queryString);
            qry.setParameter("themeIndicatorId", indicatorId, LongType.INSTANCE);
            qry.setLong("sectorId", sectorId);
            indSectors = qry.list();
            if (indSectors != null && indSectors.size() > 0) {
                exist = true;
            }

        } catch (Exception e) {
            logger.error("Unable to get sector");
            logger.debug("Exceptiion " + e);
        }

        return exist;
    }

    public static AmpSector getAmpSector(Long id)
    {
        try
        {
            AmpSector s = (AmpSector) PersistenceManager.getRequestDBSession().load(AmpSector.class, id);
            //if ((s.getDeleted()) != null && (s.getDeleted()))
                return s;
            //return null; // deleted
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static AmpSectorScheme getAmpSectorScheme(Long id) {
        Session session = null;
        Query qry = null;
        Iterator itr = null;
        AmpSectorScheme ampSectorScheme = null;
        try {
            session = PersistenceManager.getSession();
            String queryString = new String();
            queryString = "select s from " + AmpSectorScheme.class.getName()
                    + " s where (s.ampSecSchemeId=:ampSectorSchemeId)";

            qry = session.createQuery(queryString);
            qry.setParameter("ampSectorSchemeId", id, LongType.INSTANCE);
            itr = qry.list().iterator();

            if (itr.hasNext()) {
                ampSectorScheme = (AmpSectorScheme) itr.next();
            }

        } catch (Exception ex) {
            logger.error("Unable to get amp_sector_scheme info");
            logger.debug("Exception " + ex);
        }
        return ampSectorScheme;

    }

    public static Set<AmpSector> getAmpParentSectors(
            Collection<AmpSector> sectors) {
        if (sectors != null) {
            Set<AmpSector> retList = new HashSet<AmpSector>();
            for (AmpSector sector : sectors) {
                retList.addAll(SectorUtil.getAmpParentSectors(sector
                        .getAmpSectorId()));
            }
            return retList;
        }
        return null;
    }

    public static Collection<AmpSector> getAmpParentSectors(Long ampSectorId) {
        Session session = null;
        Query q = null;
        Collection<AmpSector> ret = new ArrayList<AmpSector>();
        String queryString = null;
        Iterator iter = null;

        try {
            session = PersistenceManager.getSession();

            AmpSector ampSector = (AmpSector) session.load(AmpSector.class,
                    ampSectorId);
            while (ampSector.getParentSectorId() != null) {
                ampSector = ampSector.getParentSectorId();
                ret.add(ampSector);
            }

        } catch (Exception ex) {
            logger.error("Unable to get Amp sub sectors from database "
                    + ex.getMessage());
        }
        return ret;
    }

    public static Collection searchSectorCode(String key) {
        Session sess = null;
        Collection col = null;
        Query qry = null;

        try {
            sess = PersistenceManager.getSession();
            String queryString = "select s from " + AmpSector.class.getName()
                    + " s where s.sectorCode like '%" + key + "%' and (s.deleted is null or s.deleted = false) ";
            qry = sess.createQuery(queryString);
            Iterator itr = qry.list().iterator();
            col = new ArrayList();
            while (itr.hasNext()) {
                col.add(itr.next());
            }

        } catch (Exception e) {
            logger.debug("Exception from searchSectorCode()");
            logger.debug(e.toString());
        }
        return col;

    }

    public static Collection searchSectorName(String key) {
        Session sess = null;
        Collection col = null;
        Query qry = null;

        try {
            sess = PersistenceManager.getSession();
            String sectorNameHql = AmpSector.hqlStringForName("s");
            String queryString = "select s from " + AmpSector.class.getName()
                    + " s where " + sectorNameHql + " like '%" + key + "%' and (s.deleted is null or s.deleted = false) ";
            qry = sess.createQuery(queryString);
            Iterator itr = qry.list().iterator();
            col = new ArrayList();
            while (itr.hasNext()) {
                col.add(itr.next());
            }

        } catch (Exception e) {
            logger.debug("Exception from searchSectorName()");
            logger.debug(e.toString());
        }
        return col;
    }

    /**
     *
     *
     * @return List of sectors and sub-sectors ordered by sectors alphabetically
     *         and then by sub-sectors alphabetically (Ex. A, a1, a2, a3, B, b1,
     *         b2, etc...). The names of the sectors are a embelished (upper
     *         case, or added some spaces) for better presentation. DO NOT save
     *         this objects back to the database !!!!
     */
    public static List<AmpSector> getAmpSectorsAndSubSectors(
            String configurationName) {
        List<AmpSector> ret = new ArrayList<AmpSector>();
        Long id = null;
            Collection<AmpClassificationConfiguration> configs = SectorUtil
                    .getAllClassificationConfigs();
            Iterator<AmpClassificationConfiguration> confIter = configs
                    .iterator();
            while (confIter.hasNext()) {
                AmpClassificationConfiguration conf = confIter.next();
                if (configurationName.equals(conf.getName())) {
                    if (conf.getClassification() != null)
                        id = conf.getClassification().getAmpSecSchemeId();
                }
            }
            if (id != null) {
                Collection<AmpSector> dbReturnSet = SectorUtil
                        .getAllParentSectors(id);
                Iterator<AmpSector> iter = dbReturnSet.iterator();
                while (iter.hasNext()) {
                    AmpSector ampSector = iter.next();
                    ampSector.setName(ampSector.getName().toUpperCase());
                    ret.add(ampSector);
                    Collection<AmpSector> dbChildReturnSet = SectorUtil
                            .getAllChildSectors(ampSector.getAmpSectorId());

                    Iterator<AmpSector> iterSub = dbChildReturnSet.iterator();
                    while (iterSub.hasNext()) {
                        AmpSector ampSubSector = (AmpSector) iterSub.next();
                        String temp = " -- " + ampSubSector.getName();
                        ampSubSector.setName(temp);
                        ret.add(ampSubSector);

                        Collection<AmpSector> dbChildReturnSet2 = SectorUtil
                                .getAllChildSectors(ampSubSector
                                        .getAmpSectorId());
                        if (dbChildReturnSet2 != null
                                && dbChildReturnSet2.size() > 0) {
                            for (AmpSector ampSubSubSector : dbChildReturnSet2) {
                                ampSubSubSector.setName(" -- -- "
                                        + ampSubSubSector.getName());
                                ret.add(ampSubSubSector);
                            }
                        }
                    }
                }
            }

        return ret;
    }

    /**
     * TODO: this is poor man's recursion
     * it can certainly be rewritten
     * @param configurationName
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Collection<SectorSkeleton> getAmpSectorsAndSubSectorsHierarchyFaster(String configurationName) {
        Long id = null;
        Collection<AmpClassificationConfiguration> configs = SectorUtil.getAllClassificationConfigs();
        for (AmpClassificationConfiguration conf:configs){
                if (configurationName.equals(conf.getName())) {
                    if (conf.getClassification() != null)
                        id = conf.getClassification().getAmpSecSchemeId();
                }
            }
        if (id == null)
            return new HashSet<>(); // empty result

        Map<Long, SectorSkeleton> parents = SectorSkeleton.getParentSectors(id);
        Map<Long, SectorSkeleton> children = SectorSkeleton.getAllSectors(parents);
        Map<Long, SectorSkeleton> subchildren = SectorSkeleton.getAllSectors(children);
        SectorSkeleton.setParentChildRelationships(parents, children, subchildren);
        return new TreeSet<>(parents.values());
    }


    /**
     * TODO: this is poor man's recursion
     * @param configurationName
     * @return
     */
    public static List<AmpSector> getAmpSectorsAndSubSectorsHierarchy(
            String configurationName) {
        if (AmpCaching.getInstance().sectorsCache == null)
            getAllSectors(); //force rebuilding cache
        if (AmpCaching.getInstance().sectorsCache.sectorsHierarchy.containsKey(configurationName))
            return new ArrayList<AmpSector>(AmpCaching.getInstance().sectorsCache.sectorsHierarchy.get(configurationName));

        List<AmpSector> ret = new ArrayList<AmpSector>();
        Long id = null;

            Collection<AmpClassificationConfiguration> configs = SectorUtil
                    .getAllClassificationConfigs();
            Iterator<AmpClassificationConfiguration> confIter = configs
                    .iterator();
            while (confIter.hasNext()) {
                AmpClassificationConfiguration conf = confIter.next();
                if (configurationName.equals(conf.getName())) {
                    if (conf.getClassification() != null)
                        id = conf.getClassification().getAmpSecSchemeId();
                }
            }
            if (id != null) {
                Collection<AmpSector> dbReturnSet = SectorUtil
                        .getAllParentSectors(id);
                Iterator<AmpSector> iter = dbReturnSet.iterator();
                while (iter.hasNext()) {
                    AmpSector ampSector = iter.next();
                    ret.add(ampSector);
                    Collection<AmpSector> dbChildReturnSet = SectorUtil
                            .getAllChildSectors(ampSector.getAmpSectorId());
                    ampSector.getChildren().addAll(dbChildReturnSet);
                    if (ampSector.getChildren() != null) {
                        Iterator<AmpSector> iter2 = ampSector.getChildren()
                                .iterator();
                        while (iter2.hasNext()) {
                            AmpSector ampSubSector = iter2.next();
                            Collection<AmpSector> dbChildReturnSet2 = SectorUtil
                                    .getAllChildSectors(ampSubSector
                                            .getAmpSectorId());
                            if (dbChildReturnSet2 != null)
                                ampSubSector.getChildren().addAll(
                                        dbChildReturnSet2);
                        }
                    }
                }
            }
        AmpCaching.getInstance().sectorsCache.sectorsHierarchy.put(configurationName, ret);
        return new ArrayList<AmpSector>(ret);
    }

    /*
     * this is to get the sector schemes from the ampSectorScheme table
     */
    @SuppressWarnings("unchecked")
    public static Collection<AmpSectorScheme> getSectorSchemes() {
        String queryString = null;
        Session session = null;
        Collection<AmpSectorScheme> col = null;
        Query qry = null;

        try {
            session = PersistenceManager.getSession();// TODO why not thread
                                                        // session? please check
                                                        // and remove this
                                                        // comment.
            queryString = "select pi from " + AmpSectorScheme.class.getName()
                    + " pi ";
            qry = session.createQuery(queryString);
            col = qry.list();
        } catch (Exception ex) {
            logger.error("Unable to get report names  from database "
                    + ex.getMessage());
            ex.printStackTrace(System.out);
        }
        return col;
    }

    /*
     * this is to get the level one sectors from the AmpSector table
     */
    public static Collection getSectorLevel1(Integer schemeId) {
        String queryString = null;
        Session session = null;
        Collection col = null;
        Query qry = null;

        try {
            session = PersistenceManager.getSession();
            queryString = "select pi from "
                    + AmpSector.class.getName()
                    + " pi where pi.ampSecSchemeId=:schemeId and pi.parentSectorId IS null and (pi.deleted is null or pi.deleted = false) order by " + AmpSector.hqlStringForName("pi");
            qry = session.createQuery(queryString);
            qry.setParameter("schemeId", schemeId, IntegerType.INSTANCE);
            col = qry.list();
            // session.flush();
        } catch (Exception ex) {
            logger.error("Unable to get report names  from database "
                    + ex.getMessage());
            ex.printStackTrace(System.out);
        }
        return col;
    }

    /*
     * get scheme to be edited
     */
    public static Collection getEditScheme(Integer schemeId) {
        String queryString = null;
        Session session = null;
        Collection col = null;
        Query qry = null;

        try {
            session = PersistenceManager.getSession();
            queryString = "select pi from " + AmpSectorScheme.class.getName()
                    + " pi where pi.ampSecSchemeId=:schemeId";
            qry = session.createQuery(queryString);
            qry.setParameter("schemeId", schemeId, IntegerType.INSTANCE);
            col = qry.list();
        } catch (Exception ex) {
            logger.error("Unable to get report names  from database "
                    + ex.getMessage());
            ex.printStackTrace(System.out);
        }
        return col;
    }

    /**
     * Returns All Configurations of Classifications in default order
     *
     * @return All Configurations in default order
     */
    public static List<AmpClassificationConfiguration> getAllClassificationConfigsOrdered() {

        List<AmpClassificationConfiguration> allClassificationConfigs = getAllClassificationConfigs();

        allClassificationConfigs.sort(Comparator.comparingInt(item -> sectorsOrder.indexOf(item.getName())));

        return allClassificationConfigs;
    }

    /**
     * Returns All Configurations of Classifications
     *
     * @return All Configurations
     * @throws DgException
     *             If exception occurred
     */
    public static List<AmpClassificationConfiguration> getAllClassificationConfigs() {
        String queryString = "select cls from " + AmpClassificationConfiguration.class.getName() + " cls ";
        return PersistenceManager.getSession().createQuery(queryString).list();
    }

    public static AmpClassificationConfiguration getClassificationConfigBySectorSchemeId(Long sectorSchemeId) {
        String queryString = "select cls from " + AmpClassificationConfiguration.class.getName() + " cls  where cls.classification.ampSecSchemeId = " + sectorSchemeId;
        return (AmpClassificationConfiguration) PersistenceManager.getSession().createQuery(queryString).uniqueResult();
    }

    /**
     * Returns Classification Configuration by Configuration Id
     *
     * @param configId
     *            Configuration Id
     * @return Classification Configuration using Configuration Id
     * @throws DgException
     *             If exception occurred
     */
    public static AmpClassificationConfiguration getClassificationConfigById(
            Long configId) throws DgException {

        Session session = null;
        AmpClassificationConfiguration config = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            config = (AmpClassificationConfiguration) session.load(
                    AmpClassificationConfiguration.class, configId);
        } catch (Exception ex) {
            logger.error("Unable to get configs from database "
                    + ex.getMessage());
            throw new DgException(ex);

        }

        return config;
    }

    /**
     * Returns true if specified Scheme is selected as default classification in
     * the configuration otherwise returns false.
     *
     * @param classificationId
     *            Id of classification
     * @return true If specified classification is selected as default
     *         classification in the configuration
     * @throws DgException
     *             If exception occurred
     */
    public static boolean isSchemeUsed(Long classificationId)
            throws DgException {

        Session session = null;
        boolean used = false;
        String queryString = null;
        List configs = null;
        Query qry = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            queryString = "select cls from "
                    + AmpClassificationConfiguration.class.getName()
                    + " cls where cls.classification=:classificationId ";
            qry = session.createQuery(queryString);
            qry.setLong("classificationId", classificationId);
            configs = qry.list();
            if (configs != null && configs.size() > 0) {
                used = true;
            }
        } catch (Exception ex) {
            logger.error("Unable to get configs from database "
                    + ex.getMessage());
            throw new DgException(ex);

        }

        return used;
    }

    /**
     * Returns true if specified classification is selected as default
     * classification in the configuration otherwise returns false.
     *
     * @param classificationId
     *            Id of classification
     * @return true If specified classification is selected as default
     *         classification in the configuration
     * @throws DgException
     *             If exception occurred
     */
    public static boolean isClassificationUsed(Long classificationId)
            throws DgException {

        Session session = null;
        boolean used = false;
        String queryString = null;
        List configs = null;
        Query qry = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            queryString = "select aps from "
                    + AmpActivitySector.class.getName()
                    + " aps where aps.classificationConfig=:classificationId";
            qry = session.createQuery(queryString);
            qry.setLong("classificationId", classificationId);
            configs = qry.list();
            if (configs != null && configs.size() > 0) {
                used = true;
            }
        } catch (Exception ex) {
            logger.error("Unable to get configs from database "
                    + ex.getMessage());
            throw new DgException(ex);

        }

        return used;
    }

    /**
     * adds or update classification configuration
     *
     *
     * @param configId
     *            Id of configuration
     * @param configName
     *            Name of configuration
     * @param description
     * @param multiSector
     * @param classification
     *            Default classification
     * @throws DgException
     *             If exception occurred
     */
    public static void saveClassificationConfig(Long configId,
            String configName, String description, boolean multiSector,
            AmpSectorScheme classification) throws DgException {

        Session session = null;
        AmpClassificationConfiguration config = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            if (configId != null && !configId.equals(0l)) {
                // Load existed configuration for update procedure.
                config = (AmpClassificationConfiguration) session.load(
                        AmpClassificationConfiguration.class, configId);
            } else {
                // Create new configuration
                config = new AmpClassificationConfiguration();

            }
            config.setName(configName);
            config.setDescription(description);
            config.setMultisector(multiSector);
            config.setClassification(classification);
            // beginTransaction();
            session.saveOrUpdate(config);
            // tx.commit();

        } catch (Exception ex) {
            logger.error("Unable to save config to database " + ex.getMessage());
            throw new DgException(ex);

        }

    }

    public static int getClassificationConfigCount(String name, Long id)
            throws Exception {
        int retValue = 0;
        Session session = null;
        String queryString = null;
        Query query = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            queryString = "select count(cc.id) from "
                    + AmpClassificationConfiguration.class.getName()
                    + " cc where lower(cc.name)=lower('" + name + "')";
            if (id != null) {
                queryString += " and cc.id!=" + id;
            }
            query = session.createQuery(queryString);
            retValue = (Integer) query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retValue;
    }

    /**
     * Loads AmpClassificationConfiguration bean which is primary. Please see
     * next method which is old version and was not touch to not damage
     * anything.
     *
     * @return primary configuration
     * @throws DgException
     */
    public static AmpClassificationConfiguration getPrimaryConfigClassification()
            throws DgException {
        Session session = PersistenceManager.getRequestDBSession();
        String queryString = null;
        Query qry = null;
        try {
            queryString = "select config from "
                    + AmpClassificationConfiguration.class.getName()
                    + " config inner join config.classification cls "
                    + " where config.primary=true ";
            qry = session.createQuery(queryString);
            // There must be only one primary configuration in database
            return (AmpClassificationConfiguration) qry.uniqueResult();

        } catch (Exception ex) {
            logger.error("Unable to save config to database " + ex.getMessage());
            throw new DgException(ex);

        }
    }

    /**
     * gets id of classification which is selected in primary configuration
     *
     *
     * @return Id of classification
     * @throws DgException
     *             If exception occurred
     */
    public static Long getPrimaryConfigClassificationId() throws DgException {

        Session session = null;
        Long id = null;
        String queryString = null;
        Query qry = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            queryString = "select cls.ampSecSchemeId from "
                    + AmpClassificationConfiguration.class.getName()
                    + " config inner join config.classification cls "
                    + " where config.primary=true ";
            // queryString = "select cls.ampSecSchemeId from "
            // + AmpClassificationConfiguration.class.getName() +
            // " config inner join config.classification cls "+
            // " where config.primary=true ";
            qry = session.createQuery(queryString);
            // There must be only one primary configuration in database
            id = (Long) qry.uniqueResult();

        } catch (Exception ex) {
            logger.error("Unable to get config from database ", ex);
            throw new DgException(ex);

        }
        return id;

    }

    /***
     *
     * @param classificationId
     */
    public static void deleteClassification(Long classificationId) {
        Session session = null;
        AmpClassificationConfiguration config = null;
        Transaction tx = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            // beginTransaction();
            config = (AmpClassificationConfiguration) session.load(
                    AmpClassificationConfiguration.class, classificationId);
            session.delete(config);
            // tx.commit();
            // session.flush();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /*
     * this is to delete a scheme
     */
    public static void deleteScheme(Long schemeId) {
        logger.info(" deleting the scheme");
        Session session = null;
        try {
            session = PersistenceManager.getSession();
            AmpSectorScheme scheme = (AmpSectorScheme) session.load(
                    AmpSectorScheme.class, schemeId);
            // beginTransaction();
            session.delete(scheme);
            // tx.commit();
        } catch (Exception e) {
            logger.error("Exception from deleteQuestion() :" + e.getMessage());
            e.printStackTrace(System.out);
        }
    }

    /*
     * this is to get the level one sectors from the AmpSector table
     */
    public static AmpSectorScheme getParentSchemeId(Long id) {
        String queryString = null;
        Session session = null;
        Collection col = null;
        Query qry = null;
        AmpSectorScheme schemeId = null;

        try {
            session = PersistenceManager.getSession();

            AmpSector sec = (AmpSector) session.load(AmpSector.class, id);
            schemeId = sec.getAmpSecSchemeId();

        } catch (Exception ex) {
            logger.error("Unable to get report names  from database "
                    + ex.getMessage());
            ex.printStackTrace(System.out);
        }
        return schemeId;
    }

    public static void deleteSector(Long sectorId) {
        logger.info(" deleting the Sector");
        Session session = null;
        try {
            session = PersistenceManager.getSession();
            AmpSector sector = (AmpSector) session.load(AmpSector.class,
                    sectorId);
            // beginTransaction();
            //sector.setAidlist(null);
            sector.setDeleted(true);
            session.saveOrUpdate(sector);
            AmpCaching.getInstance().sectorsCache=null;
            // tx.commit();
        } catch (Exception e) {
            logger.error("Exception from deleteQuestion() :" + e.getMessage());
            e.printStackTrace(System.out);
        }
    }

    public static Set<AmpSector> getSectorDescendents(
            Collection<AmpSector> parentSectors) {
        Set<AmpSector> generatedSectors = new HashSet<AmpSector>();

        generatedSectors.addAll(parentSectors);
        Iterator sectorIterator = parentSectors.iterator();

        while (sectorIterator.hasNext()) {// process each sector and get all its
                                            // children
            AmpSector currentSector = (AmpSector) sectorIterator.next();
            if (currentSector != null) {
                Collection childSectors = SectorUtil
                        .getAllChildSectors(currentSector.getAmpSectorId());
                generatedSectors.addAll(childSectors); // add the children
                                                        // sectors to the filter

                // add the grand children
                Iterator childSectorsIterator = childSectors.iterator();
                while (childSectorsIterator.hasNext()) {
                    AmpSector currentChild = (AmpSector) childSectorsIterator
                            .next();
                    Collection grandChildrenSectors = SectorUtil
                            .getAllChildSectors(currentChild.getAmpSectorId());
                    generatedSectors.addAll(grandChildrenSectors);
                }

            }

        }

        return generatedSectors;
    }


    /**
     * returns set of all (recursive) descendants' ids of a given set of sectors
     * @param locations
     * @return
     */
    public static Set<Long> populateWithDescendantsIds(Collection<AmpSector> sectors)
    {
        Set<Long> allOutputLocations = getRecursiveChildrenOfSectors(AlgoUtils.collectIds(new HashSet<Long>(), sectors));
        return allOutputLocations;
    }

    /**
     * recursively get all children of a set of AmpSectors, by a wave algorithm
     * @param inIds
     * @return
     */
    public static Set<Long> getRecursiveAscendantsOfSectors(Collection<Long> inIds)
    {
        return AlgoUtils.runWave(inIds,
            new DatabaseWaver("SELECT DISTINCT (parent_sector_id) FROM amp_sector WHERE (parent_sector_id IS NOT NULL) AND (amp_sector_id IN ($))"));
    }

    /**
     * recursively get all children of a set of AmpCategoryValueLocations, by a wave algorithm
     * @param inIds
     * @return
     */
    public static Set<Long> getRecursiveChildrenOfSectors(Collection<Long> inIds)
    {
        return AlgoUtils.runWave(inIds,
                new DatabaseWaver("SELECT DISTINCT amp_sector_id FROM amp_sector WHERE (deleted is null or deleted = false) AND parent_sector_id IN ($)"));
    }

    /**
     * distributes a set of input sectors by the sector scheme they belong to
     * @param in
     * @return
     */
    public static Map<String, List<Long>> distributeSectorsByScheme(final Collection<AmpSector> in) {
        final Map<String, List<Long>> ret = new HashMap<>();

        if (in == null)
            return ret;

        PersistenceManager.getSession().doWork(new Work() {

            @Override public void execute(Connection connection) throws SQLException {
                String query = String.format("SELECT amp_sector_id, sector_config_name FROM all_sectors_with_levels WHERE amp_sector_id IN (%s)", Util.toCSStringForIN(in));
                try(RsInfo rs = SQLUtils.rawRunQuery(connection, query, null)) {
                    while (rs.rs.next()) {
                        Long secId = rs.rs.getLong(1);
                        String secScheme = rs.rs.getString(2);

                        if (!ret.containsKey(secScheme)) ret.put(secScheme, new ArrayList<Long>());
                        ret.get(secScheme).add(secId);
                    }
                }
            }

        });
        return ret;
    }

    public static List<AmpActivityVersion> getActivitiesForSector(Long id) {
        Session session = null;
        List<AmpActivityVersion> activities = null;
        try {
            session = PersistenceManager.getSession();
            String queryString = "select aps from "
                    + AmpActivitySector.class.getName()
                    + " aps,"
                    + AmpActivityGroup.class.getName()
                    + " apg "
                    + " where aps.activityId.ampActivityId = apg.ampActivityLastVersion.ampActivityId and  aps.sectorId.ampSectorId=:id and apg.ampActivityLastVersion.deleted is not true";
            Query qry = session.createQuery(queryString);
            qry.setParameter("id", id);
            activities = qry.list();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return activities;
    }

    public static List<String> getAllSectorColumnNames() {
        // not much benefit from generating names
        return Arrays.asList(
                ColumnConstants.PRIMARY_SECTOR, ColumnConstants.PRIMARY_SECTOR_SUB_SECTOR, ColumnConstants.PRIMARY_SECTOR_SUB_SUB_SECTOR,
                ColumnConstants.SECONDARY_SECTOR, ColumnConstants.SECONDARY_SECTOR_SUB_SECTOR, ColumnConstants.SECONDARY_SECTOR_SUB_SUB_SECTOR,
                ColumnConstants.TERTIARY_SECTOR, ColumnConstants.TERTIARY_SECTOR_SUB_SECTOR, ColumnConstants.TERTIARY_SECTOR_SUB_SUB_SECTOR);
    }

    //region Sector Mapping
    /**
     * Returns all sector mappings
     */
    public static Collection getAllSectorMappings() {
        String queryString = null;
        Session session = null;
        Collection col = null;
        Query qry = null;

        try {
            session = PersistenceManager.getSession();
            queryString = "select asm from " + AmpSectorMapping.class.getName() + " asm";
            qry = session.createQuery(queryString);
            col = qry.list();
        } catch (Exception ex) {
            logger.error("Unable to get sectors mappings from database " + ex.getMessage());
            ex.printStackTrace(System.out);
        }
        return col;
    }

    /**
     * adds an AmpSectorMapping
     */
    public static void createSectorMapping(AmpSectorMapping sectorMapping) throws DgException {
        Session session = null;
        if (searchSectorMapping(sectorMapping.getSrcSector().getAmpSectorId(), sectorMapping.getDstSector().getAmpSectorId()) == null) {
            try {
                session = PersistenceManager.getRequestDBSession();
                session.saveOrUpdate(sectorMapping);
                session.flush();
            } catch (Exception ex) {
                logger.error("Unable to save a sector mapping " + ex.getMessage());
                throw new DgException(ex);
            }
        }
    }

    public static void createSectorMappings(List<AmpSectorMapping> sectorMappings) throws DgException {
        Session session = null;
        Collection<AmpSectorMapping> existingMappings = getAllSectorMappings();
        try {
            session = PersistenceManager.getRequestDBSession();

            for (AmpSectorMapping em : existingMappings) {
                session.delete(em);
            }
            session.flush();
            for (AmpSectorMapping asm : sectorMappings) {
                session.save(asm);
            }
            session.flush();
        } catch (Exception ex) {
            logger.error("Unable to save the list of sector mapping " + ex.getMessage());
            throw new DgException(ex);
        }
    }

    public static void deleteSectorMapping(Long id) throws DgException {
        Session session = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            AmpSectorMapping asm = (AmpSectorMapping) session.load(AmpSectorMapping.class, id);
            session.delete(asm);
            session.flush();
        } catch (Exception ex) {
            logger.error("Unable to delete a sector mapping " + ex.getMessage());
            throw new DgException(ex);
        }
    }

    private static AmpSectorMapping searchSectorMapping(Long pSrcSectorId, Long pDstSectorId) {
        String queryString = null;
        Session session = null;
        Query qry = null;
        AmpSectorMapping asm = null;

        try {
            session = PersistenceManager.getSession();
            queryString = "select asm from " + AmpSectorMapping.class.getName() +
                    " asm where asm.srcSector.ampSectorId=:srcSectorId and asm.dstSector.ampSectorId=:dstSectorId";
            qry = session.createQuery(queryString);
            qry.setParameter("srcSectorId", pSrcSectorId);
            qry.setParameter("dstSectorId", pDstSectorId);
            asm = (AmpSectorMapping)qry.uniqueResult();
        } catch (Exception ex) {
            logger.error("Unable to get sectors mappings from database " + ex.getMessage());
            ex.printStackTrace(System.out);
        }
        return asm;
    }


    //endregion
}
