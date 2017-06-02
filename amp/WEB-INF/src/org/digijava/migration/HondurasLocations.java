package org.digijava.migration;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.dgfoundation.amp.onepager.util.ActivityUtil.saveActivityNewVersion;
import static org.digijava.module.message.jobs.CloseExpiredActivitiesJob.AMP_MODIFIER_FIRST_NAME;
import static org.digijava.module.message.jobs.CloseExpiredActivitiesJob.AMP_MODIFIER_LAST_NAME;
import static org.digijava.module.message.jobs.CloseExpiredActivitiesJob.AMP_MODIFIER_USER_EMAIL;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import au.com.bytecode.opencsv.CSVReader;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpContentTranslation;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.startup.AmpBackgroundActivitiesUtil;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.LongType;

/**
 * @author Octavian Ciubotaru
 */
public class HondurasLocations {

    private static Logger logger = Logger.getLogger(HondurasLocations.class);

    private static final String HONDURAS = "Honduras";

    private Set<String> activitiesWithOldRegions;

    private static User user = new User(AMP_MODIFIER_USER_EMAIL, AMP_MODIFIER_FIRST_NAME, AMP_MODIFIER_LAST_NAME);

    private Pattern oldLocPattern = Pattern.compile("R\\d\\d - .*");

    public static void run() {
        new HondurasLocations().migrate();
    }

