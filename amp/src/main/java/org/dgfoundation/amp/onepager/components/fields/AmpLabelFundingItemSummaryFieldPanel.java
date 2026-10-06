package org.dgfoundation.amp.onepager.components.fields;

import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.models.AmpOverallFundingModel;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.categorymanager.util.CategoryConstants;

public class AmpLabelFundingItemSummaryFieldPanel<T> extends AmpComponentPanel<T> {

    private static final long serialVersionUID = -1504307221179301769L;
    AmpLabelInformationFieldPanel actual;
    AmpLabelInformationFieldPanel planned;
    
    public AmpLabelFundingItemSummaryFieldPanel(String id, String fmName,IModel<T> model,int transactionType) {
        super(id, fmName);
        
        actual = new AmpLabelInformationFieldPanel("actual", new AmpOverallFundingModel(null,
                (IModel<AmpFunding>) model, transactionType,
                CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey()),  "Actual " + fmName);
        add(actual);
        
        planned = new AmpLabelInformationFieldPanel("planned", new AmpOverallFundingModel(null,
                (IModel<AmpFunding>) model, transactionType,
                CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey()), "Planned " + fmName);
        add(planned);
    }
}
