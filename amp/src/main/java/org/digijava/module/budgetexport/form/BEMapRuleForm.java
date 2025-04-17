package org.digijava.module.budgetexport.form;

import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.dbentity.AmpColumns;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: flyer
 * Date: 2/2/12
 * Time: 5:04 PM
 */
public class BEMapRuleForm extends ActionForm {
    private Long id;
    private Long curProjectId;
    private String name;
    private boolean header;
    private Long ampColumnId;
    private List<AmpColumns> availColumns;
    private int csvColDelimiter;

    private boolean allowNone;
    private boolean allowAll;
    private String dataRetrieverClass;
    private Set<Map.Entry<String, String>> availRetrieverClasses;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCurProjectId() {
        return curProjectId;
    }

    public void setCurProjectId(Long curProjectId) {
        this.curProjectId = curProjectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHeader() {
        return header;
    }

    public void setHeader(boolean header) {
        this.header = header;
    }

    public Long getAmpColumnId() {
        return ampColumnId;
    }

    public void setAmpColumnId(Long ampColumnId) {
        this.ampColumnId = ampColumnId;
    }

    public List<AmpColumns> getAvailColumns() {
        return availColumns;
    }

    public void setAvailColumns(List<AmpColumns> availColumns) {
        this.availColumns = availColumns;
    }

    public int getCsvColDelimiter() {
        return csvColDelimiter;
    }

    public void setCsvColDelimiter(int csvColDelimiter) {
        this.csvColDelimiter = csvColDelimiter;
    }

    public boolean isAllowNone() {
        return allowNone;
    }

    public void setAllowNone(boolean allowNone) {
        this.allowNone = allowNone;
    }

    public boolean isAllowAll() {
        return allowAll;
    }

    public void setAllowAll(boolean allowAll) {
        this.allowAll = allowAll;
    }

    public String getDataRetrieverClass() {
        return dataRetrieverClass;
    }

    public void setDataRetrieverClass(String dataRetrieverClass) {
        this.dataRetrieverClass = dataRetrieverClass;
    }

    public Set<Map.Entry<String, String>> getAvailRetrieverClasses() {
        return availRetrieverClasses;
    }

    public void setAvailRetrieverClasses(Set<Map.Entry<String, String>> availRetrieverClasses) {
        this.availRetrieverClasses = availRetrieverClasses;
    }
}
