/**
 *
 */
package org.digijava.kernel.restclient;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;

import java.util.*;

public class RestClient {
    public enum Type {
        JSON
    }

    protected static final Logger logger = Logger.getLogger(RestClient.class);

    protected static final Map<Type, Client> existingClients = new TreeMap<>();
    protected static final Map<Client, String> clientsMediaType = new HashMap<>();

    protected final Client client;
    protected final String mediaType;

    public static synchronized RestClient getInstance(Type type) {
        if (!existingClients.containsKey(type)) {
            Client client;
            if (Objects.requireNonNull(type) == Type.JSON) {
                client = ClientBuilder.newClient();
                clientsMediaType.put(client, MediaType.APPLICATION_JSON);
            } else {
                throw new RuntimeException("Rest client not implemented for " + type + " type.");
            }
            existingClients.put(type, client);
        }
        return new RestClient(existingClients.get(type));
    }

    private RestClient(Client client) {
        this.client = client;
        this.mediaType = clientsMediaType.get(client);
    }

    public String requestGET(String endpointURL, Map<String, List<String>> queryParams) {
        WebTarget webTarget = client.target(endpointURL);

        if (queryParams != null) {
            for (Map.Entry<String, List<String>> entry : queryParams.entrySet()) {
                for (String value : entry.getValue()) {
                    webTarget = webTarget.queryParam(entry.getKey(), value);
                }
            }
        }

        Builder builder = webTarget.request().accept(mediaType);
        Response response = builder.get();

        String info = String.format("[HTTP %d] GET %s", response.getStatus(), webTarget.getUri());
        logger.debug(info);

        String result = response.readEntity(String.class);
        response.close(); // Always close Response in Jersey 2.x

        return result;
    }

    public Response requestPOST(String endpointURL, HttpEntity<Map<String, Object>> requestBody) {
        WebTarget webTarget = client.target(endpointURL);
        Builder builder = webTarget.request().accept(mediaType);

        if (requestBody != null) {
            for (String headerName : requestBody.getHeaders().keySet()) {
                for (String headerValue : Objects.requireNonNull(requestBody.getHeaders().get(headerName))) {
                    builder.header(headerName, headerValue);
                }
            }
        }

        Entity<?> entity = Entity.entity(
                requestBody != null ? requestBody.getBody() : null,
                mediaType
        );

        Response response = builder.post(entity);

        String info = String.format("[HTTP %d] POST %s", response.getStatus(), webTarget.getUri());
        logger.debug(info);

        return response;
    }
}
