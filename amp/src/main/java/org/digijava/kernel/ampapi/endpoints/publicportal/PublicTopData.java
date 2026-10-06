package org.digijava.kernel.ampapi.endpoints.publicportal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author Octavian Ciubotaru
 */
@JsonInclude(NON_NULL)
public class PublicTopData {
    @ApiModelProperty(dataType = "org.digijava.kernel.ampapi.swagger.types.PublicHeadersPH")
    private final Map<String, String> headers;

    @ApiModelProperty(dataType = "org.digijava.kernel.ampapi.swagger.types.PublicTopTotalsPH")
    private final Map<String, BigDecimal> subTotals;

    @ApiModelProperty(dataType = "org.digijava.kernel.ampapi.swagger.types.PublicTopTotalsPH")
    private Map<String, BigDecimal> totals;
    @ApiModelProperty(dataType = "org.digijava.kernel.ampapi.swagger.types.PublicTopDataPH")
    private final List<Map<String, String>> data;

    @ApiModelProperty(example = "5")
    private Integer count;

    @JsonProperty("numberformat")
    @ApiModelProperty(example = "###,###,###,###")
    private final String numberFormat;

    @JsonProperty("Currency")
    @ApiModelProperty(example = "USD")
    private final String currency;

    @JsonProperty("recordsperpage")
    @ApiModelProperty(example = "10")
    private Integer recordsPerPage;
    @JsonProperty("totalpagecount")
    @ApiModelProperty(example = "20")
    private Integer totalPageCount;
    @JsonProperty("page")
    @ApiModelProperty(example = "1")
    private Integer page;

    PublicTopData(Map<String, String> headers, Map<String, BigDecimal> subTotals,
                  List<Map<String, String>> data, Integer count, String numberFormat, String currency) {
        this.headers = headers;
        this.subTotals = subTotals;
        this.data = data;
        this.count = count;
        this.numberFormat = numberFormat;
        this.currency = currency;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, BigDecimal> getSubTotals() {
        return subTotals;
    }

    public List<Map<String, String>> getData() {
        return data;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getNumberFormat() {
        return numberFormat;
    }

    public String getCurrency() {
        return currency;
    }

    public Integer getRecordsPerPage() {
        return recordsPerPage;
    }

    public void setRecordsPerPage(Integer recordsPerPage) {
        this.recordsPerPage = recordsPerPage;
    }

    public Integer getTotalPageCount() {
        return totalPageCount;
    }

    public void setTotalPageCount(Integer totalPageCount) {
        this.totalPageCount = totalPageCount;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Map<String, BigDecimal> getTotals() {
        return totals;
    }

    public void setTotals(Map<String, BigDecimal> totals) {
        this.totals = totals;
    }
}
