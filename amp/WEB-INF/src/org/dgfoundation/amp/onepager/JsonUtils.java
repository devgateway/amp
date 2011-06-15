package org.dgfoundation.amp.onepager;

import java.io.StringWriter;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;


public class JsonUtils {

    private static final JsonFactory jf = new JsonFactory();

    public static String marshal(Object o) {
        StringWriter sw = new StringWriter();
        try {
            JsonGenerator gen = jf.createJsonGenerator(sw);
            new ObjectMapper().writeValue(gen, o);
            return sw.toString();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

}
