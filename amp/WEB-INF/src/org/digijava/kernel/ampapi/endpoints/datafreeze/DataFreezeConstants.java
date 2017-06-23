package org.digijava.kernel.ampapi.endpoints.datafreeze;

public final class DataFreezeConstants {
    public static final String FIELD_ID = "id";
    public static final String FIELD_ENABLED = "enabled";
    public static final String FIELD_GRACE_PERIOD = "gracePeriod";
    public static final String FIELD_FREEZING_DATE = "freezingDate";
    public static final String FIELD_OPEN_PERIOD_START = "openPeriodStart";
    public static final String FIELD_OPEN_PERIOD_END = "openPeriodEnd";
    public static final String FIELD_SEND_NOTIFICATION = "sendNotification";
    public static final String FIELD_FREEZE_OPTION = "freezeOption";
    public static final String FIELD_FILTERS = "filters";
    public static final String FIELD_CID = "cid"; // client side id
    public static final String FIELD_COUNT = "count";
    public static final String FIELD_NOTIFICATION_DAYS = "notificationDays";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATA = "data";
    public static final String RESULT = "result";
    public static final String ERRORS = "errors";
    public static final String SAVE_SUCCESSFUL = "SAVE_SUCCESSFUL";
    public static final String SAVE_FAILED = "SAVE_FAILED";
    public static final Integer DEFAULT_RECORDS_PER_PAGE = 10;
    public static final Integer DEFAULT_OFFSET = 0;
    public static final String DEFAULT_SORT_COLUMN = "id";
    public static final String DEFAULT_SORT_ORDER = "desc";
    public static final String TOTAL_RECORDS = "totalRecords";
    public static final String ORDER_ASC = "asc";
    public static final String ORDER_DESC = "desc";

    private DataFreezeConstants() {
    }
}
