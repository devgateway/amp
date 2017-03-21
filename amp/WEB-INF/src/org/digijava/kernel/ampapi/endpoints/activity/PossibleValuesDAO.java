package org.digijava.kernel.ampapi.endpoints.activity;

import java.util.List;

/**
 * @author Octavian Ciubotaru
 */
public interface PossibleValuesDAO {

    List<Object[]> getCategoryValues(String discriminatorOption);

    List<Object[]> getGenericValues(Class<?> clazz, String idFieldName, String valueFieldName);

    List<Object[]> getThemes(String configType);

    List<Object[]> getSectors(String configType);

    List<Object[]> getPossibleLocations();
}
