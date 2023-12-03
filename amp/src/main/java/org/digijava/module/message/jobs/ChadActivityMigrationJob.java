package org.digijava.module.message.jobs;

import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Only for Chad.
 *
 * ADM-3 level was removed completely. All activities linked to ADM-3 will be updated to point to their corresponding
 * parent locations at ADM-2 level.
 * Special case only for N'Djamena (ADM-2): ADM-3 locations are now ADM-2 locations.
 * Eg: '1er Arrondissement' (ADM-3) is now '1er Arrondissement' (ADM-2). The entity ids are different though.
 *
 * Script to add job:
 *     INSERT INTO amp_quartz_job_class (jc_id,jc_name, jc_class_fullname) VALUES
 *     (nextval('amp_quartz_job_class_seq'),
 *     'Migrate Chad activities to new locations',
 *     'org.digijava.module.message.jobs.ChadActivityMigrationJob');
 *
 * Script to delete the job:
 *     delete from amp_quartz_job_class
 *     where jc_class_fullname='org.digijava.module.message.jobs.ChadActivityMigrationJob';
 */
public class ChadActivityMigrationJob extends ConnectionCleaningJob implements StatefulJob {

    private static final int LEVEL_3 = 3;

    private final Logger logger = LoggerFactory.getLogger(ChadActivityMigrationJob.class);

    @Override
    public void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("Migrating Chad activities to new administrative boundaries");

        updateActivityLocations();

        deleteSubPrefectures();

