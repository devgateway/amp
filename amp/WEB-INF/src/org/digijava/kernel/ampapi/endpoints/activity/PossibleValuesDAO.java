package org.digijava.kernel.ampapi.endpoints.activity;

import java.util.List;

import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpComponentType;
import org.digijava.module.aim.dbentity.AmpContact;

/**
 * @author Octavian Ciubotaru
 */
public interface PossibleValuesDAO {

    int LOC_ID_POS = 0;
    int LOC_CAT_ID_POS = 1;
    int LOC_CAT_NAME_POS = 2;
    int LOC_PARENT_CAT_ID_POS = 3;
    int LOC_PARENT_CAT_NAME_POS = 4;
    int LOC_CAT_VAL_ID_POS = 5;
    int LOC_CAT_VAL_NAME_POS = 6;
    int LOC_ISO = 7;

    List<Object[]> getCategoryValues(String discriminatorOption);

    <T> List<T> getGenericValues(Class<T> entity);

    AmpClassificationConfiguration getAmpClassificationConfiguration(String name);

    List<Object[]> getThemes(String configType);

    List<Object[]> getSectors(String configType);

    List<Object[]> getPossibleLocations();

    List<AmpComponentType> getComponentTypes();
    
    List<AmpContact> getContacts();
    
}
