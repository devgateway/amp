package org.digijava.kernel.geocoding.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.digijava.kernel.entity.geocoding.GeoCodedActivity;
import org.digijava.kernel.entity.geocoding.GeoCodedField;
import org.digijava.kernel.entity.geocoding.GeoCodedLocation;
import org.digijava.kernel.entity.geocoding.GeoCodingProcess;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.geocoding.client.GeoCoderClient;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
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

    /**
     * Add activities to be geo coded as part of the current process. If the same activity is added twice, previous
     * geo coding information about activity is removed and the activity is geo coded again.
     *
     * @param activityIds activities to geo code
     * @throws GeoCodingNotAvailableException if geo coding is in use by someone else
     * @throws GeneralGeoCodingException if activity cannot be loaded or it already has locations assigned
     */
    public void processActivities(Set<Long> activityIds) {
        GeoCodingProcess geoCodingProcess = getCurrentGeoCoding();

        AmpTeamMember principal = getPrincipal();

        if (geoCodingProcess != null && !geoCodingProcess.getTeamMember().equals(principal)) {
            throw new GeoCodingNotAvailableException(geoCodingProcess.getTeamMember());
        }

        if (geoCodingProcess == null) {
            geoCodingProcess = new GeoCodingProcess(principal);
            PersistenceManager.getSession().save(geoCodingProcess);
        }

        // TODO update locations only if changed
        client.sendLocations(LocationUtil.getAllVisibleLocations());

        List<AmpActivityVersion> activities = new ArrayList<>();
        for (Long activityId : activityIds) {
            try {
                AmpActivityVersion v = ActivityUtil.loadActivity(activityId);
                if (locationsAreAlreadyAssigned(v)) {
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

            GeoCodedActivity geoCodedActivity = geoCodingProcess.getActivities().stream()
                    .filter(a -> a.getActivity().getAmpActivityId().equals(activity.getAmpActivityId()))
                    .findFirst()
                    .orElse(null);

            if (geoCodedActivity != null) {
                geoCodedActivity.setQueueId(queueId);
                geoCodedActivity.setStatus(GeoCodedActivity.Status.RUNNING);
                geoCodedActivity.getLocations().clear();
            } else {
                geoCodingProcess.addActivity(new GeoCodedActivity(queueId, activity));
            }
        }

        PersistenceManager.getSession().flush();
    }

    private boolean locationsAreAlreadyAssigned(AmpActivityVersion v) {
        return !v.getLocations().stream().allMatch(this::isAtNationalLevel);
    }

    private boolean isAtNationalLevel(AmpActivityLocation ampActivityLocation) {
        return ampActivityLocation.getLocation().getLocation().getParentLocation() == null;
    }

    /**
     * Returns geo coding process. Also, updates activities that were geo coded.
     *
     * @return geo coding process if the process was started
     *         or null if geo coding process did not start yet
     * @throws GeoCodingNotAvailableException if geo coding is in use by someone else
     */
    public GeoCodingProcess getGeoCodingProcess() {
        GeoCodingProcess geoCodingProcess = getCurrentGeoCoding();

        if (geoCodingProcess != null && !geoCodingProcess.getTeamMember().equals(getPrincipal())) {
            throw new GeoCodingNotAvailableException(geoCodingProcess.getTeamMember());
        }

        if (geoCodingProcess != null) {
            syncGeoCodedActivities(geoCodingProcess);
        }

        return geoCodingProcess;
    }

    /**
     * For all activities that are still being processed, retrieve the result from geo coding server and update the
     * activity to reflect the current status.
     */
    private void syncGeoCodedActivities(GeoCodingProcess geoCodingProcess) {
        for (GeoCodedActivity activity : geoCodingProcess.getActivities()) {
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
            if (geoCodingResult.getAcvlIds() != null) {
                acvlIds.addAll(geoCodingResult.getAcvlIds());
            }
        }
        return acvlIds;
    }

    private GeoCodedLocation createLocation(AmpCategoryValueLocations acvl,
            List<GeoCoderClient.GeoCodingResult> geoCodingResults) {
        GeoCodedLocation l = new GeoCodedLocation(acvl);
        for (GeoCoderClient.GeoCodingResult r : geoCodingResults) {
            if (r.getAcvlIds() != null && r.getAcvlIds().contains(acvl.getId())) {
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
        GeoCodingProcess geoCodingProcess = getCurrentGeoCoding();

        if (geoCodingProcess != null) {
            PersistenceManager.getSession().delete(geoCodingProcess);
        }
    }

    private GeoCodingProcess getCurrentGeoCoding() {
        List<GeoCodingProcess> geoCodingProcesses = PersistenceManager.getSession()
                .createCriteria(GeoCodingProcess.class)
                .list();

        return geoCodingProcesses.isEmpty() ? null : geoCodingProcesses.get(0);
    }

    private AmpTeamMember getPrincipal() {
        return TeamUtil.getCurrentAmpTeamMember();
    }

    public void changeLocationStatus(Long ampActivityId, Long acvlId, Boolean accepted) {
        GeoCodingProcess geoCoding = getGeoCodingProcess();
        if (geoCoding != null && geoCoding.getTeamMember().equals(getPrincipal())) {
            geoCoding.getActivities().stream()
                    .filter(a -> a.getActivity().getAmpActivityId().equals(ampActivityId))
                    .flatMap(a -> a.getLocations().stream())
                    .filter(l -> l.getLocation().getId().equals(acvlId))
                    .forEach(l -> l.setAccepted(accepted));
        }
    }
}
