/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpFieldPanel;
import org.dgfoundation.amp.onepager.translation.TrnLabel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.FeaturesUtil;

import java.util.function.Consumer;

/**
 * Class implementing behavior for all descending feature panels. This is an
 * {@link AmpComponentPanel} similar to {@link AmpFieldPanel} but it has a
 * special html for rendering form sections (as opposed with fields) and also
 * sets the {@link AmpComponentPanel#getFMName()} to {@link AmpFMTypes#MODULE}
 * 
 * @author mpostelnicu@dgateway.org since Sep 28, 2010
 */
public abstract class AmpFeaturePanel<T> extends AmpComponentPanel<T> implements IHeaderContributor {
    /**
     * 
     */
    private static final long serialVersionUID = 2998131911444530012L;

    protected Label labelContainer;

    public AmpFeaturePanel(String id, String fmName){
        this(id, null, fmName, false);
    }

    public AmpFeaturePanel(String id, String fmName, boolean hideLabel){
        this(id, null, fmName, hideLabel);
    }

    public AmpFeaturePanel(String id, IModel<T> model, String fmName) {
        this(id, model, fmName, false);
    }

    public AmpFeaturePanel(String id, IModel<T> model, String fmName, boolean hideLabel){
        super(id, model, fmName, AmpFMTypes.MODULE);
        this.fmType = AmpFMTypes.MODULE;
        
        labelContainer = new TrnLabel("featureLabel", fmName);
        labelContainer.setVisible(!hideLabel);
        add(labelContainer);
        setOutputMarkupId(true);
    }

    public Label getLabelContainer(){
        return labelContainer;
    }
    @Override
    public void renderHead(IHeaderResponse response){
            response.render(OnDomReadyHeaderItem.forScript("Opentip.findElements();"));
    }

    protected void configureTemplate(String roleName, Consumer<AmpTemplatesVisibility> setTemplateFilter) {
        if (roleName.equals(Constants.FUNDING_AGENCY) || roleName.equals(Constants.EXECUTING_AGENCY)
                || roleName.equals(Constants.BENEFICIARY_AGENCY)) {
            AmpTemplatesVisibility currentTemplate = AmpAuthWebSession.getYourAppSession()
                    .getAmpCurrentMember()
                    .getAmpTeam()
                    .getFmTemplate();

            AmpTemplatesVisibility defaultTemplate = FeaturesUtil.getDefaultAmpTemplateVisibility();

            if (currentTemplate != null && currentTemplate.getId() != defaultTemplate.getId()) {
                setTemplateFilter.accept(currentTemplate);
            }
        }
    }
}
