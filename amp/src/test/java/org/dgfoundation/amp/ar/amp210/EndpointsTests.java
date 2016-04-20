package org.dgfoundation.amp.ar.amp210;

import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import net.sf.json.JSONObject;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;

public class EndpointsTests extends JerseyTest {

    private WebResource ws;
    private final static String PACKAGE_NAME = "org.digijava.kernel.ampapi.endpoints.dashboards";
    private final static ObjectMapper mapper = new ObjectMapper().configure(
            DeserializationConfig.Feature.UNWRAP_ROOT_VALUE, true);
    private JsonBean filter = new JsonBean();
    private ClientConfig clientConfig;


    @Before
    public void setUp() throws Exception {
        clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

    }

    public EndpointsTests() throws Exception {
        super(new WebAppDescriptor.Builder(PACKAGE_NAME).build());

    }

//    @Test
//    public void testTops() {
//        WebResource webResource = Client.create(clientConfig).resource("http://localhost:8080/rest/dashboard").path("/tops");
//        ClientResponse response = webResource.type(MediaType.APPLICATION_JSON_TYPE).accept(MediaType.APPLICATION_JSON_TYPE)
//                .get(ClientResponse.class);
//        List <Map<String,String>> json  =response.getEntity(new GenericType (List.class));
//        Map <String,String>agency = (Map<String,String>)json.get(0);
//        Map <String,String>region = (Map<String,String>)json.get(1);
//        Map <String,String>sector = (Map<String,String>)json.get(2);
//        Assert.assertEquals("do",agency.get("id"));
//        Assert.assertEquals("Donor Agency",agency.get("name"));
//        Assert.assertEquals("re",region.get("id"));
//        Assert.assertEquals("Region",region.get("name"));
//        Assert.assertEquals("ps",sector.get("id"));
//        Assert.assertEquals("Primary Sector",sector.get("name"));
//    }

    @Test
    public void testAidPredictability() throws Exception {

        WebResource webResource = Client.create(clientConfig).resource("http://localhost:8080/rest/dashboard").path("/aid-predictability");
        ClientResponse response = webResource.type(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse.class,
                mapper.writer().writeValueAsString(filter));
        JSONObject json =response.getEntity(JSONObject.class);
        Assert.assertEquals("Expected currency: USD actual,actual "+json.get("currency"),"USD", json.get("currency"));
        Assert.assertEquals("Expected format: '###,###,###.##' actual,actual "+json.get("numberformat"),"###,###,###.##", json.get("numberformat"));
    }

}
