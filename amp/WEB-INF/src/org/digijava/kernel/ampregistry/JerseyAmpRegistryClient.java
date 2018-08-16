package org.digijava.kernel.ampregistry;

import java.io.InputStream;
import java.util.List;

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
 * @author Octavian Ciubotaru
 */
public class JerseyAmpRegistryClient implements AmpRegistryClient {

    private static final String SECRET_TOKEN_HEADER = "Secret-Token";
    private static final String AMP_OFFLINE_RELEASE_RESOURCE = "amp-offline-release";
    private static final String AMP_REGISTRY_RESOURCE = "amp-registry";

    private Client client;

    private String baseUrl;

    public JerseyAmpRegistryClient() {
        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

        client = Client.create(clientConfig);

        baseUrl = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMP_REGISTRY_URL);
    }

    @Override
    public List<AmpOfflineRelease> getReleases() {
        return client.resource(UriBuilder.fromUri(baseUrl).path(AMP_OFFLINE_RELEASE_RESOURCE).build())
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get(new GenericType<List<AmpOfflineRelease>>() { });
    }

    @Override
    public InputStream resourceStream(String absoluteUrl) {
        return client.resource(absoluteUrl).get(InputStream.class);
    }

    @Override
    public List<AmpInstallation> listAmpInstallations(String secretToken) {
        return client.resource(UriBuilder.fromUri(baseUrl).path(AMP_REGISTRY_RESOURCE).build())
                .header(SECRET_TOKEN_HEADER, secretToken)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get(new GenericType<List<AmpInstallation>>() { });
    }

    @Override
    public void createAmpInstallation(AmpInstallation installation, String secretToken) {
        client.resource(UriBuilder.fromUri(baseUrl).path(AMP_REGISTRY_RESOURCE).build())
                .header(SECRET_TOKEN_HEADER, secretToken)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .put(installation);
    }

    @Override
    public void updateAmpInstallation(Long id, AmpInstallation installation, String secretToken) {
        client.resource(UriBuilder.fromUri(baseUrl).path(AMP_REGISTRY_RESOURCE).path(id.toString()).build())
                .header(SECRET_TOKEN_HEADER, secretToken)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .post(installation);
    }

    @Override
    public void destroy() {
        client.destroy();
    }
}
