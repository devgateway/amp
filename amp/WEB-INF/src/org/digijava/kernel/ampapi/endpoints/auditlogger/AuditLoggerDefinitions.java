package org.digijava.kernel.ampapi.endpoints.auditlogger;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface AuditLoggerDefinitions {
    List<List<String>> getColumnDefinitions();
    void setColumnDefinitions(List<List<String>> columnDefinitions);
}
