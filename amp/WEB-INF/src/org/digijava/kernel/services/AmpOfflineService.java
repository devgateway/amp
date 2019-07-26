package org.digijava.kernel.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.apache.commons.io.FileUtils;
import org.dgfoundation.amp.algo.AlgoUtils;
import org.digijava.kernel.Constants;
import org.digijava.module.aim.dbentity.AmpOfflineRelease;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.persistence.PersistenceManager;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Octavian Ciubotaru
 */
@Component
public class AmpOfflineService {

    private final Logger logger = LoggerFactory.getLogger(AmpOfflineService.class);

    private AmpVersionService ampVersionService;

    @Autowired
    public void setAmpVersionService(AmpVersionService ampVersionService) {
        this.ampVersionService = ampVersionService;
    }

    /**
     * Returns latest release that is compatible with running instance of AMP. Latest release itself may not be
     * critical, however if there were some intermediary critical releases then this release will be marked as critical
     * too.
     */
    public AmpOfflineRelease findLastRelease(AmpOfflineRelease clientRelease) {
        if (clientRelease == null) {
            return null;
        }

        List<AmpOfflineRelease> releases = getReleases(clientRelease.getOs(), clientRelease.getArch());

        AmpOfflineRelease latestRelease = null;
        for (AmpOfflineRelease release : releases) {
            if (ampVersionService.isAmpOfflineCompatible(release.getVersion())) {
                latestRelease = latestRelease == null ? release : AlgoUtils.max(latestRelease, release);
            }
        }

        if (latestRelease != null) {
            latestRelease = latestRelease.clone();
            for (AmpOfflineRelease release : releases) {
                if (isCriticalIntermediateRelease(release, clientRelease, latestRelease)) {
                    latestRelease.setCritical(true);
                    break;
                }
            }
        }

        return latestRelease;
    }

    /**
     * Returns true if current release is critical and happened between from and to releases.
     */
    private boolean isCriticalIntermediateRelease(AmpOfflineRelease current, AmpOfflineRelease from,
            AmpOfflineRelease to) {
        return current.isCritical() && current.compareTo(from) >= 0 && current.compareTo(to) <= 0;
    }

    public List<AmpOfflineRelease> getLatestCompatibleReleases() {
        Map<String, AmpOfflineRelease> groupedReleases = new HashMap<>();
        for (AmpOfflineRelease release : getReleases()) {
            if (ampVersionService.isAmpOfflineCompatible(release.getVersion())) {
                String key = release.getOs() + release.getArch();
                AmpOfflineRelease maxRelease = groupedReleases.get(key);
                groupedReleases.put(key, maxRelease == null ? release : AlgoUtils.max(maxRelease, release));
            }
        }

        return new ArrayList<>(groupedReleases.values());
    }

    @SuppressWarnings("unchecked")
    public List<AmpOfflineRelease> getReleases() {
        return PersistenceManager.getSession()
                .createCriteria(AmpOfflineRelease.class)
                .setCacheable(true)
                .list();
    }

    @SuppressWarnings("unchecked")
    private List<AmpOfflineRelease> getReleases(String os, String arch) {
        return PersistenceManager.getSession()
                .createCriteria(AmpOfflineRelease.class)
                .add(Restrictions.eq("os", os))
                .add(Restrictions.eq("arch", arch))
                .setCacheable(true)
                .list();
    }

    public void addRelease(AmpOfflineRelease release, Supplier<InputStream> inputStreamSupplier) throws IOException {
        saveToDisk(release, inputStreamSupplier);

        PersistenceManager.getSession().save(release);
    }

    private void saveToDisk(AmpOfflineRelease release, Supplier<InputStream> inputStreamSupplier)
            throws IOException {
        File file = getReleaseFile(release);

        try (InputStream inputStream = inputStreamSupplier.get()) {
            Files.createDirectories(file.getParentFile().toPath());
            Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException | RuntimeException e) {
            FileUtils.deleteQuietly(file);
            throw e;
        }
    }

    public void deleteRelease(AmpOfflineRelease release) {
        try {
            File releaseFile = getReleaseFile(release);
            if (releaseFile.exists()) {
                Files.delete(releaseFile.toPath());
            }
        } catch (IOException e) {
            logger.warn("Failed to delete release file.", e);
        }

        PersistenceManager.getSession().delete(release);
    }

    public File getReleaseFile(Long id) {
        Session session = PersistenceManager.getSession();
        AmpOfflineRelease release = (AmpOfflineRelease) session.load(AmpOfflineRelease.class, id);
        return getReleaseFile(release);
    }

    public File getReleaseFile(AmpOfflineRelease release) {
        DateFormat dateFormat = new SimpleDateFormat(EPConstants.ISO8601_DATE_FORMAT);
        String groupDir = release.getVersion() + "-" + dateFormat.format(release.getDate());
        return new File(new File(Constants.AMP_OFFLINE_RELEASES, groupDir), release.toFileName());
    }
}
