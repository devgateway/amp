

package org.digijava.module.widget.dbentity;

import org.digijava.module.widget.helper.WidgetVisitor;


public class AmpWidgetTopTenDonorGroups extends AmpWidget {
	private static final long serialVersionUID = 1L;

	@Override
    public void accept(WidgetVisitor visitor) {
        visitor.visit(this);
    }

}
