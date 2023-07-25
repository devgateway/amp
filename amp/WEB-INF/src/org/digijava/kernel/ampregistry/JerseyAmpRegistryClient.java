package org.digijava.kernel.ampregistry;

import org.digijava.kernel.services.AmpOfflineService;
import org.digijava.module.aim.dbentity.AmpOfflineRelease;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.io.InputStream;
import java.util.List;

public class JerseyAmpRegistryClient implements AmpRegistryClient {

    private static final String SECRET_TOKEN_HEADER = "Secret-Token";
    private static final String AMP_OFFLINE_RELEASE_RESOURCE = "amp-offline-release";
    private static final String AMP_REGISTRY_RESOURCE = "amp-registry";
    private static final Logger logger = LoggerFactory.getLogger(JerseyAmpRegistryClient.class);

    private static final Integer JERSEY_CONNECT_TIMEOUT = getPropertyConnectTimeout();
    private static final Integer JERSEY_READ_TIMEOUT = getPropertyReadTimeout();

    private Client client;

    private String baseUrl;

    public JerseyAmpRegistryClient() {
        client = ClientBuilder.newClient();

        baseUrl = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMP_REGISTRY_URL);
    }

    @Override
    public List<AmpOfflineRelease> getReleases() {
        return client.target(UriBuilder.fromUri(baseUrl).path(AMP_OFFLINE_RELEASE_RESOURCE).build())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(new GenericType<List<AmpOfflineRelease>>() { });
    }

    @Override
    public InputStream resourceStream(String absoluteUrl) {
        return client.target(absoluteUrl).request().get(InputStream.class);
    }

    @Override
    public List<AmpInstallation> listAmpInstallations(String secretToken) {
        return client.target(UriBuilder.fromUri(baseUrl).path(AMP_REGISTRY_RESOURCE).build())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(SECRET_TOKEN_HEADER, secretToken)
                .get(new GenericType<List<AmpInstallation>>() { });
    }

    @Override
    public void createAmpInstallation(AmpInstallation installation, String secretToken) {
        client.target(UriBuilder.fromUri(baseUrl).path(AMP_REGISTRY_RESOURCE).build())
                .request()
                .header(SECRET_TOKEN_HEADER, secretToken)
                .put(Entity.entity(installation, MediaType.APPLICATION_JSON_TYPE));
    }

    @Override
    public void updateAmpInstallation(Long id, AmpInstallation installation, String secretToken) {
        client.target(UriBuilder.fromUri(baseUrl).path(AMP_REGISTRY_RESOURCE).path(id.toString()).build())
                .request()
                .header(SECRET_TOKEN_HEADER, secretToken)
                .post(Entity.entity(installation, MediaType.APPLICATION_JSON_TYPE));
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
        client.close();
    }
}