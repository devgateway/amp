package org.digijava.module.aim.util;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpLocationIndicatorValue;
import org.digijava.module.aim.exception.dynlocation.DuplicateLocationCodeException;
import org.digijava.module.aim.helper.Location;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.hibernate.type.StringType;

import java.text.Collator;
import java.util.*;

public final class LocationUtil {

    private static Logger logger = Logger.getLogger(LocationUtil.class);

    private LocationUtil() {
    }

    public static final List<String> LOCATIONS_COLUMNS_NAMES = Collections.unmodifiableList(
            Arrays.asList(ColumnConstants.LOCATION_ADM_LEVEL_0, ColumnConstants.LOCATION_ADM_LEVEL_1,
                    ColumnConstants.LOCATION_ADM_LEVEL_2, ColumnConstants.LOCATION_ADM_LEVEL_3,
                    ColumnConstants.LOCATION));

    //End Search Location.
    public static AmpLocation getAmpLocationByCVLocation(Long ampCVLocationId) {
        Session session = null;
        AmpLocation loc = null;

        try {
            session = PersistenceManager.getRequestDBSession();

            String queryString = "select l from " + AmpLocation.class.getName()
                    + " l where location=:locationId order by l.ampLocationId";
            Query qry = session.createQuery(queryString);
            qry.setLong("locationId", ampCVLocationId);

            Collection result = qry.list();
            if (result != null && result.size() > 0) {
                return (AmpLocation) result.iterator().next();
            }

        } catch (Exception e) {
            logger.error("Uanble to get location :" + e);
        }
        return loc;

    }

    public static AmpCategoryValueLocations getAmpLocationByGeoCode(String geoCode) {
        Session session = null;
        AmpCategoryValueLocations loc = null;

        try {
            session = PersistenceManager.getRequestDBSession();

            String queryString = "select l from " + AmpCategoryValueLocations.class.getName()
                    + " l where l.geoCode =:geoCode order by l.id";
            Query qry = session.createQuery(queryString);
            qry.setString("geoCode", geoCode);
            
            Collection result   = qry.list();
            if ( result != null && result.size() > 0 ) {
                return (AmpCategoryValueLocations) result.iterator().next();
            }

        } catch (Exception e) {
            logger.error("Uanble to get location :" + e);
        }
        return loc;

    }

    /**
     * Returns list of locations using their ids
     *
     * @param ids consists  selected locations id separted by comma
     * @return List of AmpCategoryValueLocations beans
     * @throws DgException if anything goes wrong
     */

    public static List<AmpCategoryValueLocations> getAllLocations(String ids) throws DgException {
        Session session = null;
        List<AmpCategoryValueLocations> col = null;

        try {

            session = PersistenceManager.getRequestDBSession();
            String queryString = " from " + AmpCategoryValueLocations.class.getName()
                    + " vl where vl.parentLocation  is null ";

            if (ids != null && ids.length() > 0) {
                String id = ids.substring(0, ids.length() - 1);
                queryString += "  or vl.parentLocation  in (" + id + ")";
            }

            Query qry = session.createQuery(queryString);
            col = qry.list();
        } catch (Exception e) {
            logger.error("Unable to get locations from database "
                    + e.getMessage());
            throw new DgException(e);
        }
        return col;
    }

    public static List<AmpCategoryValueLocations> getAllCountriesAndRegions() {
        Session session = null;
        List<AmpCategoryValueLocations> col = null;

        try {

            session = PersistenceManager.getRequestDBSession();
            String queryString = " from " + AmpCategoryValueLocations.class.getName();

            Query qry = session.createQuery(queryString);
            col = qry.list();
        } catch (Exception e) {
            logger.error("Unable to get locations from database "
                    + e.getMessage());
            throw new RuntimeException(e);
        }
        return col;
    }

    /**
     * Returns location using its id
     *
     * @param id of location
     * @return AmpCategoryValueLocations bean
     * @throws DgException if anything goes wrong
     */

    public static AmpCategoryValueLocations getAmpCategoryValueLocationById(Long id) throws DgException {
        return (AmpCategoryValueLocations) PersistenceManager.getSession().load(AmpCategoryValueLocations.class, id);
    }

    /**
     * Saves location into the database
     *
     * @param b
     * @param AmpCategoryValueLocations bean
     * @throws DgException
     */

