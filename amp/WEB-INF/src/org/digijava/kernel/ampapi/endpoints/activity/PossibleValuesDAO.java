package org.digijava.kernel.ampapi.endpoints.activity;

import java.util.List;

/**
 * @author Octavian Ciubotaru
 */
public interface PossibleValuesDAO {

    List<Object[]> getCategoryValues(String discriminatorOption);

    List<Object[]> getGenericValues(Class<?> clazz, String idFieldName, String valueFieldName);

    List<Object[]> getSpecialCaseObjectList(String configType, String configTableName,
            String entityIdColumnName, String entityValueColumnName,
            String conditionColumnName, String idColumnName, Class<?> clazz);

    List<Object[]> getPossibleLocations();
}
