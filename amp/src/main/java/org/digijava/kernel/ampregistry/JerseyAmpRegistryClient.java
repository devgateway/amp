package org.digijava.kernel.ampregistry;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import org.apache.log4j.Logger;
import org.digijava.module.aim.dbentity.AmpOfflineRelease;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.io.InputStream;
import java.util.List;

import static com.sun.jersey.api.client.config.ClientConfig.PROPERTY_CONNECT_TIMEOUT;
import static com.sun.jersey.api.client.config.ClientConfig.PROPERTY_READ_TIMEOUT;

/**
 * @author Octavian Ciubotaru
 */
public class JerseyAmpRegistryClient implements AmpRegistryClient {

    private static Logger logger = Logger.getLogger(JerseyAmpRegistryClient.class);

    private static final String SECRET_TOKEN_HEADER = "Secret-Token";
    private static final String AMP_OFFLINE_RELEASE_RESOURCE = "amp-offline-release";
    private static final String AMP_REGISTRY_RESOURCE = "amp-registry";

    private static final Integer JERSEY_CONNECT_TIMEOUT = getPropertyConnectTimeout();
    private static final Integer JERSEY_READ_TIMEOUT = getPropertyReadTimeout();

    private Client client;

    private String baseUrl;

    public JerseyAmpRegistryClient() {
        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        if (JERSEY_CONNECT_TIMEOUT != null) {
            clientConfig.getProperties().put(PROPERTY_CONNECT_TIMEOUT, JERSEY_CONNECT_TIMEOUT);
        }

        if (JERSEY_READ_TIMEOUT != null) {
            clientConfig.getProperties().put(PROPERTY_READ_TIMEOUT, JERSEY_READ_TIMEOUT);
        }
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

    /**
     * Connect timeout interval property, in milliseconds, by getting the system property indicated by the key.
     * The value MUST be an instance of Integer.
     * If the property is absent then the default value is an interval of infinity.
     *
     * @return connectTimeout
     */
    private static Integer getPropertyConnectTimeout() {
        try {
            return Integer.parseInt(System.getProperty("jersey.client.connectTimeout"));
        } catch (Throwable t) {
            logger.warn("jersey.client.connectTimeout property is invalid or not present");
        }

        return null;
    }


    /**
     * Read timeout interval property, in milliseconds, by getting the system property indicated by the key.
     * The value MUST be an instance of Integer.
     * If the property is absent then the default value is an interval of infinity.
     *
     * @return readTimeout
     */
    private static Integer getPropertyReadTimeout() {
        try {
            return Integer.parseInt(System.getProperty("jersey.client.readTimeout"));
        } catch (Throwable t) {
            logger.warn("jersey.client.readTimeout property is invalid or not present");
        }

        return null;
    }

    public void destroy() {
        client.destroy();
    }
}
