package org.digijava.kernel.ampapi.endpoints.filetype;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FileTypesMimeMapper {
    
    public final static String FILE_TYPE_WORD = "msword";
    public final static String FILE_TYPE_EXCEL = "msexcel";
    public final static String FILE_TYPE_POWERPOINT = "mspowerpoint";
    public final static String FILE_TYPE_PROJECT = "msproject";
    public final static String FILE_TYPE_VISIO = "msvisio";
    public final static String FILE_TYPE_PDF = "pdf";
    public final static String FILE_TYPE_ARCHIVE = "zipArchive";
    public final static String FILE_TYPE_LIBREOFFICE_SHEETS = "libreofficesheets";
    public final static String FILE_TYPE_LIBREOFFICE_DOCUMENTS = "libreofficedocuments";
    public final static String FILE_TYPE_CSV = "csv";
    public final static String FILE_TYPE_PNG= "png";
    public final static String FILE_TYPE_GIF = "gif";
    public final static String FILE_TYPE_JPEG = "jpeg";
    public final static String FILE_TYPE_BMP = "bmp";
    public final static String FILE_TIFF = "tiff";
    public final static String FILE_PHOTOSHOP = "psd";
    public final static String FILE_TYPE_RTF = "rtf";
    public final static String FILE_TYPE_TXT = "txt";
    
    public static final Map<String, List<String>> FILE_MIME_TYPES = new HashMap<String, List<String>>() {{
            put(FILE_TYPE_WORD, Arrays.asList("application/msword", 
                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document", 
                    "application/vnd.openxmlformats-officedocument.wordprocessingml.template")
            );
            
            put(FILE_TYPE_EXCEL, Arrays.asList("application/vnd.ms-excel", 
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", 
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.template")
            );
            
            put(FILE_TYPE_POWERPOINT, Arrays.asList("application/vnd.ms-powerpoint", 
                    "application/vnd.openxmlformats-officedocument.presentationml.presentation", 
                    "application/vnd.openxmlformats-officedocument.presentationml.template",
                    "application/vnd.openxmlformats-officedocument.presentationml.slideshow")
            );
            
            put(FILE_TYPE_PROJECT, Arrays.asList("application/vnd.ms-project"));
            
            put(FILE_TYPE_VISIO, Arrays.asList("application/vnd.ms-visio.drawing", 
                    "application/vnd.visio")
            );
            
            put(FILE_TYPE_PDF, Arrays.asList("application/pdf"));
            
            put(FILE_TYPE_ARCHIVE, Arrays.asList("application/gzip", 
                    "application/zip", 
                    "application/x-rar-compressed",
                    "application/x-7z-compressed")
            );

            put(FILE_TYPE_TXT, Arrays.asList("text/plain"));
            
            put(FILE_TYPE_LIBREOFFICE_SHEETS, Arrays.asList("application/vnd.oasis.opendocument.spreadsheet", 
                    "application/vnd.oasis.opendocument.spreadsheet-template")
            );
            
            put(FILE_TYPE_LIBREOFFICE_DOCUMENTS, Arrays.asList("application/vnd.oasis.opendocument.text", 
                    "application/vnd.oasis.opendocument.spreadsheet-template")
            );
            
            put(FILE_TYPE_CSV, Arrays.asList("text/csv"));
            
            put(FILE_TYPE_PNG, Arrays.asList("image/png"));
            
            put(FILE_TYPE_GIF, Arrays.asList("image/gif"));
            
            put(FILE_TYPE_JPEG, Arrays.asList("image/jpeg"));
            
            put(FILE_TYPE_BMP, Arrays.asList("image/x-ms-bmp"));
            
            put(FILE_TIFF, Arrays.asList("image/tiff"));
            
            put(FILE_PHOTOSHOP, Arrays.asList("image/vnd.adobe.photoshop"));
            
            put(FILE_TYPE_RTF, Arrays.asList("application/rtf"));
    }};
}
