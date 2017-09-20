package org.digijava.kernel.jobs;

import static java.util.stream.Collectors.toSet;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.ws.rs.core.MediaType;

import com.google.common.reflect.TypeToken;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import org.digijava.module.aim.dbentity.AmpOfflineRelease;
import org.digijava.kernel.services.AmpOfflineService;
import org.digijava.kernel.services.AmpVersionService;
import org.digijava.kernel.util.SpringUtil;
import org.digijava.module.aim.dbentity.AmpQuartzJobClass;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.QuartzJobForm;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.QuartzJobClassUtils;
import org.digijava.module.aim.util.QuartzJobUtils;
import org.digijava.module.message.jobs.ConnectionCleaningJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Downloads new AMP Offline releases. Will keep only releases that are compatible with this AMP.
 * Also will remove any inconsistent AmpOfflineRelease entry which does not have a corresponding file on file system.
 *
 * @author Octavian Ciubotaru
 */
public class DownloadAmpOfflineReleasesJob extends ConnectionCleaningJob {

    private final Logger logger = LoggerFactory.getLogger(DownloadAmpOfflineReleasesJob.class);

    private Client client;

    private Set<AmpOfflineRelease> existingReleases;

    private AmpVersionService ampVersionService;
    private AmpOfflineService ampOfflineService;

    @Override
    public void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            initialize(context);

            removeInvalidAmpOfflineReleases();

            downloadNewReleases();

            removeIncompatibleReleases();
        } finally {
            client.destroy();
        }
    }

    /**
     * Download all new and compatible AMPOfflineReleases.
     */
    private void downloadNewReleases() {
        String url = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMP_OFFLINE_RELEASES_URL);

        List<AmpOfflineRelease> releases = client
                .resource(url)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get(releasesType());

        releases.stream()
                .filter(this::isNewAndCompatibleRelease)
                .forEach(this::persistRelease);
    }

    /**
     * Removes AMPOfflineReleases which are no longer compatible with this AMP release.
     */
    private void removeIncompatibleReleases() {
        existingReleases.stream()
                .filter(r -> !ampVersionService.isAmpOfflineCompatible(r.getVersion()))
                .forEach(r -> ampOfflineService.deleteRelease(r));
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

        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

        client = Client.create(clientConfig);

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
            ampOfflineService.addRelease(release, () -> client.resource(release.getUrl()).get(InputStream.class));

            existingReleases.add(release);
        } catch (IOException | UniformInterfaceException e) {
            logger.warn(e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    private GenericType<List<AmpOfflineRelease>> releasesType() {
        TypeToken<List<AmpOfflineRelease>> listTypeToken = new TypeToken<List<AmpOfflineRelease>>() { };
        return new GenericType<>(listTypeToken.getType());
    }

    public static void registerJob() throws Exception {
        AmpQuartzJobClass jobClass = new AmpQuartzJobClass();
        jobClass.setClassFullname(DownloadAmpOfflineReleasesJob.class.getName());
        jobClass.setName("Download AMP Offline releases");
        QuartzJobClassUtils.addJobClasses(jobClass);

        QuartzJobForm jobForm = new QuartzJobForm();
        jobForm.setClassFullname(jobClass.getClassFullname());
        jobForm.setGroupName("ampServices");
        jobForm.setManualJob(false);
        jobForm.setName(jobClass.getName());
        jobForm.setTriggerType(2);
        jobForm.setExeTimeH("1");

        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.MINUTE, 5);
        Date startDate = instance.getTime();

        jobForm.setStartDateTime(new SimpleDateFormat("dd/MM/yyyy").format(startDate));
        jobForm.setStartH(new SimpleDateFormat("HH").format(startDate));
        jobForm.setStartM(new SimpleDateFormat("mm").format(startDate));

        QuartzJobUtils.addJob(jobForm);
    }
}
