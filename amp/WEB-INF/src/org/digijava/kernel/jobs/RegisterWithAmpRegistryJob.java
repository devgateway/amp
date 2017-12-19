package org.digijava.kernel.jobs;

import static java.util.stream.Collectors.toList;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.digijava.kernel.ampregistry.AmpInstallation;
import org.digijava.kernel.ampregistry.AmpRegistryClient;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.dbentity.AmpQuartzJobClass;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.QuartzJobForm;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.QuartzJobClassUtils;
import org.digijava.module.aim.util.QuartzJobUtils;
import org.digijava.module.message.jobs.ConnectionCleaningJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Registers this AMP installation in AMP Registry.
 *
 * @author Octavian Ciubotaru
 */
public class RegisterWithAmpRegistryJob extends ConnectionCleaningJob {

    public static final String NAME = "Register with AMP Registry";

    private static final String AMP_REGISTRY_SECRET_TOKEN_ENV_NAME = "AMP_REGISTRY_SECRET_TOKEN";

    private static final int JOB_FIRST_START_DELAY_IN_MIN = 5;

    @Override
    public void executeInternal(JobExecutionContext context) throws JobExecutionException {
        String secretToken = System.getenv(AMP_REGISTRY_SECRET_TOKEN_ENV_NAME);
        if (secretToken != null && isAmpOfflineEnabled()) {
            AmpRegistryClient client = new AmpRegistryClient();
            client.register(getCurrentInstallation(), secretToken);
        }
    }

    private boolean isAmpOfflineEnabled() {
        return FeaturesUtil.getGlobalSettingValueBoolean(GlobalSettingsConstants.AMP_OFFLINE_ENABLED);
    }

    private AmpInstallation getCurrentInstallation() {
        Site defaultSite = SiteUtils.getDefaultSite();
        Country country = getCurrentCountry();

        AmpInstallation installation = new AmpInstallation();
        installation.setIso2(country.getIso().toUpperCase());
        installation.setName(getAllTranslations(defaultSite, country.getCountryName()));
        installation.setUrls(getSiteUrls(defaultSite));
        return installation;
    }

    private Country getCurrentCountry() {
        String iso = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_COUNTRY);
        return DbUtil.getDgCountry(iso);
    }

    private List<String> getSiteUrls(Site defaultSite) {
        return defaultSite.getSiteDomains()
                .stream()
                .sorted(SiteDomain.DEFAULTS_FIRST)
                .map(SiteDomain::toUrl)
                .collect(toList());
    }

    private Map<String, String> getAllTranslations(Site site, String text) {
        try {
            Collection<Message> messages = TranslatorWorker.getAllTranslationOfBody(text, site.getId());
            Map<String, String> grouped = new HashMap<>();
            messages.forEach(m -> grouped.put(m.getLocale(), m.getMessage()));
            return grouped;
        } catch (WorkerException e) {
            throw new RuntimeException("Failed to translate country name.", e);
        }
    }

    @SuppressWarnings("unused")
    public static void registerJob() throws Exception {
        AmpQuartzJobClass jobClass = new AmpQuartzJobClass();
        jobClass.setClassFullname(RegisterWithAmpRegistryJob.class.getName());
        jobClass.setName(NAME);
        QuartzJobClassUtils.addJobClasses(jobClass);

        QuartzJobForm jobForm = new QuartzJobForm();
        jobForm.setClassFullname(jobClass.getClassFullname());
        jobForm.setGroupName("ampServices");
        jobForm.setManualJob(false);
        jobForm.setName(jobClass.getName());
        jobForm.setTriggerType(2);
        jobForm.setExeTimeH("1");

        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.MINUTE, JOB_FIRST_START_DELAY_IN_MIN);
        Date startDate = instance.getTime();

        jobForm.setStartDateTime(new SimpleDateFormat("dd/MM/yyyy").format(startDate));
        jobForm.setStartH(new SimpleDateFormat("HH").format(startDate));
        jobForm.setStartM(new SimpleDateFormat("mm").format(startDate));

        QuartzJobUtils.addJob(jobForm);
    }
}
