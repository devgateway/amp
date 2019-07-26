package org.dgfoundation.amp.nireports;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.function.Function;
import java.util.function.Supplier;

import org.dgfoundation.amp.algo.AlgoUtils;
import org.dgfoundation.amp.algo.ExceptionConsumer;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportEnvBuilder;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.nireports.runtime.GroupReportData;
import org.dgfoundation.amp.nireports.runtime.MultiHierarchiesTracker;
import org.dgfoundation.amp.nireports.schema.NiReportsSchema;

public class NiReportsEngineForTesting extends NiReportsEngine {
    
    public final static Supplier<ReportSpecificationImpl> EMPTY_REPORT_SPEC_SUPPLIER = () -> ReportSpecificationImpl.buildFor("dummy", new ArrayList<>(), 
            Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS), null, GroupingCriteria.GROUPING_TOTALS_ONLY);
    
    final ExceptionConsumer<NiReportsEngine> runnable;

    public NiReportsEngineForTesting(NiReportsSchema schema, Function<ReportSpecificationImpl, ReportSpecification> reportSpecSupplier, ExceptionConsumer<NiReportsEngine> runnable) {
        super(schema, reportSpecSupplier.apply(EMPTY_REPORT_SPEC_SUPPLIER.get()), ReportEnvBuilder.dummy());
        this.runnable = runnable;
    }

    public NiReportsEngineForTesting(NiReportsSchema schema, ExceptionConsumer<NiReportsEngine> runnable) {
        this(schema, i -> i, runnable);
    }
    
    @Override
    protected void runReport() {
        try {runnable.accept(this); this.rootReportData = new GroupReportData(this, null, Collections.emptyList());}
        catch(Exception e) {throw AlgoUtils.translateException(e);}
    }
}
