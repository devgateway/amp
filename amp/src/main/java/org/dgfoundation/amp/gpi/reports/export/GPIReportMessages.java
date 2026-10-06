package org.dgfoundation.amp.gpi.reports.export;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class GPIReportMessages {
    private static final String BUNDLE_NAME = "org.dgfoundation.amp.gpi.reports.export.resources";

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    private GPIReportMessages() {
    }

    public static String getString(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }
}
