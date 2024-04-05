package org.digijava.kernel.ampapi.endpoints.dashboards.services;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author Octavian Ciubotaru
 */
public class HeatMap {

    private List<String> summary;

    private int yCount;

    @ApiModelProperty("Total count of entries for Y axis. Can be used to detect if \"Other\" was included.")
    private int yTotalCount;

    private int xCount;

    @ApiModelProperty("Total count of entries for X axis. Can be used to detect if \"Other\" was included.")
    private int xTotalCount;

    @ApiModelProperty("May end with \"Others\" (translated) for anything cut off")
    private Set<String> yDataSet;

    @ApiModelProperty("May end with \"Others\" (translated) for anything cut off")
    private Set<String> xDataSet;

    private Collection<Long> yDataSetIds;
    private Collection<Long> xDataSetIds;

    @ApiModelProperty("Formatted amounts for Y axis")
    private List<String> yTotals;

    @ApiModelProperty("Formatted amounts for X axis")
    private List<String> xTotals;

    @ApiModelProperty("Percentage")
    private Collection<BigDecimal> yPTotals;

    @ApiModelProperty("Percentage, 100 for each X per current rules")
    private Collection<BigDecimal> xPTotals;

    private String currency;

    @ApiModelProperty("Heat map matrix. Size: yCount by xCount.")
    private El[][] matrix;

    private String source;

    public static class El {

        @ApiModelProperty("Displayed value")
        private String dv;

        @ApiModelProperty("Percentage of amount")
        private BigDecimal p;

        public String getDv() {
            return dv;
        }

        public void setDv(String dv) {
            this.dv = dv;
        }

        public BigDecimal getP() {
            return p;
        }

        public void setP(BigDecimal p) {
            this.p = p;
        }
    }

    public El[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(El[][] matrix) {
        this.matrix = matrix;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Collection<BigDecimal> getyPTotals() {
        return yPTotals;
    }

    public void setyPTotals(Collection<BigDecimal> yPTotals) {
        this.yPTotals = yPTotals;
    }

    public Collection<BigDecimal> getxPTotals() {
        return xPTotals;
    }

    public void setxPTotals(Collection<BigDecimal> xPTotals) {
        this.xPTotals = xPTotals;
    }

    public List<String> getyTotals() {
        return yTotals;
    }

    public void setyTotals(List<String> yTotals) {
        this.yTotals = yTotals;
    }

    public List<String> getxTotals() {
        return xTotals;
    }

    public void setxTotals(List<String> xTotals) {
        this.xTotals = xTotals;
    }

    public Collection<Long> getyDataSetIds() {
        return yDataSetIds;
    }

    public void setyDataSetIds(Collection<Long> yDataSetIds) {
        this.yDataSetIds = yDataSetIds;
    }

    public Collection<Long> getxDataSetIds() {
        return xDataSetIds;
    }

    public void setxDataSetIds(Collection<Long> xDataSetIds) {
        this.xDataSetIds = xDataSetIds;
    }

    public Set<String> getyDataSet() {
        return yDataSet;
    }

    public void setyDataSet(Set<String> yDataSet) {
        this.yDataSet = yDataSet;
    }

    public Set<String> getxDataSet() {
        return xDataSet;
    }

    public void setxDataSet(Set<String> xDataSet) {
        this.xDataSet = xDataSet;
    }

    public int getyCount() {
        return yCount;
    }

    public void setyCount(int yCount) {
        this.yCount = yCount;
    }

    public int getyTotalCount() {
        return yTotalCount;
    }

    public void setyTotalCount(int yTotalCount) {
        this.yTotalCount = yTotalCount;
    }

    public int getxCount() {
        return xCount;
    }

    public void setxCount(int xCount) {
        this.xCount = xCount;
    }

    public int getxTotalCount() {
        return xTotalCount;
    }

    public void setxTotalCount(int xTotalCount) {
        this.xTotalCount = xTotalCount;
    }

    public List<String> getSummary() {
        return summary;
    }

    public void setSummary(List<String> summary) {
        this.summary = summary;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
