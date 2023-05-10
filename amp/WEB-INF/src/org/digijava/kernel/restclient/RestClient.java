/**
 * 
 */
package org.digijava.kernel.restclient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.springframework.http.HttpEntity;

/**
 * A simple Rest client
 * 
 * @author Nadejda Mandrescu
 */
public class RestClient {
    public enum Type {
        JSON
    };
    
    protected static final Logger logger = Logger.getLogger(RestClient.class);
    
    protected static Map<Type, Client> existingClients = new TreeMap<Type, Client>(); 
    protected static Map<Client, String> clientsMediaType = new HashMap<Client, String>();
    
    protected Client client;
    protected String mediaType;
    
    public static synchronized RestClient getInstance(Type type) {
        if (!existingClients.containsKey(type)) {
            Client client = null;
            switch (type) {
            case JSON:
                client = Client.create();
                clientsMediaType.put(client, MediaType.APPLICATION_JSON);
                break;
            default:
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
    
    /**
     * Executes a GET request
     * @param url REST Endpoint 
     * @param queryParams (optional) query parameters, multiple values allowed per parameter 
     * @return JSON string
     */
    public String requestGET(String endpointURL, Map<String, List<String>> queryParams) {
        WebResource webResource = client.resource(endpointURL);
        
        MultivaluedMap<String, String> qP = new MultivaluedMapImpl();
        qP.putAll(queryParams);
        
        webResource = webResource.queryParams(qP);
        Builder builder = webResource.accept(mediaType);
        ClientResponse response = builder.get(ClientResponse.class);
        String info = String.format("[HTTP %d] GET %s", response.getStatus(), webResource.getURI());
        logger.debug(info);
        return response.getEntity(String.class);
    }

    public ClientResponse requestPOST(String endpointURL, HttpEntity<Map<String, Object>> requestBody) {
        WebResource webResource = client.resource(endpointURL);
        Builder builder = webResource.accept(mediaType);
        if (requestBody != null) {
            for (String headerName : requestBody.getHeaders().keySet()) {
                for (String headerValue : requestBody.getHeaders().get(headerName)) {
                    builder.header(headerName, headerValue);
                }
            }
        }
        ClientResponse response = builder.post(ClientResponse.class, requestBody.getBody());

        String info = String.format("[HTTP %d] POST %s", response.getStatus(), webResource.getURI());
        logger.debug(info);
        return response;
    }
    
}
