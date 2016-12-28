/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.dto;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * A simple comma separated list of longs 
 * 
 * @author Nadejda Mandrecsu
 */
public class LongListParam {
    public final List<Long> param; 
    public LongListParam(String str) {
        this.param = splitToListOfLongs(str);
    }
    
    /**
     * Converts a comma separated list provided as a string to a List of Longs 
     * @param commaSeparatedList a comma separated list represented as a string
     * @param clazz
     * @return
     */
    public static List<Long> splitToListOfLongs(String commaSeparatedList) {
        List<Long> result = new ArrayList<>();
        if (StringUtils.isNotBlank(commaSeparatedList)) {
            for(String element : commaSeparatedList.split(",")) {
                // no validation, leaving any exception to go up
                result.add(Long.valueOf(element.trim()));
            }
        }
        return result;
    }

}
