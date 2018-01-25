package org.digijava.kernel.ampregistry;

import java.io.InputStream;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import org.digijava.module.aim.dbentity.AmpOfflineRelease;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * Client for easy communication with AMP Registry.
 *
 * @author Octavian Ciubotaru
 */
public class AmpRegistryClient {

    private static final String SECRET_TOKEN_HEADER = "Secret-Token";
    private static final String AMP_OFFLINE_RELEASE_RESOURCE = "amp-offline-release";
    private static final String AMP_REGISTRY_RESOURCE = "amp-registry";

    public List<AmpOfflineRelease> getReleases() {
        return doWithClientReturning(client ->
                client.resource(getRegistryUrl().path(AMP_OFFLINE_RELEASE_RESOURCE).build())
                        .accept(MediaType.APPLICATION_JSON_TYPE)
                        .get(new GenericType<List<AmpOfflineRelease>>() { }));
    }

    public Supplier<InputStream> releaseFileSupplier(AmpOfflineRelease release) {
        return () -> doWithClientReturning(c -> c.resource(release.getUrl()).get(InputStream.class));
    }

    public void register(AmpInstallation installation, String secretToken) {
        doWithClient(c -> register(c, installation, secretToken));
    }

    private void register(Client client, AmpInstallation installation, String secretToken) {
        UriBuilder url = getRegistryUrl().path(AMP_REGISTRY_RESOURCE);

        List<AmpInstallation> installations = client.resource(url.build())
                .header(SECRET_TOKEN_HEADER, secretToken)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get(new GenericType<List<AmpInstallation>>() { });

        AmpInstallation existingInstallation = installations.stream()
                .filter(i -> i.getIso2().equals(installation.getIso2()))
                .findFirst()
                .orElse(null);

        if (existingInstallation == null) {
            client.resource(url.build())
                    .header(SECRET_TOKEN_HEADER, secretToken)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .put(installation);
        } else if (!existingInstallation.equals(installation)) {
            client.resource(url.path(existingInstallation.getId().toString()).build())
                    .header(SECRET_TOKEN_HEADER, secretToken)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .post(installation);
        }
    }

    private UriBuilder getRegistryUrl() {
        String url = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMP_REGISTRY_URL);
        return UriBuilder.fromUri(url);
    }

    private void doWithClient(Consumer<Client> consumer) {
        doWithClientReturning(c -> {
            consumer.accept(c);
            return Void.class;
        });
    }

    private <T> T doWithClientReturning(Function<Client, T> function) {
        Client client = null;
        try {
            ClientConfig clientConfig = new DefaultClientConfig();
            clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

            client = Client.create(clientConfig);

            return function.apply(client);
        } finally {
            if (client != null) {
                client.destroy();
            }
        }
    }
}
