package org.digijava.kernel.ampapi.filters;


/**
 * During a http request this class holds client mode value.
 *
 * @author Octavian Ciubotaru
 */
public final class AmpClientModeHolder {
    
    private static final ThreadLocal<ClientMode> CLIENT_MODE = new ThreadLocal<>();
    
    private AmpClientModeHolder() {
    
    }
    
    public static void setClientMode(ClientMode val) {
        CLIENT_MODE.set(val);
    }
    
    /**
     * Is this a request made by AMP Offline?
     *
     * @return true if we are currently in a http request and it is issued by AMP Offline client, false otherwise.
     */
    public static boolean isOfflineClient() {
        return CLIENT_MODE.get() == ClientMode.AMP_OFFLINE;
    }
    
    /**
     * Is this a request made by IATI Importer?
     *
     * @return true if we are currently in a http request and it is issued by IATI Importer client, false otherwise.
     */
    public static boolean isIatiImporterClient() {
        return CLIENT_MODE.get() == ClientMode.IATI_IMPORTER;
    }
    
}
