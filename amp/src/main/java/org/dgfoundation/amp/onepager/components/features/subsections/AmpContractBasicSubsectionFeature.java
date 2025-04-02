/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 */
package org.dgfoundation.amp.onepager.components.features.subsections;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.fields.AmpCategorySelectFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextAreaFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.IPAContract;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;

/**
 * @author aartimon@dginternational.org
 * @since Feb 8, 2011
 */
public class AmpContractBasicSubsectionFeature extends
        AmpSubsectionFeaturePanel<IPAContract> {
    
    private static Logger logger = Logger.getLogger(AmpContractBasicSubsectionFeature.class);

    /**
     * @param id
     * @param fmName
     * @param model
     * @throws Exception
     */
    public AmpContractBasicSubsectionFeature(String id,
            IModel<IPAContract> model, String fmName, final Label contractNameLabel){
        super(id, fmName, model, false, true);
        
        
        AmpTextFieldPanel<String> name = new AmpTextFieldPanel<String>("name", new PropertyModel<String>(model, "contractName"), "Contract Name");
        name.getTextContainer().add(new AjaxFormComponentUpdatingBehavior("onblur"){
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                target.add(contractNameLabel);
            }
        });
        name.setTextContainerDefaultMaxSize();
        name.getTextContainer().setRequired(true);
        add(name);

        AmpTextAreaFieldPanel description = new AmpTextAreaFieldPanel("description", new PropertyModel<String>(model, "description"), "Contract Description", false, false, false);
        add(description);
        
        AmpCategorySelectFieldPanel category;
        try {
            category = new AmpCategorySelectFieldPanel("category", CategoryConstants.IPA_ACTIVITY_CATEGORY_KEY, new PropertyModel<AmpCategoryValue>(model, "activityCategory"), "Activity Type", true, true);
            add(category);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        
        try {
            AmpCategorySelectFieldPanel type = new AmpCategorySelectFieldPanel("type", CategoryConstants.IPA_TYPE_KEY, new PropertyModel<AmpCategoryValue>(model, "contractType"), "Contract Type", true, true);
            add(type);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        
    }

}
