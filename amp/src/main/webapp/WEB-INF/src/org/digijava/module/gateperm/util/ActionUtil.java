/**
 * ActionUtil.java
 * (c) 2007 Development Gateway Foundation
 */
package org.digijava.module.gateperm.util;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.digijava.module.gateperm.core.GatePermConst;

/**
 * ActionUtil.java TODO description here
 * 
 * @author mihai
 * @package org.digijava.module.gateperm.util
 * @since 05.09.2007
 */
public final class ActionUtil {
    private static Logger logger = Logger.getLogger(ActionUtil.class);

    /**
     * Reads the available actions from the inner GateConstants.Action class.
     * This final static inner class is for grouping purposes only, its members
     * should be only string static
     * @return
     */
    public static Set<String> getAvailableActions() {
        TreeSet<String> al=new TreeSet<String>();
        Field[] fields = GatePermConst.Actions.class.getFields();
        for (int i = 0; i < fields.length; i++) {
            try {
                al.add((String) fields[i].get(null));
            } catch (IllegalArgumentException e) {
                logger.error(e.getMessage(), e);
                throw new RuntimeException( "IllegalArgumentException Exception encountered", e);
            } catch (IllegalAccessException e) {
                logger.error(e.getMessage(), e);
                throw new RuntimeException( "IllegalAccessException Exception encountered", e);
            }
        }
        return al;
    }
    

}
