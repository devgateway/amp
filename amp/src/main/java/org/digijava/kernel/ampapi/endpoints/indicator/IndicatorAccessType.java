package org.digijava.kernel.ampapi.endpoints.indicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anpicca on 04/07/2016.
 */
public enum IndicatorAccessType {
    TEMPORARY(0l),
    PRIVATE(1l),
    PUBLIC(2l),
    STANDARD(3l),
    SHARED(4l);

    public static final List<Long> VALUES = getValues();

    public long getValue() {
        return value;
    }

    private long value;
    IndicatorAccessType(Long value) {
        this.value = value;
    }

    @Override
    public String toString(){
        return Long.toString(this.value);
    }

    private static List<Long> getValues() {
        List<Long> values = new ArrayList<Long>();
        for (IndicatorAccessType type : IndicatorAccessType.values()) {
            values.add(type.value);
        }
        return values;
    }
    public static IndicatorAccessType getValueFromLong(Long l) {
        int i = 0;
        for (IndicatorAccessType type : IndicatorAccessType.values()) {
            i++;
            if (type.value == l) {
                return type;
            }
        }
        throw new IllegalArgumentException("the given number doesn't match any Status.");
    }
}