    public static void saveLocation(AmpCategoryValueLocations loc, boolean editing) throws DgException {
        Session session = null;
        Transaction tx = null;

        if (!editing) {

            /*  country check for duplicate iso and iso3 codes */
            boolean isCountry = CategoryConstants.IMPLEMENTATION_LOCATION_ADM_LEVEL_0.equalsCategoryValue(
                    loc.getParentCategoryValue());
            if (isCountry) {
                AmpCategoryValueLocations tempLoc = DynLocationManagerUtil.getLocationByIso(
                        loc.getIso(), CategoryConstants.IMPLEMENTATION_LOCATION_ADM_LEVEL_0);
                if (tempLoc != null) {
                    throw new DuplicateLocationCodeException("There is already a country with the same iso !", "iso",
                            loc.getParentCategoryValue().getValue());
                }
                tempLoc = DynLocationManagerUtil.getLocationByIso3(
                        loc.getIso3(), CategoryConstants.IMPLEMENTATION_LOCATION_ADM_LEVEL_0);
                if (tempLoc != null) {
                    throw new DuplicateLocationCodeException("There is already a country with the same iso 3!", "iso3",
                            loc.getParentCategoryValue().getValue());
                }

            }

            AmpCategoryValueLocations tempLoc =
                    DynLocationManagerUtil.getLocationByName(loc.getName(), loc.getParentCategoryValue(),
                            loc.getParentLocation());
            if (tempLoc != null) {
                throw new DuplicateLocationCodeException("There is already a location with the same name!",
                        "name", loc.getParentCategoryValue().getValue());
            }
            if (loc.getParentLocation() != null) {
                if (loc.getParentLocation().getChildLocations() == null) {
                    loc.getParentLocation().setChildLocations(new HashSet<AmpCategoryValueLocations>());
                }
                loc.getParentLocation().getChildLocations().add(loc);
            }
        }

        try {
            session = PersistenceManager.getRequestDBSession();
            session.saveOrUpdate(loc);
        } catch (Exception e) {
            logger.error("Unable to save category value location into the database " + e.getMessage());
            throw new DgException(e);
        }

        DynLocationManagerUtil.getOrCreateAmpLocationByCVL(loc);
    }

    public static class HelperLocationAncestorLocationNamesAsc implements Comparator<Location> {

        private Locale locale;
        private Collator collator;

        public HelperLocationAncestorLocationNamesAsc() {
            this.locale = new Locale("en", "EN");
        }

        public HelperLocationAncestorLocationNamesAsc(String iso) {
            this.locale = new Locale(iso.toLowerCase(), iso.toUpperCase());
        }

        public int compare(Location loc1, Location loc2) {
            collator = Collator.getInstance(locale);
            collator.setStrength(Collator.TERTIARY);
            List<String> loca1AncestorLocationNames = loc1.getAncestorLocationNames();
            List<String> loca2AncestorLocationNames = loc2.getAncestorLocationNames();
            if (loca1AncestorLocationNames == null || loca1AncestorLocationNames.isEmpty()) {
                return -1;
            }
            if (loca2AncestorLocationNames == null || loca2AncestorLocationNames.isEmpty()) {
                return 1;
            }
            StringBuilder location1FullName = new StringBuilder();
            for (String name : loca1AncestorLocationNames) {
                location1FullName.append(name);
            }
            StringBuilder location2FullName = new StringBuilder();
            for (String name : loca2AncestorLocationNames) {
                location2FullName.append(name);
            }
            return collator.compare(location1FullName.toString(), location2FullName.toString());

        }
    }

    public static int getIndicatorValuesCountByAmpLocation(AmpLocation ampLocation) {
        Session session = PersistenceManager.getSession();

        int indicatorValuesCount = (int) session.createCriteria(AmpLocationIndicatorValue.class)
                .add(Restrictions.eq("location", ampLocation))
                .setProjection(Projections.rowCount())
                .uniqueResult();

        return indicatorValuesCount;
    }

    public static List<AmpCategoryValueLocations> getAllVisibleLocations() {
        Session dbSession = PersistenceManager.getSession();

        Criteria criteria = dbSession.createCriteria(AmpCategoryValueLocations.class);
        criteria.setCacheable(true);

        criteria.add(Restrictions.eqOrIsNull("deleted", false));

        return criteria.list();
    }

    /**
     * @return Country ids with children or all countries if showAllCountries == true
     */
    public static Collection<AmpCategoryValueLocations> getCountriesWithChildren(boolean pShowAllCountries) {

        Session session = PersistenceManager.getSession();

        String queryString = "SELECT loc FROM " + AmpCategoryValueLocations.class.getName()
                + " loc WHERE loc.parentLocation IS NULL "
                + " AND (loc.deleted != true)";
        if (!pShowAllCountries) {
            queryString += " AND (loc.id IN (SELECT DISTINCT parentLocation FROM "
                    + AmpCategoryValueLocations.class.getName() + " cloc where cloc.deleted != true))";
        }

        Query qry = session.createQuery(queryString);
        qry.setCacheable(true);
        Collection<AmpCategoryValueLocations> countryCollection = qry.list();
        return countryCollection;
    }
}
