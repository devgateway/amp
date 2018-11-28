package org.digijava.kernel.ampapi.endpoints.activity;

import static org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants.TYPE_VARCHAR;

import java.lang.reflect.Field;

/**
 * @author Octavian Ciubotaru
 */
public class TestFieldInfoProvider implements FieldInfoProvider {

    public static final int MAX_STR_LEN = 10;

    @Override
    public String getType(Field f) {
        return String.class.isAssignableFrom(f.getType()) ? TYPE_VARCHAR : "unknown";
    }

    @Override
    public Integer getMaxLength(Field f) {
        return String.class.isAssignableFrom(f.getType()) && !f.getName().equals("noMaxLen") ? MAX_STR_LEN : null;
    }

    @Override
    public boolean isTranslatable(Field field) {
        return false;
    }
}