        logger.info("Finished migrating Chad activities to new administrative boundaries");
    }

    private void deleteSubPrefectures() {
        PersistenceManager.getSession()
                .createNativeQuery("update amp_category_value_location\n"
                        + "set deleted=true\n"
                        + "where location_name='N''DJAMENA' and id=9612\n")
                .executeUpdate();

        PersistenceManager.getSession()
                .createNativeQuery("update amp_category_value_location adm3\n"
                        + "set deleted=true\n"
                        + "from amp_category_value_location adm0\n"
                        + "join amp_category_value_location adm1 on adm1.parent_location=adm0.id\n"
                        + "join amp_category_value_location adm2 on adm2.parent_location=adm1.id\n"
                        + "where adm3.parent_location=adm2.id")
                .executeUpdate();
    }

    private void updateActivityLocations() {
        AmpCategoryValue zoneLocation = getCV(CategoryConstants.IMPLEMENTATION_LOCATION_KEY,
                "Administrative Level 2", "Failed to find ADM2 implementation location");

        AmpCategoryValue zoneLevel = getCV(CategoryConstants.IMPLEMENTATION_LEVEL_KEY,
                "Zone", "Failed to find Zone implementation level");

        AmpCategoryValue regionLocation = getCV(CategoryConstants.IMPLEMENTATION_LOCATION_KEY,
                "Administrative Level 1", "Failed to find ADM1 implementation location");

        AmpCategoryValue regionLevel = getCV(CategoryConstants.IMPLEMENTATION_LEVEL_KEY,
                "Provincial", "Failed to find Provincial implementation level");

        for (Long activityId : getADM3ActivityIds()) {
            updateActivity(activityId, zoneLocation, zoneLevel);
        }

        for (Long activityId : getADM2NdjamenaActivityIds()) {
            updateActivity(activityId, regionLocation, regionLevel);
        }
    }

    private void updateActivity(Long activityId,
                                AmpCategoryValue zoneLocation, AmpCategoryValue zoneLevel) {

        AmpActivityVersion activity = (AmpActivityVersion) PersistenceManager.getSession()
                .load(AmpActivityVersion.class, activityId);

        activity.getCategories().removeIf(cv ->
                cv.getAmpCategoryClass().getKeyName().equals(CategoryConstants.IMPLEMENTATION_LOCATION_KEY)
                        || cv.getAmpCategoryClass().getKeyName().equals(CategoryConstants.IMPLEMENTATION_LEVEL_KEY));

        activity.getCategories().add(zoneLocation);
        activity.getCategories().add(zoneLevel);

        List<AmpActivityLocation> newActivityLocations = new ArrayList<>();

        for (AmpActivityLocation activityLocation : activity.getLocations()) {

            AmpCategoryValueLocations location = activityLocation.getLocation();

            AmpCategoryValueLocations newLocation = getNewLocation(location);

            AmpActivityLocation newActivityLocation = newActivityLocations.stream()
                    .filter(al -> al.getLocation().equals(newLocation))
                    .findFirst()
                    .orElseGet(() -> new AmpActivityLocation(activityLocation.getActivity(), newLocation,
                            0f, activityLocation.getLatitude(), activityLocation.getLongitude()));

            if (activityLocation.getLocationPercentage() != null) {
                newActivityLocation.setLocationPercentage(
                        newActivityLocation.getLocationPercentage() + activityLocation.getLocationPercentage());
            }

            newActivityLocations.add(newActivityLocation);
        }

        activity.getLocations().clear();
        activity.getLocations().addAll(newActivityLocations);
    }

    private AmpCategoryValueLocations getNewLocation(AmpCategoryValueLocations loc) {
        AmpCategoryValueLocations parentLocation = loc.getParentLocation();
        if (parentLocation.getName().equals("N'DJAMENA") && getAdmLevel(loc) == LEVEL_3) {
            return (AmpCategoryValueLocations) PersistenceManager.getSession().createQuery(
                    "from AmpCategoryValueLocations\n"
                            + "where name=:name\n"
                            + "and id<>:oldId")
                    .setParameter("name", loc.getName(), StringType.INSTANCE)
                    .setParameter("oldId", loc.getId(), LongType.INSTANCE)
                    .uniqueResult();
        } else {
            return parentLocation;
        }
    }

    private int getAdmLevel(AmpCategoryValueLocations loc) {
        int level = 0;
        while (loc.getParentLocation() != null) {
            loc = loc.getParentLocation();
            level++;
        }
        return level;
    }

    private AmpCategoryValue getCV(String implementationLocationKey, String o, String s) {
        return CategoryManagerUtil.getAmpCategoryValueCollectionByKey(implementationLocationKey)
                .stream()
                .filter(cv -> cv.getValue().equals(o))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(s));
    }

    private List<Long> getADM3ActivityIds() {
        return PersistenceManager.getSession().createNativeQuery(
        "select distinct a.amp_activity_id\n"
                + "from amp_activity a\n"
                + "         join amp_activity_location al on a.amp_activity_id=al.amp_activity_id\n"
                + "         join amp_category_value_location cvl on al.location_id = cvl.id\n"
                + "         join amp_category_value_location cvlp on cvl.parent_location=cvlp.id\n"
                + "         join amp_category_value_location cvlp2 on cvlp.parent_location=cvlp2.id\n"
                + "         join amp_category_value_location cvlp3 on cvlp2.parent_location=cvlp3.id\n"
                + "where cvlp3.parent_location is null")
            .addScalar("amp_activity_id", LongType.INSTANCE)
            .list();
    }

    private List<Long> getADM2NdjamenaActivityIds() {
        return PersistenceManager.getSession().createNativeQuery(
        "select distinct a.amp_activity_id\n"
                + "from amp_activity a\n"
                + "join amp_activity_location al on a.amp_activity_id=al.amp_activity_id\n"
                + "join amp_category_value_location cvl_adm2 on al.location_id = cvl_adm2.id\n"
                + "join amp_category_value_location cvl_adm1 on cvl_adm2.parent_location = cvl_adm1.id\n"
                + "join amp_category_value_location cvl_adm0 on cvl_adm1.parent_location = cvl_adm0.id\n"
                + "where cvl_adm2.location_name='N''DJAMENA' and cvl_adm0.parent_location is null")
            .addScalar("amp_activity_id", LongType.INSTANCE)
            .list();
    }
}
