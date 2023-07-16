package org.digijava.kernel.jobs;

import static java.util.stream.Collectors.toList;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.ampregistry.AmpInstallation;
import org.digijava.kernel.ampregistry.AmpRegistryService;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.persistence.PersistenceManager;
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

import com.google.common.hash.Hashing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Registers this AMP installation in AMP Registry.
 *
 * @author Octavian Ciubotaru
 */
public class RegisterWithAmpRegistryJob extends ConnectionCleaningJob {

    public static final String NAME = "Register with AMP Registry";

    private static final String AMP_REGISTRY_SECRET_TOKEN_ENV_NAME = "AMP_REGISTRY_SECRET_TOKEN";
    private static final String AMP_REGISTRY_PRIVATE_KEY_ENV_NAME = "AMP_REGISTRY_PRIVATE_KEY";
    
    public static final String AMP_DEVELOPMENT_ENV_NAME = "AMP_DEVELOPMENT";
    
    public static final String REGISTRY_STG_URL = "https://amp-registry-stg.ampsite.net/";
    public static final String OFFLINE_ENABLED = "true";

    private static final int JOB_FIRST_START_DELAY_IN_MIN = 5;

    private final Logger logger = LoggerFactory.getLogger(RegisterWithAmpRegistryJob.class);

    private AmpRegistryService ampRegistryService = AmpRegistryService.INSTANCE;

    @Override
    public void executeInternal(JobExecutionContext context) throws JobExecutionException {
        if (FeaturesUtil.isAmpOfflineEnabled()) {
            String secretToken = System.getenv(AMP_REGISTRY_SECRET_TOKEN_ENV_NAME);
            
            if (isDevServer()) {
                String registryUrl = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMP_REGISTRY_URL);
                if (!REGISTRY_STG_URL.equals(registryUrl)) {
                    updateRegistryStgURL();
                }
                
                secretToken = generateDevSecretToken();
            }
            
            if (secretToken != null) {
                ampRegistryService.register(getCurrentInstallation(), secretToken);
            } else {
                logger.warn(AMP_REGISTRY_SECRET_TOKEN_ENV_NAME + " environment variable was not setup.");
            }
        }
    }
    
    private String generateDevSecretToken() {
        String privateKey = System.getenv(AMP_REGISTRY_PRIVATE_KEY_ENV_NAME);
        String isoCode = getCurrentCountry().getIso().toUpperCase();
        String hashKey = Hashing.sha256().hashString(isoCode + privateKey, StandardCharsets.UTF_8).toString();
        return String.format("%s%s", isoCode, hashKey);
    }

    private AmpInstallation getCurrentInstallation() {
        Site defaultSite = SiteUtils.getDefaultSite();
        Country country = getCurrentCountry();

        AmpInstallation installation = new AmpInstallation();
        installation.setIso2(country.getIso().toUpperCase());
        
        List<String> siteUrls = new ArrayList<>(new LinkedHashSet<>(getSiteUrls(defaultSite)));
        Map<String, String> allTranslations = getAllTranslations(defaultSite, country.getCountryName());
        
        // AMP-27350 add URLs in all translated names if AMP is in stg (dev) mode
        if (Boolean.parseBoolean(System.getProperty(AMP_DEVELOPMENT_ENV_NAME))) {
            allTranslations.replaceAll((k, v) -> String.format("%s (%s)", v, String.join(", ", siteUrls)));
        }
        
        installation.setUrls(siteUrls);
        installation.setName(allTranslations);
        installation.setServerId(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMP_SERVER_ID));
        
        return installation;
    }
    
    private boolean isDevServer() {
        return Boolean.parseBoolean(System.getProperty(AMP_DEVELOPMENT_ENV_NAME));
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
    
    private void updateRegistryStgURL() {
        PersistenceManager.getSession().doWork(connection -> SQLUtils.executeQuery(connection,
                String.format("UPDATE amp_global_settings SET settingsvalue = '%s' WHERE settingsname ='%s'",
                        REGISTRY_STG_URL, GlobalSettingsConstants.AMP_REGISTRY_URL)));
        
        FeaturesUtil.buildGlobalSettingsCache(FeaturesUtil.getGlobalSettings());
    }

    @SuppressWarnings("unused")
    public static void registerJob() throws Exception {
        AmpQuartzJobClass jobClass = new AmpQuartzJobClass();
        jobClass.setClassFullname(RegisterWithAmpRegistryJob.class.getName());
        jobClass.setName(NAME);
        jobClass.setSchedName(NAME);
        QuartzJobClassUtils.addJobClasses(jobClass);

        QuartzJobForm jobForm = new QuartzJobForm();
        jobForm.setClassFullname(jobClass.getClassFullname());
        jobForm.setGroupName("ampServices");
        jobForm.setManualJob(false);
        jobForm.setName(jobClass.getName());
        jobForm.setTriggerType(QuartzJobForm.HOURLY);
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
