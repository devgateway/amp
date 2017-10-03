package org.dgfoundation.amp.nireports;

/**
 * schema-independent way of specifying a currency in NiReports. It is not really used throughout NiReports Core, as it is kept for the benefit of the schemas / developers only
 * @author Dolghier Constantin
 *
 */
public interface NiCurrency {
    public long getId();
    public String getCurrencyName();
    public String getCurrencyCode();
}
