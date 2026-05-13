package org.dgfoundation.amp.nireports;

import java.util.function.Supplier;

/**
 * utility classes for use across NiReports
 * @author Dolghier Constantin
 *
 */
public class NiUtils {
    
    /**
     * throws a {@link RuntimeException} with a given message in case a given boolean is true
     */
    public static void failIf(boolean b, String msg) {
        if (b)
            throw new RuntimeException(msg);    
    }
    
    /**
     * throws a {@link RuntimeException} with a message fetched off a given {@link Supplier} in case a given boolean is true 
     * @param b
     * @param msg
     */
    public static void failIf(boolean b, Supplier<String> msg) {
        if (b)
            throw new RuntimeException(msg.get());
    }
    
    /**
     * returns ((Number) obj).intValue() or -1, if obj is null
     * @param obj
     * @return
     */
    public static int getInt(Object obj) {
        if (obj == null)
            return -1;
        return ((Number) obj).intValue();
    }
}
