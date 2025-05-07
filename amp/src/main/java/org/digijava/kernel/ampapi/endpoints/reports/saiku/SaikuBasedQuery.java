package org.digijava.kernel.ampapi.endpoints.reports.saiku;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiModelProperty;
import org.dgfoundation.amp.reports.saiku.export.AMPReportExportConstants;
import org.digijava.kernel.ampapi.endpoints.reports.Reports;

import java.io.IOException;
import java.util.List;

/**
 * @author Octavian Ciubotaru
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SaikuBasedQuery implements Cloneable {

    // apparently saiku does not send this field but it is used internally for adding extra columns for colorization
    @JsonProperty("add_columns")
    private List<String> additionalColumns;

    private QueryModel queryModel;

    /**
     * Used internally to designate session reports.
     */
    @JsonIgnore
    private Boolean dinamic;

    @JsonProperty("MD5")
    @ApiModelProperty("Pagination token. Use the same token if only page or recordsPerPage changed.")
    private String md5;

    private QuerySettings querySettings;

    @JsonProperty(AMPReportExportConstants.EXCEL_TYPE_PARAM)
    @ApiModelProperty("Excel formatting")
    private Reports.ExcelType excelType;

    public List<String> getAdditionalColumns() {
        return additionalColumns;
    }

    public void setAdditionalColumns(List<String> additionalColumns) {
        this.additionalColumns = additionalColumns;
    }

    public QueryModel getQueryModel() {
        return queryModel;
    }

    public void setQueryModel(QueryModel queryModel) {
        this.queryModel = queryModel;
    }

    public Boolean getDinamic() {
        return dinamic;
    }

    public void setDinamic(Boolean dinamic) {
        this.dinamic = dinamic;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public QuerySettings getQuerySettings() {
        return querySettings;
    }

    public void setQuerySettings(QuerySettings querySettings) {
        this.querySettings = querySettings;
    }

    public Reports.ExcelType getExcelType() {
        return excelType;
    }

    public void setExcelType(Reports.ExcelType excelType) {
        this.excelType = excelType;
    }

    @Override
    public final SaikuBasedQuery clone() {
        try {
            return (SaikuBasedQuery) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException(e);
        }
    }

    public static SaikuBasedQuery fromString(String content) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            return mapper.readValue(content, SaikuBasedQuery.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Cannot deserialize: " + content, e);
        }
    }
}
