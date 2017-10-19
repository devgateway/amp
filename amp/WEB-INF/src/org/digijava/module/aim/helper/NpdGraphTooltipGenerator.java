package org.digijava.module.aim.helper;

import org.jfree.chart.imagemap.ToolTipTagFragmentGenerator;

public class NpdGraphTooltipGenerator implements ToolTipTagFragmentGenerator {

    
    
    public NpdGraphTooltipGenerator() {
        
    }

    public String generateToolTipFragment(String toolTipText) {
        return " title=\"" + toolTipText + "\" alt=\"\"";
    }

}
