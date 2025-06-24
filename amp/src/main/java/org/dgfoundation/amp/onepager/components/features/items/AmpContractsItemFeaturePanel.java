/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 */
package org.dgfoundation.amp.onepager.components.features.items;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.subsections.*;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.digijava.module.aim.dbentity.IPAContract;

/**
 * @author aartimon@dginternational.org
 * @since Feb 7, 2011
 */
public class AmpContractsItemFeaturePanel extends AmpFeaturePanel<IPAContract> {
    

    /**
     * @param id
     * @param fmName
     * @throws Exception
     */
    public AmpContractsItemFeaturePanel(String id, String fmName,
            IModel<IPAContract> contractModel){
        super(id, contractModel, fmName, true);
        
        IPAContract c = contractModel.getObject();
        
        if (c.getContractName() == null || c.getContractName().trim().compareTo("") == 0){
            c.setContractName(TranslatorUtil.getTranslation("New Contract"));
        }
        
        final Label contractNameLabel = new Label("contractName", new PropertyModel<String>(contractModel, "contractName"));
        contractNameLabel.setOutputMarkupId(true);
        add(contractNameLabel);

        AmpContractBasicSubsectionFeature basicInfo = new AmpContractBasicSubsectionFeature("basicInfo", contractModel, "Contract Info", contractNameLabel);
        add(basicInfo);
        
        AmpContractDetailsSubsectionFeature details = new AmpContractDetailsSubsectionFeature("details", contractModel, "Contract Details");
        add(details);

        AmpContractOrganizationsSubsectionFeature orgs = new AmpContractOrganizationsSubsectionFeature("organizations", contractModel, "Contract Organizations");
        add(orgs);
        
        AmpContractFundingAllocationSubsectionFeature fundingAlloc = new AmpContractFundingAllocationSubsectionFeature("fundAlloc", contractModel, "Funding Allocation");
        add(fundingAlloc);
        
        AmpContractDisbursementsSubsectionFeature disb = new AmpContractDisbursementsSubsectionFeature("disbursements", contractModel, "Contract Disbursements");
        add(disb);
    }

}
