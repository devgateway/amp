package org.dgfoundation.amp.ar;

import org.dgfoundation.amp.nireports.testcases.drc.DRCBenchmarks;
import org.dgfoundation.amp.nireports.testcases.tanzania.TanzaniaBenchmarks;

/**
 * the entry point for all of NiReports' offline performance benchmarks
 * @author Dolghier Constantin
 *
 */
public class AllBenchmarks {
    
    public static void main(String[] args) {
        TanzaniaBenchmarks.main(args);
        DRCBenchmarks.main(args);
    }
}
