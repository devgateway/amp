/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.sections;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.features.items.AmpPIItemFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.dgfoundation.amp.onepager.helper.PIFormSectionSurveyComparator;
import org.dgfoundation.amp.onepager.models.PersistentObjectModel;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.helper.Constants;

import java.util.*;

/**
 * Paris Indicators section
 * @author aartimon@dginternational.org
 * @since Mar 29, 2011 
 */
public class AmpPIFormSectionFeature extends AmpFormSectionFeaturePanel {
    private transient static final Comparator<AmpAhsurvey> SURVEY_COMPARATOR = new PIFormSectionSurveyComparator();

    public AmpPIFormSectionFeature(String id, String fmName,
            final IModel<AmpActivityVersion> am) throws Exception {
        super(id, fmName, am);
        
        updateSurveySet(am);
        final AbstractReadOnlyModel<List<AmpAhsurvey>> listModel = OnePagerUtil
                .getReadOnlyListModelFromSetModel(new PropertyModel<Set<AmpAhsurvey>>(am, "survey"), SURVEY_COMPARATOR);

        final ListView<AmpAhsurvey> list = new ListView<AmpAhsurvey>("list", listModel) {
            @Override
            protected void populateItem(final ListItem<AmpAhsurvey> item) {
                AmpPIItemFeaturePanel indicator = new AmpPIItemFeaturePanel("item", "PI Item",
                        PersistentObjectModel.getModel(item.getModelObject()), PersistentObjectModel.getModel(item.getModelObject().getAmpDonorOrgId()),
                        am);
                item.add(indicator);
            }
        };
        //list.setReuseItems(true);
        list.setOutputMarkupId(true);
        add(list);
        
        AmpAjaxLinkField addButton = new AmpAjaxLinkField("updateSurveys", "Update Available Surveys", "Update Available Surveys") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                updateSurveySet(am);
                list.removeAll();

                target.add(list.getParent());
                target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(AmpPIFormSectionFeature.this));
            }
        };
        add(addButton);
    }

    private void updateSurveySet(IModel<AmpActivityVersion> am) {
        /*Comparator sfComp = new Comparator() {
            public int compare(Object o1, Object o2) {
                SurveyFunding sf1 = (SurveyFunding) o1;
                SurveyFunding sf2 = (SurveyFunding) o2;
                return sf1.getFundingOrgName().trim().toLowerCase().compareTo(sf2.getFundingOrgName().trim().toLowerCase());
            }
        };*/
        IModel<Set<AmpAhsurvey>> surveys = new PropertyModel<Set<AmpAhsurvey>>(am, "survey");
        
        if (surveys.getObject() == null)
            surveys.setObject(new TreeSet<AmpAhsurvey>());
        
        if (am.getObject().getFunding() == null)
            am.getObject().setFunding(new HashSet());
        Set<AmpFunding> fundingSet = am.getObject().getFunding();
        
        Set<AmpOrganisation> piCertOrgs = new HashSet<AmpOrganisation>();
        Iterator<AmpFunding> it = fundingSet.iterator();
        while (it.hasNext()) {
            AmpFunding fund = it.next();
            AmpOrganisation auxOrg = fund.getAmpDonorOrgId();
            
            //check to see if funding org is Bilateral or Multilateral 
            if(auxOrg!=null && auxOrg.getOrgGrpId()!=null &&auxOrg.getOrgGrpId().getOrgType()!=null)
            if("BIL".equalsIgnoreCase(auxOrg.getOrgGrpId().getOrgType().getOrgTypeCode()) || 
                    "MUL".equalsIgnoreCase(auxOrg.getOrgGrpId().getOrgType().getOrgTypeCode()) ) {
                
                //check to see if actual funding exist
                if (fund.getFundingDetails() != null && !fund.getFundingDetails().isEmpty()){
                    
                    Iterator<AmpFundingDetail> it2 = fund.getFundingDetails().iterator();
                    while (it2.hasNext()) {
                        AmpFundingDetail fd = it2.next();
                        if (fd.getTransactionType() == Constants.DISBURSEMENT){
                            piCertOrgs.add(auxOrg);
                            break;
                        }
                    }
                }
            }
        }

        //removing surveys that are not in the funding list any more
        Iterator<AmpAhsurvey> it2 = surveys.getObject().iterator();
        ArrayList<AmpAhsurvey> deleteSurveysList = new ArrayList<AmpAhsurvey>();
        ArrayList<AmpOrganisation> existingSurveyOrgs = new ArrayList<AmpOrganisation>();
        while (it2.hasNext()) {
            AmpAhsurvey survey = it2.next();
            AmpOrganisation org = survey.getAmpDonorOrgId();
            
            if (!piCertOrgs.contains(org))
                deleteSurveysList.add(survey);
            else
                existingSurveyOrgs.add(org);
        }
        surveys.getObject().removeAll(deleteSurveysList);
        
        //adding non-existing surveys to the list
        Iterator<AmpOrganisation> it3 = piCertOrgs.iterator();
        while (it3.hasNext()) {
            AmpOrganisation org = it3.next();
            
            if (!existingSurveyOrgs.contains(org)){
                AmpAhsurvey as = new AmpAhsurvey();
                as.setAmpActivityId(am.getObject());
                as.setAmpDonorOrgId(org);
                surveys.getObject().add(as);
            }
        }
    }
    
}
