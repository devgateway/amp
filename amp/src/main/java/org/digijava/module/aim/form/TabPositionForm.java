package org.digijava.module.aim.form;

import org.apache.struts.action.ActionForm;

public class TabPositionForm extends ActionForm {

    private static final long serialVersionUID = 1L;
    private Long reportId;
    private Integer position;
    
    public Long getReportId() {
        return reportId;
    }
    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }
    public Integer getPosition() {
        return position;
    }
    public void setPosition(Integer position) {
        this.position = position;
    }

}
