package org.dgfoundation.amp.onepager.components.fields;

import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.models.AmpOverallFundingModel;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.categorymanager.util.CategoryConstants;

public class AmpLabelFundingMtefSummaryFieldPanel<T> extends AmpComponentPanel<T> {
    
    private static final long serialVersionUID = -6867114928999531569L;
    AmpLabelInformationFieldPanel<?> projection;
    AmpLabelInformationFieldPanel<?> pipeline;
    
    public AmpLabelFundingMtefSummaryFieldPanel(String id, String fmName, IModel<T> model, int transactionType) {
        super(id, fmName);
        
        projection = new AmpLabelInformationFieldPanel("projection", new AmpOverallFundingModel(null,
                (IModel<AmpFunding>) model, transactionType,
                CategoryConstants.MTEF_PROJECTION_PROJECTION.getValueKey()),  fmName + " Projection");

        add(projection);
        
        pipeline = new AmpLabelInformationFieldPanel("pipeline", new AmpOverallFundingModel(null,
                (IModel<AmpFunding>) model, transactionType,
                CategoryConstants.MTEF_PROJECTION_PIPELINE.getValueKey()), fmName + " Pipeline");

        add(pipeline);
    }

}
