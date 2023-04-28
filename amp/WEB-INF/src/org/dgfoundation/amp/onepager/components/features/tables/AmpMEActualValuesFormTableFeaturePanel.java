package org.dgfoundation.amp.onepager.components.features.tables;

import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.components.ListItem;
import org.dgfoundation.amp.onepager.components.MEListEditor;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;

/**
 * @author tmugo@developmentgateway.org
 * since Apr 27 2023
 * */
public class AmpMEActualValuesFormTableFeaturePanel extends AmpMEValuesFormTableFeaturePanel {
    public AmpMEActualValuesFormTableFeaturePanel(String id, IModel<AmpIndicator> model, String fmName, boolean hideLeadingNewLine, int titleHeaderColSpan) throws Exception {
        super(id, model, fmName, hideLeadingNewLine, 7);
        try {
            list = new MEListEditor<AmpIndicatorValue>("listIndicators", setModel) {
                @Override
                protected void onPopulateItem(ListItem<AmpIndicatorValue> item) {
                    super.onPopulateItem(item);
                    appendActualValueToItem(item);
                }
            };

            add(list);
            addExpandableList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }



    }
}
