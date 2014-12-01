package org.dgfoundation.amp.onepager.components.features.tables;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.AttributeModifier;
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
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converter.DoubleConverter;
import org.dgfoundation.amp.onepager.components.fields.*;
import org.dgfoundation.amp.onepager.events.TotalBudgetStructureUpdateEvent;
import org.dgfoundation.amp.onepager.events.UpdateEventBehavior;
import org.dgfoundation.amp.onepager.models.AmpBudgetStructureModel;
import org.dgfoundation.amp.onepager.models.AmpThemeSearchModel;
import org.dgfoundation.amp.onepager.models.PersistentObjectModel;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.dgfoundation.amp.onepager.util.AmpDividePercentageField;
import org.dgfoundation.amp.onepager.yui.AmpAutocompleteFieldPanel;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpActivityBudgetStructure;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.util.AmpAutoCompleteDisplayable;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.categorymanager.action.CategoryManager;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

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
		
		//CategoryManagerUtil.getAmpCategoryValuesFromListByKey("budgetStructure")
		Collection<AmpCategoryValue> posVal =  CategoryManagerUtil.getAmpCategoryValueCollectionByKey("budgetStructure");
		
		Iterator<AmpCategoryValue> it = posVal.iterator();
		while(it.hasNext()){
			AmpCategoryValue acv = it.next();

			AmpActivityBudgetStructure abs = new AmpActivityBudgetStructure();
			abs.setActivity(am.getObject());
			abs.setBudgetStructureName(TranslatorUtil.getTranslatedText(acv.getValue()));
			abs.setBudgetStructurePercentage(new Float(0));
			if(!setModel.getObject().contains(abs))
				setModel.getObject().add(abs);
		}
		
		AbstractReadOnlyModel<List<AmpActivityBudgetStructure>> listModel = new AbstractReadOnlyModel<List<AmpActivityBudgetStructure>>() {
			private static final long serialVersionUID = 1L;

			@Override
			public List<AmpActivityBudgetStructure> getObject() {
				Set<AmpActivityBudgetStructure> allProgs = setModel.getObject();
				Set<AmpActivityBudgetStructure> specificProgs = new HashSet<AmpActivityBudgetStructure>();
				
				if (allProgs!=null){
					Iterator<AmpActivityBudgetStructure> it = allProgs.iterator();
					while (it.hasNext()) {
						AmpActivityBudgetStructure prog = it.next();
						if (prog != null )
							specificProgs.add(prog);
					}
				}
				/*Collection<AmpCategoryValue> posVal =  CategoryManagerUtil.getAmpCategoryValueCollectionByKey("budgetStructure");
				Iterator<AmpCategoryValue> it = posVal.iterator();
				while(it.hasNext()){
					AmpCategoryValue acv = it.next();
					AmpActivityBudgetStructure abs = new AmpActivityBudgetStructure();
					abs.setActivity(am.getObject());
					abs.setBudgetStructureName(acv.getValue());
					abs.setBudgetStructurePercentage(new Float(0));
					if(!specificProgs.contains(abs))
						setModel.getObject().add(abs);
				}*/
				return new ArrayList<AmpActivityBudgetStructure>(specificProgs);
			}
		};

		WebMarkupContainer wmc = new WebMarkupContainer("ajaxIndicator");
		add(wmc);
		AjaxIndicatorAppender iValidator = new AjaxIndicatorAppender();
		wmc.add(iValidator);
		
		final AmpPercentageCollectionValidatorField<AmpActivityBudgetStructure> percentageValidationField = new AmpPercentageCollectionValidatorField<AmpActivityBudgetStructure>(
				"programPercentageTotal", listModel, "programPercentageTotal") {
			@Override
			public Number getPercentage(AmpActivityBudgetStructure item) {
				return item.getBudgetStructurePercentage();
			}
		};
		percentageValidationField.setIndicatorAppender(iValidator);
		percentageValidationField.add(UpdateEventBehavior.of(TotalBudgetStructureUpdateEvent.class));
		add(percentageValidationField);
		
		final AmpMinSizeCollectionValidationField<AmpActivityBudgetStructure> minSizeCollectionValidationField = new AmpMinSizeCollectionValidationField<AmpActivityBudgetStructure>(
				"minSizeProgramValidator", listModel, "minSizeProgramValidator"){
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				//the required star should be visible, depending on whether the validator is active or not
				reqStar.setVisible(isVisible());
						
			}	
		};
		minSizeCollectionValidationField.setIndicatorAppender(iValidator);
		add(minSizeCollectionValidationField);
		
		final AmpUniqueCollectionValidatorField<AmpActivityBudgetStructure> uniqueCollectionValidationField = new AmpUniqueCollectionValidatorField<AmpActivityBudgetStructure>(
				"uniqueProgramsValidator", listModel, "uniqueProgramsValidator") {
			@Override
		 	public Object getIdentifier(AmpActivityBudgetStructure t) {
				return t.getBudgetStructureName();
		 	}	
		};
		uniqueCollectionValidationField.setIndicatorAppender(iValidator);
		add(uniqueCollectionValidationField);
		
		/*AmpTextFieldPanel<Double> amount = new AmpTextFieldPanel<Double>("proposedAmount1",
				null,"",true,true) {
			public IConverter getInternalConverter(java.lang.Class<?> type) {
				DoubleConverter converter = (DoubleConverter) DoubleConverter.INSTANCE;
				NumberFormat formatter = FormatHelper.getDecimalFormat(true);
				converter.setNumberFormat(getLocale(), formatter);
				return converter; 
			}
		};
		amount.getTextContainer().add(new AttributeModifier("size", new Model<String>("1")));
		add(amount);*/
		
		AmpTextFieldPanel<Double> amount = new AmpTextFieldPanel<Double>("totalBudgetAmount",
				new AmpBudgetStructureModel(new PropertyModel<Set<AmpActivityBudgetStructure>>(am, "actBudgetStructure")), "",true,true) {
			public IConverter getInternalConverter(java.lang.Class<?> type) {
				DoubleConverter converter = (DoubleConverter) DoubleConverter.INSTANCE;
				NumberFormat formatter = FormatHelper.getDecimalFormat(true);
				converter.setNumberFormat(getLocale(), formatter);
				return converter; 
			}
		};
		amount.add(UpdateEventBehavior.of(TotalBudgetStructureUpdateEvent.class));
		amount.getTextContainer().add(new AttributeModifier("size", new Model<String>("1")));
		amount.getTextContainer().add(new AttributeModifier("readonly", new Model<String>("readonly")));
		add(amount);

        /*final AmpTreeCollectionValidatorField<AmpActivityBudgetStructure> treeCollectionValidatorField = new AmpTreeCollectionValidatorField<AmpActivityBudgetStructure>("treeValidator", listModel, "Tree Validator") {
            @Override
            public AmpAutoCompleteDisplayable getItem(AmpActivityBudgetStructure t) {
                return t.getProgram();
            }
        };
        treeCollectionValidatorField.setIndicatorAppender(iValidator);
        add(treeCollectionValidatorField);
*/
		list = new ListView<AmpActivityBudgetStructure>("listBudget", listModel) {
			private static final long serialVersionUID = 7218457979728871528L;
			@Override
			protected void populateItem(final ListItem<AmpActivityBudgetStructure> item) {
				final MarkupContainer listParent=this.getParent();
				
				PropertyModel<Double> percModel = new PropertyModel<Double>(
						item.getModel(), "budgetStructurePercentage");
				AmpPercentageTextField percentageField = new AmpPercentageTextField("percent", percModel, "budgetStructurePercentage",percentageValidationField,false){
					@Override
					protected void onAjaxOnUpdate(final AjaxRequestTarget target) {
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
				return (int)((float)(loc.getBudgetStructurePercentage()));
			}
			@Override
			public boolean itemInCollection(AmpActivityBudgetStructure item) {
				/*if (item != null && item.getProgramSetting() != null && item.getProgramSetting().getAmpProgramSettingsId() == programSettings.getObject().getAmpProgramSettingsId())
					return true;*/
				return true;
			}
			
		});
		
	}

	protected void onBudgetSectionChanged(AjaxRequestTarget target) {
		send(AmpBudgetSectionFormTableFeature.this, Broadcast.BREADTH, new TotalBudgetStructureUpdateEvent(target));
	}
}
