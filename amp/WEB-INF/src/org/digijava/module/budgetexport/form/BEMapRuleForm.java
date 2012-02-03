package org.digijava.module.budgetexport.form;

import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.dbentity.AmpColumns;

import java.util.List;

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
}
