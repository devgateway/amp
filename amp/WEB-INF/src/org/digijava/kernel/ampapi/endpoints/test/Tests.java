package org.digijava.kernel.ampapi.endpoints.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;

import com.sun.jersey.api.uri.UriComponent;

public class Tests {
    private final static ObjectMapper mapper = new ObjectMapper().configure(
    		DeserializationConfig.Feature.UNWRAP_ROOT_VALUE, true);

    public static void main(String[] args) throws IOException {
        try {
            String json;

            
            JsonBean j=new JsonBean();
            List<Long>ids=new ArrayList<Long>();
            ids.add(1L);
            ids.add(2L);
            ids.add(3L);
            ids.add(4L);
            j.set("activityId", ids);
            j.set("adminLevel", "Region");
            
            
            List<String>idString=new ArrayList<String>();
            idString.add("string1");
            idString.add("string2");
            idString.add("string3");
            idString.add("string4");
            j.set("idString", idString);
            json = mapper.writer().writeValueAsString(j);
            
            System.out.println(json);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
