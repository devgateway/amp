package org.digijava.kernel.ampapi.filters;

/**
 * During a http request this class holds ampOfflineMode value.
 *
 * @author Octavian Ciubotaru
 */
public class AmpOfflineModeHolder {

    private static final ThreadLocal<Boolean> ampOfflineMode = new ThreadLocal<>();

    public static void setAmpOfflineMode(Boolean val) {
        ampOfflineMode.set(val);
    }

    /**
     * Is this a request made by AMP Offline?
     *
     * @return true if we are currently in a http request and it is issued by AMP Offline client, false otherwise.
     */
    public static boolean isAmpOfflineMode() {
        Boolean offlineMode = ampOfflineMode.get();
        return offlineMode != null && offlineMode;
    }
}
