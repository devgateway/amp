/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.tables;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.AmpFundingAmountComponent;
import org.dgfoundation.amp.onepager.components.AmpTableFundingAmountComponent;
import org.dgfoundation.amp.onepager.components.ListEditor;
import org.dgfoundation.amp.onepager.components.ListEditorRemoveButton;
import org.dgfoundation.amp.onepager.components.fields.*;
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
                try{
                    AmpCategorySelectFieldPanel adjustmentTypes = new AmpCategorySelectFieldPanel(
                            "adjustmentType", CategoryConstants.ADJUSTMENT_TYPE_KEY,
                            new PropertyModel<AmpCategoryValue>(model,"adjustmentType"),
                            CategoryConstants.ADJUSTMENT_TYPE_NAME, //fmname
                            false, false, false, null, false);
                    adjustmentTypes.getChoiceContainer().setRequired(true);
                    adjustmentTypes.getChoiceContainer().add(new AttributeModifier("style", "width: 100px;"));
                    item.add(adjustmentTypes);

                } catch(Exception e) {
                    logger.error("AmpCategoryGroupFieldPanel initialization failed");
                }

                // read the list of organizations from related organizations page, and
                // create a unique set with the orgs chosen
                AbstractReadOnlyModel<List<AmpOrganisation>> orgsList = new AmpRelatedOrgsModel(activityModel, null, true);


                // selector for related orgs
                AmpSelectFieldPanel<AmpOrganisation> orgSelect = buildSelectFieldPanel("orgSelect",
                        "Component Organization", "reportingOrganization",
                        model, orgsList);
                item.add(orgSelect);

                // selector for second related orgs
                AmpSelectFieldPanel<AmpOrganisation> secondOrgSelect = buildSelectFieldPanel("secondOrgSelect",
                        "Component Second Responsible Organization", "componentSecondResponsibleOrganization",
                        model, orgsList);
                item.add(secondOrgSelect);

                AmpFundingAmountComponent amountComponent = new AmpFundingAmountComponent<AmpComponentFunding>("fundingAmount",
                        model, "Amount", "transactionAmount", "Currency",
                        "currency", "Transaction Date", "transactionDate", false,"6");
                amountComponent.getAmount().getTextContainer().setRequired(false);
                item.add(amountComponent);

                AmpTextFieldPanel<String> description = new AmpTextFieldPanel<String>("description", new PropertyModel<String>(model, "description"), "Description", false);
                item.add(description);

                item.add(new ListEditorRemoveButton("delete", "Delete"));
            }
        };

        add(editorList);

    }

    private AmpSelectFieldPanel<AmpOrganisation> buildSelectFieldPanel(String id, String fmName, String expression,
                                                                       IModel<AmpComponentFunding> model,
                                                                       AbstractReadOnlyModel<List<AmpOrganisation>>
                                                                               orgsList) {
        AmpSelectFieldPanel<AmpOrganisation> selectField = new AmpSelectFieldPanel<AmpOrganisation>(id,
                new PropertyModel<AmpOrganisation>(model, expression), orgsList, fmName
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
                List<AmpComponentFunding> result = new ArrayList<AmpComponentFunding>();
                Set<AmpComponentFunding> allComp = compFundsModel.getObject();
                if (allComp != null){
                    Iterator<AmpComponentFunding> iterator = allComp.iterator();
                    while (iterator.hasNext()) {
                        AmpComponentFunding comp = (AmpComponentFunding) iterator
                        .next();
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
