package org.dgfoundation.amp.newreports;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.std.SerializerBase;

/**
 * @author Octavian Ciubotaru
 */
public class FilterRuleSerializer extends SerializerBase<FilterRule> {

    public FilterRuleSerializer() {
        super(FilterRule.class);
    }

    @Override
    public void serialize(FilterRule rule, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        switch (rule.filterType) {
        case SINGLE_VALUE:
            writeValue(rule, jgen);
            break;
        case VALUES:
            writeValues(rule, jgen);
            break;
        case RANGE:
            writeRange(rule, jgen);
            break;
        default:
            throw new RuntimeException("Unsupported filter type: " + rule.filterType);
        }
    }

    private void writeValue(FilterRule rule, JsonGenerator jgen) throws IOException {
        jgen.writeString(rule.value);
    }

    private void writeValues(FilterRule rule, JsonGenerator jgen) throws IOException {
        if (rule.values == null) {
            jgen.writeNull();
        } else {
            jgen.writeStartArray();
            for (String value : rule.values) {
                jgen.writeString(value);
            }
            jgen.writeEndArray();
        }
    }

    private void writeRange(FilterRule rule, JsonGenerator jgen) throws IOException {
        if (rule.min == null && rule.max == null) {
            jgen.writeNull();
        } else {
            jgen.writeStartObject();
            if (rule.min != null) {
                jgen.writeStringField("start", rule.min);
            }
            if (rule.max != null) {
                jgen.writeStringField("end", rule.max);
            }
            jgen.writeEndObject();
        }
    }
}
