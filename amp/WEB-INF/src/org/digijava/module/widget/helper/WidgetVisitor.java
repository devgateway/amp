
package org.digijava.module.widget.helper;

import org.digijava.module.widget.dbentity.AmpDaTable;
import org.digijava.module.widget.dbentity.AmpWidgetIndicatorChart;
import org.digijava.module.widget.dbentity.AmpWidgetOrgProfile;

/**
 *
 * @author medea
 */
public interface WidgetVisitor {
    public void visit(AmpWidgetIndicatorChart chart);
    public void visit(AmpDaTable table);
    public void visit(AmpWidgetOrgProfile org);
 
}
