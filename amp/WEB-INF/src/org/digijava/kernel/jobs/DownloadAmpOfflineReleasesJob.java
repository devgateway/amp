package org.digijava.kernel.jobs;

import org.digijava.kernel.ampregistry.AmpRegistryService;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.services.AmpOfflineService;
import org.digijava.kernel.services.AmpVersionService;
import org.digijava.kernel.util.SpringUtil;
import org.digijava.module.aim.dbentity.AmpOfflineRelease;
import org.digijava.module.aim.dbentity.AmpQuartzJobClass;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.QuartzJobForm;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.QuartzJobClassUtils;
import org.digijava.module.aim.util.QuartzJobUtils;
import org.digijava.module.message.jobs.NonConcurrentJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

/**
 * Downloads new AMP Offline releases. Will keep only releases that are compatible with this AMP.
 * Also will remove any inconsistent AmpOfflineRelease entry which does not have a corresponding file on file system.
 *
 * @author Octavian Ciubotaru
 */
public class DownloadAmpOfflineReleasesJob extends NonConcurrentJob {

    private final Logger logger = LoggerFactory.getLogger(DownloadAmpOfflineReleasesJob.class);

    public static final String NAME = "Download AMP Offline releases";

    private Set<AmpOfflineRelease> existingReleases;

    private AmpVersionService ampVersionService;
    private AmpOfflineService ampOfflineService;

    private AmpRegistryService ampRegistryService = AmpRegistryService.INSTANCE;

    @Override
    public void executeNonConcurrentInternal(JobExecutionContext context) throws JobExecutionException {
        if (FeaturesUtil.isAmpOfflineEnabled()) {
            Set<AmpOfflineRelease> newReleases = new HashSet<>();
            PersistenceManager.inTransaction(() -> {
                try {
                    initialize(context);
                    removeInvalidAmpOfflineReleases();
                    newReleases.addAll(getReleasesForDownload());
                } catch (JobExecutionException e) {
                    throw new RuntimeException(e);
                }
            });

            newReleases.forEach(release -> persistRelease(release));

            saveReleasesInDb(newReleases);
            removeIncompatibleReleases();
        }
    }

    private void saveReleasesInDb(Set<AmpOfflineRelease> releasesForDownload) {
        PersistenceManager.inTransaction(() -> {
            releasesForDownload.forEach(release -> ampOfflineService.persistReleaseInDb(release));
        });
    }

    /**
     * Download all new and compatible AMPOfflineReleases.
     */
    private Set<AmpOfflineRelease> getReleasesForDownload() {
        return ampRegistryService.getReleases()
                .stream()
                .filter(this::isNewAndCompatibleRelease)
                .collect(Collectors.toSet());
    }

    /**
     * Removes AMPOfflineReleases which are no longer compatible with this AMP release.
     */
    private void removeIncompatibleReleases() {
        PersistenceManager.inTransaction(() -> {
            existingReleases.stream()
                    .filter(r -> !ampVersionService.isAmpOfflineCompatible(r.getVersion()))
                    .forEach(r -> ampOfflineService.deleteRelease(r));
        });
    }

    /**
     * Init services, jersey client, existing releases.
     */
    private void initialize(JobExecutionContext context) throws JobExecutionException {
        try {
            ServletContext sc = (ServletContext) context.getScheduler().getContext().get(Constants.AMP_SERVLET_CONTEXT);
            ampVersionService = SpringUtil.getBean(sc, AmpVersionService.class);
            ampOfflineService = SpringUtil.getBean(sc, AmpOfflineService.class);
        } catch (SchedulerException e) {
            throw new JobExecutionException("Failed to initialize job.", e);
        }

        existingReleases = new HashSet<>(ampOfflineService.getReleases());
    }

    /**
     * Remove existing AmpOfflineReleases which don't have a corresponding release file.
     */
    private void removeInvalidAmpOfflineReleases() {
        Set<AmpOfflineRelease> invalidReleases = existingReleases.stream()
                .filter(this::withoutReleaseFile)
                .collect(toSet());
        invalidReleases.forEach(r -> ampOfflineService.deleteRelease(r));
        existingReleases.removeAll(invalidReleases);
    }

    /**
     * Returns true if release file is missing for a specific AmpOfflineRelease.
     */
    private boolean withoutReleaseFile(AmpOfflineRelease ampOfflineRelease) {
        return !ampOfflineService.getReleaseFile(ampOfflineRelease).exists();
    }

    /**
     * Returns true if release is new and compatible with this AMP release.
     */
    private boolean isNewAndCompatibleRelease(AmpOfflineRelease release) {
        return !existingReleases.contains(release) && ampVersionService.isAmpOfflineCompatible(release.getVersion());
    }

    /**
     * Persist release in the system.
     */
    private void persistRelease(AmpOfflineRelease release) {
        try {
            ampOfflineService.saveToDisk(release, ampRegistryService.releaseFileSupplier(release));
            existingReleases.add(release);
        } catch (Exception e) {
            logger.warn(e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    @SuppressWarnings("unused")
    public static void registerJob() throws Exception {
        AmpQuartzJobClass jobClass = new AmpQuartzJobClass();
        jobClass.setClassFullname(DownloadAmpOfflineReleasesJob.class.getName());
        jobClass.setName(NAME);
        QuartzJobClassUtils.addJobClasses(jobClass);

        QuartzJobForm jobForm = new QuartzJobForm();
        jobForm.setClassFullname(jobClass.getClassFullname());
        jobForm.setGroupName("ampServices");
        jobForm.setManualJob(false);
        jobForm.setName(jobClass.getName());
        jobForm.setTriggerType(QuartzJobForm.HOURLY);
        jobForm.setExeTimeH("1");

        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.MINUTE, 5);
        Date startDate = instance.getTime();

        jobForm.setStartDateTime(new SimpleDateFormat("dd/MM/yyyy").format(startDate));
        jobForm.setStartH(new SimpleDateFormat("HH").format(startDate));
        jobForm.setStartM(new SimpleDateFormat("mm").format(startDate));

        QuartzJobUtils.addJob(jobForm);
    }

    @Override
    public boolean shouldExecuteInTransaction() {
        return false;
    }
}