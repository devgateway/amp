package org.dgfoundation.amp.onepager.components;

import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;

import java.util.Set;

/**
 * @author tmugo@developmentgateway.org
 * @since Apr 27 2023
 */
public class MEListEditor<T> extends ExpandableListEditor<T> {

    public MEListEditor(String id, IModel<Set<T>> model) {
        super(id, model);
    }

    @Override
    protected void onPopulateItem(ListItem<T> item) {
        boolean itemEnabled = item.isEnabled();
        boolean fmMode = ((AmpAuthWebSession) getSession()).isFmMode();

        AmpIndicator indicator = (AmpIndicator) item.getModel().getObject();
        item.setEnabled(itemEnabled);
    }

    public boolean isExpandable() {
        return true;
    }
}
