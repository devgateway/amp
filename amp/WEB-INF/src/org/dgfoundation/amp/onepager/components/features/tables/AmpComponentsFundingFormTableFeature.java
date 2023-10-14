/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.tables;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.components.AmpFundingAmountComponent;
import org.dgfoundation.amp.onepager.components.ListEditor;
import org.dgfoundation.amp.onepager.components.ListEditorRemoveButton;
import org.dgfoundation.amp.onepager.components.fields.*;
import org.dgfoundation.amp.onepager.events.ContactChangedEvent;
import org.dgfoundation.amp.onepager.events.FundingOrgListUpdateEvent;
import org.dgfoundation.amp.onepager.events.UpdateEventBehavior;
import org.dgfoundation.amp.onepager.models.AbstractMixedSetModel;
import org.dgfoundation.amp.onepager.models.AmpRelatedOrgsModel;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpComponentFunding;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;

import java.util.*;

import static org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants.*;

/**
 * @author aartimon@dginternational.org 
 * @since Nov 25, 2010
 */
public class AmpComponentsFundingFormTableFeature extends
        AmpFormTableFeaturePanel {
    private final ListEditor<AmpComponentFunding> editorList;

    /**
     */
    public AmpComponentsFundingFormTableFeature(String id,
            final IModel<AmpComponent> componentModel,
            final IModel<Set<AmpComponentFunding>> compFundsModel, 
            final IModel<AmpActivityVersion> activityModel, String fmName,
            final int transactionType) throws Exception {
        super(id, activityModel, fmName);
        setTitleHeaderColSpan(6);

        AbstractMixedSetModel<AmpComponentFunding> listModel = new AbstractMixedSetModel<AmpComponentFunding>(compFundsModel) {
            @Override
            public boolean condition(AmpComponentFunding item) {
                return (item.getTransactionType().equals(transactionType) &&
                        item.getComponent().hashCode() == componentModel.getObject().hashCode());
            }
        };

        editorList = new ListEditor<AmpComponentFunding>("list", listModel){
            @Override
            protected void onPopulateItem(org.dgfoundation.amp.onepager.components.ListItem<AmpComponentFunding> item) {
                IModel<AmpComponentFunding> model = item.getModel();
                if (model.getObject().getComponentFundingDocuments() == null)
                    model.getObject().getComponentFundingDocuments().addAll(new HashSet<>());
                if (getSession().getMetaData(OnePagerConst.COMPONENT_FUNDING_NEW_ITEMS)== null)
                    getSession().setMetaData(OnePagerConst.COMPONENT_FUNDING_NEW_ITEMS,new HashMap<>());
                if (getSession().getMetaData(OnePagerConst.COMPONENT_FUNDING_DELETED_ITEMS) == null)
                    getSession().setMetaData(OnePagerConst.COMPONENT_FUNDING_DELETED_ITEMS,  new HashMap<>());
                if (getSession().getMetaData(OnePagerConst.COMPONENT_FUNDING_EXISTING_ITEM_TITLES) == null)
                    getSession().setMetaData(OnePagerConst.COMPONENT_FUNDING_EXISTING_ITEM_TITLES,  new HashMap<>());
                try{
                    AmpCategorySelectFieldPanel adjustmentTypes = new AmpCategorySelectFieldPanel(
                            "adjustmentType", CategoryConstants.ADJUSTMENT_TYPE_KEY,
                            new PropertyModel<>(model, "adjustmentType"),
                            COMPONENT_FUNDING_ADJUSTMENT_TYPE, //fmname
                            false, false, false, null, false);
                    adjustmentTypes.getChoiceContainer().setRequired(true);
                    adjustmentTypes.getChoiceContainer().add(new AttributeModifier("style", "width: 100px;"));
                    item.add(adjustmentTypes);

                } catch(Exception e) {
                    logger.info("Unable to add adjustment type dropdown: ",e);
                }
                try {
                     AmpTextAreaFieldPanel rejectReason = new AmpTextAreaFieldPanel("componentRejectReason",  new PropertyModel<>(model, "componentRejectReason"), "Reject Reason", false, false, false);
                    rejectReason.setOutputMarkupId(true);
                    rejectReason.setVisible(false);
                    rejectReason.add(new AttributeModifier("style", "display: none;"));
                    item.add(rejectReason);

                    AmpCategorySelectFieldPanel componentFundingStatus = new AmpCategorySelectFieldPanel(
                            "componentFundingStatus", CategoryConstants.COMPONENT_FUNDING_STATUS_KEY,
                            new PropertyModel<>(model, "componentFundingStatus"),
                            COMPONENT_FUNDING_STATUS, //fmname
                            false, false, false, null, false)
                            ;
                    componentFundingStatus.getChoiceContainer().add(new AjaxFormComponentUpdatingBehavior("onchange") {
                        @Override
                        protected void onUpdate(AjaxRequestTarget target) {
                            String selectedValue = model.getObject().getComponentFundingStatus().getValue();
                            logger.info("Selected Status: "+selectedValue);
                            if ("rejected".equalsIgnoreCase(selectedValue)) {
                                target.appendJavaScript("$('#" + rejectReason.getMarkupId() + "').show();");
                            } else {
                                target.appendJavaScript("$('#" + rejectReason.getMarkupId() + "').hide();");
                            }


                        }
                    });
                    componentFundingStatus.getChoiceContainer().setRequired(true);
                    componentFundingStatus.getChoiceContainer().add(new AttributeModifier("style", "width: 100px;"));
                    item.add(componentFundingStatus);
                } catch (Exception e)
                {
                    logger.info("Unable to add component funding status dropdown: ",e);
                }
                try {
                    final AmpComponentFundingResourcesTableFeature resourcesList =
                            new AmpComponentFundingResourcesTableFeature("componentFundingDocuments", "Component Funding Documents", model);
                    item.add(resourcesList);

                    final AmpComponentFundingNewResourceFieldPanel newDoc =
                        new AmpComponentFundingNewResourceFieldPanel("addNewComponentFundingDocument", model, "Add New Document", resourcesList);
                newDoc.setOutputMarkupId(true);
                item.add(newDoc);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

//

                // read the list of organizations from related organizations page, and
                // create a unique set with the orgs chosen
                AbstractReadOnlyModel<List<AmpOrganisation>> orgsList = new AmpRelatedOrgsModel(activityModel, null, true);


                // selector for related orgs
                AmpSelectFieldPanel<AmpOrganisation> orgSelect = buildSelectFieldPanel("orgSelect",
                        COMPONENT_ORGANIZATION, "reportingOrganization",
                        model, orgsList);
                item.add(orgSelect);

                // selector for second related orgs
                AmpSelectFieldPanel<AmpOrganisation> secondOrgSelect = buildSelectFieldPanel("secondOrgSelect",
                        COMPONENT_SECOND_REPORTING_ORGANIZATION, "componentSecondResponsibleOrganization",
                        model, orgsList);
                item.add(secondOrgSelect);

                AmpFundingAmountComponent amountComponent = new AmpFundingAmountComponent<>("fundingAmount",
                        model, COMPONENT_FUNDING_AMOUNT, "transactionAmount", COMPONENT_FUNDING_CURRENCY,
                        "currency", COMPONENT_FUNDING_TRANSACTION_DATE, "transactionDate", false, "6");
                amountComponent.getAmount().getTextContainer().setRequired(false);
                item.add(amountComponent);

                AmpTextFieldPanel<String> description = new AmpTextFieldPanel<>("description", new PropertyModel<>(model, "description"), COMPONENT_FUNDING_DESCRIPTION, false);
                item.add(description);

//                final AmpNewResourceFieldPanel<AmpActivityVersion> newDoc =
//                        new AmpNewResourceFieldPanel<AmpActivityVersion>("addNewDocument", am, "Add New Document", resourcesList, false);
//                newDoc.setOutputMarkupId(true);
//                add(newDoc);

                item.add(new ListEditorRemoveButton("delete", "Delete"));
            }
        };

        add(editorList);

    }

    private AmpSelectFieldPanel<AmpOrganisation> buildSelectFieldPanel(String id, String fmName, String expression,
                                                                       IModel<AmpComponentFunding> model,
                                                                       AbstractReadOnlyModel<List<AmpOrganisation>>
                                                                               orgsList) {
        AmpSelectFieldPanel<AmpOrganisation> selectField = new AmpSelectFieldPanel<>(id,
                new PropertyModel<>(model, expression), orgsList, fmName
                , false, true, null, false);
        selectField.add(UpdateEventBehavior.of(FundingOrgListUpdateEvent.class));
        selectField.getChoiceContainer().add(new AttributeModifier("style", "width: 100px;"));
        return selectField;
    }

    public ListEditor<AmpComponentFunding> getEditorList() {
        return editorList;
    }

    private AbstractReadOnlyModel<List<AmpComponentFunding>> getSubsetModel( final IModel<Set<AmpComponentFunding>> compFundsModel , final int transactionType)
    {
        
        
        return new AbstractReadOnlyModel<List<AmpComponentFunding>>() {
            private static final long serialVersionUID = 370618487459839210L;

            @Override
            public List<AmpComponentFunding> getObject() {
                List<AmpComponentFunding> result = new ArrayList<>();
                Set<AmpComponentFunding> allComp = compFundsModel.getObject();
                if (allComp != null){
                    for (AmpComponentFunding comp : allComp) {
                        if (comp.getTransactionType() == transactionType)
                            //if (comp.getComponent().hashCode() == componentModel.getObject().hashCode())
                            result.add(comp);
                    }
                }
                
                return result;
            }
        };
    }

}
