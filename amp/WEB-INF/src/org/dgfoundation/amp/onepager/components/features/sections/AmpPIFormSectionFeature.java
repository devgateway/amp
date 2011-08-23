/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.sections;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.features.items.AmpPIItemFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.dgfoundation.amp.onepager.models.PersistentObjectModel;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpAhsurvey;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.SurveyFunding;

/**
 * Paris Indicators section
 * @author aartimon@dginternational.org
 * @since Mar 29, 2011 
 */
public class AmpPIFormSectionFeature extends AmpFormSectionFeaturePanel {
	public AmpPIFormSectionFeature(String id, String fmName,
			final IModel<AmpActivityVersion> am) throws Exception {
		super(id, fmName, am);
		
		updateSurveySet(am);
		final AbstractReadOnlyModel<List<AmpAhsurvey>> listModel = OnePagerUtil 
				.getReadOnlyListModelFromSetModel(new PropertyModel(am, "survey"));
		
		final ListView<AmpAhsurvey> list = new ListView<AmpAhsurvey>("list", listModel) {
			@Override
			protected void populateItem(final ListItem<AmpAhsurvey> item) {
				AmpPIItemFeaturePanel indicator = new AmpPIItemFeaturePanel("item", "PI Item", PersistentObjectModel.getModel(item.getModelObject()), PersistentObjectModel.getModel(item.getModelObject().getAmpDonorOrgId()));
				item.add(indicator);
			}
		};
		list.setReuseItems(true);
		list.setOutputMarkupId(true);
		add(list);
		
		AmpAjaxLinkField addbutton = new AmpAjaxLinkField("updateSurveys", "Update Available Surveys", "Update Available Surveys") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				updateSurveySet(am);
				list.removeAll();

				target.addComponent(list.getParent());
				target.appendJavascript(OnePagerConst.getToggleChildrenJS(AmpPIFormSectionFeature.this));
			}
		};
		add(addbutton);
	}

	private void updateSurveySet(IModel<AmpActivityVersion> am) {
		Comparator sfComp = new Comparator() {
            public int compare(Object o1, Object o2) {
                SurveyFunding sf1 = (SurveyFunding) o1;
                SurveyFunding sf2 = (SurveyFunding) o2;
                return sf1.getFundingOrgName().trim().toLowerCase().compareTo(sf2.getFundingOrgName().trim().toLowerCase());
            }
        };
        IModel<Set<AmpAhsurvey>> surveys = new PropertyModel<Set<AmpAhsurvey>>(am, "survey");
        
        if (surveys.getObject() == null)
        	surveys.setObject(new HashSet<AmpAhsurvey>());
        
        if (am.getObject().getFunding() == null)
        	am.getObject().setFunding(new HashSet());
        Set<AmpFunding> fundings = am.getObject().getFunding();
        
        
        Set<AmpOrganisation> piCertOrgs = new HashSet<AmpOrganisation>();
        
        Iterator<AmpFunding> it = fundings.iterator();
        while (it.hasNext()) {
			AmpFunding fund = (AmpFunding) it.next();
			AmpOrganisation auxOrg = fund.getAmpDonorOrgId();
			
			//check to see if funding org is Bilateral or Multilateral 
			if(auxOrg!=null && auxOrg.getOrgGrpId()!=null &&auxOrg.getOrgGrpId().getOrgType()!=null)
    		if("BIL".equalsIgnoreCase(auxOrg.getOrgGrpId().getOrgType().getOrgTypeCode()) || 
    				"MUL".equalsIgnoreCase(auxOrg.getOrgGrpId().getOrgType().getOrgTypeCode()) ) {
    			
    			//check to see if actual fundings exist
    			if (fund.getFundingDetails() != null && !fund.getFundingDetails().isEmpty()){
    				
    				Iterator<AmpFundingDetail> it2 = fund.getFundingDetails().iterator();
    				while (it2.hasNext()) {
						AmpFundingDetail fd = (AmpFundingDetail) it2.next();
						if (fd.getTransactionType() == Constants.DISBURSEMENT){
							piCertOrgs.add(auxOrg);
							break;
						}
					}
    			}
    		}
		}
        
        
        //removing survey's that are not in the funding list any more
        Iterator<AmpAhsurvey> it2 = surveys.getObject().iterator();
        ArrayList<AmpAhsurvey> deleteSurveysList = new ArrayList<AmpAhsurvey>();
        ArrayList<AmpOrganisation> existingSurveyOrgs = new ArrayList<AmpOrganisation>();
        while (it2.hasNext()) {
			AmpAhsurvey surv = (AmpAhsurvey) it2.next();
			AmpOrganisation org = surv.getAmpDonorOrgId();
			
			if (!piCertOrgs.contains(org))
				deleteSurveysList.add(surv);
			else
				existingSurveyOrgs.add(org);
		}
        surveys.getObject().removeAll(deleteSurveysList);
        
        //adding non-existing surveys to the list
        Iterator<AmpOrganisation> it3 = piCertOrgs.iterator();
        while (it3.hasNext()) {
			AmpOrganisation org = (AmpOrganisation) it3.next();
			
			if (!existingSurveyOrgs.contains(org)){
				AmpAhsurvey as = new AmpAhsurvey();
				as.setAmpActivityId(am.getObject());
				as.setAmpDonorOrgId(org);
				surveys.getObject().add(as);
			}
		}
	}
	
}
