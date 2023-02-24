package org.dgfoundation.amp.reports.saiku.export;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;

public class AMPReportExportConstants {

    public static final String PDF = "pdf";
    public static final String CSV = "csv";
    public static final String XLSX = "xlsx";
    public static final String HTML = "html";
    public static final String XML = "xml";
    
    public static final String APPLICATION_TYPE = "application";
    
    public static final Map<String, MediaType> MEDIA_TYPES = Collections.unmodifiableMap(
            new HashMap<String, MediaType>() {{
        put(PDF, new MediaType(APPLICATION_TYPE, PDF));
        put(XLSX, new MediaType(APPLICATION_TYPE, XLSX));
        put(CSV, new MediaType(APPLICATION_TYPE, CSV));
        put(XML, MediaType.APPLICATION_ATOM_XML_TYPE);
    }});

    public static final String EXCEL_TYPE_PARAM = "xls_type";
}
