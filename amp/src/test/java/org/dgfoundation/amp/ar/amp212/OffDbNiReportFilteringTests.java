package org.dgfoundation.amp.ar.amp212;

/**
 * 
 * sanity checks for NiReports filtering offdb
 * 
 * @author Dolghier Constantin
 *
 */
public class OffDbNiReportFilteringTests extends FilteringSanityChecks {

    public OffDbNiReportFilteringTests() {
        inTransactionRule = null;
        nrRunReports = 0;
    }
}
