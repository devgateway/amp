package org.digijava.kernel.ampapi.endpoints.dashboards.services;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Octavian Ciubotaru
 * TODO rename to a generic name
 */
public class FundingTypeChartData {

    @ApiModelProperty("name of the report")
    private String name;

    @ApiModelProperty("title of the report")
    private String title;

    @ApiModelProperty("Measures of the report")
    private List<Measure> measures;

    private String source;

    private BigDecimal total;

    private String sumarizedTotal;

    private String currency;

    private List<FundingTypeAmountsForYear> values;

    public FundingTypeChartData() {
        measures = new ArrayList<>();
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

    public List<Measure> getMeasures() {
        return measures;
    }

    public void setMeasure(final List<Measure> measure) {
        this.measures = measure;
    }

    public void addMeasure(String original, String translated) {
        this.measures.add(new Measure(original, translated));
    }

    class Measure {
        private String original;
        private String translated;

        Measure(String original, String translated) {
            this.original = original;
            this.translated = translated;
        }

        public String getOriginal() {
            return original;
        }

        public void setOriginal(final String original) {
            this.original = original;
        }

        public String getTranslated() {
            return translated;
        }

        public void setTranslated(final String translated) {
            this.translated = translated;
        }
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
