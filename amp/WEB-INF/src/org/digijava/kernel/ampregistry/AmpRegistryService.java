package org.digijava.kernel.ampregistry;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.digijava.module.aim.dbentity.AmpOfflineRelease;

/**
 * @author Octavian Ciubotaru
 */
public class AmpRegistryService {

    public static final AmpRegistryService INSTANCE = new AmpRegistryService(JerseyAmpRegistryClient::new);

    private Supplier<AmpRegistryClient> factory;

    public AmpRegistryService(Supplier<AmpRegistryClient> factory) {
        this.factory = factory;
    }

    public List<AmpOfflineRelease> getReleases() {
        return doWithClientReturning(AmpRegistryClient::getReleases);
    }

    public Supplier<InputStream> releaseFileSupplier(AmpOfflineRelease release) {
        return () -> doWithClientReturning(c -> c.resourceStream(release.getUrl()));
    }

    public void register(AmpInstallation installation, String secretToken) {
        doWithClient(client -> {
            List<AmpInstallation> installations = client.listAmpInstallations(secretToken);

            AmpInstallation existingInstallation = installations.stream()
                    .filter(i -> Objects.equals(i.getServerId(), installation.getServerId()))
                    .findFirst()
                    .orElse(null);

            // older AMPs registered installations without specifying the serverId property
            // if serverId is not specified the match by country code
            if (existingInstallation == null) {
                existingInstallation = installations.stream()
                        .filter(i -> i.getServerId() == null && Objects.equals(i.getIso2(), installation.getIso2()))
                        .findFirst()
                        .orElse(null);
            }

            if (existingInstallation == null) {
                client.createAmpInstallation(installation, secretToken);
            } else if (!existingInstallation.equals(installation)) {
                client.updateAmpInstallation(existingInstallation.getId(), installation, secretToken);
            }
        });
    }

    private void doWithClient(Consumer<AmpRegistryClient> consumer) {
        doWithClientReturning(c -> {
            consumer.accept(c);
            return Void.class;
        });
    }

    private <T> T doWithClientReturning(Function<AmpRegistryClient, T> function) {
        AmpRegistryClient client = factory.get();
        try {
            return function.apply(client);
        } finally {
            client.destroy();
        }
    }
}
