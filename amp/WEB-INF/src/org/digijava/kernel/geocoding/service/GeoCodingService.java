package org.digijava.kernel.geocoding.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.digijava.kernel.entity.geocoding.GeoCodedActivity;
import org.digijava.kernel.entity.geocoding.GeoCodedField;
import org.digijava.kernel.entity.geocoding.GeoCodedLocation;
import org.digijava.kernel.entity.geocoding.GeoCoding;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.geocoding.client.GeoCoderClient;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Octavian Ciubotaru
 */
public class GeoCodingService {

    private static final Logger logger = LoggerFactory.getLogger(GeoCodingService.class);

    private static GeoCoderClient client = new GeoCoderClient();

    public void processActivities(Set<Long> activityIds) {
        GeoCoding geoCoding = getCurrentGeoCoding();

        AmpTeamMember principal = getPrincipal();

        if (geoCoding != null && !geoCoding.getTeamMember().equals(principal)) {
            throw new GeoCodingNotAvailableException(geoCoding.getTeamMember());
        }

        if (geoCoding == null) {
            geoCoding = new GeoCoding(principal);
            PersistenceManager.getSession().save(geoCoding);
        }

        // TODO update locations only if changed
        client.sendLocations(LocationUtil.getAllVisibleLocations());

        List<AmpActivityVersion> activities = new ArrayList<>();
        for (Long activityId : activityIds) {
            try {
                AmpActivityVersion v = ActivityUtil.loadActivity(activityId);
                if (!v.getLocations().isEmpty()) {
                    throw new GeneralGeoCodingException(
                            "Activity with id " + activityId + " already has locations assigned.");
                }
                activities.add(v);
            } catch (DgException e) {
                throw new GeneralGeoCodingException("Failed to load activity with id " + activityId, e);
            }
        }

        List<Long> queueIds = client.processActivities(activities);

        for (int i = 0; i < activities.size(); i++) {
            Long queueId = queueIds.get(i);
            AmpActivityVersion activity = activities.get(i);

            GeoCodedActivity geoCodedActivity = geoCoding.getActivities().stream()
                    .filter(a -> a.getActivity().getAmpActivityId().equals(activity.getAmpActivityId()))
                    .findFirst()
                    .orElse(null);

            if (geoCodedActivity != null) {
                geoCodedActivity.setQueueId(queueId);
                geoCodedActivity.setStatus(GeoCodedActivity.Status.RUNNING);
                geoCodedActivity.getLocations().clear();
            } else {
                geoCoding.addActivity(new GeoCodedActivity(queueId, activity));
            }
        }

        PersistenceManager.getSession().flush();
    }

    /**
     * Returns current geo coding. Also, updates activities that were geo coded.
     *
     * If another team member is in geo coding process, then return null.
     *
     * @return geo coding status if there the process was started
     *         null - if geo coding process did not start yet or already started but for another user
     */
    public GeoCoding getProcessingStatus() {
        GeoCoding geoCoding = getCurrentGeoCoding();

        if (geoCoding != null) {
            syncGeoCoding(geoCoding);
        }

        return geoCoding;
    }

    /**
     * For all activities that are still being processed, retrieve the result from geo coding server and update the
     * activity to reflect the current status.
     */
    private void syncGeoCoding(GeoCoding geoCoding) {
        for (GeoCodedActivity activity : geoCoding.getActivities()) {
            if (activity.getStatus() == GeoCodedActivity.Status.RUNNING) {
                syncGeoCodedActivity(activity);
            }
        }

        PersistenceManager.getSession().flush();
    }

    private void syncGeoCodedActivity(GeoCodedActivity activity) {
        try {
            List<GeoCoderClient.GeoCodingResult> geoCodingResults = client.getGeoCodingResults(activity.getQueueId());

            for (Long acvlId : getAcvlIds(geoCodingResults)) {
                AmpCategoryValueLocations acvl = getAcvl(acvlId);
                if (acvl == null) {
                    logger.error("Unknown AmpCategoryValueLocations id " + acvlId);
                    activity.setStatus(GeoCodedActivity.Status.ERROR);
                    break;
                }
                activity.addLocation(createLocation(acvl, geoCodingResults));
            }

            if (activity.getStatus() == GeoCodedActivity.Status.ERROR) {
                activity.getLocations().clear();
            } else {
                activity.setStatus(GeoCodedActivity.Status.COMPLETED);
            }
        } catch (GeoCoderClient.GeoCodingNotProcessedException e) {
            // ok, do nothing
        } catch (GeoCoderClient.GeoCodingNotFoundException e) {
            logger.error("Geo coding not found", e);
            activity.setStatus(GeoCodedActivity.Status.ERROR);
        }
    }

    private AmpCategoryValueLocations getAcvl(Long acvlId) {
        return (AmpCategoryValueLocations) PersistenceManager.getSession().get(AmpCategoryValueLocations.class, acvlId);
    }

    private Set<Long> getAcvlIds(List<GeoCoderClient.GeoCodingResult> geoCodingResults) {
        Set<Long> acvlIds = new HashSet<>();
        for (GeoCoderClient.GeoCodingResult geoCodingResult : geoCodingResults) {
            acvlIds.addAll(geoCodingResult.getAcvlIds());
        }
        return acvlIds;
    }

    private GeoCodedLocation createLocation(AmpCategoryValueLocations acvl,
            List<GeoCoderClient.GeoCodingResult> geoCodingResults) {
        GeoCodedLocation l = new GeoCodedLocation(acvl);
        for (GeoCoderClient.GeoCodingResult r : geoCodingResults) {
            if (r.getAcvlIds().contains(acvl.getId())) {
                l.addField(new GeoCodedField(r.getField(), r.getValue()));
            }
        }
        return l;
    }

    public void assignLocationsToActivities() {
    }

    /**
     * Accepted locations are not saved.
     */
    public void finishGeoCoding() {
        GeoCoding geoCoding = getCurrentGeoCoding();

        if (geoCoding != null) {
            PersistenceManager.getSession().delete(geoCoding);
        }
    }

    private GeoCoding getCurrentGeoCoding() {
        List<GeoCoding> geoCodings = PersistenceManager.getSession()
                .createCriteria(GeoCoding.class)
                .list();

        return geoCodings.isEmpty() ? null : geoCodings.get(0);
    }

    private AmpTeamMember getPrincipal() {
        return TeamUtil.getCurrentAmpTeamMember();
    }
}
