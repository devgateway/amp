package org.dgfoundation.amp.onepager.components.features.sections;

import java.util.*;


import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.components.AmpRequiredComponentContainer;
import org.dgfoundation.amp.onepager.components.ListEditor;
import org.dgfoundation.amp.onepager.components.fields.AmpFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpGroupFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpSelectFieldPanel;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.util.AidEffectivenessIndicatorUtil;


public class AmpAidEffectivenessFormSectionFeature extends
		AmpFormSectionFeaturePanel 
		implements AmpRequiredComponentContainer {
	
	private List<org.apache.wicket.markup.html.form.FormComponent<?>> requiredFormComponents = new ArrayList<org.apache.wicket.markup.html.form.FormComponent<?>>();


    final IChoiceRenderer<AmpAidEffectivenessIndicatorOption> renderer = new IChoiceRenderer<AmpAidEffectivenessIndicatorOption>() {
        @Override
        public Object getDisplayValue(AmpAidEffectivenessIndicatorOption object) {
            return object.getAmpIndicatorOptionName();
        }

        @Override
        public String getIdValue(AmpAidEffectivenessIndicatorOption object, int index) {
            return String.valueOf(object.getAmpIndicatorOptionId());
        }
    };


	public AmpAidEffectivenessFormSectionFeature(String id, String fmName,
			IModel<AmpActivityVersion> am) throws Exception {
		super(id, fmName, am);
		
		AmpAuthWebSession session = (AmpAuthWebSession) getSession();

        //RepeatingView listItems = new RepeatingView("selectedEffectivenessIndicatorOptions");
        AmpActivityVersion activity = am.getObject();

        AidEffectivenessIndicatorUtil.populateSelectedOptions(activity);

        /*
        final IModel<Set<AmpAidEffectivenessIndicatorOption>> setModel
                = new PropertyModel<Set<AmpAidEffectivenessIndicatorOption>>(am, "selectedEffectivenessIndicatorOptions");
        AbstractReadOnlyModel<List<AmpAidEffectivenessIndicatorOption>> listModel = OnePagerUtil.getReadOnlyListModelFromSetModel(setModel);
        */

        final ListEditor<AmpAidEffectivenessIndicatorOption> indicatorsList = new ListEditor<AmpAidEffectivenessIndicatorOption>(
                "selectedEffectivenessIndicatorOptions", new Model(new HashSet(am.getObject().getSelectedEffectivenessIndicatorOptions()))) {

            @Override
            protected void onPopulateItem(org.dgfoundation.amp.onepager.components.ListItem<AmpAidEffectivenessIndicatorOption> componentOuter) {



                AmpAidEffectivenessIndicator indicator = componentOuter.getModelObject().getIndicator();
                AmpFieldPanel<AmpAidEffectivenessIndicatorOption> indicatorChoices = null;
                if (indicator.getIndicatorType() == 0) {
                    indicatorChoices = new AmpGroupFieldPanel<AmpAidEffectivenessIndicatorOption>(
                            "ampIndicatorOptionId", new Model<AmpAidEffectivenessIndicatorOption>(componentOuter.getModelObject()), indicator.getOptions(),
                            indicator.getAmpIndicatorName() + ":", false, false, renderer, indicator.getTooltipText());

                } else {
                    indicatorChoices = new AmpSelectFieldPanel <AmpAidEffectivenessIndicatorOption>(
                            "ampIndicatorOptionId", new Model<AmpAidEffectivenessIndicatorOption>(componentOuter.getModelObject()), indicator.getOptions(),
                            indicator.getAmpIndicatorName() + ":", false, false, renderer, true);

                }

                indicatorChoices.setTitleTooltip(new Label("tooltip", indicator.getTooltipText()));
                componentOuter.add(indicatorChoices);
            }

            //@Override
            protected void populateItem(ListItem<AmpAidEffectivenessIndicatorOption> componentOuter) {



                /*
                RadioGroup group = new RadioGroup("group", componentOuter.getModel());

                ListView<AmpAidEffectivenessIndicatorOption>optionsList = new ListView<AmpAidEffectivenessIndicatorOption>("listOptions",
                        new ArrayList<AmpAidEffectivenessIndicatorOption>(componentOuter.getModelObject().getIndicator().getOptions())) {
                    @Override
                    protected void populateItem(ListItem<AmpAidEffectivenessIndicatorOption> componentInner) {

                        componentInner.add(new Radio("id", componentInner.getModel()));
                        componentInner.add(new org.apache.wicket.markup.html.basic.Label("label", componentInner.getModelObject().getAmpIndicatorOptionName()));

                    }
                };
                group.add(optionsList);
                componentOuter.add(group);
                */



                /*if (true) {
                    final RadioChoice<AmpAidEffectivenessIndicatorOption> indicatorChoices = new RadioChoice<AmpAidEffectivenessIndicatorOption>
                            ("ampIndicatorOptionId", new Model<AmpAidEffectivenessIndicatorOption>(componentOuter.getModelObject()),
                                    componentOuter.getModelObject().getIndicator().getOptions(), renderer);

                    componentOuter.add(indicatorChoices);

                } else {
                    DropDownChoice indicatorChoices = new DropDownChoice("ampIndicatorOptionId",
                            new Model<AmpAidEffectivenessIndicatorOption>(componentOuter.getModelObject()),
                            componentOuter.getModelObject().getIndicator().getOptions(), renderer);
                    componentOuter.add(indicatorChoices);
                    componentOuter.add(indicatorChoices);
                }


                Label indicatorName = new Label("indicatorName", componentOuter.getModelObject().getIndicator().getAmpIndicatorName() + ":");
                componentOuter.add(indicatorName);*/


                /*
                AmpAidEffectivenessIndicator indicator = componentOuter.getModelObject().getIndicator();
                if (indicator.getIndicatorType() == 0) {
                    AmpGroupFieldPanel<AmpAidEffectivenessIndicatorOption> indicatorChoices = new AmpGroupFieldPanel<AmpAidEffectivenessIndicatorOption>(
                            "ampIndicatorOptionId", new Model<AmpAidEffectivenessIndicatorOption>(componentOuter.getModelObject()), indicator.getOptions(),
                            indicator.getAmpIndicatorName() + ":", false, false, renderer, indicator.getTooltipText());

                    componentOuter.add(indicatorChoices);indicatorChoices.setVisible(true);
                } else {

                    AmpSelectFieldPanel<AmpAidEffectivenessIndicatorOption> indicatorChoices = new AmpSelectFieldPanel <AmpAidEffectivenessIndicatorOption>(
                            "ampIndicatorOptionId", new Model<AmpAidEffectivenessIndicatorOption>(componentOuter.getModelObject()), indicator.getOptions(),
                            indicator.getAmpIndicatorName() + ":", false, false, renderer, false);
                 }*/




                //componentOuter.add(optionsList);


                //RadioGroup group = new RadioGroup("group", component.getModel());

                /*
                RepeatingView listItems = new RepeatingView("listOptions");
                for (AmpAidEffectivenessIndicatorOption option : component.getModelObject().getIndicator().getOptions()) {
                    listItems.add(new Radio("id", component.getModel()));
                    listItems.add(new org.apache.wicket.markup.html.basic.Label("label", option.getAmpIndicatorOptionName()));
                }*/

                //component.add(new TextField("ampIndicatorOptionId", new PropertyModel(component.getModelObject(), "ampIndicatorOptionId")));
                /*
                for (AmpAidEffectivenessIndicatorOption option : component.getModelObject().getIndicator().getOptions()) {
                    group.add(new Radio("id", component.getModel()));
                    group.add(new org.apache.wicket.markup.html.basic.Label("label", option.getAmpIndicatorOptionName()));
                }
                */


                //component.add(group);
                //component.add(listItems);

            }

        };
        //indicatorsList.setReuseItems(true);
        add(indicatorsList);



        /*


*/


	}

    public List<org.apache.wicket.markup.html.form.FormComponent<?>> getRequiredFormComponents() {
		return requiredFormComponents;
	}


}