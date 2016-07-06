package org.digijava.kernel.ampapi.endpoints.indicator;

import org.digijava.kernel.ampapi.endpoints.currency.CurrencyEPConstants;
import org.digijava.module.common.util.DateTimeUtil;

import java.text.SimpleDateFormat;

/**
 * Indicator Endpoint Constants
 * 
 * @author apicca
 */
public class IndicatorEPConstants {

    public static final String INSERTED = "INSERTED";
    public static final String IMPORTED = "IMPORTED";
    public static final String SAVED = "SAVED";
    public static final String DELETED = "DELETED";

    public static final String DEFAULT_INDICATOR_ORDER_FIELD = "created_on";

    public static final SimpleDateFormat DATE_FORMATTER = DateTimeUtil.getStrictSimpleDateFormat(
            CurrencyEPConstants.DATE_FORMAT);
    public static final int DEFAULT_COUNT = 10;

    public static final long ACCESS_TYPE_PUBLIC = 2;
    public static final int COLOR_RAMP_INDEX = 8; // index of last element of the colors arrays.
    public static final long PAYLOAD_INDEX = 1; // indicate

    public static final String RESULT = "result";
    public static final String ID = "id";
    public static final String LABEL = "label";
    public static final String VALUE = "value";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String NUMBER_OF_CLASSES = "numberOfClasses";
    public static final String UNIT = "unit";
    public static final String ADM_LEVEL_ID = "admLevelId";
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
    public static final String NUMBER_OF_IMPORTED_RECORDS="numberOfImportedRecords";
    public static final String SHARED_WORKSPACES="sharedWorkspaces";

    public static final String FIELD_NAME = "indicator_name";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_NUMBER_OF_CLASSES = "number_of_classes";
    public static final String FIELD_UNIT = "unit";
    public static final String FIELD_ADM_LEVEL_ID = "adm_level";
    public static final String FIELD_ACCESS_TYPE_ID = "access_type";
    public static final String FIELD_CREATED_ON = "created_on";
    public static final String FIELD_UPDATED_ON = "updated_on";
    public static final String FIELD_ID = "id";

    public static final String PAGE = "page";
    public static final String DATA = "data";
}
