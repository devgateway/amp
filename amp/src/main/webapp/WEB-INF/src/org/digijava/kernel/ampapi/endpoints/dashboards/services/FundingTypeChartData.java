package org.digijava.kernel.ampapi.endpoints.dashboards.services;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Octavian Ciubotaru
 */
public class FundingTypeChartData {

    @ApiModelProperty("name of the report")
    private String name;

    @ApiModelProperty("title of the report")
    private String title;

    private String source;

    private BigDecimal total;

    private String sumarizedTotal;

    private String currency;

    private List<FundingTypeAmountsForYear> values;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getSumarizedTotal() {
        return sumarizedTotal;
    }

    public void setSumarizedTotal(String sumarizedTotal) {
        this.sumarizedTotal = sumarizedTotal;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<FundingTypeAmountsForYear> getValues() {
        return values;
    }

    public void setValues(List<FundingTypeAmountsForYear> values) {
        this.values = values;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
