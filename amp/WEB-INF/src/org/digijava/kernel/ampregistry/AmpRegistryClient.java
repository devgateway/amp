package org.digijava.kernel.ampregistry;

import java.io.InputStream;
import java.util.List;

import org.digijava.module.aim.dbentity.AmpOfflineRelease;

/**
 * @author Octavian Ciubotaru
 */
public interface AmpRegistryClient {

    List<AmpOfflineRelease> getReleases();

    InputStream resourceStream(String absoluteUrl);

    List<AmpInstallation> listAmpInstallations(String secretToken);

    void createAmpInstallation(AmpInstallation installation, String secretToken);

    void updateAmpInstallation(Long id, AmpInstallation installation, String secretToken);

    void destroy();
}
