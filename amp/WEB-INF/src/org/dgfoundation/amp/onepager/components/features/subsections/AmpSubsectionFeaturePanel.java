/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components.features.subsections;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * @author mpostelnicu@dgateway.org
 * since Nov 4, 2010
 */
public abstract class AmpSubsectionFeaturePanel<T> extends AmpFeaturePanel<T> {

    private TransparentWebMarkupContainer slider;
    private Label requiredAsterisk;

    /**
     * @param id
     * @param fmName
     * @throws Exception
     */
    public AmpSubsectionFeaturePanel(String id, String fmName){
        this(id, fmName,null);

    }

    /**
     * @param id
     * @param fmName
     * @param model
     * @throws Exception
     */
    public AmpSubsectionFeaturePanel(String id, String fmName, IModel<T> model){
        this(id, fmName, model, false);
    }

    public AmpSubsectionFeaturePanel(String id, String fmName, IModel<T> model, boolean hideLabel){
        this(id, fmName, model , hideLabel, false);
    }
    public AmpSubsectionFeaturePanel(String id, String fmName, IModel<T> model, boolean hideLabel, boolean hideAmountsInThousandsWarning) {
        this(id, fmName,  model, hideLabel, hideAmountsInThousandsWarning,true);
    }

    public AmpSubsectionFeaturePanel(String id, String fmName, IModel<T> model, boolean hideLabel, boolean hideAmountsInThousandsWarning,boolean showSummary) {
        super(id, model,fmName, hideLabel);
        Model<String> labelText = new Model<String>();
        TransparentWebMarkupContainer fundingItemsContainer = new TransparentWebMarkupContainer("fundingItemsContainer");
        fundingItemsContainer.setOutputMarkupId(true);
        add(fundingItemsContainer);
        Label amountsInThousandsLabel = new Label("amountsInThousands", labelText);
        amountsInThousandsLabel.setVisibilityAllowed(false);
        fundingItemsContainer.add(amountsInThousandsLabel);

        String amountsInThousandsStr = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS);
        if (amountsInThousandsStr != null){
            int atVal = Integer.parseInt(amountsInThousandsStr);
            if (atVal == AmpARFilter.AMOUNT_OPTION_IN_THOUSANDS){
                labelText.setObject(TranslatorUtil.getTranslation("Please enter amounts in thousands (000)"));
                amountsInThousandsLabel.setVisibilityAllowed(true);
            }
            else
                if (atVal == AmpARFilter.AMOUNT_OPTION_IN_MILLIONS){
                    labelText.setObject(TranslatorUtil.getTranslation("Please enter amounts in millions (000 000)"));
                    amountsInThousandsLabel.setVisibilityAllowed(true);
                }
        }

        amountsInThousandsLabel.setVisibilityAllowed(!hideAmountsInThousandsWarning);

        slider = new TransparentWebMarkupContainer("slider");
        slider.setOutputMarkupId(true);
        fundingItemsContainer.add(slider);
        requiredAsterisk = getRequiredAsterisk("requiredAsterisk");
        requiredAsterisk.add(new AttributeModifier("style", "display:none;"));
        fundingItemsContainer.add(requiredAsterisk);
        if (showSummary) {
            TransparentWebMarkupContainer summary = new TransparentWebMarkupContainer("featureSummary");
            fundingItemsContainer.add(summary);
        }
    }
    public AmpComponentPanel getRequiredItemValidator(String wicketId, String fmName) {
        AmpComponentPanel panel = new AmpComponentPanel(wicketId, fmName +" validator") {

            @Override
            protected void onConfigure() {
                super.onConfigure();
                boolean shouldShow = this.isVisible();
                this.setMarkupId("dummy");
                if (shouldShow) {
                    this.setMarkupId(fmName);
                }
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

    public TransparentWebMarkupContainer getSlider() {
        return slider;
    }


}
