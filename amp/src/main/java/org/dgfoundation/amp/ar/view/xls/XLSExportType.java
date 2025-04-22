package org.dgfoundation.amp.ar.view.xls;

public enum XLSExportType {
    SIMPLE_XLS_EXPORT, PLAIN_XLS_EXPORT, RICH_XLS_EXPORT;
    
    public static XLSExportType buildWithParams(boolean isPlain, boolean isRich){
        return isPlain ? PLAIN_XLS_EXPORT : (isRich ? RICH_XLS_EXPORT : SIMPLE_XLS_EXPORT); 
    }
}
