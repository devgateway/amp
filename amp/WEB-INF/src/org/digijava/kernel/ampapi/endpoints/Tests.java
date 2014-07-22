package org.digijava.kernel.ampapi.endpoints;

import java.util.ArrayList;
import java.util.List;


import org.digijava.kernel.ampapi.endpoints.util.FilterParam;
import org.digijava.kernel.ampapi.endpoints.util.FiltersParams;
import org.glassfish.jersey.uri.UriComponent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

public class Tests {
    private final static ObjectMapper mapper = new ObjectMapper().configure(
            SerializationFeature.WRAP_ROOT_VALUE, false);

    public static void main(String[] args) {
        try {
            FiltersParams p = new FiltersParams();
            p.setParams(new ArrayList<FilterParam>());

            List<FilterParam> params=new ArrayList<FilterParam>();
            
            FilterParam param1=new FilterParam();
            param1.setFilterName("adminLevel");
            param1.setFilterValue(new ArrayList<String>());
            param1.getFilterValue().add("Region");
            params.add(param1);
            
//            FilterParam param2=new FilterParam();
//            param2.setFilterName("activityStatus");
//            param2.setFilterValue(new ArrayList<String>());
//            param2.getFilterValue().add("validated");
//            param2.getFilterValue().add("draft");
//            params.add(param2);            
            p.setParams(params);
            String json;

            json = mapper.writer().writeValueAsString(p);
            System.out.println(json);
            String enconded = UriComponent.encode(json,
                    UriComponent.Type.QUERY_PARAM_SPACE_ENCODED);
            System.out.println(enconded);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
