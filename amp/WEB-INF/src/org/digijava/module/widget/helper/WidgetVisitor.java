
package org.digijava.module.widget.helper;

import org.digijava.module.widget.dbentity.AmpDaTable;
import org.digijava.module.widget.dbentity.AmpParisIndicatorTableWidget;
import org.digijava.module.widget.dbentity.AmpSectorTableWidget;
import org.digijava.module.widget.dbentity.AmpWidgetIndicatorChart;
import org.digijava.module.widget.dbentity.AmpWidgetTopTenDonorGroups;

/**
 *
 * @author medea
 */
public interface WidgetVisitor {
    public void visit(AmpWidgetIndicatorChart chart);
    public void visit(AmpDaTable table);
    public void visit(AmpSectorTableWidget sectorTable);
    public void visit(AmpParisIndicatorTableWidget piTable);
    public void visit(AmpWidgetTopTenDonorGroups topTenDonorsTb);
 
}
