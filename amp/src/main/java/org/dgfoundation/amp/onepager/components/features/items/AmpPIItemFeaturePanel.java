/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 */
package org.dgfoundation.amp.onepager.components.features.items;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.AmpSearchOrganizationComponent;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.sections.AmpPIFormSectionFeature;
import org.dgfoundation.amp.onepager.components.fields.AmpAddLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpSelectFieldPanel;
import org.dgfoundation.amp.onepager.models.AmpOrganisationSearchModel;
import org.dgfoundation.amp.onepager.models.DateToYearModel;
import org.dgfoundation.amp.onepager.models.PersistentObjectModel;
import org.dgfoundation.amp.onepager.translation.TrnLabel;
import org.dgfoundation.amp.onepager.yui.AmpAutocompleteFieldPanel;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.util.DbUtil;

import java.util.*;

/**
 * @author aartimon@dginternational.org
 * @since Mar 30, 2011
 */
public class AmpPIItemFeaturePanel extends AmpFeaturePanel<AmpAhsurvey> {
    private static final List<String> CHOICE_LIST = new ArrayList<String>();
    private static final int YEAR_RANGE = 5;

    static {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        for (int i = year-YEAR_RANGE; i<=year+YEAR_RANGE; i++){
            CHOICE_LIST.add(String.valueOf(i));
        }
    }

    public AmpPIItemFeaturePanel(String id, String fmName, final IModel<AmpAhsurvey> survey,
                                 final IModel<AmpOrganisation> surveyOrg, final IModel<AmpActivityVersion> am){
        super(id, survey, fmName, true);
        if (survey.getObject().getResponses() == null)
            survey.getObject().setResponses(new HashSet<AmpAhsurveyResponse>());

        if (survey.getObject().getPointOfDeliveryDonor() == null)
            survey.getObject().setPointOfDeliveryDonor(survey.getObject().getAmpDonorOrgId());
        Label indicatorNameLabel = new Label("orgName", surveyOrg);
        add(indicatorNameLabel);

        PropertyModel<Date> surveyPropertyModel = new PropertyModel<Date>(survey, "surveyDate");
        AmpSelectFieldPanel<String> yearSelector = new AmpSelectFieldPanel<String>("yearSelect",
                new DateToYearModel(surveyPropertyModel), CHOICE_LIST, "Year Select", true, true,null,true);
        if (surveyPropertyModel.getObject() != null){
            yearSelector.setIgnorePermissions(true);
            yearSelector.setEnabled(false);
        }
        add(yearSelector);

        AmpLinkField newItem = new AmpAddLinkField("addNewItem", "Add new item") {
            @Override
            protected void onClick(AjaxRequestTarget target) {
                AmpAhsurvey as = new AmpAhsurvey();
                as.setAmpActivityId(am.getObject());
                as.setAmpDonorOrgId(surveyOrg.getObject());
                am.getObject().getSurvey().add(as);
                AmpPIFormSectionFeature parent = this.findParent(AmpPIFormSectionFeature.class);
                target.add(parent);
                target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(parent));
            }
        };
        add(newItem);

        AmpLinkField deleteItem = new AmpDeleteLinkField("deleteItem", "Delete Item") {
            @Override
            protected void onClick(AjaxRequestTarget target) {
                Set<AmpAhsurvey> surveySet = am.getObject().getSurvey();
                for (Iterator<AmpAhsurvey> iterator = surveySet.iterator(); iterator.hasNext(); ) {
                    AmpAhsurvey ahsurvey = iterator.next();
                    if (ahsurvey.equals(survey.getObject())){
                        iterator.remove();
                        break;
                    }
                }
                AmpPIFormSectionFeature parent = this.findParent(AmpPIFormSectionFeature.class);
                target.add(parent);
                target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(parent));
            }
        };
        add(deleteItem);

        final Label pod = new Label("PoD", new PropertyModel<String>(survey, "pointOfDeliveryDonor.name"));
        pod.setOutputMarkupId(true);
        add(pod);
        final AmpAutocompleteFieldPanel<AmpOrganisation> searchOrgs=new AmpAutocompleteFieldPanel<AmpOrganisation>("searchAutocomplete","Search Organizations",true,true,AmpOrganisationSearchModel.class,id) {         
            @Override
            protected String getChoiceValue(AmpOrganisation choice) {
                return DbUtil.filter(choice.getName());
            }
            
            @Override
            protected boolean showAcronyms() {
                return true;
            }
            
            @Override
            protected String getAcronym(AmpOrganisation choice) {
                return choice.getAcronym();
            }

            @Override
            public void onSelect(AjaxRequestTarget target, AmpOrganisation choice) {
                survey.getObject().setPointOfDeliveryDonor(choice);
                target.add(pod);
            }

            @Override
            public Integer getChoiceLevel(AmpOrganisation choice) {
                return null;
            }
        };
        AmpSearchOrganizationComponent<String> searchOrganization = new AmpSearchOrganizationComponent<String>("orgSearch", new Model<String> (),
                "Search Organizations", searchOrgs, null);
        add(searchOrganization);

        
        final AbstractReadOnlyModel<List<AmpAhsurveyIndicator>> listModel = new AbstractReadOnlyModel<List<AmpAhsurveyIndicator>>() {
            private static final long serialVersionUID = 3706184421459839210L;
            @Override
            public List<AmpAhsurveyIndicator> getObject() {
                ArrayList<AmpAhsurveyIndicator> list = new ArrayList<AmpAhsurveyIndicator>(DbUtil.getAllAhSurveyIndicators());
                Collections.sort(list, new AmpAhsurveyIndicator.AhsurveyIndicatorComparator());
                return list;
            }
        };

        ListView<AmpAhsurveyIndicator> list = new ListView<AmpAhsurveyIndicator>("list", listModel) {
            @Override
            protected void populateItem(final ListItem<AmpAhsurveyIndicator> item) {
                AmpAhsurveyIndicator sv = item.getModelObject();
                
                Label indCode = new Label("indCode", new PropertyModel<String>(sv, "indicatorCode"));
                item.add(indCode);
                Label indName = new TrnLabel("indName", new PropertyModel<String>(sv, "name"));
                item.add(indName);

                String code = sv.getIndicatorCode();
                if (code.compareTo("7") == 0){
                    String msg = "No question here. This indicator is calculated by the system based on information entered for disbursements for this project/programme";
                    Label l = new TrnLabel("qList", new Model<String>(msg));
                    item.add(l);
                } else 
                    if (code.compareTo("10a") == 0){
                        String msg = "No question at the activity level; this indicator is calculated using the Calendar Module";
                        Label l = new TrnLabel("qList", new Model<String>(msg));
                        item.add(l);
                    } else 
                        if (code.compareTo("10b") == 0){
                            String msg = "No question at the activity level; this indicator is calculated using the Document Management Module";
                            Label l = new TrnLabel("qList", new Model<String>(msg));
                            item.add(l);
                        } else 
                            if (code.compareTo("10b") == 0){
                                String msg = "No question at the activity level; this indicator is calculated using the Document Management Module";
                                Label l = new TrnLabel("qList", new Model<String>(msg));
                                item.add(l);
                            } else {
                                AmpPIQuestionItemFeaturePanel q = new AmpPIQuestionItemFeaturePanel("qList", "PI Questions List", PersistentObjectModel.getModel(sv), survey);
                                item.add(q);
                            }
            }
        };
        list.setReuseItems(true);
        add(list);
    }

}
