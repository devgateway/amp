package org.digijava.module.autopatcher.core;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class PatcherMessages {
    private static final String BUNDLE_NAME = "org.digijava.module.autopatcher.core.messages"; //$NON-NLS-1$

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
            .getBundle(BUNDLE_NAME);

    private PatcherMessages() {
    }

    public static String getString(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }
}
