package org.digijava.kernel.ampapi.endpoints.indicator;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Indicator Endpoint Constants
 * 
 * @author apicca
 */
public class IndicatorEPConstants {

    public static final String DEFAULT_INDICATOR_ORDER_FIELD = "createdOn";

    public static final int DEFAULT_COUNT = 10;

    public static final long ACCESS_TYPE_PUBLIC = 2;
    public static final int COLOR_RAMP_INDEX = 8; // index of last element of the colors arrays.
    public static final long PAYLOAD_INDEX = 1; // indicate
    
    public static final long RED_GREEN_PALETTE_INDEX = 9;
    public static final long BLUE_PURPLE_PALETTE_INDEX = 10;
    public static final Integer MAX_ADMIN_LEVEL_SUPPORTED = 4;
   
    public static final String RESULT = "result";
    public static final String VALUES = "values";
    public static final String INDICATOR = "indicator";
    public static final String ID = "id";
    public static final String LABEL = "label";
    public static final String VALUE = "value";
    public static final String GEO_CODE_ID = "geoId";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String NUMBER_OF_CLASSES = "numberOfClasses";
    public static final String UNIT = "unit";
    public static final String ADM_LEVEL_ID = "admLevelId";
    public static final String ADM_LEVEL_NAME = "admLevelName";
    public static final String ADMIN_LEVEL = "adminLevel";
    public static final String ADM_PREFIX = "adm-";
    public static final String ACCESS_TYPE_ID = "accessTypeId";
    public static final String COLOR_RAMP_ID = "colorRampId";
    public static final String CREATED_ON = "createdOn";
    public static final String UPDATED_ON = "updatedOn";
    public static final String CREATE_BY = "createdBy";
    public static final String COLOR_RAMP = "colorRamp";
    public static final String COLOR = "color";
    public static final String RECORDS_PER_PAGE = "recordsPerPage";
    public static final String CURRENT_PAGE_NUMBER = "currentPageNumber";
    public static final String TOTAL_PAGE_COUNT = "totalPageCount";
    public static final String TOTAL_RECORDS = "totalRecords";
    public static final String NUMBER_OF_IMPORTED_RECORDS = "numberOfImportedRecords";
    public static final String SHARED_WORKSPACES = "sharedWorkspaces";
    public static final String IS_POPULATION = "isPopulation";
    public static final String INDICATOR_TYPE_ID = "indicatorTypeId";
    public static final String CAN_DO_GAP_ANALYSIS = "canDoGapAnalysis";
    public static final String DO_GAP_ANALYSIS = "gapAnalysis";
    public static final String OPTION_TO_SAVE_VALUES = "option";
    public static final String IS_MULTI_COLOR = "isMultiColor";

    public static final String FIELD_NAME = "name";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_NUMBER_OF_CLASSES = "number_of_classes";
    public static final String FIELD_UNIT = "unit";
    public static final String FIELD_ADM_LEVEL_ID = "admLevel";
    public static final String FIELD_ACCESS_TYPE_ID = "accessType";
    public static final String FIELD_CREATED_ON = "createdOn";
    public static final String FIELD_UPDATED_ON = "updatedOn";
    public static final String FIELD_CREATED_BY = "email";
    public static final String FIELD_ID = "id";
    public static final String FIELD_ZERO_CATEGORY_ENABLED = "zeroCategoryEnabled";

    public static final String PAGE = "page";
    public static final String DATA = "data";
    
    public static final Set<Long> MULTI_COLOR_PALETTES = Collections.unmodifiableSet((new HashSet<Long>() {{
        add(RED_GREEN_PALETTE_INDEX);   
        add(BLUE_PURPLE_PALETTE_INDEX);
    }}));
}
