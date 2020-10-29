package org.digijava.kernel.geocoding.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.dgfoundation.amp.onepager.util.PercentagesUtil;
import org.dgfoundation.amp.onepager.util.SaveContext;
import org.digijava.kernel.entity.geocoding.GeoCodedActivity;
import org.digijava.kernel.entity.geocoding.GeoCodedField;
import org.digijava.kernel.entity.geocoding.GeoCodedLocation;
import org.digijava.kernel.entity.geocoding.GeoCodingProcess;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.geocoding.client.GeoCoderClient;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.startup.AMPStartupListener;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Octavian Ciubotaru
 */
public class GeoCodingService {

    private static final Logger logger = LoggerFactory.getLogger(GeoCodingService.class);

    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);

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
            if (activity != null && activity.getStatus() == GeoCodedActivity.Status.RUNNING) {
                syncGeoCodedActivity(activity);
            }
        }

        PersistenceManager.getSession().flush();
    }

    private void syncGeoCodedActivity(GeoCodedActivity activity) {
        try {
            List<GeoCoderClient.GeoCodingResult> geoCodingResults = client.getGeoCodingResults(activity.getQueueId());

            populateGeoCodingResultsWithAmpLocations(geoCodingResults);

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
            activity.setStatus(GeoCodedActivity.Status.RUNNING);
        } catch (GeoCoderClient.GeoCodingNotFoundException e) {
            logger.error("Geo coding not found", e);
            activity.setStatus(GeoCodedActivity.Status.ERROR);
        } catch (GeoCoderClient.GeoCodingErrorException e) {
            logger.error("Error occurred during the geo coding", e);
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

    private void populateGeoCodingResultsWithAmpLocations(List<GeoCoderClient.GeoCodingResult> geoCodingResults) {
        List<AmpCategoryValueLocations> allVisibleLocations = LocationUtil.getAllVisibleLocations();
        for (GeoCoderClient.GeoCodingResult geoCodingResult : geoCodingResults) {
            if (geoCodingResult.getLocation() != null) {
                geoCodingResult.setAcvlIds(matchAmpLocations(allVisibleLocations, geoCodingResult.getLocation()));
            }
        }
    }

    private Set<Long> matchAmpLocations(final List<AmpCategoryValueLocations> allVisibleLocations,
                                        final GeoCoderClient.GeoCodingLocation location) {
        Set<Long> acvlIds = new HashSet<>();
        for (AmpCategoryValueLocations ampLocation : allVisibleLocations) {
            if (GeoCodingLocationMatcher.match(ampLocation, location)) {
                acvlIds.add(ampLocation.getId());
            }
        }

        if (acvlIds.isEmpty()) {
            logger.info(String.format("----- No Location Matched for [%s]", location));
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

    /**
     * Accepted locations are not saved.
     */
    public void cancelGeoCoding() {
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

    public void saveActivities() {
        Session session = PersistenceManager.getSession();
        session.setFlushMode(FlushMode.MANUAL);

        GeoCodingProcess geoCoding = getGeoCodingProcess();
        if (geoCoding != null) {
            Iterator<GeoCodedActivity> iterator = geoCoding.getActivities().iterator();
            while (iterator.hasNext()) {
                GeoCodedActivity activity = iterator.next();
                if (allLocationsHaveStatusSet(activity)) {
                    if (acceptedLocationsExist(activity)) {
                        updateActivity(activity);
                    }
                    iterator.remove();
                }
            }
        }
    }

    private boolean acceptedLocationsExist(GeoCodedActivity activity) {
        return activity.getLocations().stream().anyMatch(l -> l.getAccepted() == Boolean.TRUE);
    }

    private boolean allLocationsHaveStatusSet(GeoCodedActivity activity) {
        return activity.getLocations().stream().allMatch(l -> l.getAccepted() != null);
    }

    private void updateActivity(GeoCodedActivity geoCodedActivity) {

        AmpActivityVersion activity = geoCodedActivity.getActivity();

        if (locationsAreAlreadyAssigned(activity)) {
            throw new GeneralGeoCodingException("Locations are already assigned!");
        }

        updateCategoryValues(geoCodedActivity, activity);

        updateLocations(geoCodedActivity, activity);

        org.dgfoundation.amp.onepager.util.ActivityUtil.saveActivity(activity, null, getPrincipal(),
                SiteUtils.getDefaultSite(), new java.util.Locale("en"),
                AMPStartupListener.SERVLET_CONTEXT_ROOT_REAL_PATH, activity.getDraft(), SaveContext.job());
    }

    private void updateLocations(GeoCodedActivity geoCodedActivity, AmpActivityVersion activity) {
        int numLocations = (int) geoCodedActivity.getLocations().stream()
                .filter(GeoCodedLocation::getAccepted)
                .count();

        int scale = FeaturesUtil.getGlobalSettingValueInteger(
                Constants.GlobalSettings.DECIMAL_LOCATION_PERCENTAGES_DIVIDE);

        PercentagesUtil.SplitResult split = PercentagesUtil.split(ONE_HUNDRED, numLocations, scale);

        activity.getLocations().clear();

        for (GeoCodedLocation geoCodedLocation : geoCodedActivity.getLocations()) {
            if (geoCodedLocation.getAccepted()) {
                AmpActivityLocation activityLocation = new AmpActivityLocation();
                activityLocation.setLocationPercentage(split.getValueFor(activity.getLocations().size()).floatValue());
                activityLocation.setLocation(
                        LocationUtil.getAmpLocationByCVLocation(geoCodedLocation.getLocation().getId()));
                activity.addLocation(activityLocation);
            }
        }
    }

    private void updateCategoryValues(GeoCodedActivity geoCodedActivity, AmpActivityVersion activity) {
        AmpCategoryValue implementationLocation = getImplementationLocation(geoCodedActivity);
        AmpCategoryValue implementationLevel = getImplementationLevel(implementationLocation);

        replaceCategoryValue(activity, implementationLocation);
        replaceCategoryValue(activity, implementationLevel);
    }

    private void replaceCategoryValue(AmpActivityVersion activity, AmpCategoryValue cv) {
        activity.getCategories().removeIf(c -> c.getAmpCategoryClass().equals(cv.getAmpCategoryClass()));
        activity.getCategories().add(cv);
    }

    private AmpCategoryValue getImplementationLevel(AmpCategoryValue implementationLocation) {
        Iterator<AmpCategoryValue> iterator = implementationLocation.getUsedValues().iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        } else {
            throw new GeneralGeoCodingException("Could not determine implementation level.");
        }
    }

    private AmpCategoryValue getImplementationLocation(GeoCodedActivity geoCodedActivity) {
        AmpCategoryValue implementationLocation = null;
        for (GeoCodedLocation location : geoCodedActivity.getLocations()) {
            AmpCategoryValue cat = location.getLocation().getParentCategoryValue();
            if (implementationLocation == null) {
                implementationLocation = cat;
            } else if (!implementationLocation.equals(cat)) {
                throw new GeneralGeoCodingException("Different implementation levels selected");
            }
        }
        return implementationLocation;
    }

    public void resetLocationStatuses() {
        GeoCodingProcess geoCodingProcess = getGeoCodingProcess();
        if (geoCodingProcess != null) {
            geoCodingProcess.getActivities().stream()
                    .flatMap(a -> a.getLocations().stream())
                    .forEach(l -> l.setAccepted(null));
        }
    }
}
