package org.digijava.module.parisindicator.helper.export;

import org.digijava.kernel.request.Site;
import org.digijava.module.parisindicator.helper.row.PIReportAbstractRow;

import java.util.Collection;
import java.util.Map;

public abstract class PIAbstractExport {

    private Site site;
    private String langCode;
    private String currency;

    public abstract Collection generateDataSource(Collection<PIReportAbstractRow> rows) throws Exception;

    public abstract Map<String, String> getParameters(int yearSeparator) throws Exception;

    public PIAbstractExport(Site site, String langcode, String currency) {
        this.site = site;
        this.langCode = langcode;
        this.currency = currency;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public String getLangCode() {
        return langCode;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
