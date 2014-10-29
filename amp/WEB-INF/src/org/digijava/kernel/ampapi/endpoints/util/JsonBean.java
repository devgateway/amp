package org.digijava.kernel.ampapi.endpoints.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * 
 * @author jdeanquin
 * 
 */
public class JsonBean {

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
}
