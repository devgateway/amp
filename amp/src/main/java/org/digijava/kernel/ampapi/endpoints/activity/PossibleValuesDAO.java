package org.digijava.kernel.ampapi.endpoints.activity;

import org.digijava.module.aim.dbentity.*;

import java.util.List;

/**
 * @author Octavian Ciubotaru
 */
public interface PossibleValuesDAO {

    int LOC_CAT_ID_POS = 0;
    int LOC_CAT_NAME_POS = 1;
    int LOC_PARENT_CAT_ID_POS = 2;
    int LOC_PARENT_CAT_NAME_POS = 3;
    int LOC_CAT_VAL_ID_POS = 4;
    int LOC_CAT_VAL_NAME_POS = 5;
    int LOC_ISO = 6;
    int LOC_OLD_ID = 7;

    int SECTOR_PARENT_ID_POS = 2;
    int THEME_PARENT_ID_POS = 2;

    List<Object[]> getCategoryValues(String discriminatorOption);

    <T> List<T> getGenericValues(Class<T> entity);

    AmpClassificationConfiguration getAmpClassificationConfiguration(String name);

    List<Object[]> getThemes(String configType);

    boolean isThemeValid(String configType, Long id);

    List<Object[]> getSectors(String configType);

    boolean isSectorValid(String configType, Long id);

    List<Object[]> getPossibleLocations();

    boolean isLocationValid(Long id);

    List<AmpOrganisation> getOrganisations();

    boolean isOrganizationValid(Long id);

    List<AmpIndicatorRiskRatings> getIndicatorRiskRatings();

    boolean isIndicatorRiskRatingValid(Long id);

    List<AmpIndicator> getIndicators();

    boolean isIndicatorValid(Long id);

    List<AmpThemeMapping> getMappedThemes();
}
