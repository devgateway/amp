package org.digijava.kernel.ampapi.endpoints;

import java.util.ArrayList;
import java.util.List;


import org.digijava.kernel.ampapi.endpoints.util.FilterParam;
import org.digijava.kernel.ampapi.endpoints.util.FiltersParams;
import org.glassfish.jersey.uri.UriComponent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class Tests {
    private final static ObjectMapper mapper = new ObjectMapper().configure(
            SerializationFeature.WRAP_ROOT_VALUE, true);

    public static void main(String[] args) {
        try {
            FiltersParams p = new FiltersParams();
            p.setParams(new ArrayList<FilterParam>());
            FilterParam param=new FilterParam();
            param.setFilterName("sectorName");
            param.setFilterValue(new ArrayList<String>());
            param.getFilterValue().add("Primary");
            p.getParams().add(param);

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
