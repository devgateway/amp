package org.digijava.module.aim.util;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.algo.AlgoUtils;
import org.dgfoundation.amp.algo.DatabaseWaver;
import org.digijava.kernel.ampapi.endpoints.indicator.IndicatorEPConstants;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.exception.DynLocationStructuralException;
import org.digijava.module.aim.exception.DynLocationStructureStringException;
import org.digijava.module.aim.form.DynLocationManagerForm;
import org.digijava.module.aim.form.DynLocationManagerForm.Option;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryConstants.HardCodedCategoryValue;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.hibernate.NonUniqueResultException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.type.BooleanType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

public class DynLocationManagerUtil {
    public static final int NON_HIERARCHY_CELLS_CNT = 6;
    private static Logger logger = Logger
            .getLogger(DynLocationManagerUtil.class);
    
    public static List<AmpCategoryValueLocations> regionsOfDefaultCountry = new ArrayList<AmpCategoryValueLocations>();
    
    public static void clearRegionsOfDefaultCountryCache() {
        synchronized (DynLocationManagerUtil.regionsOfDefaultCountry) {
            DynLocationManagerUtil.regionsOfDefaultCountry.clear();
        }
    }
    
    public static Collection<AmpCategoryValueLocations> getHighestLayerLocations(DynLocationManagerForm myForm, ActionMessages errors) {
        Collection<AmpCategoryValueLocations> locations = null;
        Session dbSession = null;
        Collection<AmpCategoryValueLocations> rootLocations = null;
        HashSet<AmpCategoryValueLocations> badLayerLocations = new HashSet<AmpCategoryValueLocations>();

        myForm.setUnorganizedLocations(badLayerLocations);

        try {
            dbSession = PersistenceManager.getSession();
            String queryString = "select loc from " + AmpCategoryValueLocations.class.getName() + " loc";
            Query qry = dbSession.createQuery(queryString);
            locations = qry.list();
            if (locations != null && locations.size() > 0) {
                rootLocations = findRootLocations(locations);
                checkTree(rootLocations, badLayerLocations);
                if (badLayerLocations.size() > 0) {
                    String errorListStr = collectionToString(badLayerLocations);
                    errors.add("title", new ActionMessage(
                            "error.aim.dynRegionManager.badLayerProblem",
                            errorListStr));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rootLocations;
    }

    
    public static List<AmpCategoryValueLocations> loadLocations(Set<Long> allSelectedLocations) {
        if (allSelectedLocations == null)
            return null;
        Session dbSession = PersistenceManager.getSession();
        
        String queryString = "select loc from "
                + AmpCategoryValueLocations.class.getName() + " loc WHERE loc.id IN (" + Util.toCSStringForIN(allSelectedLocations) + ")";
        Query qry = dbSession.createQuery(queryString);
        List<AmpCategoryValueLocations> locations = qry.list();
        return locations;
    }
    
    
    public static void deleteLocation(Long id, ActionMessages errors) {
        Session dbSession = PersistenceManager.getSession();
        try {
            AmpCategoryValueLocations loc = (AmpCategoryValueLocations) dbSession.load(AmpCategoryValueLocations.class, id);
            loc.setDeleted(true);
            dbSession.save(loc);
            
            AmpLocation ampLocation = LocationUtil.getAmpLocationByCVLocation(loc.getId());
            if (ampLocation != null && LocationUtil.getIndicatorValuesCountByAmpLocation(ampLocation) == 0) {
                dbSession.delete(ampLocation);
            }
            
            for (AmpCategoryValueLocations l : loc.getChildLocations()) {
                deleteLocation(l.getId(), errors);
            }
            
        } catch (Exception e) {
            errors.add("title", new ActionMessage("error.aim.dynRegionManager.cannotSaveOrUpdate"));
            logger.error(e.getMessage(), e);
        }
    }

    public static void saveStructure(String treeStructure, String unorganizedLocations, List<AmpCategoryValue> levelLocations, ActionMessages errors) {
        if (treeStructure.length() < 4)
            return;
        Collection<AmpCategoryValueLocations> locations = null;
        Session dbSession = null;
        HashMap<Long, AmpCategoryValueLocations> locationsMap = new HashMap<Long, AmpCategoryValueLocations>();
        try {
            dbSession = PersistenceManager.getSession();
//beginTransaction();
            String queryString = "select loc from "
                    + AmpCategoryValueLocations.class.getName() + " loc ";
            Query qry = dbSession.createQuery(queryString);
            locations = qry.list();

            Iterator<AmpCategoryValueLocations> iter = locations.iterator();
            while (iter.hasNext()) {
                AmpCategoryValueLocations loc = iter.next();
                locationsMap.put(loc.getId(), loc);
            }

            NodeInfo nodeInfo = new NodeInfo(treeStructure);

            while (nodeInfo.hasNext()) {
                nodeInfo.nextInfo();
                Long id = nodeInfo.getId();
                Long parentId = nodeInfo.getParentId();
                Integer layerIndex = nodeInfo.getLayerIndex();

                AmpCategoryValueLocations loc = locationsMap.get(id);
                AmpCategoryValue layer = levelLocations.get(layerIndex);
                AmpCategoryValueLocations parent = locationsMap.get(parentId);

                loc.setParentCategoryValue(layer);
                loc.setParentLocation(parent);
                if (parent != null) {
                    if (parent.getChildLocations() == null)
                        parent
                                .setChildLocations(new TreeSet<AmpCategoryValueLocations>(
                                        alphabeticalLocComp));
                    parent.getChildLocations().add(loc);
                }

            }

            TreeSet<AmpCategoryValueLocations> returnLocations = new TreeSet<AmpCategoryValueLocations>(
                    alphabeticalLocComp);
            HashSet<AmpCategoryValueLocations> badLayerLocations = new HashSet<AmpCategoryValueLocations>();

            NodeInfo unorgInfo = new NodeInfo(unorganizedLocations);
            while (unorgInfo.hasNext()) {
                unorgInfo.nextInfo();
                Long id = unorgInfo.getId();
                AmpCategoryValueLocations loc = locationsMap.get(id);
                locations.remove(loc);
            }

            Collection<AmpCategoryValueLocations> rootLocations = findRootLocations(locations);
            checkTree(rootLocations, badLayerLocations);
            if (badLayerLocations.size() > 0) {
                String errorListStr = collectionToString(badLayerLocations);
                if (errors != null)
                    errors.add("title", new ActionMessage(
                            "error.aim.dynRegionManager.badLayerProblem",
                            errorListStr));
                throw new DynLocationStructuralException(
                        "Some locations seem to have the wrong Implementation Location category associated: "
                                + errorListStr);
            }
            //tx.commit();
        } catch (Exception e) {
            if (errors != null)
                errors.add("title", new ActionMessage(
                        "error.aim.dynRegionManager.treeSavingProblem"));
            e.printStackTrace();
        }
    }

    private static Collection<AmpCategoryValueLocations> findRootLocations(Collection<AmpCategoryValueLocations> allLocations) {
        TreeSet<AmpCategoryValueLocations> returnLocations = new TreeSet<AmpCategoryValueLocations>(alphabeticalLocComp);
        
        for(AmpCategoryValueLocations loc : allLocations) {
            if (loc.getParentLocation() == null && !loc.isSoftDeleted()) {
                returnLocations.add(loc);
            }
        }
        
        return returnLocations;
    }

    private static void checkSiblings(
            Collection<AmpCategoryValueLocations> siblingLocations,
            Collection<AmpCategoryValueLocations> badLayerLocations,
            Integer parentLayerIndex) {

        Iterator<AmpCategoryValueLocations> iter = siblingLocations.iterator();
        while (iter.hasNext()) {
            AmpCategoryValueLocations loc = iter.next();
            if (loc.getParentCategoryValue().getIndex() <= parentLayerIndex) {
                loc.getParentLocation().getChildLocations().remove(loc);
                loc.setParentLocation(null);
                badLayerLocations.add(loc);
            } else if (loc.getChildLocations() != null
                    && loc.getChildLocations().size() > 0) {
                checkSiblings(loc.getChildLocations(), badLayerLocations, loc
                        .getParentCategoryValue().getIndex());
            }
        }
    }

    public static void checkTree(Collection<AmpCategoryValueLocations> rootLocations, Collection<AmpCategoryValueLocations> badLayerLocations) {

        badLayerLocations.clear();

        Iterator<AmpCategoryValueLocations> locIter = rootLocations.iterator();
        while (locIter.hasNext()) {
            AmpCategoryValueLocations tempLoc = locIter.next();
            if (tempLoc.getChildLocations() != null && tempLoc.getChildLocations().size() > 0) {
                checkSiblings(tempLoc.getChildLocations(), badLayerLocations, tempLoc.getParentCategoryValue().getIndex());
            }
        }

    }

    public static String collectionToString(Collection<? extends Object> col)
            throws NullPointerException {
        if (col == null)
            throw new NullPointerException("col param cannot be null");
        if (col.size() == 0)
            return "";

        String retString = "";
        Iterator<? extends Object> iter = col.iterator();
        while (true) {
            retString += iter.next();
            if (iter.hasNext())
                retString += ", ";
            else
                break;
        }

        return retString;
    }

    public static void synchronizeCountries() {
        logger.info("Starting countries synchronization");
        Session dbSession = null;
        Collection<Country> countries = null;
        try {
            dbSession = PersistenceManager.getSession();
//beginTransaction();
            String queryString = "select c from " + Country.class.getName()
                    + " c ";
            Query qry = dbSession.createQuery(queryString);
            countries = qry.list();

            if (countries == null)
                return;

            Set<AmpCategoryValueLocations> countryLocations = DynLocationManagerUtil
                    .getLocationsByLayer(CategoryConstants.IMPLEMENTATION_LOCATION_ADM_LEVEL_0);
            HashMap<String, AmpCategoryValueLocations> nameToLocationsMap = new HashMap<String, AmpCategoryValueLocations>();
            HashMap<String, AmpCategoryValueLocations> iso3ToLocationsMap = new HashMap<String, AmpCategoryValueLocations>();

            if (countryLocations != null && countryLocations.size() > 0) {
                for (AmpCategoryValueLocations locCountry : countryLocations) {
                    nameToLocationsMap.put(locCountry.getName(), locCountry);
                    if (locCountry.getIso3() != null
                            && locCountry.getIso3().length() > 0) {
                        iso3ToLocationsMap
                                .put(locCountry.getIso3(), locCountry);
                    }
                }
            }

            AmpCategoryValue layer = CategoryConstants.IMPLEMENTATION_LOCATION_ADM_LEVEL_0.getAmpCategoryValueFromDB();
            if (layer == null) {
                logger
                        .error("No Country value found in category Implementation Location. Please correct this.");
                throw new Exception(
                        "No Country value found in category Implementation Location. Please correct this.");
            }

            for (Country country : countries) {
                if (country.getIso3() != null && country.getIso3().length() > 0
                        && iso3ToLocationsMap.get(country.getIso3()) != null) {
                    continue;
                }
                if (nameToLocationsMap.get(country.getCountryName()) == null) {
                    AmpCategoryValueLocations locCountry = new AmpCategoryValueLocations();
                    locCountry.setParentCategoryValue(layer);
                    locCountry.setName(country.getCountryName());
                    locCountry.setIso3(country.getIso3());
                    locCountry.setIso(country.getIso());
                    if (country.getCountryId() != null)
                        locCountry.setCode(country.getCountryId().toString());
                    dbSession.save(locCountry);
                }
            }

            Long maxCountryCode = 0L;
            HashMap<String, Country> namesToDgCountriesMap = new HashMap<String, Country>();
            HashMap<String, Country> iso3ToDgCountriesMap = new HashMap<String, Country>();
            for (Country country : countries) {
                namesToDgCountriesMap.put(country.getCountryName(), country);
                if (country.getIso3() != null && country.getIso3().length() > 0) {
                    iso3ToDgCountriesMap.put(country.getIso3(), country);
                }
                if (country.getCountryId() != null
                        && country.getCountryId() > maxCountryCode) {
                    maxCountryCode = country.getCountryId();
                }
            }

            if (countryLocations != null) {
                for (AmpCategoryValueLocations location : countryLocations) {
                    if (namesToDgCountriesMap.get(location.getName()) == null) {
                        Country country = null;
                        if (location.getIso3() != null
                                && location.getIso3().length() > 0)
                            country = iso3ToDgCountriesMap.get(location
                                    .getIso3());
                        if (country == null)
                            country = new Country();
                        country.setCountryName(location.getName());
                        country.setIso(location.getIso());
                        country.setIso3(location.getIso3());
                        country.setAvailable(true);
                        country.setDecCtryFlag("t");
                        country.setStat("t");
                        country.setShowCtry("t");
                        if (location.getCode() != null) {
                            try {
                                country.setCountryId(Long.parseLong(location
                                        .getCode()));
                            } catch (NumberFormatException e) {
                                logger
                                        .info("Cannot transform location country code ('"
                                                + location.getCode()
                                                + "') to dg Country code. Settting country id as maximum.");
                                country.setCountryId(maxCountryCode + 1);
                            }
                        } else {
                            country.setCountryId(maxCountryCode + 1);
                        }
                        dbSession.saveOrUpdate(country);
                    }
                }
            }
            //tx.commit();
            logger.info("Countries synchronization done.");
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Countries synchronization NOT performed.");
        }
    }

    public static AmpCategoryValueLocations getLocationByIso3(
            String locationIso3, HardCodedCategoryValue hcLocationLayer) {
        try {
            AmpCategoryValue layer = hcLocationLayer.getAmpCategoryValueFromDB();
            return getLocationByIso3(locationIso3, layer);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 
     * @param locationIso
     * @param cvLocationLayer
     *            the AmpCategoryValue specifying the layer (level) of the
     *            location...like Country or Region
     * @return
     */
    public static AmpCategoryValueLocations getLocationByIso3(
            String locationIso3, AmpCategoryValue cvLocationLayer) {
        Session dbSession = null;

        try {
            dbSession = PersistenceManager.getSession();
            String queryString = "select loc from "
                    + AmpCategoryValueLocations.class.getName()
                    + " loc where (loc.iso3=:iso3)";
            if (cvLocationLayer != null) {
                queryString += " AND (loc.parentCategoryValue=:cvId) ";
            }
            Query qry = dbSession.createQuery(queryString);
            if (cvLocationLayer != null) {
                qry.setParameter("cvId", cvLocationLayer.getId(), LongType.INSTANCE);
            }
            qry.setParameter("iso3", locationIso3, StringType.INSTANCE);
            return (AmpCategoryValueLocations) qry
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static AmpCategoryValueLocations getLocationByName(
            String locationName, AmpCategoryValue cvLocationLayer,
            AmpCategoryValueLocations parentLocation)
            throws NullPointerException, NonUniqueResultException {
        Session dbSession = null;

        if (cvLocationLayer == null)
            throw new NullPointerException(
                    "Value for Implementation Location cannot be null");
        try {
            dbSession = PersistenceManager.getSession();
            String locationNameHql = AmpCategoryValueLocations.hqlStringForName("loc");
            String queryString = "select loc from "
                    + AmpCategoryValueLocations.class.getName()
                    + " loc where (" + locationNameHql + "=:name) "
                    + " AND (loc.parentCategoryValue=:cvId) ";
            if (parentLocation == null) {
                queryString += "AND (loc.parentLocation is null) ";
            } else {
                queryString += "AND (loc.parentLocation=:parentLocationId) ";
            }
            Query qry = dbSession.createQuery(queryString);
            qry.setParameter("cvId", cvLocationLayer.getId(), LongType.INSTANCE);
            qry.setParameter("name", locationName,StringType.INSTANCE);
            if (parentLocation != null)
                qry.setParameter("parentLocationId", parentLocation.getId(), LongType.INSTANCE);

            Collection<AmpCategoryValueLocations> locations = qry.list();
            if (locations != null && locations.size() > 0) {
                return locations.toArray(new AmpCategoryValueLocations[0])[0];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static AmpCategoryValueLocations getLocationByName(
            String locationName, AmpCategoryValue cvLocationLayer)
            throws NullPointerException, NonUniqueResultException {
        Session dbSession = null;

        if (cvLocationLayer == null)
            throw new NullPointerException(
                    "Value for Implementation Location cannot be null");
        try {
            dbSession = PersistenceManager.getSession();
            String locationNameHql = AmpCategoryValueLocations.hqlStringForName("loc");
            String queryString = "select loc from "
                    + AmpCategoryValueLocations.class.getName()
                    + " loc where (" + locationNameHql + "=:name) "
                    + " AND (loc.parentCategoryValue=:cvId) ";
            Query qry = dbSession.createQuery(queryString);
            qry.setParameter("cvId", cvLocationLayer.getId(), LongType.INSTANCE);
            qry.setParameter("name", locationName,StringType.INSTANCE);
            Collection<AmpCategoryValueLocations> locations = qry.list();
            if (locations != null && locations.size() > 0) {
                return locations.toArray(new AmpCategoryValueLocations[0])[0];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static AmpCategoryValueLocations getLocationByIso(
            String locationIso, HardCodedCategoryValue hcLocationLayer) {
        return getLocationByIso(locationIso, hcLocationLayer, true);
    }

    /**
     * This method does not read the layer as a <code>HardCodedCategoryValue</code> value
     * from the cache, instead reads it from the database
     *
     * We need this in cases when the default value is cached (say, implementation_location=Region)
     * But we need to load (say, the defaultCountry, where implementation_location=Country)
     * Otherwise we get null.
     * 
     * !! CONSTANTIN: on 12/03/2014, the code branches under if (readLayerFromCache) {} else {} are both reimplementations of the same thing. Rewriting to ignore the parameter !!
     *
     * @param locationIso
     * @param hcLocationLayer
     * @param readLayerFromCache
     * @return
     */
    public static AmpCategoryValueLocations getLocationByIso(
            String locationIso, HardCodedCategoryValue hcLocationLayer, boolean readLayerFromCache) {
        try {
            AmpCategoryValue layer = hcLocationLayer.getAmpCategoryValueFromDB();
            return getLocationByIso(locationIso, layer);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 
     * @param locationIso
     * @param cvLocationLayer
     *            the AmpCategoryValue specifying the layer (level) of the
     *            location...like Country or Region
     * @return
     */
    public static AmpCategoryValueLocations getLocationByIso(
            String locationIso, AmpCategoryValue cvLocationLayer) {
        Session dbSession = null;

        try {
            dbSession = PersistenceManager.getSession();
            String queryString = "select loc from "
                    + AmpCategoryValueLocations.class.getName()
                    + " loc where (lower(loc.iso) like :iso)";
            if (cvLocationLayer != null) {
                queryString += " AND (loc.parentCategoryValue=:cvId) ";
            }
            Query qry = dbSession.createQuery(queryString);
            qry.setCacheable(true);
            if (cvLocationLayer != null) {
                qry.setParameter("cvId", cvLocationLayer.getId(), LongType.INSTANCE);
            }
            qry.setParameter("iso", locationIso.toLowerCase(),StringType.INSTANCE);
            return (AmpCategoryValueLocations) qry
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static AmpCategoryValueLocations getLocationOpenedSession(Long id) {
        Session dbSession = null;
        try {
            dbSession = PersistenceManager.getRequestDBSession();
            String queryString = "select loc from "
            + AmpCategoryValueLocations.class.getName()
            + " loc where (loc.id=:id)" ;                   
            Query qry = dbSession.createQuery(queryString);
            qry.setParameter("id", id, LongType.INSTANCE);
            return (AmpCategoryValueLocations)qry.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static AmpCategoryValueLocations getLocationByCode(
            String locationCode, HardCodedCategoryValue hcLocationLayer) {
        try {
            AmpCategoryValue layer = hcLocationLayer.getAmpCategoryValueFromDB();
            return getLocationByCode(locationCode, layer);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 
     * @param locationIso
     * @param cvLocationLayer
     *            the AmpCategoryValue specifying the layer (level) of the
     *            location...like Country or Region
     * @return
     */
    public static AmpCategoryValueLocations getLocationByCode(
            String locationCode, AmpCategoryValue cvLocationLayer) {
        Session dbSession = null;

        try {
            dbSession = PersistenceManager.getSession();
            String queryString = "select loc from "
                    + AmpCategoryValueLocations.class.getName()
                    + " loc where (loc.code=:code)";
            if (cvLocationLayer != null) {
                queryString += " AND (loc.parentCategoryValue=:cvId) ";
            }
            Query qry = dbSession.createQuery(queryString);
            if (cvLocationLayer != null) {
                qry.setParameter("cvId", cvLocationLayer.getId(), LongType.INSTANCE);
            }
            qry.setParameter("code", locationCode,StringType.INSTANCE);
            return (AmpCategoryValueLocations) qry
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 
     * @param id
     * @param initChildLocs
     *            child locations are lazily initialized, so if you want to
     *            access them put "true" here
     * @return
     */
    public static AmpCategoryValueLocations getLocation(Long id,
            boolean initChildLocs) {
        Session dbSession = null;
        try {
            dbSession = PersistenceManager.getSession();
            String queryString = "select loc from "
                    + AmpCategoryValueLocations.class.getName()
                    + " loc where (loc.id=:id)";
            Query qry = dbSession.createQuery(queryString);
            qry.setLong("id", id);
            AmpCategoryValueLocations returnLoc = (AmpCategoryValueLocations) qry
                    .uniqueResult();
            if (initChildLocs)
                returnLoc.getChildLocations().size();
            return returnLoc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static AmpCategoryValueLocations getLocationByIdRequestSession(Long id) {
        Session dbSession                                                                               = null;
        try {
            dbSession                           = PersistenceManager.getRequestDBSession();

            AmpCategoryValueLocations returnLoc = (AmpCategoryValueLocations) dbSession.get(AmpCategoryValueLocations.class, id);
            return returnLoc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * returns set of all (recursive) descendants' ids of a given set of locations
     * @param locations
     * @return
     */
    public static Set<Long> populateWithDescendantsIds(Collection<AmpCategoryValueLocations> locations, boolean includeDeleted) {
        Set<Long> allOutputLocations = getRecursiveChildrenOfCategoryValueLocations(AlgoUtils.collectIds(new HashSet<Long>(), locations), includeDeleted);
        
        return allOutputLocations;
    }
    
    /**
     * returns set of all (recursive) descendants of a given set of locations
     * @param destCollection
     * @param locations
     */
    public static void populateWithDescendants(Set<AmpCategoryValueLocations> destCollection, 
            Collection<AmpCategoryValueLocations> locations, boolean includeDeleted) {
        
        Set<Long> allOutputLocations = populateWithDescendantsIds(locations, includeDeleted);
        
        for(Long outputId:allOutputLocations) {
            destCollection.add(getLocation(outputId, false));
        }
    }
    
    /**
     * recursively get all ancestors (parents) of a set of AmpCategoryValueLocations, by a wave algorithm
     * @param inIds
     * @return
     */
    public static Set<Long> getRecursiveAscendantsOfCategoryValueLocations(Collection<Long> inIds, boolean includeDeleted) {
        String exludeDeletedCriteria = " deleted IS NOT TRUE AND";
        
        if (includeDeleted) {
            exludeDeletedCriteria = "";
        }
        
        return AlgoUtils.runWave(inIds, 
                new DatabaseWaver("SELECT DISTINCT(parent_location) FROM amp_category_value_location WHERE "
                        + exludeDeletedCriteria + " (parent_location IS NOT NULL) AND (id IN ($))"));
    }
        
    /**
     * recursively get all children of a set of AmpCategoryValueLocations, by a wave algorithm
     * @param inIds
     * @param includedDeleted
     * @return
     */
    public static Set<Long> getRecursiveChildrenOfCategoryValueLocations(Collection<Long> inIds, boolean includeDeleted) {
        String exludeDeletedCriteria = " deleted IS NOT TRUE AND";
        
        if (includeDeleted) {
            exludeDeletedCriteria = "";
        }
        
        return AlgoUtils.runWave(inIds, 
                new DatabaseWaver("SELECT DISTINCT id FROM amp_category_value_location WHERE "
                        + exludeDeletedCriteria + " parent_location IN ($)"));
    }
    
    public static void populateWithAscendants(Collection <AmpCategoryValueLocations> destCollection, 
            Collection<AmpCategoryValueLocations> locations ) {
        if (  locations != null ) {
            Iterator<AmpCategoryValueLocations> iterLoc = locations.iterator();
            while (iterLoc.hasNext()) {
                AmpCategoryValueLocations loc    = iterLoc.next();
                
                while (loc.getParentLocation() != null){
                    loc     = loc.getParentLocation();
                    destCollection.add(loc);
                }
            }
        }       
    }
    
    /**
     * returns the ACVL of the country the current AMP installation is running on
     * @return
     */
    public static AmpCategoryValueLocations getDefaultCountry()
    {
        AmpCategoryValueLocations country = DynLocationManagerUtil.getLocationByIso(
                FeaturesUtil.getDefaultCountryIso(), CategoryConstants.IMPLEMENTATION_LOCATION_ADM_LEVEL_0);
        return country;
    }
    
    public static Collection<AmpCategoryValueLocations> getRegionsOfDefCountryHierarchy() throws DgException 
    {   
        synchronized (DynLocationManagerUtil.regionsOfDefaultCountry)
        {
            if ( DynLocationManagerUtil.regionsOfDefaultCountry.isEmpty())
            {
                AmpCategoryValueLocations country = DynLocationManagerUtil.getDefaultCountry();
                if (country != null){
                    country = getLocationOpenedSession( country.getId() );
                    
                    Set<AmpCategoryValueLocations> children             = country.getChildLocations();
                    
                    DynLocationManagerUtil.regionsOfDefaultCountry.addAll(children);
                }
            }
            return Collections.unmodifiableList(regionsOfDefaultCountry);
        }
     }
    
    public static Set<AmpCategoryValueLocations> getLocationsOfTypeAdmLevel1OfDefCountry()
            throws Exception {
        TreeSet<AmpCategoryValueLocations> returnSet = new TreeSet<AmpCategoryValueLocations>(
                alphabeticalLocComp);
        String defCountryIso = FeaturesUtil.getDefaultCountryIso();
        if (defCountryIso != null) {
            Set<AmpCategoryValueLocations> allRegions = getLocationsByLayer(
                    CategoryConstants.IMPLEMENTATION_LOCATION_ADM_LEVEL_1);
            if (allRegions != null && allRegions.size() > 0) {
                Iterator<AmpCategoryValueLocations> regIter = allRegions
                        .iterator();
                while (regIter.hasNext()) {
                    AmpCategoryValueLocations reg = regIter.next();
                    AmpCategoryValueLocations country = getAncestorByLayer(reg,
                            CategoryConstants.IMPLEMENTATION_LOCATION_ADM_LEVEL_0);
                    if (defCountryIso.equals(country.getIso())) {
                        returnSet.add(reg);
                    }
                }
            }
        } else
            throw new Exception("No default country iso could be retrieved!");
        return returnSet;
    }

    public static Set<AmpCategoryValueLocations> getLocationsOfTypeRegion() {
        return getLocationsByLayer(CategoryConstants.IMPLEMENTATION_LOCATION_ADM_LEVEL_1);
    }

    public static Set<AmpCategoryValueLocations> getLocationsByLayer(HardCodedCategoryValue hcLayer) {
        try {
            AmpCategoryValue layer = hcLayer.getAmpCategoryValueFromDB();
            return getLocationsByLayer(layer);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static AmpCategoryValueLocations getAncestorByLayer(
            AmpCategoryValueLocations loc, HardCodedCategoryValue hcValue) {
        if (loc == null)
            logger
                    .error("loc parameter in getAncestorByLayer should not be null");
        AmpCategoryValueLocations temp = loc;
        if (temp.getParentCategoryValue().getValue().equals(
                hcValue.getValueKey()))
            return temp;
        while (temp.getParentLocation() != null) {
            temp = temp.getParentLocation();
            if (temp.getParentCategoryValue().getValue().equals(
                    hcValue.getValueKey()))
                return temp;
        }
        return null;
    }

    
    
    
    /**
     * 
     * @param cvLayer
     * @return
     */
    public static Set<AmpCategoryValueLocations> getLocationsByLayer(AmpCategoryValue cvLayer) {
        TreeSet<AmpCategoryValueLocations> returnSet = new TreeSet<AmpCategoryValueLocations>(
                alphabeticalLocAndIdComp);
        try {
            Session dbSession = PersistenceManager.getSession();
            String queryString = "select loc from "
                    + AmpCategoryValueLocations.class.getName()
                    + " loc where (loc.parentCategoryValue=:cvId) "
                    + " and (loc.deleted != true)";
            Query qry = dbSession.createQuery(queryString);
            qry.setLong("cvId", cvLayer.getId());
            qry.setCacheable(true);
            returnSet.addAll(qry.list());
            return returnSet;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> getParents(AmpCategoryValueLocations loc) {
        ArrayList<String> returnList = new ArrayList<String>();
        if (loc == null)
            return returnList;
        else
            returnList.add(loc.getName());
        AmpCategoryValueLocations temp = loc;
        while (temp.getParentLocation() != null) {
            temp = temp.getParentLocation();
            returnList.add(0, temp.getName());
        }
        return returnList;
    }

    public static Map<Integer, String> getLayerToAncestorNameMap(
            AmpCategoryValueLocations loc) {
        TreeMap<Integer, String> returnList = new TreeMap<Integer, String>();
        if (loc == null)
            return returnList;
        else
            returnList.put(loc.getParentCategoryValue().getIndex(), loc
                    .getName());
        AmpCategoryValueLocations temp = loc;
        while (temp.getParentLocation() != null) {
            temp = temp.getParentLocation();
            returnList.put(temp.getParentCategoryValue().getIndex(), temp
                    .getName());
        }
        return returnList;
    }

    /**
     * 
     * @param ampCVLocation
     * @return If there is a corresponding AmpLocation object in the database
     *         then it is returned. Otherwise a new entity is being created and
     *         saved to the db.
     * @throws Exception
     */
    public static AmpLocation getOrCreateAmpLocationByCVL(AmpCategoryValueLocations ampCVLocation) {
        if (ampCVLocation == null) {
            throw new IllegalArgumentException("Category Value Location cannot be null");
        }

        AmpLocation ampLoc = LocationUtil.getAmpLocationByCVLocation(ampCVLocation.getId());

        if (ampLoc == null) {
            ampLoc = new AmpLocation();
            ampLoc.setName(ampCVLocation.getName());
            ampLoc.setLocation(ampCVLocation);
            
            AmpCategoryValueLocations regionLocation = DynLocationManagerUtil
                    .getAncestorByLayer(ampCVLocation, CategoryConstants.IMPLEMENTATION_LOCATION_ADM_LEVEL_1);
            
            if (regionLocation != null) {
                ampLoc.setRegionLocation(regionLocation);
            }
            
            DbUtil.add(ampLoc);
        }
        
        return ampLoc;
    }
    
    /** Get AmpLocation object by Category Value Location Id. If it doesn't exist, create a new one.
     * 
     * @param ampCVLocationId
     * @return
     * @throws Exception
     */
    public static AmpLocation getOrCreateAmpLocationByCVLId(Long ampCVLocationId) {
        AmpCategoryValueLocations acvLocation = getLocationByIdRequestSession(ampCVLocationId);
        
        return getOrCreateAmpLocationByCVL(acvLocation);
    }

    /**
     * 
     * @param indexOfLayer
     * @return Number of locations of the layer specified by indexOfLayer
     */
    public static int getNumOfLocations(int indexOfLayer) {
        Session dbSession = null;
        try {
            AmpCategoryValue cvLocationType = CategoryManagerUtil
                    .getAmpCategoryValueFromDb(
                            CategoryConstants.IMPLEMENTATION_LOCATION_KEY,
                            (long) indexOfLayer);
            if (cvLocationType != null) {
                dbSession = PersistenceManager.getSession();
                String queryString = "select count(loc) from "
                        + AmpCategoryValueLocations.class.getName()
                        + " loc where (loc.parentCategoryValue=:cvId)";
                Query qry = dbSession.createQuery(queryString);
                qry.setCacheable(true);
                qry.setParameter("cvId", cvLocationType.getId(), LongType.INSTANCE);
                Long longValue = (Long) qry.uniqueResult();
                return longValue.intValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static Comparator<AmpCategoryValueLocations> alphabeticalLocComp = new Comparator<AmpCategoryValueLocations>() {
        public int compare(AmpCategoryValueLocations o1, AmpCategoryValueLocations o2) {
            return o1.getName().compareTo(o2.getName());
        }
    };

    public static Comparator<AmpCategoryValueLocations> alphabeticalLocAndIdComp = new Comparator<AmpCategoryValueLocations>() {
        public int compare(AmpCategoryValueLocations o1, AmpCategoryValueLocations o2) {

            int result = o1.getName().compareTo(o2.getName());
            if ( result != 0 ) { return result; }

            result = Long.compare( o1.getId(), o2.getId());
            if ( result != 0 ) { return result;}

            return result;
        }
    };
    
    public static ErrorCode importExcelFile(InputStream inputStream, Option option) throws AimException {
        return importExcelFile(getCells(inputStream), option);
    }
    
    public static List<List<String>> getCells(InputStream inputStream) {
        try {
            List<List<String>> rows = new ArrayList<>();
            Workbook workBook = WorkbookFactory.create(inputStream);
            Sheet hssfSheet = workBook.getSheetAt(0);
            int physicalNumberOfCells = hssfSheet.getRow(0).getPhysicalNumberOfCells();
            for (int j = 0; j < hssfSheet.getPhysicalNumberOfRows(); j++) {
                List<String> row = new ArrayList<>(physicalNumberOfCells);
                Row hssfRow = hssfSheet.getRow(j);
                for (int i = 0; i < physicalNumberOfCells; i++) {
                    row.add(getValue(hssfRow.getCell(i)));
                }
                rows.add(row);
            }
            return rows;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ErrorCode importExcelFile(List<List<String>> rows, Option option) throws AimException {
        try {
            List<String> header = rows.get(0);

            List<AmpCategoryValue> implLocs = new ArrayList<AmpCategoryValue>(
                    CategoryManagerUtil.getAmpCategoryValueCollectionByKeyExcludeDeleted(CategoryConstants.IMPLEMENTATION_LOCATION_KEY));
            
            int i = 1;
            int hierarchyNumberOfCells=implLocs.size();
            // last five cells are not hierarchy cells and first cell is db id
            if (hierarchyNumberOfCells + NON_HIERARCHY_CELLS_CNT != header.size()) {
                return ErrorCode.NUMBER_NOT_MATCH;
            }

            for (AmpCategoryValue location : implLocs) {
                String cellValue = header.get(i);
                if (!StringUtils.equalsIgnoreCase(cellValue, location.getValue())
                        && !StringUtils.equalsIgnoreCase(cellValue, TranslatorWorker.translateText(location.getValue()))) {
                    return ErrorCode.NAME_NOT_MATCH;
                }
                
                i++;
            }

            for (int j = 1; j < rows.size(); j++) {
                AmpCategoryValueLocations parentLoc=null;
                List<String> hssfRow = rows.get(j);

                    String cell = hssfRow.get(0);
                    Long databaseId = StringUtils.isBlank(cell) ? null : new BigDecimal(cell).longValue();

                    List<String> locationNames = new ArrayList<String>();
                    int k = 1;

                    for (; k <= hierarchyNumberOfCells; k++) {
                        cell = hssfRow.get(k);
                        if (cell == null) {
                            k = hierarchyNumberOfCells + 1;
                            break;
                        }
                        String location = cell;
                        if (location == null || location.trim().length() == 0) {
                            k = hierarchyNumberOfCells + 1;
                            break;
                        }
                        locationNames.add(location);
                    }

                    String latitude = hssfRow.get(k++);
                    String longitude = hssfRow.get(k++);
                    String geoID = hssfRow.get(k++);
                    
                    if (geoID != null && geoID.contains(".0")) {
                        geoID = geoID.replace(".0", "");
                    }
                    
                    String iso = hssfRow.get(k++);
                    String iso3 = hssfRow.get(k++);
                    for (k = 0; k < locationNames.size(); k++) {
                        String name = locationNames.get(k);
                        AmpCategoryValue implLoc = implLocs.get(k);
                        if (k == locationNames.size() - 1) {
                            AmpCategoryValueLocations location = new AmpCategoryValueLocations();

                            AmpCategoryValueLocations currentLoc = getLocationByName(name, implLoc, parentLoc);
                            if (currentLoc != null) {
                                if (option.equals(Option.NEW)) {
                                    break;
                                } else {
                                    location = currentLoc;
                                }
                            } else {
                                if (option.equals(Option.OVERWRITE)) {
                                    if (databaseId != null && databaseId != 0) {
                                        location = getLocationByIdRequestSession(databaseId);
                                    } else {
                                        break;
                                    }
                                }
                            }
                            if (location != null) {
                                if (location.getParentCategoryValue() != null && !location.getParentCategoryValue().equals(implLoc)) {
                                    break;
                                }
                                
                                AmpCategoryValueLocations oldParent = location.getParentLocation();
                                
                                location.setName(name);
                                location.setGsLat(getValueOrNull(latitude));
                                location.setGsLong(getValueOrNull(longitude));
                                location.setGeoCode(getValueOrNull(geoID));
                                location.setIso(getValueOrNull(iso));
                                location.setIso3(getValueOrNull(iso3));
                                location.setParentCategoryValue(implLoc);
                                location.setParentLocation(parentLoc);
                                location.setDeleted(false);
                                boolean edit = location.getId() != null;
    
    
                                if (edit && oldParent != null && parentLoc != null
                                        && !oldParent.getId().equals(parentLoc.getId())) {
                                    oldParent.getChildLocations().remove(location);
                                    parentLoc.getChildLocations().add(location);
                                }
                                
                                LocationUtil.saveLocation(location, edit);
                            }
                        } else {
                            parentLoc = getLocationByName(name, implLoc, parentLoc);
                            if (parentLoc == null) {
                                logger.error("Parent location is null");
                                return ErrorCode.INCORRECT_CONTENT;
                            }
                        }
                    }
            }

        }
        catch (Exception e) {
            logger.error("Exception throwed during the import of the regions.", e);
            return ErrorCode.INCORRECT_CONTENT;
        }

        return ErrorCode.CORRECT_CONTENT;
    }

    private static String getValue(Cell cell) {
        if (cell != null) {
            if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                return "" + cell.getNumericCellValue();
            }
            return cell.getStringCellValue();
        }
        
        return "";
    }
    
    private static String getValueOrNull(String value) {
        return StringUtils.isNotBlank(value) ? value : null;
    }
    
    public enum ErrorCode{
        NUMBER_NOT_MATCH, NAME_NOT_MATCH, INCORRECT_CONTENT, CORRECT_CONTENT, INEXISTANT_ADM_LEVEL, LOCATION_NOT_FOUND
    }

    public static class NodeInfo {
        private int i = 0;
        private String[] pairs = null;

        private Long id = null;
        private Long parentId = null;
        private Integer layerIndex = null;

        public NodeInfo(String structureStr) {
            this.pairs = structureStr.split("\\|");
        }

        public void nextInfo() throws DynLocationStructureStringException {
            if (i < pairs.length && pairs[i].length() > 0) {
                String[] ids = pairs[i++].split("p|h");
                if (ids.length != 3) {
                    throw new DynLocationStructureStringException(
                            "Exactly 3 tokens should be found in each pair. This pair is wrong: "
                                    + pairs[i]);
                }
                this.id = Long.parseLong(ids[0]);
                this.parentId = Long.parseLong(ids[1]);
                this.layerIndex = Integer.parseInt(ids[2]);
            } else
                i++;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getParentId() {
            return parentId;
        }

        public void setParentId(Long parentId) {
            this.parentId = parentId;
        }

        public Integer getLayerIndex() {
            return layerIndex;
        }

        public void setLayerIndex(Integer layerIndex) {
            this.layerIndex = layerIndex;
        }

        public boolean hasNext() {
            return i < this.pairs.length;
        }

    }
    
 public static List <AmpIndicatorLayer> getIndicatorByCategoryValueId (Long id) {
        Session dbSession = PersistenceManager.getSession();
        String queryString = "select ind from "
                + AmpIndicatorLayer.class.getName()
                + " ind where ind.admLevel.id=:id ";
        Query qry = dbSession.createQuery(queryString);
        qry.setCacheable(true);
        qry.setLong("id", id);
        return qry.list();
     
 }

 public static List <AmpIndicatorLayer> getIndicatorByCategoryValueIdAndIndicatorId(Long admLevelId, Long indicatorId) {
        Session dbSession = PersistenceManager.getSession();
        String queryString = "select ind from "
                + AmpIndicatorLayer.class.getName()
                + " ind where ind.admLevel.id=:id and ind.id=:indicatorId ";
        Query qry = dbSession.createQuery(queryString);
        qry.setCacheable(true);
        qry.setLong("id", admLevelId);
        qry.setLong("indicatorId", indicatorId);
        return qry.list();

 }

    public static List <AmpIndicatorLayer> getIndicatorLayers () {
        return getIndicatorLayers(IndicatorEPConstants.DEFAULT_INDICATOR_ORDER_FIELD, "");

    }

    public static List <AmpIndicatorLayer> getIndicatorLayers (String orderBy, String sort) {
        Session dbSession = PersistenceManager.getSession();

        String queryString = "select indicator from "
                + AmpIndicatorLayer.class.getName() + " indicator "
                + " left join indicator.createdBy.user c "
                + " order by " + orderBy + " " + sort;
        Query qry = dbSession.createQuery(queryString);
        return qry.list();

    }

    public static List <AmpIndicatorLayer> getIndicatorLayerByAccessType(long accessTypeId, String orderBy, String sort) {
        Session dbSession = PersistenceManager.getSession();
        String queryString = "select indicator from "
                + AmpIndicatorLayer.class.getName() + " indicator where indicator.accessType.id=:accessTypeId order by " + orderBy + " " + sort;
        Query qry = dbSession.createQuery(queryString);
        qry.setLong("accessTypeId", accessTypeId);
        return qry.list();

    }

    public static List <AmpIndicatorLayer> getIndicatorLayerByCreatedBy (AmpTeamMember teamMember, String orderBy, String sort) {
        Session dbSession = PersistenceManager.getSession();
        String queryString = "select indicator from " + AmpIndicatorLayer.class.getName() + " indicator ";
        queryString += " left join indicator.sharedWorkspaces s ";
        queryString += " where indicator.createdBy.user.id=:userId ";

        Collection<AmpTeam> workspaces = null;
        TeamMember tm = TeamUtil.getCurrentMember();
        if (tm != null) {
            workspaces=TeamMemberUtil.getAllTeamsForUser(tm.getEmail());
        }
        queryString += " or ( s.workspace.ampTeamId in( " + Util.toCSStringForIN(workspaces) + ")) ";
        queryString += " order by " + orderBy + " " + sort;

        Query qry = dbSession.createQuery(queryString);
        qry.setLong("userId", teamMember.getUser().getId());
        return qry.list();

    }

    public static AmpIndicatorLayer getIndicatorLayerById (Long id) {
        Session dbSession = PersistenceManager.getSession();
        String queryString = "select ind from "
                + AmpIndicatorLayer.class.getName() + " ind where id=:id";
        Query qry = dbSession.createQuery(queryString);
        qry.setLong("id", id);
        if (qry.list().size()==1)
            return (AmpIndicatorLayer) qry.list().get(0);
        else
            return null;

    }

    public static AmpIndicatorLayer getIndicatorLayerByName (String name) {
        Session dbSession = PersistenceManager.getSession();
        String queryString = "select ind from "
                + AmpIndicatorLayer.class.getName() + " ind where name=:name";
        Query qry = dbSession.createQuery(queryString);
        qry.setParameter("name", name,StringType.INSTANCE);
        if (qry.list().size() > 0)
            return (AmpIndicatorLayer) qry.list().get(0);
        else
            return null;

    }

 public static List <AmpLocationIndicatorValue> getLocationIndicatorValueByLocation (AmpCategoryValueLocations location) {
     Session dbSession = PersistenceManager.getSession();
        String queryString = "select value from "
                + AmpLocationIndicatorValue.class.getName()
                + " value where value.location.id=:id)";
        Query qry = dbSession.createQuery(queryString);
        qry.setCacheable(true);
        qry.setParameter("id", location.getId(), LongType.INSTANCE);
        return qry.list(); 
 }

    public static List <AmpLocationIndicatorValue> getLocationIndicatorValueByLocationAndIndicatorName (AmpCategoryValueLocations location,String name) {
        Session dbSession = PersistenceManager.getSession();
        String queryString = "select value from "
                + AmpLocationIndicatorValue.class.getName()
                + " value where value.location.id=:id "
                + " and value.indicator.name=:name ";

        Query qry = dbSession.createQuery(queryString);
        qry.setCacheable(true);
        qry.setParameter("id", location.getId(), LongType.INSTANCE);
        qry.setParameter("name", name,StringType.INSTANCE);
        return qry.list();
    }

 public static List <AmpLocationIndicatorValue> getLocationIndicatorValueByLocationAndIndicator (AmpCategoryValueLocations location,AmpIndicatorLayer indicator) {
     Session dbSession = PersistenceManager.getSession();
        String queryString = "select value from "
                + AmpLocationIndicatorValue.class.getName()
                + " value where value.location.id=:id "
                + " and value.indicator.id=:indicatorId ";

        Query qry = dbSession.createQuery(queryString);
        qry.setCacheable(true);
        qry.setParameter("id", location.getId(),LongType.INSTANCE);
        qry.setParameter("indicatorId", indicator.getId(), LongType.INSTANCE);
        return qry.list();
 }
 
 public static void deleteIndicatorLayer (AmpIndicatorLayer indLayer) {
     Session dbSession = PersistenceManager.getSession();
     String queryString = "delete from "
                + AmpLocationIndicatorValue.class.getName()
                + "  where indicator.id=:indicatorLayerId";
    
     Query qry = dbSession.createQuery(queryString);
     qry.setParameter("indicatorLayerId", indLayer.getId(), LongType.INSTANCE);
     qry.executeUpdate();
     dbSession.delete(indLayer);
 }
 
 public static AmpLocationIndicatorValue getLocationIndicatorValue (Long indicator, Long location) {
     Session dbSession = PersistenceManager.getSession();
        String queryString = "select value from "
                + AmpLocationIndicatorValue.class.getName()
                + " value where value.location.id=:locationId and value.indicator.id=:indicatorId)";
        Query qry = dbSession.createQuery(queryString);
        qry.setCacheable(true);
        qry.setParameter("locationId", location,LongType.INSTANCE);
        qry.setParameter("indicatorId", indicator, LongType.INSTANCE);
        return (AmpLocationIndicatorValue)qry.uniqueResult(); 
 }


    /**
     * 
     * @param id
     * @param cvLocationLayer
     *            the AmpCategoryValue specifying the layer (level) of the
     *            location...like Country or Region
     * @return
     */
    public static AmpCategoryValueLocations getLocationById(
            long id, AmpCategoryValue cvLocationLayer) {
        Session dbSession = null;

        try {
            dbSession = PersistenceManager.getSession();
            String queryString = "select loc from "
                    + AmpCategoryValueLocations.class.getName()
                    + " loc where (loc.id=:id)";
            if (cvLocationLayer != null) {
                queryString += " AND (loc.parentCategoryValue=:cvId) ";
            }
            Query qry = dbSession.createQuery(queryString);
            if (cvLocationLayer != null) {
                qry.setParameter("cvId", cvLocationLayer.getId(), LongType.INSTANCE);
            }
            qry.setParameter("id", id, LongType.INSTANCE);
            return (AmpCategoryValueLocations) qry
                    .uniqueResult();
        } catch (Exception e) {
            logger.error("Exception getting AmpCategoryValueLocations by ID: " + id,e);
        }
        return null;
    }
    public static class ErrorWrapper{
        ErrorCode errorCode;
        Set<String> moreinfo;
        public ErrorWrapper(ErrorCode errorCode) {
            this.errorCode=errorCode;
        }
        public ErrorWrapper(ErrorCode errorCode,Set<String>moreinfo){
            this(errorCode);
            this.moreinfo=moreinfo;
        }
        public ErrorCode getErrorCode() {return errorCode;}
        public void setErrorCode(ErrorCode errorCode) {this.errorCode = errorCode;}
        public Set<String> getMoreinfo() {return moreinfo;}
        public void setMoreinfo(Set<String> moreinfo) {this.moreinfo = moreinfo;}
        
    }
    
    /**
     * Reset population flag for all AmpIndicatorLayer entries to the given state
     * @param isPopulation
     * @return
     */
    public static int setIndicatorLayersPopulation(boolean isPopulation, List<Long> ids) {
        String whereIds = (ids == null || ids.isEmpty()) ? "" : " where o.id in (" + Util.toCSString(ids) + ")";
        return PersistenceManager.getSession().createQuery("update " + AmpIndicatorLayer.class.getName() + " o "
                + "set o.population=:isPopulation" + whereIds).setParameter("isPopulation", isPopulation, BooleanType.INSTANCE).executeUpdate();
    }
    
    public static List<Long> getIndicatorLayersIdsByTypeExcludeAdm(Long indicatorTypeId, Long implLocIdToExclude) {
        return PersistenceManager.getSession().createQuery("select o.id from " + AmpIndicatorLayer.class.getName() + " o "
                + "where o.indicatorType != null and o.indicatorType.id = :indicatorTypeId "
                + "and o.admLevel != null and o.admLevel.id != :implLocIdToExclude")
                .setLong("indicatorTypeId", indicatorTypeId).setLong("implLocIdToExclude", implLocIdToExclude)
                .list();
    }

}
