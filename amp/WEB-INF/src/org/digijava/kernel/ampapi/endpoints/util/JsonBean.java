package org.digijava.kernel.ampapi.endpoints.util;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
/**
 * 
 * @author jdeanquin
 *
 */
public class JsonBean {
	
	public JsonBean(){
		
	}
	
    protected Map<String,Object> param = new HashMap<String,Object>();
    public Object get(String name) {
        return param.get(name);
    }
    @JsonAnyGetter
    public Map<String,Object> any() {
        return param;
    }

    @JsonAnySetter
    public void set(String name, Object value) {
    	//we firs try to translate the text
    	param.put(name, value);
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
