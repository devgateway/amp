package org.digijava.kernel.ampapi.endpoints.dashboards.services;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Octavian Ciubotaru
 */
public class TopChartData {

    private List<TopChartAmount> values;

    private Double total;

    private String sumarizedTotal;

    private String currency;

    private Integer maxLimit;

    private BigDecimal totalPositive;

    private String name;

    private String title;

    private String source;

    public List<TopChartAmount> getValues() {
        return values;
    }

    public void setValues(List<TopChartAmount> values) {
        this.values = values;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getSumarizedTotal() {
        return sumarizedTotal;
    }

    public void setSumarizedTotal(String sumarizedTotal) {
        this.sumarizedTotal = sumarizedTotal;
    }

    public Integer getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(Integer maxLimit) {
        this.maxLimit = maxLimit;
    }

    public BigDecimal getTotalPositive() {
        return totalPositive;
    }

    public void setTotalPositive(BigDecimal totalPositive) {
        this.totalPositive = totalPositive;
    }

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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
