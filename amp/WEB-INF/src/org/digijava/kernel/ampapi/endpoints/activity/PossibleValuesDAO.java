package org.digijava.kernel.ampapi.endpoints.activity;

import java.util.List;

import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpComponentType;

/**
 * @author Octavian Ciubotaru
 */
public interface PossibleValuesDAO {

    List<Object[]> getCategoryValues(String discriminatorOption);

    <T> List<T> getGenericValues(Class<T> entity);

    AmpClassificationConfiguration getAmpClassificationConfiguration(String name);

    List<Object[]> getThemes(String configType);

    List<Object[]> getSectors(String configType);

    List<Object[]> getPossibleLocations();

    List<AmpComponentType> getComponentTypes();
}