    private void migrate() {
        logger.info("MIGRATION STARTED!");

        Multimap<String, Loc> locs = LinkedHashMultimap.create();
        Set<String> actsWithoutLocs = new HashSet<>();

        try {
            InputStream is = HondurasLocations.class.getResourceAsStream("HondurasLocations.csv");
            CSVReader reader = new CSVReader(new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8)));
            reader.readAll().forEach(row -> {
                if (!row[0].equals("Workspace")) {
                    if (isNotEmpty(row[2])) {
                        if (isNotEmpty(row[3])) {
                            Stream.of(row[3].split("\\s*,\\s*")).forEach(m -> locs.put(row[1], new Loc(row[2], m)));
                        } else {
                            locs.put(row[1], new Loc(row[2], null));
                        }
                    } else {
                        actsWithoutLocs.add(row[1]);
                    }
                }
            });
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read config file.", e);
        }

        findActivitiesLinkedWithOldRegions();

        crashIfNotAllActivitiesAreCovered(locs, actsWithoutLocs);

        try {
            AmpBackgroundActivitiesUtil.createActivityUserIfNeeded(user);
            PersistenceManager.cleanupSession(PersistenceManager.getSession()); // commit user in case it was created
        } catch (Exception e) {
            throw new RuntimeException("Failed to create user.", e);
        }

        // update activities with locations
        locs.asMap().forEach(this::updateLocation);

        // update activities without locations
        actsWithoutLocs.forEach(id -> updateLocation(id, emptyList()));

        logger.info("MIGRATION FINISHED!");
    }

    private void crashIfNotAllActivitiesAreCovered(Multimap<String, Loc> locs, Set<String> actsWithoutLocs) {
        HashSet<String> invalidActs = new HashSet<>(activitiesWithOldRegions);
        invalidActs.removeAll(actsWithoutLocs);
        invalidActs.removeAll(locs.keySet());
        if (!invalidActs.isEmpty()) {
            logger.warn("Some activities will still reference old regions: " + invalidActs);
        }
    }

    private void updateLocation(String ampId, Collection<Loc> locs) {
        logger.info("UPDATING: " + ampId);
        try {
            Session session = PersistenceManager.getSession();
            List<AmpContentTranslation> translations = new ArrayList<>();

            AmpActivityVersion a = ActivityUtil.loadActivity(findActivityId(ampId));

            AmpTeamMember teamMember = AmpBackgroundActivitiesUtil.createActivityTeamMemberIfNeeded(a.getTeam(), user);

            a.getLocations().removeIf(this::isOldLocation);
            a.getLocations().addAll(createActivityLocations(a, locs, a.getLocations()));

            float perc = 100f / a.getLocations().size();
            a.getLocations().forEach(l -> l.setLocationPercentage(perc));

            saveActivityNewVersion(a, translations, teamMember, a.getDraft(), session, false, false);

            PersistenceManager.cleanupSession(session);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update locations for activity " + ampId, e);
        }
    }

    private Collection<AmpActivityLocation> createActivityLocations(AmpActivityVersion a, Collection<Loc> locs,
            Set<AmpActivityLocation> locations) {
        return locs.stream()
                .filter(l -> !alreadyPresent(l, locations))
                .map(l -> createActivityLocation(a, l))
                .collect(toList());
    }

    private boolean alreadyPresent(Loc loc, Set<AmpActivityLocation> locations) {
        if (isNotEmpty(loc.department) && isNotEmpty(loc.municipality)) {
            return locations.stream()
                    .anyMatch(ampLoc -> ampLoc.getLocation().getLocation().getName().equals(loc.municipality)
                            && ampLoc.getLocation().getLocation().getParentLocation().getName().equals(loc.department)
                            && ampLoc.getLocation().getLocation().getParentLocation().getParentLocation().getName().equals(HONDURAS));
        } else if (isNotEmpty(loc.department)) {
            return locations.stream()
                    .anyMatch(ampLoc -> ampLoc.getLocation().getLocation().getName().equals(loc.department)
                            && ampLoc.getLocation().getLocation().getParentLocation().getName().equals(HONDURAS));
        } else {
            throw new RuntimeException("Invalid location d: " + loc.department + " m: " + loc.municipality);
        }
    }

    private AmpActivityLocation createActivityLocation(AmpActivityVersion a, Loc loc) {
        AmpActivityLocation activityLocation = new AmpActivityLocation();
        activityLocation.setActivity(a);
        activityLocation.setLocation(findLocation(loc));
        return activityLocation;
    }

    private AmpLocation findLocation(Loc loc) {
        AmpCategoryValueLocations cvLocation = findCVLocation(loc);

        if (cvLocation == null) {
            throw new RuntimeException("Could not find location for d: " + loc.department + " m: " + loc.municipality);
        }

        AmpLocation ampLoc = LocationUtil.getAmpLocationByCVLocation(cvLocation.getId());

        if (ampLoc == null) {
            ampLoc = new AmpLocation();
            ampLoc.setLocation(cvLocation);

            AmpCategoryValueLocations regionLocation = DynLocationManagerUtil.getAncestorByLayer(cvLocation, CategoryConstants.IMPLEMENTATION_LOCATION_REGION);
            if (regionLocation != null) {
                ampLoc.setRegionLocation(regionLocation);
            }

            DbUtil.add(ampLoc);
        }

        return ampLoc;
    }

    private AmpCategoryValueLocations findCVLocation(Loc loc) {
        if (isNotEmpty(loc.department) && isNotEmpty(loc.municipality)) {
            return (AmpCategoryValueLocations) PersistenceManager.getSession()
                    .createCriteria(AmpCategoryValueLocations.class)
                    .createAlias("parentLocation", "p")
                    .createAlias("p.parentLocation", "pp")
                    .add(Restrictions.eq("name", loc.municipality))
                    .add(Restrictions.eq("p.name", loc.department))
                    .add(Restrictions.eq("pp.name", HONDURAS))
                    .uniqueResult();
        } else if (isNotEmpty(loc.department)) {
            return (AmpCategoryValueLocations) PersistenceManager.getSession()
                    .createCriteria(AmpCategoryValueLocations.class)
                    .createAlias("parentLocation", "p")
                    .add(Restrictions.eq("name", loc.department))
                    .add(Restrictions.eq("p.name", HONDURAS))
                    .uniqueResult();
        } else {
            throw new RuntimeException("Invalid location " + loc.department + " " + loc.municipality);
        }
    }

    private boolean isOldLocation(AmpActivityLocation activityLocation) {
        return oldLocPattern.matcher(activityLocation.getLocation().getLocation().getName()).matches();
    }

    private Long findActivityId(String ampId) {
        return (Long) PersistenceManager.getSession()
                .createSQLQuery("select amp_activity_id from amp_activity where amp_id=:ampId")
                .addScalar("amp_activity_id", LongType.INSTANCE)
                .setString("ampId", ampId)
                .uniqueResult();
    }

    /**
     * Old regions start with a prefix "R01 - " and are under Honduras (id=94)
     */
    private void findActivitiesLinkedWithOldRegions() {
        String sql = "select a.amp_id "
                + "from amp_activity a "
                + "join amp_activity_location al on a.amp_activity_id=al.amp_activity_id "
                + "join amp_location l on al.amp_location_id=l.amp_location_id "
                + "join amp_category_value_location cvl on l.location_id=cvl.id "
                + "where cvl.parent_location = 94 and cvl.location_name ~ 'R\\d\\d - .*'";
        activitiesWithOldRegions = new HashSet<>(PersistenceManager.getSession().createSQLQuery(sql).list());
    }

    private static class Loc {

        String department;
        String municipality;

        Loc(String department, String municipality) {
            this.department = department;
            this.municipality = municipality;
        }
    }
}
