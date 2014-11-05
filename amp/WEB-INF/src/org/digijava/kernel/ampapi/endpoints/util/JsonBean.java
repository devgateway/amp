package org.digijava.kernel.ampapi.endpoints.util;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.digijava.kernel.ampapi.endpoints.gis.GisEndPoints;

/**
 * 
 * @author jdeanquin
 * 
 */
public class JsonBean {
	private static final Logger logger = Logger.getLogger(JsonBean.class);

	public JsonBean() {

	}

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
			ObjectMapper mapper11 = new ObjectMapper();
			mapper11.configure(
					org.codehaus.jackson.map.DeserializationConfig.Feature.UNWRAP_ROOT_VALUE,
					false);
			mapper11.configure(
					org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);

			return mapper11.readValue(jb, JsonBean.class);
		} catch (IOException e) {
			logger.error("Cannot deserialize json bean", e);
			return null;
		}
	}

	@Override
	public String toString() {
        ObjectMapper mapper = new ObjectMapper().configure(
        		DeserializationConfig.Feature.UNWRAP_ROOT_VALUE, true);
        String json=null;
		try {
			json = mapper.writer().writeValueAsString(this);
		} catch (IOException e) {
				
		}
		return "JsonBean [" + json  +"]";
	}
	
}
