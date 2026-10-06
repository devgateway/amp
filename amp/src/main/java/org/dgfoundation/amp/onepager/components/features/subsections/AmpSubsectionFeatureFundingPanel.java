package org.dgfoundation.amp.onepager.components.features.subsections;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptReferenceHeaderItem;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.components.QuarterInformationPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpLabelFundingItemSummaryFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpLabelFundingMtefSummaryFieldPanel;
import org.dgfoundation.amp.onepager.events.OverallFundingTotalsEvents;
import org.dgfoundation.amp.onepager.events.UpdateEventBehavior;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.helper.Constants;

public class AmpSubsectionFeatureFundingPanel<T> extends AmpSubsectionFeaturePanel<T> {

    public AmpSubsectionFeatureFundingPanel(String id, String fmName, IModel<T> model,Integer transactionType) {
        this(id, fmName, model, false, false, false,transactionType);
    }

    public AmpSubsectionFeatureFundingPanel(String id, String fmName, IModel<T> model, boolean hideLabel,
            boolean hideAmountsInThousandsWarning, boolean showSummary,Integer transactionType) {
        super(id, fmName, model, hideLabel, hideAmountsInThousandsWarning, false);
        
        AmpComponentPanel<AmpFunding> c = null;
        if (transactionType == Constants.MTEFPROJECTION) {
            c = new AmpLabelFundingMtefSummaryFieldPanel<AmpFunding>("featureSummary", fmName, (IModel<AmpFunding>) model, transactionType);
        } else {
            c = new AmpLabelFundingItemSummaryFieldPanel<AmpFunding>("featureSummary", fmName, (IModel<AmpFunding>) model, transactionType);
        }
        
        c.add(UpdateEventBehavior.of(OverallFundingTotalsEvents.class));
        add(c);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(JavaScriptReferenceHeaderItem.forReference(QuarterInformationPanel.JS_FILE));
    }
}
