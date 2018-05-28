package org.dgfoundation.amp.nireports.behaviours;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.amp.AmpReportsScratchpad;
import org.dgfoundation.amp.nireports.runtime.VSplitStrategy;
import org.digijava.module.aim.dbentity.AmpCurrency;

/**
 * the behaviour of a Proposed Project Cost column
 * @author Viorel Chihai
 *
 */
public class PPCColumnBehaviour extends NumericalColumnBehaviour {
    public static PPCColumnBehaviour getInstance() { 
        return INSTANCE; 
    }
    
    private static final PPCColumnBehaviour INSTANCE = new PPCColumnBehaviour();
    
    protected PPCColumnBehaviour() { }
    
    @Override
    public List<VSplitStrategy> getSubMeasureHierarchies(NiReportsEngine context) {
        if (context != null && context.canSplittingStrategyBeAdded()) {
            AmpCurrency usedCurrency = AmpReportsScratchpad.get(context).getUsedCurrency();
                    
            VSplitStrategy byCurrency = CurrencySplittingStrategy.getInstance(usedCurrency);
            
            return Arrays.asList(byCurrency);
        }
        
        return Collections.emptyList();
    }
    
}
