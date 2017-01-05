package org.digijava.kernel.ampapi.endpoints.util.types;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * @author Octavian Ciubotaru
 */
public class ListOfLongs extends ArrayList<Long> {

    public ListOfLongs(String value) {
        super(splitToListOfLongs(value));
    }

    /**
     * Converts a comma separated list provided as a string to a List of Longs
     * @param commaSeparatedList a comma separated list represented as a string
     * @param clazz
     * @return
     */
    private static List<Long> splitToListOfLongs(String commaSeparatedList) {
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
