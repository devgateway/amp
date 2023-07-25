/**
 *
 */
package org.digijava.kernel.restclient;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import javax.ws.rs.core.Response;

import java.util.*;

public class RestClient {
    public enum Type {
        JSON
    }

    protected static final Logger logger = LoggerFactory.getLogger(RestClient.class);

    protected static Map<Type, Client> existingClients = new TreeMap<>();
    protected static Map<Client, String> clientsMediaType = new HashMap<>();

    protected WebTarget webTarget;
    protected String mediaType;

    public static synchronized RestClient getInstance(Type type) {
        if (!existingClients.containsKey(type)) {
            ClientConfig config = new ClientConfig();
            Client client = JerseyClientBuilder.newBuilder()
                    .withConfig(config)
                    .build();
            if (Objects.requireNonNull(type) == Type.JSON) {
                clientsMediaType.put(client, MediaType.APPLICATION_JSON);
            } else {
                throw new RuntimeException("Rest client not implemented for " + type + " type.");
            }
            existingClients.put(type, client);
        }
        return new RestClient(existingClients.get(type));
    }

    private RestClient(Client client) {
        this.webTarget = client.target("");
        this.mediaType = clientsMediaType.get(client);
    }

    /**
     * Executes a GET request
     *
     * @param endpointURL REST Endpoint
     * @param queryParams (optional) query parameters, multiple values allowed per parameter
     * @return JSON string
     */
    public String requestGET(String endpointURL, Map<String, List<String>> queryParams) {
        WebTarget target = webTarget.path(endpointURL);

        // Build query parameters
        for (Map.Entry<String, List<String>> entry : queryParams.entrySet()) {
            String paramName = entry.getKey();
            List<String> paramValues = entry.getValue();
            for (String paramValue : paramValues) {
                target = target.queryParam(paramName, paramValue);
            }
        }

        Invocation.Builder builder = target.request(mediaType);
        Response response = builder.get();
        String info = String.format("[HTTP %d] GET %s", response.getStatus(), target.getUri());
        logger.debug(info);
        return response.getEntity().toString();
    }
}