package org.digijava.kernel.ampapi.endpoints.gpi;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GPIEPConstants {
    public static final String FIELD_AMOUNT = "amount";
    public static final String FIELD_DATE = "indicatorDate";
    public static final String FIELD_DONOR_ID = "donorId";
    public static final String FIELD_DONOR = "donor";
    public static final String FIELD_CURRENCY_CODE = "currencyCode";
    public static final String FIELD_CURRENCY = "currency";
    public static final String FIELD_ID = "id";
    public static final String FIELD_NOTES = "notes";
    public static final String FIELD_NOTES_DATE = "notesDate";
    public static final String FIELD_INDICATOR_CODE = "indicatorCode";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String RESULT = "result";
    public static final String DELETED = "deleted";
    public static final String SAVED = "SAVED";
    public static final String SAVE_FAILED = "SAVE_FAILED";
    public static final String DATA = "data";
    public static final String CID = "cid";
    public static final String ERRORS = "errors";
    public static final Integer DEFAULT_RECORDS_PER_PAGE = 10;
    public static final Integer DEFAULT_OFFSET = 0;
    public static final String DEFAULT_SORT_COLUMN = "ampGPINiAidOnBudgetId";
    public static final String DEFAULT_SORT_ORDER = "desc";
    public static final String TOTAL_RECORDS = "totalRecords";
    public static final String ORDER_ASC = "asc";
    public static final String ORDER_DESC = "desc"; 
    public static final String DEFAULT_DONOR_NOTES_SORT_COLUMN = "notesDate";
    public static final Integer DEFAULT_HTTPS_PORT = 443;
    public static final Integer DEFAULT_HTTP_PORT = 80;
    
    public static final Map<String, String> SORT_FIELDS = Collections.unmodifiableMap(new HashMap<String, String>() {{
            put(FIELD_AMOUNT, "amount");
            put(FIELD_DATE, "indicatorDate");
            put(FIELD_DONOR, "donor.name");
            put(FIELD_CURRENCY, "currency.currencyName"); 
            put(DEFAULT_SORT_COLUMN, "ampGPINiAidOnBudgetId");
        }});    
    
    public static final Map<String, String> DONOR_NOTES_SORT_FIELDS = Collections.unmodifiableMap(new HashMap<String, String>() {{
        put(FIELD_NOTES_DATE, "notesDate");
        put(FIELD_NOTES, "notes");
        put(FIELD_DONOR, "donor.name");
        
    }});    
}
