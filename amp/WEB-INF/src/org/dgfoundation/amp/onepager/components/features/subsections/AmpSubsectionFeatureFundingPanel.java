package org.dgfoundation.amp.onepager.components.features.subsections;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptReferenceHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.ValidationError;
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
    public T getModelObject(){
        return (T) this.getDefaultModelObject();
    }
    public boolean getIfModelForFundingItemIsEmptyOrNull() {

        AmpFunding funding = (AmpFunding) getModel().getObject();
        return funding.getFundingDetails() == null || funding.getFundingDetails().isEmpty();
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(JavaScriptReferenceHeaderItem.forReference(QuarterInformationPanel.JS_FILE));
    }


    public AmpComponentPanel getRequiredItemValidator(String wicketId, String fmName) {
        AmpComponentPanel panel = new AmpComponentPanel(wicketId, "Required Validator for " + fmName) {
            private Label requiredAsterisk;

            @Override
            protected void onInitialize() {
                super.onInitialize();
                requiredAsterisk = getRequiredAsterisk("requiredAsterik");
                add(requiredAsterisk);
            }

            @Override
            protected void onConfigure() {
                super.onConfigure();
                boolean shouldShow = this.isVisible();
                requiredAsterisk.setVisible(shouldShow);
                requiredAsterisk.add(AttributeModifier.replace("style",
                        "color:red; font-weight:bold; margin-left:5px;" +
                                "display:" + (shouldShow ? "inline-block" : "none") + ";"));
            }
        };
        panel.setOutputMarkupId(true);
        return panel;
    }
    private Label getRequiredAsterisk(String wicketId) {
        Label requiredStar = new Label(wicketId, "*");
        requiredStar.setOutputMarkupId(true);
        return requiredStar;
    }
    public void validateIfCommitmentOrDisbursementIsRequired(AjaxRequestTarget ajaxRequestTarget) {
        AmpFunding funding = (AmpFunding) getModel().getObject();
        if (funding.getFundingDetails() == null || funding.getFundingDetails().isEmpty()) {
            error(new ValidationError("You should have atleast a single transaction added."));
            error("You should have atleast a single transaction added.");
        }
    }
}
