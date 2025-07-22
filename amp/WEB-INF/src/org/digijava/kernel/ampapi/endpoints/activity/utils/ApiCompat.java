package org.digijava.kernel.ampapi.endpoints.activity.utils;

/**
 * @author Octavian Ciubotaru
 */
public final class ApiCompat {

    private static ThreadLocal<String> requestedMediaTypeTL = new ThreadLocal<>();

    private ApiCompat() {
    }

    public static String getRequestedMediaType() {
        return requestedMediaTypeTL.get();
    }

    public static void setRequestedMediaType(String requestedMediaType) {
        requestedMediaTypeTL.set(requestedMediaType);
    }

    public static void withRequestedMediaType(String mediaType, Runnable r) {
        try {
            requestedMediaTypeTL.set(mediaType);
            r.run();
        } finally {
            requestedMediaTypeTL.remove();
        }
    }
}
