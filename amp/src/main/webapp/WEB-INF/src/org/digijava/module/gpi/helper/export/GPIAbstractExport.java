package org.digijava.module.gpi.helper.export;

import java.util.Collection;
import java.util.Map;

import org.digijava.kernel.request.Site;
import org.digijava.module.gpi.helper.row.GPIReportAbstractRow;

public abstract class GPIAbstractExport {

    private Site site;
    private String langCode;
    private String currency;

    public abstract Collection generateDataSource(Collection<GPIReportAbstractRow> rows) throws Exception;

    public abstract Map<String, String> getParameters(int yearSeparator) throws Exception;

    public GPIAbstractExport(Site site, String langcode, String currency) {
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
