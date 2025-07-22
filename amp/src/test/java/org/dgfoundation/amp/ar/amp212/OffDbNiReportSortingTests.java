package org.dgfoundation.amp.ar.amp212;

/**
 * 
 * sanity checks for NiReports running offdb
 * 
 * @author Alexandru Cartaleanu
 *
 */
public class OffDbNiReportSortingTests extends SortingSanityChecks {

    public OffDbNiReportSortingTests() {
        inTransactionRule = null;
        nrRunReports = 0;
    }
}
