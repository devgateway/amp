package org.digijava.kernel.ampapi.endpoints.util;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.algo.AlgoUtils;

/**
 * 
 * @author jdeanquin
 * 
 */
public class JsonBean {
    private static final Logger logger = Logger.getLogger(JsonBean.class);

    public JsonBean() {}

    protected LinkedHashMap<String, Object> param = new LinkedHashMap<String, Object>();

    public Object get(String name) { 
        return param.get(name);
    }

    @JsonAnyGetter
    public Map<String, Object> any() {
        return param;
    }

    @JsonAnySetter
    public void set(String name, Object value) {
        // we first try to translate the text
        param.put(name, value);
    }
    
    @JsonIgnore
    public Integer getSize() {
        return param.size();
    }
    
    /**
     * makes a shallow copy
     * @return
     */
    public JsonBean copy() {
        JsonBean res = new JsonBean();
        res.param = new LinkedHashMap<String, Object>(this.param);
        return res;
    }
    
    public String getString(String name) {
        Object o = get(name);
        if (o != null) {
            return o.toString();
        } else {
            return null;
        }
    }

    public static JsonBean getJsonBeanFromString(String jb) {
        try {
            if (jb == null) {
                return null;
            }
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            return mapper.readValue(jb, JsonBean.class);
        } catch (IOException e) {
            logger.error("Cannot deserialize json bean", e);
            return null;
        }
    }

    /**
     * renders this bean as a String
     */
    public String asJsonString() {
        try {
            return new ObjectMapper().writer().writeValueAsString(this);
        }
        catch(Exception e) {
            throw AlgoUtils.translateException(e);
        }
    }
    
    @Override
    public String toString() {
        return "JsonBean [" + asJsonString() +"]";
    }
    
}
