package org.dgfoundation.amp.onepager.components.features.tables;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.extensions.ajax.markup.html.AjaxIndicatorAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.fields.AmpMinSizeCollectionValidationField;
import org.dgfoundation.amp.onepager.components.fields.AmpPercentageCollectionValidatorField;
import org.dgfoundation.amp.onepager.components.fields.AmpPercentageTextField;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.events.TotalBudgetStructureUpdateEvent;
import org.dgfoundation.amp.onepager.events.UpdateEventBehavior;
import org.dgfoundation.amp.onepager.models.AmpBudgetStructureModel;
import org.dgfoundation.amp.onepager.util.AmpDividePercentageField;
import org.digijava.module.aim.dbentity.AmpActivityBudgetStructure;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

import java.util.*;

/**
 * @author apopescu@dginternational.org
 * since Nov 21, 2013
 */
public class AmpBudgetSectionFormTableFeature extends AmpFormTableFeaturePanel <AmpActivityVersion,AmpActivityBudgetStructure>{

    /**
     * @param id
     * @param fmName
     * @param am
     * @throws Exception
     */
    public AmpBudgetSectionFormTableFeature(String id, String fmName,
            final IModel<AmpActivityVersion> am) throws Exception{
        this( id, fmName, am, false);
    }
    
    public AmpBudgetSectionFormTableFeature(String id, String fmName,
            final IModel<AmpActivityVersion> am,boolean required) throws Exception {
        super(id, am, fmName,false,required);
        final IModel<Set<AmpActivityBudgetStructure>> setModel=new PropertyModel<Set<AmpActivityBudgetStructure>>(am,"actBudgetStructure");
        if (setModel.getObject() == null)
            setModel.setObject(new HashSet<AmpActivityBudgetStructure>());
        
        Collection<AmpCategoryValue> posVal =  CategoryManagerUtil.getAmpCategoryValueCollectionByKey("budgetStructure");
        if (posVal != null) {
            Iterator<AmpCategoryValue> it = posVal.iterator();
            while (it.hasNext()) {
                AmpCategoryValue acv = it.next();
                AmpActivityBudgetStructure abs = new AmpActivityBudgetStructure();
                abs.setActivity(am.getObject());
                abs.setBudgetStructureName(acv.getValue());
                abs.setBudgetStructurePercentage(new Float(0));
                if (!setModel.getObject().contains(abs))
                    setModel.getObject().add(abs);
            }
        }
        
        AbstractReadOnlyModel<List<AmpActivityBudgetStructure>> listModel = new AbstractReadOnlyModel<List<AmpActivityBudgetStructure>>() {
            private static final long serialVersionUID = 1L;

            @Override
            public List<AmpActivityBudgetStructure> getObject() {
                Set<AmpActivityBudgetStructure> actBudgetItems = setModel.getObject();
                Set<AmpActivityBudgetStructure> specificActBudgetItems = new HashSet<AmpActivityBudgetStructure>();
                
                if (actBudgetItems!=null){
                    Iterator<AmpActivityBudgetStructure> it = actBudgetItems.iterator();
                    while (it.hasNext()) {
                        AmpActivityBudgetStructure actBugStruct = it.next();
                        if (actBugStruct != null )
                            specificActBudgetItems.add(actBugStruct);
                    }
                }

                return new ArrayList<AmpActivityBudgetStructure>(specificActBudgetItems);
            }
        };

        WebMarkupContainer wmc = new WebMarkupContainer("ajaxIndicator");
        add(wmc);
        AjaxIndicatorAppender iValidator = new AjaxIndicatorAppender();
        wmc.add(iValidator);
        
        final AmpPercentageCollectionValidatorField<AmpActivityBudgetStructure> percentageValidationField = new AmpPercentageCollectionValidatorField<AmpActivityBudgetStructure>(
                "budgetSectorPercentageTotal", listModel, "budgetSectorPercentageTotal") {
            @Override
            public Number getPercentage(AmpActivityBudgetStructure item) {
                return item.getBudgetStructurePercentage();
            }
        };
        percentageValidationField.setIndicatorAppender(iValidator);
        percentageValidationField.add(UpdateEventBehavior.of(TotalBudgetStructureUpdateEvent.class));
        percentageValidationField.setOutputMarkupId(true);
        add(percentageValidationField);
        
        final AmpMinSizeCollectionValidationField<AmpActivityBudgetStructure> minSizeCollectionValidationField = new AmpMinSizeCollectionValidationField<AmpActivityBudgetStructure>(
                "minSizeBudgetSectorValidator", listModel, "minSizeBudgetSectorValidator"){
            @Override
            protected void onConfigure() {
                super.onConfigure();
                
                //the required star should be visible, depending on whether the validator is active or not
                reqStar.setVisible(isVisible());
                        
            }   
        };
        minSizeCollectionValidationField.setIndicatorAppender(iValidator);
        add(minSizeCollectionValidationField);
        
        AmpTextFieldPanel<Double> amount = new AmpTextFieldPanel<Double>("totalBudgetAmount",
                new AmpBudgetStructureModel(new PropertyModel<Set<AmpActivityBudgetStructure>>(am, "actBudgetStructure")), "",true,true) {
        };
        amount.add(UpdateEventBehavior.of(TotalBudgetStructureUpdateEvent.class));
        amount.getTextContainer().add(new AttributeModifier("size", new Model<String>("1")));
        amount.getTextContainer().add(new AttributeModifier("readonly", new Model<String>("readonly")));
        add(amount);

        list = new ListView<AmpActivityBudgetStructure>("listBudget", listModel) {
            private static final long serialVersionUID = 7218457979728871528L;
            @Override
            protected void populateItem(final ListItem<AmpActivityBudgetStructure> item) {
                final MarkupContainer listParent = this.getParent();
                
                PropertyModel<Double> percModel = new PropertyModel<Double>(
                        item.getModel(), "budgetStructurePercentage");
                AmpPercentageTextField percentageField = new AmpPercentageTextField("percent", percModel, "budgetStructurePercentage",percentageValidationField,false){
                    @Override
                    protected void onAjaxOnUpdate(final AjaxRequestTarget target) {
                        super.onAjaxOnUpdate(target);
                        onBudgetSectionChanged(target);
                    }
                };
                percentageField.getTextContainer().add(new AttributeModifier("style", "width: 40px;"));
                item.add(percentageField);
                
                item.add(new Label("name", new PropertyModel<String>(item.getModel(), "budgetStructureName")).setEscapeModelStrings(false));
                
            }
        };
        list.setReuseItems(true);
        add(list);


        add(new AmpDividePercentageField<AmpActivityBudgetStructure>("dividePercentage", "Divide Percentage", "Divide Percentage", setModel, new Model<ListView<AmpActivityBudgetStructure>>(list)){
            private static final long serialVersionUID = 1L;

            @Override
            public void setPercentage(AmpActivityBudgetStructure loc, int val) {
                loc.setBudgetStructurePercentage((float) val);
            }
            @Override
            public int getPercentage(AmpActivityBudgetStructure loc) {
                return loc.getBudgetStructurePercentage().intValue();
            }
            @Override
            public boolean itemInCollection(AmpActivityBudgetStructure item) {
                return true;
            }
            
        });
        
    }
    
    protected void onBudgetSectionChanged(AjaxRequestTarget target) {
        send(AmpBudgetSectionFormTableFeature.this, Broadcast.BREADTH, new TotalBudgetStructureUpdateEvent(target));
    }
}
