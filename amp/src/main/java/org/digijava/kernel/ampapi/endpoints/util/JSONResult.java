package org.digijava.kernel.ampapi.endpoints.util;


import io.swagger.annotations.ApiModelProperty;

public class JSONResult {
    private ReportMetadata metadata;
    private String mdx = "";

    @ApiModelProperty("Error message if report cannot be found or loaded.")
    private String errorMessage = "";
    
    public String getMdx() {
        return mdx;
    }

    public void setMdx(String mdx) {
        this.mdx = mdx;
    }

    public ReportMetadata getReportMetadata() {
        return metadata;
    }

    public void setReportMetadata(ReportMetadata metadata) {
        this.metadata = metadata;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }   
}
