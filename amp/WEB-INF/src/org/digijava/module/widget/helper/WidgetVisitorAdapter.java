
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
public class WidgetVisitorAdapter implements WidgetVisitor {

    public void visit(AmpWidgetIndicatorChart chart) {
       
    }

    public void visit(AmpDaTable table) {
        
    }


	@Override
	public void visit(AmpSectorTableWidget sectorTable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AmpParisIndicatorTableWidget piTable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AmpWidgetTopTenDonorGroups topTenDonorsTb) {
		// TODO Auto-generated method stub
		
	}

}
