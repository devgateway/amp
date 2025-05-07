/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 */
package org.dgfoundation.amp.onepager.components.features.subsections;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.fields.AmpCategorySelectFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpDatePickerFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.digijava.module.aim.dbentity.IPAContract;
import org.digijava.module.categorymanager.util.CategoryConstants;

import java.util.Date;

/**
 * @author aartimon@dginternational.org
 * @since Feb 8, 2011
 */
public class AmpContractDetailsSubsectionFeature extends
        AmpSubsectionFeaturePanel<IPAContract> {

    /**
     * @param id
     * @param fmName
     * @param model
     * @throws Exception
     */
    public AmpContractDetailsSubsectionFeature(String id,
            IModel<IPAContract> model, String fmName){
        super(id, fmName, model, false, true);

        try {
            AmpCategorySelectFieldPanel status = new AmpCategorySelectFieldPanel("status", CategoryConstants.IPA_STATUS_KEY, new PropertyModel(model, "status"), "Status", true, true);
            add(status);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        AmpDatePickerFieldPanel startTendering = new AmpDatePickerFieldPanel("startTendering", (IModel<Date>)new PropertyModel<Date>(model, "startOfTendering"), "Start of Tendering");
        add(startTendering);
        
        AmpDatePickerFieldPanel validity = new AmpDatePickerFieldPanel("validity", (IModel<Date>)new PropertyModel<Date>(model, "contractValidity"), "Validity");
        add(validity);
        
        AmpDatePickerFieldPanel signature = new AmpDatePickerFieldPanel("signature", (IModel<Date>)new PropertyModel<Date>(model, "signatureOfContract"), "Signature");
        add(signature);
        
        AmpDatePickerFieldPanel completion = new AmpDatePickerFieldPanel("completion", (IModel<Date>)new PropertyModel<Date>(model, "contractCompletion"), "Completion");
        add(completion);
        
        AmpTextFieldPanel<String> contractorName = new AmpTextFieldPanel<String>("contractorName", new PropertyModel<String>(model, "contractingOrganizationText"), "Contractor Name");
        contractorName.setTextContainerDefaultMaxSize();
        add(contractorName);
    }

}
